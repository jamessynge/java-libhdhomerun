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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

public class GetIPv4BroadcastAddresses {
  public static final String PREFER_IPV4_STACK = "java.net.preferIPv4Stack";
  public static final String PREFER_IPV6_ADDRESSES = "java.net.preferIPv6Addresses";

  public static List<Inet4Address> getBroadcastAddresses() throws SocketException
  {
    List<Inet4Address> result = new ArrayList<Inet4Address>();

    String oldSetting = requestIPv4Stack();
    try {
      List<InterfaceAddress> interfaceAddresses = getIPv4InterfaceAddresses();

      for (InterfaceAddress interfaceAddress : interfaceAddresses) {
        InetAddress broadcast = interfaceAddress.getBroadcast();
        if (!(broadcast instanceof Inet4Address))
          continue;
        if (broadcast.isLoopbackAddress())
          continue;
        Inet4Address broadcast4 = (Inet4Address) broadcast;
        if ("255.255.255.255".equals(broadcast4.getHostAddress())) {
          // Not a SUBNET broadcast address
          continue;
        }
        result.add(broadcast4);
      }
    }
    finally {
      restoreIPv4StackPreference(oldSetting);
    }

    if (result.isEmpty()) {
      if (!Boolean.getBoolean(PREFER_IPV4_STACK)) {
        String msg =
          "Unable to find any IPv4 broadcast addresses.  Please set the Java\n" +
          "system property " + PREFER_IPV4_STACK + " to true.  Note that JUnit or TestNG\n" +
          "may load the network stack before System.setProperty(\"" + PREFER_IPV4_STACK + "\",\"true\")\n" +
          "can be executed, so the property may have to be set on the java command-line.";
        final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.warning(msg);
      }
    }

    return result;
  }

  public static List<InterfaceAddress> getIPv4InterfaceAddresses() throws SocketException
  {
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    List<InterfaceAddress> result = new ArrayList<InterfaceAddress>();
    while (interfaces.hasMoreElements()) {
      NetworkInterface networkInterface = interfaces.nextElement();
      if (networkInterface.isLoopback())
        continue;
      List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
      result.addAll(interfaceAddresses);
    }
    return result;
  }

  private static String requestIPv4Stack() {
    String result = System.getProperty(PREFER_IPV4_STACK);

    if ("true".equalsIgnoreCase(System.getProperty(PREFER_IPV6_ADDRESSES)))
      return result;

    System.setProperty(PREFER_IPV4_STACK, "true");
    return result;
  }

  private static void restoreIPv4StackPreference(String oldSetting) {
    if (oldSetting == null)
      System.clearProperty(PREFER_IPV4_STACK);
    else
      System.setProperty(PREFER_IPV4_STACK, oldSetting);
    return;
  }

  public static void main(String[] args) throws SocketException {
    List<Inet4Address> broadcastAddresses = getBroadcastAddresses();
    System.out.println("Broadcast addresses: ");
    for (Inet4Address inet4Address : broadcastAddresses) {
      System.out.print("    ");
      System.out.println(inet4Address.toString());
    }
    return;
  }
}
