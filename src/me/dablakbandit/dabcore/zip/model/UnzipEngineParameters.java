package me.dablakbandit.dabcore.zip.model;

import java.io.FileOutputStream;

import me.dablakbandit.dabcore.zip.crypto.IDecrypter;
import me.dablakbandit.dabcore.zip.unzip.UnzipEngine;

public class UnzipEngineParameters{

	private ZipModel zipModel;

	private FileHeader fileHeader;

	private LocalFileHeader localFileHeader;

	private IDecrypter iDecryptor;

	private FileOutputStream outputStream;

	private UnzipEngine unzipEngine;

	public ZipModel getZipModel(){
		return zipModel;
	}

	public void setZipModel(ZipModel zipModel){
		this.zipModel = zipModel;
	}

	public FileHeader getFileHeader(){
		return fileHeader;
	}

	public void setFileHeader(FileHeader fileHeader){
		this.fileHeader = fileHeader;
	}

	public LocalFileHeader getLocalFileHeader(){
		return localFileHeader;
	}

	public void setLocalFileHeader(LocalFileHeader localFileHeader){
		this.localFileHeader = localFileHeader;
	}

	public IDecrypter getIDecryptor(){
		return iDecryptor;
	}

	public void setIDecryptor(IDecrypter decrypter){
		iDecryptor = decrypter;
	}

	public FileOutputStream getOutputStream(){
		return outputStream;
	}

	public void setOutputStream(FileOutputStream outputStream){
		this.outputStream = outputStream;
	}

	public UnzipEngine getUnzipEngine(){
		return unzipEngine;
	}

	public void setUnzipEngine(UnzipEngine unzipEngine){
		this.unzipEngine = unzipEngine;
	}

}
