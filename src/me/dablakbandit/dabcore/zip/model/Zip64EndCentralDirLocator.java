package me.dablakbandit.dabcore.zip.model;

public class Zip64EndCentralDirLocator{

	private long signature;

	private int noOfDiskStartOfZip64EndOfCentralDirRec;

	private long offsetZip64EndOfCentralDirRec;

	private int totNumberOfDiscs;

	public long getSignature(){
		return signature;
	}

	public void setSignature(long signature){
		this.signature = signature;
	}

	public int getNoOfDiskStartOfZip64EndOfCentralDirRec(){
		return noOfDiskStartOfZip64EndOfCentralDirRec;
	}

	public void setNoOfDiskStartOfZip64EndOfCentralDirRec(int noOfDiskStartOfZip64EndOfCentralDirRec){
		this.noOfDiskStartOfZip64EndOfCentralDirRec = noOfDiskStartOfZip64EndOfCentralDirRec;
	}

	public long getOffsetZip64EndOfCentralDirRec(){
		return offsetZip64EndOfCentralDirRec;
	}

	public void setOffsetZip64EndOfCentralDirRec(long offsetZip64EndOfCentralDirRec){
		this.offsetZip64EndOfCentralDirRec = offsetZip64EndOfCentralDirRec;
	}

	public int getTotNumberOfDiscs(){
		return totNumberOfDiscs;
	}

	public void setTotNumberOfDiscs(int totNumberOfDiscs){
		this.totNumberOfDiscs = totNumberOfDiscs;
	}

}
