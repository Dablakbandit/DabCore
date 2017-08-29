package me.dablakbandit.dabcore.zip.model;

public class Zip64ExtendedInfo{

	private int header;

	private int size;

	private long compressedSize;

	private long unCompressedSize;

	private long offsetLocalHeader;

	private int diskNumberStart;

	public Zip64ExtendedInfo(){
		compressedSize = -1;
		unCompressedSize = -1;
		offsetLocalHeader = -1;
		diskNumberStart = -1;
	}

	public int getHeader(){
		return header;
	}

	public void setHeader(int header){
		this.header = header;
	}

	public int getSize(){
		return size;
	}

	public void setSize(int size){
		this.size = size;
	}

	public long getCompressedSize(){
		return compressedSize;
	}

	public void setCompressedSize(long compressedSize){
		this.compressedSize = compressedSize;
	}

	public long getUnCompressedSize(){
		return unCompressedSize;
	}

	public void setUnCompressedSize(long unCompressedSize){
		this.unCompressedSize = unCompressedSize;
	}

	public long getOffsetLocalHeader(){
		return offsetLocalHeader;
	}

	public void setOffsetLocalHeader(long offsetLocalHeader){
		this.offsetLocalHeader = offsetLocalHeader;
	}

	public int getDiskNumberStart(){
		return diskNumberStart;
	}

	public void setDiskNumberStart(int diskNumberStart){
		this.diskNumberStart = diskNumberStart;
	}

}
