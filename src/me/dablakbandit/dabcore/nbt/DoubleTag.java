package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public final class DoubleTag extends Tag{
	private final double value;
	
	public DoubleTag(String name, double value){
		super(name);
		this.value = value;
	}
	
	@Override
	public Double getValue(){
		return value;
	}
	
	public double doubleValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof DoubleTag))
			return false;
		if(!super.equals(obj))
			return false;
		DoubleTag doubleTag = (DoubleTag)obj;
		return Double.compare(doubleTag.value, value) == 0;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
}
