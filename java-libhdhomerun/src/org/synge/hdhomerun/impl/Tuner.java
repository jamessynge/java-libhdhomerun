package org.synge.hdhomerun.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.synge.hdhomerun.ChannelMapType;
import org.synge.hdhomerun.IDevice;
import org.synge.hdhomerun.ITuner;
import org.synge.hdhomerun.ITunerStatus;
import org.synge.hdhomerun.Modulation;

public class Tuner implements ITuner {

  public Tuner(Device device, int tunerNumber) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void clearChannel() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public IDevice getDevice() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITunerStatus getLastStatus() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getTunerNumber() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public ITunerStatus refreshStatus() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String setChannel(Modulation modulation, int channelOrFrequency)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ChannelMapType setChannelMapType(ChannelMapType type)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setProgram(int program) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setRTPTarget(InetSocketAddress target) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setTarget(String target) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setUDPTarget(InetSocketAddress target) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setVirtualChannel(String virtualChannel) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean waitForSignalLock(long timeout, TimeUnit unit)
      throws IOException, InterruptedException {
    // TODO Auto-generated method stub
    return false;
  }

}
