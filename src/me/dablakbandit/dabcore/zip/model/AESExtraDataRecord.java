package me.dablakbandit.dabcore.zip.model;

public class AESExtraDataRecord{

	private long signature;
	private int dataSize;
	private int versionNumber;
	private String vendorID;
	private int aesStrength;
	private int compressionMethod;

	public AESExtraDataRecord(){
		signature = -1;
		dataSize = -1;
		versionNumber = -1;
		vendorID = null;
		aesStrength = -1;
		compressionMethod = -1;
	}

	public long getSignature(){
		return signature;
	}

	public void setSignature(long signature){
		this.signature = signature;
	}

	public int getDataSize(){
		return dataSize;
	}

	public void setDataSize(int dataSize){
		this.dataSize = dataSize;
	}

	public int getVersionNumber(){
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber){
		this.versionNumber = versionNumber;
	}

	public String getVendorID(){
		return vendorID;
	}

	public void setVendorID(String vendorID){
		this.vendorID = vendorID;
	}

	public int getAesStrength(){
		return aesStrength;
	}

	public void setAesStrength(int aesStrength){
		this.aesStrength = aesStrength;
	}

	public int getCompressionMethod(){
		return compressionMethod;
	}

	public void setCompressionMethod(int compressionMethod){
		this.compressionMethod = compressionMethod;
	}

}
