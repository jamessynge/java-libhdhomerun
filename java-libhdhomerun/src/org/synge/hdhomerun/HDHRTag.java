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

import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TAG_DEVICE_ID;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TAG_DEVICE_TYPE;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TAG_ERROR_MESSAGE;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TAG_GETSET_NAME;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_TAG_GETSET_VALUE;

/**
 * Enum for the tags in the Tag-Length-Value tuples within HDHomeRun packets.
 * 
 * @author James Synge
 */
public enum HDHRTag {
	DeviceType(HDHOMERUN_TAG_DEVICE_TYPE),
	DeviceID(HDHOMERUN_TAG_DEVICE_ID),

	GetSetName(HDHOMERUN_TAG_GETSET_NAME),
	GetSetValue(HDHOMERUN_TAG_GETSET_VALUE),

	ErrorMessage(HDHOMERUN_TAG_ERROR_MESSAGE);

	public final byte code;

	private HDHRTag(byte code) {
		this.code = code;
	}

	public static HDHRTag lookup(byte code) {
		for (HDHRTag tag : values()) {
			if (tag.code == code)
				return tag;
		}
		return null;
	}
}
