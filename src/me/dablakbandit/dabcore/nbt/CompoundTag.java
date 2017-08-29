package me.dablakbandit.dabcore.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CompoundTag extends Tag{
	
	public static final int			DEFAULT_INITIAL_CAPACITY	= 32;
	
	private final Map<String, Tag>	value;
	
	public CompoundTag(String name, Map<String, Tag> value){
		super(name);
		this.value = value;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CompoundTag(String name){
		this(name, new HashMap());
	}
	
	@Override
	public Map<String, Tag> getValue(){
		return value;
	}
	
	public void addTag(Tag tag){
		value.put(tag.getName(), tag);
	}
	
	public Tag getTag(String key){
		return value.get(key);
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(!(obj instanceof CompoundTag))
			return false;
		if(!super.equals(obj))
			return false;
		CompoundTag compoundTag = (CompoundTag)obj;
		return Objects.equals(value, compoundTag.value);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(super.hashCode(), value);
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
}
