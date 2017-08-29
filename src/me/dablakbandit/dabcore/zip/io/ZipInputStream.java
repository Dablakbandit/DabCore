package me.dablakbandit.dabcore.zip.io;

import java.io.IOException;
import java.io.InputStream;

import me.dablakbandit.dabcore.zip.exception.ZipException;

public class ZipInputStream extends InputStream{

	private BaseInputStream is;

	public ZipInputStream(BaseInputStream is){
		this.is = is;
	}

	@Override
	public int read() throws IOException{
		int readByte = is.read();
		if(readByte != -1){
			is.getUnzipEngine().updateCRC(readByte);
		}
		return readByte;
	}

	@Override
	public int read(byte[] b) throws IOException{
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException{
		int readLen = is.read(b, off, len);
		if(readLen > 0 && is.getUnzipEngine() != null){
			is.getUnzipEngine().updateCRC(b, off, readLen);
		}
		return readLen;
	}

	@Override
	public void close() throws IOException{
		close(false);
	}

	public void close(boolean skipCRCCheck) throws IOException{
		try{
			is.close();
			if(!skipCRCCheck && is.getUnzipEngine() != null){
				is.getUnzipEngine().checkCRC();
			}
		}catch(ZipException e){
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public int available() throws IOException{
		return is.available();
	}

	@Override
	public long skip(long n) throws IOException{
		return is.skip(n);
	}

}
