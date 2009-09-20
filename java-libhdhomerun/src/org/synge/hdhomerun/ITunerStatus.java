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

public interface ITunerStatus {
	IDevice getDevice();
	ITuner getTuner();
	int getTunerNumber();

	Modulation getRequestedModulation();	// Requested or actual modulation
	Modulation getLockedModulation();			// Locked modulation (set once lock on signal established)

	int getChannel();
	int getFrequency();

	ChannelMapType getChannelMapType();

//	Object getPIDFilter();	// TODO Come up with data type for the filter (list of selected PID ranges)

	int getProgram();

	String getVirtualChannel();

	Inet4Address getTargetAddress();
	int getTargetPort();
	boolean isRealTimeProtocol();

	/**
	 * @return
	 */
	int getSignalStrengthPct();

	/**
	 * Based on analog signal to noise ratio.
	 * @return
	 */
	int getSignalToNoiseQuality();

	/**
	 * Number of uncorrectable digital errors detected.
	 * @return
	 */
	int getSymbolErrorQuality();

	/**
	 * Transport stream bits per second.
	 * @return
	 */
	int getTSBitsPerSecond();

	/**
	 * Utilization (percentage full) of the transport stream.
	 * @return
	 */
	int getTSUtilizationPct();

	/**
	 * Count of uncorrectable reception errors.
	 * @return
	 */
	int getTSErrorCount();

	/**
	 * Incremented when there is a jump in sequence numbers.
	 * @return
	 */
	int getTSMissedPacketCounter();

	/**
	 * Count of CRC errors encountered in transport stream.
	 * @return
	 */
	int getTSCRCErrorCounter();


	/**
	 * Bits per second after filtering.
	 * @return
	 */
	int getFilteredBitsPerSecond();

	/**
	 * Packets per second sent through the network.
	 * @return
	 */
	int getPacketsPerSecond();

	/**
	 * Packets or TS frames dropped before transmission.
	 * @return
	 */
	int getDropCount();

	/**
	 * Reason for stopping the stream
	 * @return
	 */
	int getStopCode();
}
