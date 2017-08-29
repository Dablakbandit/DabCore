package me.dablakbandit.dabcore.zip.model;

public class DataDescriptor{

	private String crc32;

	private int compressedSize;

	private int uncompressedSize;

	public String getCrc32(){
		return crc32;
	}

	public void setCrc32(String crc32){
		this.crc32 = crc32;
	}

	public int getCompressedSize(){
		return compressedSize;
	}

	public void setCompressedSize(int compressedSize){
		this.compressedSize = compressedSize;
	}

	public int getUncompressedSize(){
		return uncompressedSize;
	}

	public void setUncompressedSize(int uncompressedSize){
		this.uncompressedSize = uncompressedSize;
	}

}
