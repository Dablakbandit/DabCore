package me.dablakbandit.dabcore.nbt;

public final class ShortTag extends Tag{

	private final short value;

	public ShortTag(final String name, final short value){
		super(name);
		this.value = value;
	}

	@Override
	public Short getValue(){
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Short" + append + ": " + value;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + value;
		return result;
	}

	@Override
	public boolean equals(final Object obj){
		if(this == obj){ return true; }
		if(!super.equals(obj)){ return false; }
		if(!(obj instanceof ShortTag)){ return false; }
		final ShortTag other = (ShortTag) obj;
		if(value != other.value){ return false; }
		return true;
	}

}
