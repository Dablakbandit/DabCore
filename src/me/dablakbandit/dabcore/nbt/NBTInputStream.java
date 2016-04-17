package me.dablakbandit.dabcore.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class NBTInputStream implements Closeable {

	private final DataInputStream is;

	public NBTInputStream(InputStream is, final boolean gzipped) throws IOException{
		if(gzipped){
			is = new GZIPInputStream(is);
		}
		this.is = new DataInputStream(is);
	}

	public NBTInputStream(final InputStream is) throws IOException{
		this.is = new DataInputStream(new GZIPInputStream(is));
	}

	public NBTInputStream(final DataInputStream is){
		this.is = is;
	}

	public Tag readTag() throws IOException{
		return readTag(0);
	}

	private Tag readTag(final int depth) throws IOException{
		final int type = is.readByte() & 0xFF;
		String name;
		if(type != NBTConstants.TYPE_END){
			final int nameLength = is.readShort() & 0xFFFF;
			final byte[] nameBytes = new byte[nameLength];
			is.readFully(nameBytes);
			name = new String(nameBytes, NBTConstants.CHARSET);
		} else {
			name = "";
		}
		return readTagPayload(type, name, depth);
	}

	private Tag readTagPayload(final int type, final String name, final int depth) throws IOException{
		switch (type){
		case NBTConstants.TYPE_END:
			if(depth == 0){
				throw new IOException("[DabCore] TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
			}else {
				return new EndTag();
			}
		case NBTConstants.TYPE_BYTE:
			return new ByteTag(name, is.readByte());
		case NBTConstants.TYPE_SHORT:
			return new ShortTag(name, is.readShort());
		case NBTConstants.TYPE_INT:
			return new IntTag(name, is.readInt());
		case NBTConstants.TYPE_LONG:
			return new LongTag(name, is.readLong());
		case NBTConstants.TYPE_FLOAT:
			return new FloatTag(name, is.readFloat());
		case NBTConstants.TYPE_DOUBLE:
			return new DoubleTag(name, is.readDouble());
		case NBTConstants.TYPE_BYTE_ARRAY:
			int length = is.readInt();
			byte[] bytes = new byte[length];
			is.readFully(bytes);
			return new ByteArrayTag(name, bytes);
		case NBTConstants.TYPE_STRING:
			length = is.readShort();
			bytes = new byte[length];
			is.readFully(bytes);
			return new StringTag(name, new String(bytes, NBTConstants.CHARSET));
		case NBTConstants.TYPE_LIST:
			final int childType = is.readByte();
			length = is.readInt();
			final List<Tag> tagList = new ArrayList<Tag>();
			for(int i = 0; i < length; i++){
				final Tag tag = readTagPayload(childType, "", depth + 1);
				if(tag instanceof EndTag){ throw new IOException("[DabCore] TAG_End not permitted in a list."); }
				tagList.add(tag);
			}
			return new ListTag(name, NBTUtils.getTypeClass(childType), tagList);
		case NBTConstants.TYPE_COMPOUND:
			final Map<String, Tag> tagMap = new HashMap<String, Tag>();
			while(true){
				final Tag tag = readTag(depth + 1);
				if(tag instanceof EndTag){
					break;
				}else{
					tagMap.put(tag.getName(), tag);
				}
			}
			return new CompoundTag(name, tagMap);
		case NBTConstants.TYPE_INT_ARRAY:
			length = is.readInt();
			final int[] ints = new int[length];
			for(int i = 0; i < length; i++){
				ints[i] = is.readInt();
			}
			return new IntArrayTag(name, ints);
		default:
			throw new IOException("[DabCore] Invalid tag type: " + type + ".");
		}
	}

	@Override
	public void close() throws IOException{
		is.close();
	}
}
