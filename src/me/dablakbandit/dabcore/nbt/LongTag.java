package me.dablakbandit.dabcore.nbt;

public final class LongTag extends Tag{

	private final long value;

	public LongTag(final String name, final long value){
		super(name);
		this.value = value;
	}

	@Override
	public Long getValue(){
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Long" + append + ": " + value;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj){
		if(this == obj){ return true; }
		if(!super.equals(obj)){ return false; }
		if(!(obj instanceof LongTag)){ return false; }
		final LongTag other = (LongTag) obj;
		if(value != other.value){ return false; }
		return true;
	}

}
