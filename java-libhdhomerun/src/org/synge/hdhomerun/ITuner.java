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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Represents one of the tuners of an HDHomeRun.
 *
 * @author James Synge
 */
public interface ITuner {
	IDevice getDevice();
	int getTunerNumber();

	ITunerStatus refreshStatus() throws IOException;
	ITunerStatus getLastStatus();

	String setChannel(Modulation modulation, int channelOrFrequency) throws IOException;
	void clearChannel() throws IOException;

	ChannelMapType setChannelMapType(ChannelMapType type) throws IOException;

// TODO Come up with data type for the filter (list of selected PID ranges)
//	void setPIDFilter(Object filter);

	void setProgram(int program) throws IOException;

	void setVirtualChannel(String virtualChannel) throws IOException;

	void setUDPTarget(InetSocketAddress target) throws IOException;
	void setRTPTarget(InetSocketAddress target) throws IOException;
	void setTarget(String target) throws IOException;

	boolean waitForSignalLock(long timeout, TimeUnit unit) throws IOException, InterruptedException;
}
