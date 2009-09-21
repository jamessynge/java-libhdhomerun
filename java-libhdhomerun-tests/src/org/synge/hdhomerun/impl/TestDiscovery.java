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

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import org.junit.Test;
import org.synge.hdhomerun.IDevice;
import org.synge.hdhomerun.IDevice.IDeviceLocator;

public class TestDiscovery {
  @Test
  public void testDiscovery()
  {
    final IDeviceLocator locator = IDevice.LOCATOR;

    // Find all the devices on the local subnet.
    List<IDevice> devicesOnSubnet = locator.locateOnSubnet(1, TimeUnit.SECONDS);

    // Then confirm we can find each of them by ip address and by id.
    for (IDevice expectedDevice : devicesOnSubnet) {
      InetAddress address = expectedDevice.getInetAddress();
      int id = expectedDevice.getDeviceID();

      IDevice byAddress = locator.locateByAddress(address, 1, TimeUnit.SECONDS);
      assertNotNull(byAddress);
      assertEquals(expectedDevice, byAddress);

      IDevice byID = locator.locateByID(id, 1, TimeUnit.SECONDS);
      assertNotNull(byID);
      assertEquals(expectedDevice, byID);
    }

    return;
  }
}
