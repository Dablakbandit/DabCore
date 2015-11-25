package me.dablakbandit.dabcore.nbt;

public final class StringTag extends Tag{

	private final String value;

	public StringTag(final String name, final String value){
		super(name);
		this.value = value;
	}

	@Override
	public String getValue(){
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_String" + append + ": " + value;
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
		if(!(obj instanceof StringTag)){ return false; }
		final StringTag other = (StringTag) obj;
		if(value == null){
			if(other.value != null){ return false; }
		} else if(!value.equals(other.value)){ return false; }
		return true;
	}

}
