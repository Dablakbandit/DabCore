package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public final class FloatTag extends Tag{
	private final float value;
	
	public FloatTag(String name, float value){
		super(name);
		this.value = value;
	}
	
	@Override
	public Float getValue(){
		return value;
	}
	
	public float floatValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof FloatTag))
			return false;
		if(!super.equals(obj))
			return false;
		FloatTag floatTag = (FloatTag)obj;
		return Float.compare(floatTag.value, value) == 0;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
}
