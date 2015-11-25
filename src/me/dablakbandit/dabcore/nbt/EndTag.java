package me.dablakbandit.dabcore.nbt;

public final class EndTag extends Tag{

	private final Object value = null;

	public EndTag(){
		super("");
	}

	@Override
	public Object getValue(){
		return value;
	}

	@Override
	public String toString(){
		return "TAG_End";
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
		if(!(obj instanceof EndTag)){ return false; }
		final EndTag other = (EndTag) obj;
		if(value == null){
			if(other.value != null){ return false; }
		}else if(!value.equals(other.value)){ return false; }
		return true;
	}

}
