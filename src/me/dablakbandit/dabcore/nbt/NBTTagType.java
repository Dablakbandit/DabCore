package me.dablakbandit.dabcore.nbt;

public enum NBTTagType{
	TAG_END(0, EndTag.class, "TAG_End"), TAG_BYTE(1, ByteTag.class, "TAG_Byte"), TAG_SHORT(2, ShortTag.class, "TAG_Short"), TAG_INT(3, IntTag.class, "TAG_Int"), TAG_LONG(4, LongTag.class, "TAG_Long"), TAG_FLOAT(5, FloatTag.class, "TAG_Float"), TAG_DOUBLE(6, DoubleTag.class, "TAG_Double"),
	TAG_BYTE_ARRAY(7, ByteArrayTag.class, "TAG_Byte_Array"), TAG_STRING(8, StringTag.class, "TAG_String"), TAG_LIST(9, ListTag.class, "TAG_List"), TAG_COMPOUND(10, CompoundTag.class, "TAG_Compound"), TAG_INT_ARRAY(11, IntArrayTag.class, "TAG_Int_Array");
	
	private final int					typeByte;
	private final Class<? extends Tag>	tagClass;
	private final String				mojangName;
	
	NBTTagType(int typeByte, Class<? extends Tag> tagClass, String mojangName){
		this.typeByte = typeByte;
		this.tagClass = tagClass;
		this.mojangName = mojangName;
	}
	
	public int getTypeByte(){
		return typeByte;
	}
	
	public Class<? extends Tag> getTagClass(){
		return tagClass;
	}
	
	public String getMojangName(){
		return mojangName;
	}
	
	public static NBTTagType fromTypeByte(int typeByte){
		for(NBTTagType value : values())
			if(value.typeByte == typeByte)
				return value;
		throw new IllegalArgumentException("[JNBT] No " + NBTCompression.class.getSimpleName() + " enum constant with typeByte: " + typeByte);
	}
	
	public static NBTTagType fromTagClass(Class<? extends Tag> tagClass){
		for(NBTTagType value : values())
			if(value.tagClass == tagClass)
				return value;
		throw new IllegalArgumentException("[JNBT] No " + NBTCompression.class.getSimpleName() + " enum constant with tagClass: " + tagClass);
	}
	
	public static NBTTagType fromMojangName(String mojangName){
		for(NBTTagType value : values())
			if(value.mojangName.equals(mojangName))
				return value;
		throw new IllegalArgumentException("[JNBT] No " + NBTCompression.class.getSimpleName() + " enum constant with mojangName: " + mojangName);
	}
}
