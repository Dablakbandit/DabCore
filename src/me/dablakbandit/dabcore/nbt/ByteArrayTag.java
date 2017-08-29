package me.dablakbandit.dabcore.nbt;

import java.util.Arrays;
import java.util.Objects;

public final class ByteArrayTag extends Tag{
	private final byte[] value;
	
	public ByteArrayTag(String name, byte[] value){
		super(name);
		this.value = value;
	}
	
	@Override
	public byte[] getValue(){
		return value;
	}
	
	public int size(){
		return value.length;
	}
	
	public byte get(int index){
		return value[index];
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof ByteArrayTag))
			return false;
		if(!super.equals(obj))
			return false;
		ByteArrayTag byteArrayTag = (ByteArrayTag)obj;
		return Arrays.equals(value, byteArrayTag.value);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
	}
	
	@Override
	public String toString(){
		
		final StringBuilder hex = new StringBuilder();
		for(final byte b : value){
			final String hexDigits = Integer.toHexString(b).toUpperCase();
			if(hexDigits.length() == 1){
				hex.append("0");
			}
			hex.append(hexDigits).append(" ");
		}
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Byte_Array" + append + ": " + hex.toString();
	}
}
