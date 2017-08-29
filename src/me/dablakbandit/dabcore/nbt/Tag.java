package me.dablakbandit.dabcore.nbt;

import java.util.Objects;

public abstract class Tag{
	private final String name;
	
	protected Tag(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract Object getValue();
	
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Tag))
			return false;
		Tag tag = (Tag)o;
		return Objects.equals(name, tag.name);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(name);
	}
	
	@Override
	public String toString(){
		return getTagPrefixedToString(getValue().toString());
	}
	
	protected String getTagPrefixedToString(String... valueParts){
		StringBuilder out = new StringBuilder(32);
		out.append(getClass().getSimpleName());
		out.append(name == null ? "" : "(\"" + name + "\")");
		out.append(": ");
		for(String valuePart : valueParts)
			out.append(valuePart);
		return out.toString();
	}
}
