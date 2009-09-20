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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.synge.hdhomerun.IDevice;
import org.synge.hdhomerun.ITuner;

/**
 * TODO Come up with a way to read the number of tuners from the device.  This
 * is of interest because Silicondust has indicated they may produce a device
 * with eight tuners. Using the output of "get /sys/debug" may help:
 * 
 *    mem: nbm=112/107 npf=641 dmf=291
 *    loop: pkt=0
 *    cal: t0=10965 t1=10975
 * 
 * The third line *may* indicate the tuner ids (i.e. tuner 0 and tuner 1).
 * For now, I'm assuming the count is 2.
 *
 * @author James Synge
 */
public class Device implements IDevice {
  private final int deviceId;
  private final InetAddress ipAddr;

  public Device(int deviceId, InetAddress ipAddr) {
    this.deviceId = deviceId;
    this.ipAddr = ipAddr;
    return;
  }

  @Override
  public String toString() {
    String result = String.format("Device %x @ %s", deviceId, ipAddr.getHostAddress());
    return result;
  }
  
  /*
   * TODO Create a pool of channels.
   * For now, just create a new channel when requested.
   */
  public SocketChannel allocateChannel() throws IOException {
    InetAddress addr = getInetAddress();
    InetSocketAddress socketAddress =
      new InetSocketAddress(addr, HDHomeRunConstants.HDHOMERUN_CONTROL_TCP_PORT);

    SocketChannel channel = SocketChannel.open(socketAddress);
    return channel;
  }

  public void releaseChannel(SocketChannel channel) {
    if (channel != null) {
      try {
        channel.close();
      }
      catch (IOException ex) {
      }
      finally {
        channel = null;
      }
    }
  }

  public int getDeviceID() {
    return deviceId;
  }

  public InetAddress getInetAddress() {
    return ipAddr;
  }

  @Override
  public int getTunerCount() {
    return 2;
  }

  public ITuner getTuner(int tunerNumber) {
    if (0 <= tunerNumber && tunerNumber < getTunerCount())
      return new Tuner(this, tunerNumber);

    throw new IllegalArgumentException("Invalid tuner number: " + tunerNumber);
  }
}
