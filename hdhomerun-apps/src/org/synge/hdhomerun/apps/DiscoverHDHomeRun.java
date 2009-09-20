package org.synge.hdhomerun.apps;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.synge.hdhomerun.IDevice;
import org.synge.hdhomerun.impl.HDHomeRunConstants;

public class DiscoverHDHomeRun {
  static {
    System.setProperty("java.net.preferIPv4Stack", "true");
  }

  public static void main(String[] args) {
    List<IDevice> devices1OnSubnet = IDevice.LOCATOR.locateOnSubnet(5, TimeUnit.SECONDS);

    IDevice firstDeviceOnSubnet = IDevice.LOCATOR.locateByID(HDHomeRunConstants.HDHOMERUN_DEVICE_ID_WILDCARD, 5, TimeUnit.SECONDS);
    
    return;
  }
}
