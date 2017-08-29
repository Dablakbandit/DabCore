package me.dablakbandit.dabcore.nbt;

public final class EndTag extends Tag{
	public EndTag(){
		super("");
	}
	
	@Override
	public Object getValue(){
		return null;
	}
	
	@Override
	public String toString(){
		return getTagPrefixedToString();
	}
}
