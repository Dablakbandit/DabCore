package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public final class IntTag extends Tag{
	private final int value;
	
	public IntTag(String name, int value){
		super(name);
		this.value = value;
	}
	
	@Override
	public Integer getValue(){
		return value;
	}
	
	public int intValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof IntTag))
			return false;
		if(!super.equals(obj))
			return false;
		IntTag intTag = (IntTag)obj;
		return value == intTag.value;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
}
