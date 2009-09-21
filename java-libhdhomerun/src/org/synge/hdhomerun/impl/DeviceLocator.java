/**
 * Copyright (c) 2009 James Synge
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.synge.hdhomerun.impl;

import static org.synge.hdhomerun.HDHRPacketType.DiscoverRequest;
import static org.synge.hdhomerun.HDHRTag.DeviceID;
import static org.synge.hdhomerun.HDHRTag.DeviceType;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_DEVICE_ID_WILDCARD;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_DEVICE_TYPE_TUNER;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_DISCOVER_UDP_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.synge.hdhomerun.HDHRPacketType;
import org.synge.hdhomerun.HDHRTag;
import org.synge.hdhomerun.IDevice;
import org.synge.hdhomerun.IDevice.IDeviceLocator;

/**
 *
 * @author James Synge
 */
public class DeviceLocator implements IDeviceLocator {
  @Override
  public IDevice locateByAddress(InetAddress address, int timeout, TimeUnit timeUnit) {
    List<InetAddress> destinationAddresses = Collections.singletonList(address);
    try {
      List<IDevice> results = locate(destinationAddresses, HDHOMERUN_DEVICE_ID_WILDCARD, true, timeout, timeUnit);
      if (results.isEmpty())
        return null;
      return results.get(0);
    }
    catch (IOException ex) {
      throw new RuntimeException("Error sending or receiving discover request", ex);
    }
  }

  @Override
  public IDevice locateByID(int deviceID, int timeout, TimeUnit timeUnit) {
    final List<IDevice> results = locateOnSubnet(deviceID, true, timeout, timeUnit);
    if (results.isEmpty())
      return null;
    return results.get(0);
  }

  @Override
  public List<IDevice> locateOnSubnet(int timeout, TimeUnit timeUnit) {
    return locateOnSubnet(HDHOMERUN_DEVICE_ID_WILDCARD, false, timeout, timeUnit);
  }

  /**
   * This method is public to improve testability.
   * 
   */
  public List<IDevice> locateOnSubnet(int deviceID, boolean oneOnly, int timeout, TimeUnit timeUnit) {
    List<? extends InetAddress> broadcastAddresses = null;
    try {
      broadcastAddresses = GetIPv4BroadcastAddresses.getBroadcastAddresses();
    }
    catch (SocketException ex) {
      throw new RuntimeException("Error getting the subnet broadcast addresses", ex);
    }

    try {
      List<IDevice> result = locate(broadcastAddresses, deviceID, oneOnly, timeout, timeUnit);
      return result;
    }
    catch (IOException ex) {
      throw new RuntimeException("Error sending or receiving discover request", ex);
    }
  }

  /**
   * This method is public to improve testability.
   * 
   */
  public List<IDevice> locate(List<? extends InetAddress> destinationAddresses, int deviceID, boolean oneOnly, int timeout, TimeUnit timeUnit) throws IOException {
    long maxMillis = timeUnit.toMillis(timeout);
    if (maxMillis < 1)
      maxMillis = 1;

    List<IDevice> result = new ArrayList<IDevice>();
    DatagramChannel channel = DatagramChannel.open();
    boolean interrupted = false;
    try {
      DatagramListener listener = createListener(channel, oneOnly);
      BlockingQueue<HDHRPacket> responses = listener.getResponseQueue();
      try {
        sendDiscoverRequests(channel, destinationAddresses, deviceID);

        long now = System.currentTimeMillis();
        long endMillis = now + maxMillis;
        
        while (now < endMillis) {
          long duration = endMillis - now;
          try {
            HDHRPacket response = responses.poll(duration, TimeUnit.MILLISECONDS);
            if (response == null)
              break;

            IDevice device = createDevice(response);
            result.add(device);
      
            if (oneOnly)
              break;
          }
          catch (InterruptedException ex) {
            interrupted = true;
            break;
          }
          now = System.currentTimeMillis();
        }
      }
      finally {
        listener.interrupt();
        try {
          listener.join(500);
        } catch (InterruptedException ex) {
          interrupted = true;
        }
      }
    }
    finally {
      channel.close();
    }

    if (interrupted)
      Thread.currentThread().interrupt();

    return result;
  }

  private IDevice createDevice(HDHRPacket packet) {
    Integer id = packet.getTagValueInteger(HDHRPacketType.DiscoverReply, HDHRTag.DeviceID);
    if (id == null) {
      throw new IllegalArgumentException("Packet must be a Discover Reply with a Device ID");
    }

    InetSocketAddress receivedFrom = packet.getReceivedFrom();
    InetAddress address = receivedFrom.getAddress();
    return new Device(id.intValue(), address);
  }

  /**
   * 
   * @param channel
   * @param destinationAddresses
   * @param deviceID
   * @throws IOException
   */
  private static void sendDiscoverRequests(DatagramChannel channel, List<? extends InetAddress> destinationAddresses, int deviceID) throws IOException {
    // Create the request for info.

    HDHRPacket packet = new HDHRPacket();
    packet.start(DiscoverRequest);
    packet.putTLV(DeviceType, HDHOMERUN_DEVICE_TYPE_TUNER);
    packet.putTLV(DeviceID, deviceID);
    packet.end();

    for (InetAddress destinationAddress : destinationAddresses) {
      SocketAddress sockAddr = new InetSocketAddress(destinationAddress, HDHOMERUN_DISCOVER_UDP_PORT);
      packet.send(channel, sockAddr);
      packet.rewind();
    }

    return;
  }

  private DatagramListener createListener(DatagramChannel channel, boolean oneOnly)
  {
    BlockingQueue<HDHRPacket> responses = new ArrayBlockingQueue<HDHRPacket>(20);
    DatagramListener listener = new DatagramListener(channel, responses, oneOnly);
    listener.setDaemon(true);
    listener.start();
    return listener;
  }

  private static class DatagramListener extends Thread {
    private final DatagramChannel channel;
    private final BlockingQueue<HDHRPacket> responseQueue;
    private final boolean oneOnly;

    public DatagramListener(DatagramChannel channel, BlockingQueue<HDHRPacket> sink, boolean oneOnly) {
      this.channel = channel;
      this.responseQueue = sink;
      this.oneOnly = oneOnly;
    }

    public void run() {
      try {
        while (true) {
          HDHRPacket packet = new HDHRPacket();
          if (packet.receive(channel) != null) {
            getResponseQueue().put(packet);

            if (oneOnly) {
              return;
            }
          }
        }
      }
      catch (ClosedChannelException ex) {
        return;
      }
      catch (InterruptedException ex) {
        return;
      }
      catch (IOException ex) {
        return;
      }
    }

    public BlockingQueue<HDHRPacket> getResponseQueue() {
      return responseQueue;
    }
  }
}
