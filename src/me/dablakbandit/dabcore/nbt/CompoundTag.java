package me.dablakbandit.dabcore.nbt;

import java.util.Collections;
import java.util.Map;

public final class CompoundTag extends Tag{

	private final Map<String, Tag> value;

	public CompoundTag(final String name, final Map<String, Tag> value){
		super(name);
		this.value = Collections.unmodifiableMap(value);
	}

	@Override
	public Map<String, Tag> getValue(){
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		final StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_Compound" + append + ": " + value.size() + " entries\r\n{\r\n");
		for(final Map.Entry<String, Tag> entry : value.entrySet()){
			bldr.append("   " + entry.getValue().toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
		}
		bldr.append("}");
		return bldr.toString();
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj){
		if(this == obj){ return true; }
		if(!super.equals(obj)){ return false; }
		if(!(obj instanceof CompoundTag)){ return false; }
		final CompoundTag other = (CompoundTag)obj;
		if (value == null){
			if(other.value != null){ return false; }
		}else if(!value.equals(other.value)){ return false; }
		return true;
	}

}
