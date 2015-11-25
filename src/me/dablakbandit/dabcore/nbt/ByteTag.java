package me.dablakbandit.dabcore.nbt;

public final class ByteTag extends Tag{

	private final byte value;

	public ByteTag(final String name, final byte value){
		super(name);
		this.value = value;
	}

	@Override
	public Byte getValue() {
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Byte" + append + ": " + value;
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
		if(!(obj instanceof ByteTag)){ return false; }
		final ByteTag other = (ByteTag)obj;
		if(value != other.value){ return false; }
		return true;
	}

}
