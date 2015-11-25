package me.dablakbandit.dabcore.nbt;

import java.util.Arrays;

public final class IntArrayTag extends Tag{

	private final int[] value;

	public IntArrayTag(final String name, final int[] value){
		super(name);
		this.value = value;
	}

	@Override
	public int[] getValue(){
		return value;
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

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(final Object obj){
		if(this == obj){ return true; }
		if(!super.equals(obj)){ return false; }
		if(!(obj instanceof IntArrayTag)){ return false; }
		final IntArrayTag other = (IntArrayTag) obj;
		if(!Arrays.equals(value, other.value)){ return false; }
		return true;
	}

}
