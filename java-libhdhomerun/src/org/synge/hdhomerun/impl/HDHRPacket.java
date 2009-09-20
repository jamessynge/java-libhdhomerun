package org.synge.hdhomerun.impl;

import static org.synge.hdhomerun.HDHRPacketType.GetSetReply;
import static org.synge.hdhomerun.HDHRPacketType.GetSetRequest;
import static org.synge.hdhomerun.HDHRTag.ErrorMessage;
import static org.synge.hdhomerun.HDHRTag.GetSetName;
import static org.synge.hdhomerun.HDHRTag.GetSetValue;
import static org.synge.hdhomerun.impl.HDHomeRunConstants.HDHOMERUN_MAX_PACKET_SIZE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.zip.CRC32;

import org.synge.hdhomerun.HDHRPacketType;
import org.synge.hdhomerun.HDHRTag;


public class HDHRPacket {
	private final ByteBuffer bb;
	private InetSocketAddress receivedFrom = null;

	public HDHRPacket(ByteBuffer byteBuffer) {
		this.bb = byteBuffer;
	}

	public HDHRPacket() {
		this.bb = ByteBuffer.allocate(HDHOMERUN_MAX_PACKET_SIZE);
	}

	public static void makeGetSetRequest(SocketChannel socket, String name, String value) throws IOException {
		HDHRPacket packet = new HDHRPacket();
		packet.start(GetSetRequest);
		packet.putTLV(GetSetName, name);
		if (value != null)
			packet.putTLV(GetSetValue, value);
		packet.end();
		packet.write(socket);
	}

	public static String recvGetSetResponse(SocketChannel socket, String name) throws IOException {
		HDHRPacket packet = new HDHRPacket();
		packet.read(socket);

		String msg = packet.validate();
		if (msg != null)
			throw new IllegalStateException(msg);

		String result = packet.getTagValueString(GetSetReply, GetSetName);
		if (!name.equals(result)) {
			msg = String.format("Unexpected order of responses.\n\tExpected: %s\n\tActual: %s",
					name, result);
			throw new IllegalStateException(msg);
		}

		result = packet.getTagValueString(GetSetReply, GetSetValue);
		if (result != null)
			return result;

		result = packet.getTagValueString(GetSetRequest, ErrorMessage);
		if (result != null)
			throw new IllegalArgumentException(result);

		throw new IllegalStateException("Should have received a valid response");
	}

	/**
	 * Clears the byte buffer, and prepares it to send a packet
	 * of type <tt>packetType</tt>.
	 * 
	 * @param packetType
	 */
	public void start(HDHRPacketType type) {
		bb.clear();
		bb.putShort(type.code);
		bb.putShort((short) 0);	// Use zero for the length until we know the length.
		return;
	}

	/**
	 * Adds the length and CRC to the packet, and flips the buffer
	 * (ready for writing).
	 */
	public void end()
	{
		// Set the length now that we know it.
		int currentLen = bb.position();
		int payloadLength = currentLen - 4;

		bb.putShort(2, (short) payloadLength);

		// Compute the CRC of the header and payload.

		int crc = computeCRC(currentLen);

		// Append the CRC after the payload.
		putCRC(crc);

		bb.flip();
		return;
	}

	private int computeCRC(int currentLen) {
		CRC32 crc32 = new CRC32();
		if (bb.hasArray()) {
			byte[] bytes = bb.array();
			int start = bb.arrayOffset();
			crc32.update(bytes, start, currentLen);
		}
		else {
			for (int index = 0; index < currentLen; index++) {
				byte b = bb.get(index);
				crc32.update(b);
			}
		}

		int crc = (int) (crc32.getValue() & 0xffffffff);
		return crc;
	}

	/**
	 * Adds a tag and its integer value.
	 * @param tag
	 * @param value
	 */
	public void putTLV(HDHRTag tag, int value) {
		bb.put(tag.code);
		putVarlen(4);
		bb.putInt(value);
	}

	/**
	 * Adds a tag and its string value.
	 * @param tag
	 * @param value
	 * @throws UnsupportedEncodingException If the value is not an ASCII string
	 */
	public void putTLV(HDHRTag tag, String value) throws UnsupportedEncodingException {
		byte[] bytes = value.getBytes("ascii");
		bb.put(tag.code);
		putVarlen(bytes.length + 1);
		bb.put(bytes);
		bb.put((byte) 0);
		return;
	}

	private void putVarlen(int len) {
		if (len <= 127) {
			bb.put((byte) (len & 0x7f));
		}
		else {
			int lo = 0x80 | (len & 0x7f);
			int hi = (len >>> 7);
			bb.put((byte) (lo & 0xff));
			bb.put((byte) (hi & 0xff));
		}
		return;
	}

	private void putCRC(int crc) {
		bb.order(ByteOrder.LITTLE_ENDIAN);
		try {
			bb.putInt(crc);
		}
		finally {
			bb.order(ByteOrder.BIG_ENDIAN);
		}
		return;
	}

	// Methods for "parsing" a packet.

	public HDHRPacketType getPacketType() {
		short s = bb.getShort();
		HDHRPacketType pt = HDHRPacketType.lookup(s);
		return pt;
	}

	public short getPayloadLength() {
		short s = bb.getShort();
		return s;
	}

	public HDHRTag getTag() {
		byte b = bb.get();
		HDHRTag tag = HDHRTag.lookup(b);
		return tag;
	}

	public short getVarlen() {
		byte b = bb.get();
		if ((b & 0x80) == 0)
			return b;

		int len = b & 0x7f;
		b = bb.get();
		len |= ((b & 0xff) << 7);
		return (short) (len & 0xffff);
	}

	public int getInt() {
		int i = bb.getInt();
		return i;
	}

	public byte getByte() {
		return bb.get();
	}

