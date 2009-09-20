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

import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TYPE_DISCOVER_REQ;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TYPE_DISCOVER_RPY;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TYPE_GETSET_REQ;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TYPE_GETSET_RPY;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TYPE_UPGRADE_REQ;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TYPE_UPGRADE_RPY;

/**
 * Enum for the types of HDHomeRun request and reply packets.
 * 
 * @author James Synge
 */
public enum HDHRPacketType {
	DiscoverRequest(HDHOMERUN_TYPE_DISCOVER_REQ),
	DiscoverReply(HDHOMERUN_TYPE_DISCOVER_RPY),

	GetSetRequest(HDHOMERUN_TYPE_GETSET_REQ),
	GetSetReply(HDHOMERUN_TYPE_GETSET_RPY),

	UpgradeRequest(HDHOMERUN_TYPE_UPGRADE_REQ),
	UpgradeReply(HDHOMERUN_TYPE_UPGRADE_RPY);

	public final short code;

	private HDHRPacketType(short code) {
		this.code = code;
	}

	public static HDHRPacketType lookup(short code) {
		for (HDHRPacketType packetType : values()) {
			if (packetType.code == code)
				return packetType;
		}
		return null;
	}
}
