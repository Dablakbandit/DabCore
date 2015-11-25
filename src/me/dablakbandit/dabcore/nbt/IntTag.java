package me.dablakbandit.dabcore.nbt;

public final class IntTag extends Tag{

	private final int value;

	public IntTag(final String name, final int value){
		super(name);
		this.value = value;
	}

	@Override
	public Integer getValue(){
		return value;
	}

	@Override
	public String toString(){
		final String name = getName();
		String append = "";
		if((name != null) && !name.equals("")){
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Int" + append + ": " + value;
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
		if(!(obj instanceof IntTag)){ return false; }
		final IntTag other = (IntTag) obj;
		if(value != other.value){ return false; }
		return true;
	}

}
