package me.dablakbandit.dabcore.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ListTag extends Tag{
	
	public static final int		DEFAULT_INITIAL_CAPACITY	= 4;
	
	private final NBTTagType	type;
	private final List<Tag>		value;
	
	public ListTag(String name, NBTTagType type, List<Tag> value){
		super(name);
		this.type = type;
		this.value = value;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListTag(String name, NBTTagType type){
		this(name, type, new ArrayList());
	}
	
	public NBTTagType getType(){
		return type;
	}
	
	@Override
	public List<Tag> getValue(){
		return value;
	}
	
	public int size(){
		return value.size();
	}
	
	public Tag get(int index){
		return value.get(index);
	}
	
	public void addTag(Tag tag){
		value.add(tag);
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof ListTag))
			return false;
		if(!super.equals(obj))
			return false;
		ListTag listTag = (ListTag)obj;
		return Objects.equals(type, listTag.type) && Objects.equals(value, listTag.value);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), type, value);
	}
	
	@Override
	public String toString(){
		
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		final StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_List" + append + ": " + value.size() + " entries of type " + type.toString() + "\r\n{\r\n");
		for(final Tag t : value){
			bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
		}
		bldr.append("}");
		return bldr.toString();
	}
}
