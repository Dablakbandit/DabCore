package me.dablakbandit.dabcore.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public final class NBTOutputStream implements Closeable{

	private final DataOutputStream os;

	public NBTOutputStream(final OutputStream os) throws IOException{
		this.os = new DataOutputStream(new GZIPOutputStream(os));
	}

	public NBTOutputStream(OutputStream os, final boolean gzipped) throws IOException{
		if(gzipped){
			os = new GZIPOutputStream(os);
		}
		this.os = new DataOutputStream(os);
	}

	public void writeTag(final Tag tag) throws IOException{
		final int type = NBTUtils.getTypeCode(tag.getClass());
		final String name = tag.getName();
		final byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
		os.writeByte(type);
		os.writeShort(nameBytes.length);
		os.write(nameBytes);
		if(type == NBTConstants.TYPE_END){ throw new IOException("[MapMaze] Named TAG_End not permitted."); }
		writeTagPayload(tag);
	}

	private void writeTagPayload(final Tag tag) throws IOException{
		final int type = NBTUtils.getTypeCode(tag.getClass());
		switch (type){
		case NBTConstants.TYPE_END:
			break;
		case NBTConstants.TYPE_BYTE:
			writeByteTagPayload((ByteTag) tag);
			break;
		case NBTConstants.TYPE_SHORT:
			writeShortTagPayload((ShortTag) tag);
			break;
		case NBTConstants.TYPE_INT:
			writeIntTagPayload((IntTag) tag);
			break;
		case NBTConstants.TYPE_LONG:
			writeLongTagPayload((LongTag) tag);
			break;
		case NBTConstants.TYPE_FLOAT:
			writeFloatTagPayload((FloatTag) tag);
			break;
		case NBTConstants.TYPE_DOUBLE:
			writeDoubleTagPayload((DoubleTag) tag);
			break;
		case NBTConstants.TYPE_BYTE_ARRAY:
			writeByteArrayTagPayload((ByteArrayTag) tag);
			break;
		case NBTConstants.TYPE_STRING:
			writeStringTagPayload((StringTag) tag);
			break;
		case NBTConstants.TYPE_LIST:
			writeListTagPayload((ListTag) tag);
			break;
		case NBTConstants.TYPE_COMPOUND:
			writeCompoundTagPayload((CompoundTag) tag);
			break;
		case NBTConstants.TYPE_INT_ARRAY:
			writeIntArrayTagPayload((IntArrayTag) tag);
			break;
		default:
			throw new IOException("[MapMaze] Invalid tag type: " + type + ".");
		}
	}

	private void writeByteTagPayload(final ByteTag tag) throws IOException{
		os.writeByte(tag.getValue());
	}

	private void writeByteArrayTagPayload(final ByteArrayTag tag) throws IOException{
		final byte[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		os.write(bytes);
	}

	private void writeCompoundTagPayload(final CompoundTag tag) throws IOException{
		for(final Tag childTag : tag.getValue().values()){
			writeTag(childTag);
		}
		os.writeByte((byte) 0);
	}

	private void writeListTagPayload(final ListTag tag) throws IOException{
		final Class<? extends Tag> clazz = tag.getType();
		final List<Tag> tags = tag.getValue();
		final int size = tags.size();
		os.writeByte(NBTUtils.getTypeCode(clazz));
		os.writeInt(size);
		for(int i = 0; i < size; i++){
			writeTagPayload(tags.get(i));
		}
	}

	private void writeStringTagPayload(final StringTag tag) throws IOException{
		final byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
		os.writeShort(bytes.length);
		os.write(bytes);
	}

	private void writeDoubleTagPayload(final DoubleTag tag) throws IOException{
		os.writeDouble(tag.getValue());
	}

	private void writeFloatTagPayload(final FloatTag tag) throws IOException{
		os.writeFloat(tag.getValue());
	}

	private void writeLongTagPayload(final LongTag tag) throws IOException{
		os.writeLong(tag.getValue());
	}

	private void writeIntTagPayload(final IntTag tag) throws IOException{
		os.writeInt(tag.getValue());
	}

	private void writeShortTagPayload(final ShortTag tag) throws IOException{
		os.writeShort(tag.getValue());
	}

	private void writeIntArrayTagPayload(final IntArrayTag tag) throws IOException{
		final int[] ints = tag.getValue();
		os.writeInt(ints.length);
		for(int i = 0; i < ints.length; i++){
			os.writeInt(ints[i]);
		}
	}

	@Override
	public void close() throws IOException{
		os.close();
	}
}
