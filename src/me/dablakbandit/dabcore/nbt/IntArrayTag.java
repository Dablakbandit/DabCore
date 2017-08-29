package me.dablakbandit.dabcore.nbt;

import java.util.Arrays;
import java.util.Objects;

public final class IntArrayTag extends Tag{
	private final int[] value;
	
	public IntArrayTag(String name, int[] value){
		super(name);
		this.value = value;
	}
	
	@Override
	public int[] getValue(){
		return value;
	}
	
	public int size(){
		return value.length;
	}
	
	public int get(int index){
		return value[index];
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof IntArrayTag))
			return false;
		if(!super.equals(obj))
			return false;
		IntArrayTag intArrayTag = (IntArrayTag)obj;
		return Arrays.equals(value, intArrayTag.value);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
	
	@Override
	public String toString(){
		
		final StringBuilder integers = new StringBuilder();
		for(final int b : value){
			integers.append(b).append(" ");
		}
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Int_Array" + append + ": " + integers.toString();
	}
}
