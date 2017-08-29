package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public final class ShortTag extends Tag{
	private final short value;
	
	public ShortTag(String name, short value){
		super(name);
		this.value = value;
	}
	
	public ShortTag(String name, int value){
		this(name, (short)value);
	}
	
	@Override
	public Short getValue(){
		return value;
	}
	
	public short shortValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof ShortTag))
			return false;
		if(!super.equals(obj))
			return false;
		ShortTag shortTag = (ShortTag)obj;
		return value == shortTag.value;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
}
