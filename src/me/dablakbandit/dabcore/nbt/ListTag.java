package me.dablakbandit.dabcore.nbt;

import java.util.Collections;
import java.util.List;

public final class ListTag extends Tag{

	private final Class<? extends Tag> type;

	private final List<Tag> value;

	public ListTag(final String name, final Class<? extends Tag> type, final List<Tag> value){
		super(name);
		this.type = type;
		this.value = Collections.unmodifiableList(value);
	}

	public Class<? extends Tag> getType(){
		return type;
	}

	@Override
	public List<Tag> getValue(){
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
		bldr.append("TAG_List" + append + ": " + value.size() + " entries of type " + NBTUtils.getTypeName(type) + "\r\n{\r\n");
		for(final Tag t : value){
			bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
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
		if(!(obj instanceof ListTag)){ return false; }
		final ListTag other = (ListTag) obj;
		if(value == null){
			if(other.value != null){ return false; }
		} else if(!value.equals(other.value)){ return false; }
		return true;
	}

}
