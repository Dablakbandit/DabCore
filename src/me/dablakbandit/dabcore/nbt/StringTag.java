package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public final class StringTag extends Tag{
	private final String value;
	
	public StringTag(String name, String value){
		super(name);
		this.value = value;
	}
	
	@Override
	public String getValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof StringTag))
			return false;
		if(!super.equals(obj))
			return false;
		StringTag stringTag = (StringTag)obj;
		return Objects.equals(value, stringTag.value);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
	
	@Override
	public String toString(){
		return getTagPrefixedToString("\"", value, "\"");
	}
}
