package me.dablakbandit.dabcore.zip.model;

import java.util.ArrayList;

public class CentralDirectory{

	private ArrayList fileHeaders;

	private DigitalSignature digitalSignature;

	public ArrayList getFileHeaders(){
		return fileHeaders;
	}

	public void setFileHeaders(ArrayList fileHeaders){
		this.fileHeaders = fileHeaders;
	}

	public DigitalSignature getDigitalSignature(){
		return digitalSignature;
	}

	public void setDigitalSignature(DigitalSignature digitalSignature){
		this.digitalSignature = digitalSignature;
	}

}
