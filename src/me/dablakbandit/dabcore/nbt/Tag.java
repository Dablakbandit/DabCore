package me.dablakbandit.dabcore.nbt;

public abstract class Tag{

	private final String name;

	public Tag(final String name){
		this.name = name;
	}

	public final String getName(){
		return name;
	}

	public abstract Object getValue();

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj){
		if(this == obj){ return true; }
		if(obj == null){ return false; }
		if(!(obj instanceof Tag)){ return false; }
		final Tag other = (Tag) obj;
		if(name == null){
			if(other.name != null){ return false; }
		} else if(!name.equals(other.name)){ return false; }
		return true;
	}
	
}
