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

/**
 * Enum for the types of channel maps (logically, a map from channel number
 * to frequency).  So far as I can tell, these aren't baked into the HDHomeRun,
 * but instead are used to guide the process of scanning for channels available
 * from a signal source.
 * 
 * @author James Synge
 */
public enum ChannelMapType {
	US_BCAST(0, "us-bcast"),
	US_CABLE(1, "us-cable"),
	US_HRC(2, "us-hrc"),
	US_IRC(3, "us-irc"),
	UK_BCAST(4, "uk-bcast"),
	NZ_BCAST(5, "nz-bcast");

	public final String keyword;
	private final int bit;	// Not sure that I need the bit/mask form.
	private final int mask;
	private ChannelMapType(int bit, String keyword) {
		this.bit = bit;
		this.mask = 1 << bit;
		this.keyword = keyword;
		return;
	}

	
//	public static final int CHANNEL_MAP_US_ALL =
//  	(CHANNEL_MAP_US_BCAST | CHANNEL_MAP_US_CABLE | CHANNEL_MAP_US_HRC | CHANNEL_MAP_US_IRC);

	public static ChannelMapType find(String keyword) {
		for (ChannelMapType type : values()) {
			if (type.keyword.equals(keyword))
				return type;
		}
		return null;
	}
}
