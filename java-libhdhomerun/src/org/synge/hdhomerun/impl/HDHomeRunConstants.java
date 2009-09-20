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

/**
 * Constants copied from hdhomerun_*.h as of 2008-07-27.  Changes since
 * then in hdhomerun_*.h are not yet reflected.
 *
 * @author James Synge
 * 
 * Regex: ^[\t ]*#define[\t ]+(\S+)[\t ]+([^\n]+)$
 * Replace:   public static final int $1 = $2;
 */
public final class HDHomeRunConstants {
  public static final int HDHOMERUN_DISCOVER_UDP_PORT = 65001;
  public static final int HDHOMERUN_CONTROL_TCP_PORT = 65001;

  public static final int HDHOMERUN_MAX_PACKET_SIZE = 1460;
  public static final int HDHOMERUN_MAX_PAYLOAD_SIZE = 1452;

  public static final short HDHOMERUN_TYPE_DISCOVER_REQ = 0x0002;
  public static final short HDHOMERUN_TYPE_DISCOVER_RPY = 0x0003;
  public static final short HDHOMERUN_TYPE_GETSET_REQ = 0x0004;
  public static final short HDHOMERUN_TYPE_GETSET_RPY = 0x0005;
  public static final short HDHOMERUN_TYPE_UPGRADE_REQ = 0x0006;
  public static final short HDHOMERUN_TYPE_UPGRADE_RPY = 0x0007;

  public static final byte HDHOMERUN_TAG_DEVICE_TYPE = 0x01;
  public static final byte HDHOMERUN_TAG_DEVICE_ID = 0x02;
  public static final byte HDHOMERUN_TAG_GETSET_NAME = 0x03;
  public static final byte HDHOMERUN_TAG_GETSET_VALUE = 0x04;
  public static final byte HDHOMERUN_TAG_ERROR_MESSAGE = 0x05;

  public static final int HDHOMERUN_DEVICE_TYPE_WILDCARD = 0xFFFFFFFF;
  public static final int HDHOMERUN_DEVICE_TYPE_TUNER = 0x00000001;
  public static final int HDHOMERUN_DEVICE_ID_WILDCARD = 0xFFFFFFFF;

  public static final int HDHOMERUN_MIN_PEEK_LENGTH = 4;

  public static final int CHANNEL_MAP_US_BCAST = (1 << 0);
  public static final int CHANNEL_MAP_US_CABLE = (1 << 1);
  public static final int CHANNEL_MAP_US_HRC = (1 << 2);
  public static final int CHANNEL_MAP_US_IRC = (1 << 3);
  public static final int CHANNEL_MAP_US_ALL =
  	(CHANNEL_MAP_US_BCAST | CHANNEL_MAP_US_CABLE | CHANNEL_MAP_US_HRC | CHANNEL_MAP_US_IRC);

  public static final int CHANNEL_MAP_UK_BCAST = (1 << 4);
  public static final int CHANNEL_MAP_NZ_BCAST = (1 << 5);

  public static final int HDHOMERUN_CHANNELSCAN_PROGRAM_NORMAL = 0;
  public static final int HDHOMERUN_CHANNELSCAN_PROGRAM_NODATA = 1;
  public static final int HDHOMERUN_CHANNELSCAN_PROGRAM_CONTROL = 2;
  public static final int HDHOMERUN_CHANNELSCAN_PROGRAM_ENCRYPTED = 3;

  public static final int HDHOMERUN_CHANNELSCAN_OPTION_REVERSE = (1 << 0);
  public static final int HDHOMERUN_CHANNELSCAN_OPTION_REFINE_CHANNEL_MAP = (1 << 1);

  /*
   * Not ideal (unlike C and C++, Java doesn't have a const array,
   * where the using code is blocked from writing to the array).
   * 
   * TODO add a type to support this.
   * 
   * TODO Silicon dust has changed their frequency tables in more
   * recent versions.  Perhaps this data should be stored in a
   * resource file.
   */
  public static final int[][] CHANNEL_MAP_RANGES = new int[][] {
  	{CHANNEL_MAP_US_BCAST,   2,   4,  57000000, 6000000},
  	{CHANNEL_MAP_US_BCAST,   5,   6,  79000000, 6000000},
  	{CHANNEL_MAP_US_BCAST,   7,  13, 177000000, 6000000},
  	{CHANNEL_MAP_US_BCAST,  14,  69, 473000000, 6000000},

  	{CHANNEL_MAP_US_CABLE,   1,   1,  75000000, 6000000},
  	{CHANNEL_MAP_US_CABLE,   2,   4,  57000000, 6000000},
  	{CHANNEL_MAP_US_CABLE,   5,   6,  79000000, 6000000},
  	{CHANNEL_MAP_US_CABLE,   7,  13, 177000000, 6000000},
  	{CHANNEL_MAP_US_CABLE,  14,  22, 123000000, 6000000},
  	{CHANNEL_MAP_US_CABLE,  23,  94, 219000000, 6000000},
  	{CHANNEL_MAP_US_CABLE,  95,  99,  93000000, 6000000},
  	{CHANNEL_MAP_US_CABLE, 100, 136, 651000000, 6000000},

  	{CHANNEL_MAP_US_HRC,     1,   1,  73753600, 6000300},
  	{CHANNEL_MAP_US_HRC,     2,   4,  55752700, 6000300},
  	{CHANNEL_MAP_US_HRC,     5,   6,  79753900, 6000300},
  	{CHANNEL_MAP_US_HRC,     7,  13, 175758700, 6000300},
  	{CHANNEL_MAP_US_HRC,    14,  22, 121756000, 6000300},
  	{CHANNEL_MAP_US_HRC,    23,  94, 217760800, 6000300},
  	{CHANNEL_MAP_US_HRC,    95,  99,  91754500, 6000300},
  	{CHANNEL_MAP_US_HRC,   100, 136, 649782400, 6000300},

  	{CHANNEL_MAP_US_IRC,     1,   1,  75012500, 6000000},
  	{CHANNEL_MAP_US_IRC,     2,   4,  57012500, 6000000},
  	{CHANNEL_MAP_US_IRC,     5,   6,  81012500, 6000000},
  	{CHANNEL_MAP_US_IRC,     7,  13, 177012500, 6000000},
  	{CHANNEL_MAP_US_IRC,    14,  22, 123012500, 6000000},
  	{CHANNEL_MAP_US_IRC,    23,  41, 219012500, 6000000},
  	{CHANNEL_MAP_US_IRC,    42,  42, 333025000, 6000000},
  	{CHANNEL_MAP_US_IRC,    43,  94, 339012500, 6000000},
  	{CHANNEL_MAP_US_IRC,    95,  97,  93012500, 6000000},
  	{CHANNEL_MAP_US_IRC,    98,  99, 111025000, 6000000},
  	{CHANNEL_MAP_US_IRC,   100, 136, 651012500, 6000000},

  	{CHANNEL_MAP_UK_BCAST,  21,  68, 474000000, 8000000},
  	{CHANNEL_MAP_NZ_BCAST,  25,  62, 506000000, 8000000},

  	{0,                      0,   0,         0,       0}};
}
