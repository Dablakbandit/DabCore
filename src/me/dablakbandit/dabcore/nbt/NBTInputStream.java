package me.dablakbandit.dabcore.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public final class NBTInputStream implements Closeable{
	private final DataInputStream is;
	
	@Deprecated
	public NBTInputStream(InputStream is) throws IOException{
		this(is, NBTCompression.GZIP);
	}
	
	@Deprecated
	public NBTInputStream(InputStream is, boolean gzipped) throws IOException{
		this(is, gzipped ? NBTCompression.GZIP : NBTCompression.UNCOMPRESSED);
	}
	
	@Deprecated
	public NBTInputStream(DataInputStream is){
		this.is = is;
	}
	
	public NBTInputStream(InputStream is, NBTCompression compression) throws IOException{
		NBTCompression resolvedCompression;
		if(compression == NBTCompression.FROM_BYTE){
			int compressionByte = is.read();
			if(compressionByte < 0){
				// noinspection NewExceptionWithoutArguments
				throw new EOFException();
			}
			resolvedCompression = NBTCompression.fromId(compressionByte);
		}else{
			resolvedCompression = compression;
		}
		
		switch(resolvedCompression){
		case UNCOMPRESSED:
			this.is = new DataInputStream(is);
			break;
		case GZIP:
			this.is = new DataInputStream(new GZIPInputStream(is));
			break;
		case ZLIB:
			this.is = new DataInputStream(new InflaterInputStream(is));
			break;
		case FROM_BYTE:
			throw new AssertionError("FROM_BYTE Should have been handled already");
		default:
			throw new AssertionError("[JNBT] Unimplemented " + NBTCompression.class.getSimpleName() + ": " + compression);
		}
	}
	
	public Tag readTag() throws IOException{
		return readTag(0);
	}
	
	private Tag readTag(int depth) throws IOException{
		int type = is.readByte() & 0xFF;
		
		String name;
		if(type == NBTConstants.TYPE_END){
			name = "";
		}else{
			int nameLength = is.readShort() & 0xFFFF;
			byte[] nameBytes = new byte[nameLength];
			is.readFully(nameBytes);
			name = new String(nameBytes, NBTConstants.CHARSET);
		}
		
		return readTagPayload(type, name, depth);
	}
	
	private Tag readTagPayload(int type, String name, int depth) throws IOException{
		switch(type){
		case NBTConstants.TYPE_END:
			return readEndTagPayload(depth);
		case NBTConstants.TYPE_BYTE:
			return readByteTagPayload(name);
		case NBTConstants.TYPE_SHORT:
			return readShortTagPayload(name);
		case NBTConstants.TYPE_INT:
			return readIntTagPayload(name);
		case NBTConstants.TYPE_LONG:
			return readLongTagPayload(name);
		case NBTConstants.TYPE_FLOAT:
			return readFloatTagPayload(name);
		case NBTConstants.TYPE_DOUBLE:
			return readDoubleTagPayload(name);
		case NBTConstants.TYPE_BYTE_ARRAY:
			return readByteArrayTagPayload(name);
		case NBTConstants.TYPE_STRING:
			return readStringTagPayload(name);
		case NBTConstants.TYPE_LIST:
			return readListTagPayload(name, depth);
		case NBTConstants.TYPE_COMPOUND:
			return readCompoundTagPayload(name, depth);
		case NBTConstants.TYPE_INT_ARRAY:
			return readIntArrayPayload(name);
		default:
			throw new IOException("[JNBT] Invalid tag type: " + type + '.');
		}
	}
	
	private Tag readEndTagPayload(int depth) throws IOException{
		if(depth == 0)
			throw new IOException("[JNBT] TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
		
		return new EndTag();
	}
	
	private Tag readByteTagPayload(String name) throws IOException{
		return new ByteTag(name, is.readByte());
	}
	
	private Tag readShortTagPayload(String name) throws IOException{
		return new ShortTag(name, is.readShort());
	}
	
	private Tag readIntTagPayload(String name) throws IOException{
		return new IntTag(name, is.readInt());
	}
	
	private Tag readLongTagPayload(String name) throws IOException{
		return new LongTag(name, is.readLong());
	}
	
	private Tag readFloatTagPayload(String name) throws IOException{
		return new FloatTag(name, is.readFloat());
	}
	
	private Tag readDoubleTagPayload(String name) throws IOException{
		return new DoubleTag(name, is.readDouble());
	}
	
	private Tag readByteArrayTagPayload(String name) throws IOException{
		int length = is.readInt();
		byte[] bytes = new byte[length];
		is.readFully(bytes);
		return new ByteArrayTag(name, bytes);
	}
	
	private Tag readStringTagPayload(String name) throws IOException{
		int length = is.readShort();
		byte[] bytes = new byte[length];
		is.readFully(bytes);
		String string = new String(bytes, NBTConstants.CHARSET);
		return new StringTag(name, string);
	}
	
	private Tag readListTagPayload(String name, int depth) throws IOException{
		int typeByte = is.readByte();
		int length = is.readInt();
		List<Tag> tagList = new ArrayList<>(length);
		for(int i = 0; i < length; i++){
			Tag tag = readTagPayload(typeByte, "", depth + 1);
			if(tag instanceof EndTag)
				throw new IOException("[JNBT] TAG_End not permitted in a list.");
			tagList.add(tag);
		}
		NBTTagType tagType = NBTTagType.fromTypeByte(typeByte);
		return new ListTag(name, tagType, tagList);
	}
	
	private Tag readCompoundTagPayload(String name, int depth) throws IOException{
		Map<String, Tag> tagMap = new HashMap<>(CompoundTag.DEFAULT_INITIAL_CAPACITY);
		while(true){
			Tag tag = readTag(depth + 1);
			if(tag instanceof EndTag)
				break;
			
			tagMap.put(tag.getName(), tag);
		}
		return new CompoundTag(name, tagMap);
	}
	
	private Tag readIntArrayPayload(String name) throws IOException{
		int length = is.readInt();
		int[] ints = new int[length];
		for(int i = 0; i < length; i++)
			ints[i] = is.readInt();
		
		return new IntArrayTag(name, ints);
	}
	
	@Override
	public void close() throws IOException{
		is.close();
	}
}
