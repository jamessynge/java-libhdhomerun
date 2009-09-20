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

  public static List<Inet4Address> getBroadcastAddresses() throws SocketException
  {
    List<Inet4Address> result = new ArrayList<Inet4Address>();
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

    if (result.isEmpty()) {
      if (!Boolean.getBoolean(PREFER_IPV4_STACK)) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Unable to find any IPv4 broadcast addresses;\nPlease set Java system property " + PREFER_IPV4_STACK + " to true.");
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

  public static void main(String[] args) throws SocketException {
    List<Inet4Address> broadcastAddresses = getBroadcastAddresses();
    return;
  }
}
