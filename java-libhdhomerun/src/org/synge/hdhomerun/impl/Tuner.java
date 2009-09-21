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
