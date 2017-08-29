package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public final class ByteTag extends Tag{
	private final byte value;
	
	public ByteTag(String name, byte value){
		super(name);
		this.value = value;
	}
	
	public ByteTag(String name, int value){
		this(name, (byte)value);
	}
	
	@Override
	public Byte getValue(){
		return value;
	}
	
	public byte byteValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof ByteTag))
			return false;
		if(!super.equals(obj))
			return false;
		ByteTag byteTag = (ByteTag)obj;
		return value == byteTag.value;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
}
