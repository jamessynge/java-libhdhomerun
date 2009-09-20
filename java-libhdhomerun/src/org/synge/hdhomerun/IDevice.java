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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.synge.hdhomerun.impl.DeviceLocator;

/**
 * Represents an HDHomeRun device on the network.
 *
 * @author James Synge
 */
public interface IDevice {
  /**
   * Returns the HDHomeRun's device ID (the hexadecimal representation is
   * printed on the bottom of the HDHomeRun).
   * 
   * @return the device ID
   */
	int getDeviceID();

	/**
	 * Returns the IP address of the HDHomeRun (at this time, 2009-08-30, the
	 * HDHomeRun only supports IPv4).
	 * 
	 * @return the address
	 */
	InetAddress getInetAddress();

	/**
	 * Returns the number of tuners that the HDHomeRun supports (at this time,
	 * 2009-08-30, the result is 2).
	 * 
	 * @return
	 */
	int getTunerCount();

	/**
	 * Returns an object that provides information about and control over the
	 * specified tuner.
	 * 
	 * @param tunerNumber
	 * @return the requested ITuner
	 * @throws IndexOutOfBoundsException if the tunerNumber is not in the range
	 *     0 to N-1, where N is the count of tuners that the device supports.
	 */
	ITuner getTuner(int tunerNumber) throws IndexOutOfBoundsException;

	interface IDeviceLocator {
	  IDevice locateByAddress(Inet4Address address, int timeout, TimeUnit timeUnit);
    IDevice locateByID(int deviceID, int timeout, TimeUnit timeUnit);
    List<IDevice> locateOnSubnet(int timeout, TimeUnit timeUnit);
	}

	static final IDeviceLocator LOCATOR = new DeviceLocator();

}
