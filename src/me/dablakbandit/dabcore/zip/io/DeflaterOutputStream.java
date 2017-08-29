package me.dablakbandit.dabcore.zip.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.model.ZipModel;
import me.dablakbandit.dabcore.zip.model.ZipParameters;
import me.dablakbandit.dabcore.zip.util.InternalZipConstants;
import me.dablakbandit.dabcore.zip.util.Zip4jConstants;

public class DeflaterOutputStream extends CipherOutputStream{

	private byte[] buff;
	protected Deflater deflater;
	private boolean firstBytesRead;

	public DeflaterOutputStream(OutputStream outputStream, ZipModel zipModel){
		super(outputStream, zipModel);
		deflater = new Deflater();
		buff = new byte[InternalZipConstants.BUFF_SIZE];
		firstBytesRead = false;
	}

	@Override
	public void putNextEntry(File file, ZipParameters zipParameters) throws ZipException{
		super.putNextEntry(file, zipParameters);
		if(zipParameters.getCompressionMethod() == Zip4jConstants.COMP_DEFLATE){
			deflater.reset();
			if((zipParameters.getCompressionLevel() < 0 || zipParameters.getCompressionLevel() > 9) && zipParameters.getCompressionLevel() != -1){ throw new ZipException("invalid compression level for deflater. compression level should be in the range of 0-9"); }
			deflater.setLevel(zipParameters.getCompressionLevel());
		}
	}

	@Override
	public void write(byte[] b) throws IOException{
		write(b, 0, b.length);
	}

	private void deflate() throws IOException{
		int len = deflater.deflate(buff, 0, buff.length);
		if(len > 0){
			if(deflater.finished()){
				if(len == 4){ return; }
				if(len < 4){
					decrementCompressedFileSize(4 - len);
					return;
				}
				len -= 4;
			}
			if(!firstBytesRead){
				super.write(buff, 2, len - 2);
				firstBytesRead = true;
			}else{
				super.write(buff, 0, len);
			}
		}
	}

	@Override
	public void write(int bval) throws IOException{
		byte[] b = new byte[1];
		b[0] = (byte)bval;
		write(b, 0, 1);
	}

	@Override
	public void write(byte[] buf, int off, int len) throws IOException{
		if(zipParameters.getCompressionMethod() != Zip4jConstants.COMP_DEFLATE){
			super.write(buf, off, len);
		}else{
			deflater.setInput(buf, off, len);
			while(!deflater.needsInput()){
				deflate();
			}
		}
	}

	@Override
	public void closeEntry() throws IOException, ZipException{
		if(zipParameters.getCompressionMethod() == Zip4jConstants.COMP_DEFLATE){
			if(!deflater.finished()){
				deflater.finish();
				while(!deflater.finished()){
					deflate();
				}
			}
			firstBytesRead = false;
		}
		super.closeEntry();
	}

	@Override
	public void finish() throws IOException, ZipException{
		super.finish();
	}
}
