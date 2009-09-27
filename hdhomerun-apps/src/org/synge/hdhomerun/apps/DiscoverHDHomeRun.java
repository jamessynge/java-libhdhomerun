package org.synge.hdhomerun.apps;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.synge.hdhomerun.IDevice;
import org.synge.hdhomerun.IDeviceFactory;

/**
 * Prints a list of HDHomeRun devices.
 *
 * @author James Synge
 */
public class DiscoverHDHomeRun {
  static {
    System.setProperty("java.net.preferIPv4Stack", "true");
  }

  public static void main(String[] args) {
    List<IDevice> devicesOnSubnet = IDeviceFactory.INSTANCE.discoverOnSubnet(5, TimeUnit.SECONDS);
    for (IDevice device : devicesOnSubnet) {
      System.out.println(device.toString());
    }
    return;
  }
}