	private boolean locateTag(HDHRPacketType type, HDHRTag tag) {
		bb.position(0);
		HDHRPacketType typeInPacket = getPacketType();
		if (typeInPacket != type)
			return false;		// Wrong type of packet

		int crcStart = 4 + getPayloadLength();

		while (bb.position() < crcStart - 1) {
			HDHRTag tagInPacket = getTag();
			if (tagInPacket == tag) {
				return true;	// Found the tag; now at the length
			}

			short len = getVarlen();
			bb.position(bb.position() + len);
		}

		return false;		// Tag not found
	}

	public byte[] getTagValueBytes(HDHRPacketType type, HDHRTag tag) {
		int originalPosition = bb.position();
		try {
			if (!locateTag(type, tag))
				return null;

			short len = getVarlen();
			byte[] bytes = new byte[len];
			bb.get(bytes);
			return bytes;
		}
		finally {
			bb.position(originalPosition);
		}
	}

  public Integer getTagValueInteger(HDHRPacketType type, HDHRTag tag) {
    int originalPosition = bb.position();
    try {
      if (!locateTag(type, tag))
        return null;

      short len = getVarlen();
      if (len != 4) {
        return null;
      }

      int result = bb.getInt();
      return Integer.valueOf(result);
    }
    finally {
      bb.position(originalPosition);
    }
  }

	public String getTagValueString(HDHRPacketType type, HDHRTag tag) {
		int originalPosition = bb.position();
		try {
			if (!locateTag(type, tag))
				return null;

			short len = getVarlen();
			if (len > 0)
				len -= 1;
			char[] chars = new char[len];
			for (int i = 0; i < chars.length; i++) {
				byte b = bb.get();
				chars[i] = (char) (b & 0x7f);	// Assuming ASCII
			}

			String result = new String(chars);
			return result;
		}
		finally {
			bb.position(originalPosition);
		}
	}
	
	public int getPayloadRemaining() {
		return bb.remaining() - 4;
	}
	
	public String validate() {
		if (bb.position() != 0)
			return "ByteBuffer must be ready for reading"; 
		int limit = bb.limit();
		if (limit < 8)
			return "Packet is too short: " + limit;
		if (limit > HDHomeRunConstants.HDHOMERUN_MAX_PACKET_SIZE)
			return "Packet is too long: " + limit;

		bb.mark();
		try {

			if (getPacketType() == null)
				return String.format("Not a valid packet type: 0x%04x", bb.getShort(0));

			short pl = getPayloadLength();
			if (pl != limit - 8)
				return String.format("Expected payload size of %d, but found %d", limit - 8, pl);

			int end = limit - 4;
			while (bb.position() < end - 1) {
				String s = validateTLV(end);
				if (s != null)
					return s;
			}

			if (bb.position() != end)
				return "Payload did not end at CRC";

			int crcOfData = computeCRC(end);
			int crcInPacket = getCRC();

			if (crcOfData != crcInPacket)
				return String.format("CRC check failed;\n\tcomputed: 0x%08x\n\treceived: 0x%08x", crcOfData, crcInPacket);

			return null;
		}
		finally {
			bb.reset();
		}
	}

	private String validateTLV(int end) {
		getTag();	// Ignore result; not required to be the set of tags known to this program.
		short len = getVarlen();
		if (len < 0)
			return "Data length is negative";
		if (bb.position() + len > end)
			return String.format("Data length (%d) is too long (> %d)", len, end - bb.position());
		bb.position(bb.position() + len);
		return null;
	}

	private int getCRC() {
		bb.order(ByteOrder.LITTLE_ENDIAN);
		try {
			return bb.getInt();
		}
		finally {
			bb.order(ByteOrder.BIG_ENDIAN);
		}
	}

	public int send(DatagramChannel channel, SocketAddress destination) throws IOException {
	  int bytesSent = channel.send(bb, destination);
	  return bytesSent;
	}

	public InetSocketAddress receive(DatagramChannel channel) throws IOException {
		bb.clear();
		SocketAddress sourceAddress = channel.receive(bb);
		if (sourceAddress != null) {
			bb.flip();
		}
		setReceivedFrom(sourceAddress);
		return getReceivedFrom();
	}

	public InetSocketAddress getReceivedFrom() {
		return receivedFrom;
	}

  public void setReceivedFrom(SocketAddress sa) {
    if (sa == null) {
      receivedFrom = null;
    }
    else if (sa instanceof InetSocketAddress) {
      receivedFrom = (InetSocketAddress) sa;
    }
    else {
      throw new IllegalArgumentException("The address must be an InetSocketAddress, not a " + sa.getClass().getCanonicalName());
    }
    return;
  }

	public int write(SocketChannel channel) throws IOException {
	  int bytesSent = 0;
		while (bb.remaining() > 0) {
			bytesSent = channel.write(bb);
		}
		setReceivedFrom(null);
	  return bytesSent;
	}

	// TODO Deal with partial packets.
	public int read(SocketChannel channel) throws IOException {
		// Read the minimum length (header + CRC).
		bb.clear();
		bb.limit(8);
		do {
			channel.read(bb);
		} while (bb.remaining() > 0);

		// Determine the entire length.

		short payloadLength = bb.getShort(2);
		if (payloadLength < 0 || payloadLength > HDHomeRunConstants.HDHOMERUN_MAX_PAYLOAD_SIZE)
			throw new IllegalStateException("Invalid payload length: " + payloadLength);

		int totalLength = payloadLength + 8;
		bb.limit(totalLength);
		while (bb.remaining() > 0) {
			channel.read(bb);
		}

		bb.flip();
		setReceivedFrom(channel.socket().getRemoteSocketAddress());
		return totalLength;
	}

  public void rewind() {
    bb.rewind();
    return;
  }
}
