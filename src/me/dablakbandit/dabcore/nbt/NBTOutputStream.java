package me.dablakbandit.dabcore.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterOutputStream;

public final class NBTOutputStream implements Closeable{
	private final DataOutputStream os;
	
	@Deprecated
	public NBTOutputStream(OutputStream os) throws IOException{
		this(os, NBTCompression.GZIP);
	}
	
	@Deprecated
	public NBTOutputStream(OutputStream os, boolean gzipped) throws IOException{
		this(os, gzipped ? NBTCompression.GZIP : NBTCompression.UNCOMPRESSED);
	}
	
	public NBTOutputStream(OutputStream os, NBTCompression compression) throws IOException{
		switch(compression){
		case UNCOMPRESSED:
			this.os = new DataOutputStream(os);
			break;
		case GZIP:
			this.os = new DataOutputStream(new GZIPOutputStream(os));
			break;
		case ZLIB:
			this.os = new DataOutputStream(new InflaterOutputStream(os));
			break;
		case FROM_BYTE:
			throw new IllegalArgumentException(NBTCompression.FROM_BYTE.name() + " is only for reading.");
		default:
			throw new AssertionError("[JNBT] Unimplemented " + NBTCompression.class.getSimpleName() + ": " + compression);
		}
	}
	
	public void writeTag(Tag tag) throws IOException{
		NBTTagType type = NBTTagType.fromTagClass(tag.getClass());
		byte[] nameBytes = tag.getName().getBytes(NBTConstants.CHARSET);
		
		if(type == NBTTagType.TAG_END)
			throw new IOException("[JNBT] Named TAG_End not permitted.");
		
		os.writeByte(type.getTypeByte());
		os.writeShort(nameBytes.length);
		os.write(nameBytes);
		
		writeTagPayload(tag);
	}
	
	private void writeTagPayload(Tag tag) throws IOException{
		NBTTagType type = NBTTagType.fromTagClass(tag.getClass());
		switch(type){
		case TAG_END:
			writeEndTagPayload((EndTag)tag);
			break;
		case TAG_BYTE:
			writeByteTagPayload((ByteTag)tag);
			break;
		case TAG_SHORT:
			writeShortTagPayload((ShortTag)tag);
			break;
		case TAG_INT:
			writeIntTagPayload((IntTag)tag);
			break;
		case TAG_LONG:
			writeLongTagPayload((LongTag)tag);
			break;
		case TAG_FLOAT:
			writeFloatTagPayload((FloatTag)tag);
			break;
		case TAG_DOUBLE:
			writeDoubleTagPayload((DoubleTag)tag);
			break;
		case TAG_BYTE_ARRAY:
			writeByteArrayTagPayload((ByteArrayTag)tag);
			break;
		case TAG_STRING:
			writeStringTagPayload((StringTag)tag);
			break;
		case TAG_LIST:
			writeListTagPayload((ListTag)tag);
			break;
		case TAG_COMPOUND:
			writeCompoundTagPayload((CompoundTag)tag);
			break;
		case TAG_INT_ARRAY:
			writeIntArrayTagPayload((IntArrayTag)tag);
			break;
		default:
			throw new AssertionError("[JNBT] Unimplemented " + NBTTagType.class.getSimpleName() + ": " + type);
		}
	}
	
	private void writeByteTagPayload(ByteTag tag) throws IOException{
		os.writeByte(tag.getValue());
	}
	
	private void writeByteArrayTagPayload(ByteArrayTag tag) throws IOException{
		byte[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		os.write(bytes);
	}
	
	private void writeCompoundTagPayload(CompoundTag tag) throws IOException{
		for(Tag childTag : tag.getValue().values())
			writeTag(childTag);
		
		os.writeByte((byte)0); // end tag - better way?
	}
	
	private void writeListTagPayload(ListTag tag) throws IOException{
		NBTTagType tagType = tag.getType();
		List<Tag> tags = tag.getValue();
		int length = tags.size();
		
		os.writeByte(tagType.getTypeByte());
		os.writeInt(length);
		for(Tag t : tags)
			writeTagPayload(t);
	}
	
	private void writeStringTagPayload(StringTag tag) throws IOException{
		byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
		os.writeShort(bytes.length);
		os.write(bytes);
	}
	
	private void writeDoubleTagPayload(DoubleTag tag) throws IOException{
		os.writeDouble(tag.getValue());
	}
	
	private void writeFloatTagPayload(FloatTag tag) throws IOException{
		os.writeFloat(tag.getValue());
	}
	
	private void writeLongTagPayload(LongTag tag) throws IOException{
		os.writeLong(tag.getValue());
	}
	
	private void writeIntTagPayload(IntTag tag) throws IOException{
		os.writeInt(tag.getValue());
	}
	
	private void writeShortTagPayload(ShortTag tag) throws IOException{
		os.writeShort(tag.getValue());
	}
	
	private void writeIntArrayTagPayload(IntArrayTag tag) throws IOException{
		int[] ints = tag.getValue();
		os.writeInt(ints.length);
		for(int i : ints)
			os.writeInt(i);
	}
	
	private void writeEndTagPayload(EndTag tag){
	}
	
	@Override
	public void close() throws IOException{
		os.close();
	}
}
