package me.dablakbandit.dabcore.nbt;

public final class FloatTag extends Tag{

	private final float value;

	public FloatTag(final String name, final float value){
		super(name);
		this.value = value;
	}

	@Override
	public Float getValue(){
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Float" + append + ": " + value;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + Float.floatToIntBits(value);
		return result;
	}

	@Override
	public boolean equals(final Object obj){
		if(this == obj){ return true; }
		if(!super.equals(obj)){ return false; }
		if(!(obj instanceof FloatTag)){ return false; }
		final FloatTag other = (FloatTag) obj;
		if(Float.floatToIntBits(value) != Float.floatToIntBits(other.value)){ return false; }
		return true;
	}

}
