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
package org.synge.hdhomerun;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.synge.hdhomerun.impl.DeviceFactory;

/**
 * Provides methods for discovering the HDHomeRun devices on your network.
 *
 * @author James Synge
 */
public interface IDeviceFactory {
  static final IDeviceFactory INSTANCE = new DeviceFactory();

  /**
   * Broadcasts a DISCOVER request for all HDHomeRun devices to each of the IPv4
   * subnets to which this system is connected and then collects up the
   * responses until the timeout has expired.
   * 
   * @param timeout amount of time to wait for responses from the time the request is sent
   * @param timeUnit units of the timeout parameter
   * @return a list of IDevices, one for each DISCOVER response received
   */
  List<IDevice> discoverOnSubnet(int timeout, TimeUnit timeUnit);

  /**
   * Sends a DISCOVER request to the specified address (presumably of a known
   * HDHomeRun), and returns an IDevice for that HDHomeRun if it responds with
   * a DISCOVER response.  Returns as soon as a response is received, or when
   * the timeout has expired if there is no response.
   * 
   * @param address address of the known HDHomeRun
   * @param timeout maximum amount of time to wait
   * @param timeUnit units of the timeout parameter
   * @return the IDevice for the HDHomeRun, or null if no response was received
   */
  IDevice discoverByAddress(InetAddress address, int timeout, TimeUnit timeUnit);

  /**
   * Broadcasts a DISCOVER request for a specific HDHomeRun devices to each of
   * the IPv4 subnets to which this system is connected and returns an IDevice
   * for that HDHomeRun if it responds with a DISCOVER response.  Returns as
   * soon as a response is received, or when the timeout has expired if there
   * is no response.
   * 
   * @param deviceID ID of the known HDHomeRun; the ID is printed (in
   *    hexadecimal) on the bottom of the HDHomeRun device
   * @param timeout maximum amount of time to wait
   * @param timeUnit units of the timeout parameter
   * @return the IDevice for the HDHomeRun, or null if no response was received
   */
  IDevice discoverByID(int deviceID, int timeout, TimeUnit timeUnit);
}
