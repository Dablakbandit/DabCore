package me.dablakbandit.dabcore.utils;

public class AttributeHelper {

	protected final String name;
	protected final Object attrib;

	public AttributeHelper(String name, Object attrib){
		this.name = name;
		this.attrib = attrib;
	}

	public String getName(){
		return name;
	}

	public Object getAttrib(){
		return attrib;
	}
}
