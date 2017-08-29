package me.dablakbandit.dabcore.zip.io;

import java.io.IOException;
import java.io.OutputStream;

import me.dablakbandit.dabcore.zip.model.ZipModel;

public class ZipOutputStream extends DeflaterOutputStream{

	public ZipOutputStream(OutputStream outputStream){
		this(outputStream, null);
	}

	public ZipOutputStream(OutputStream outputStream, ZipModel zipModel){
		super(outputStream, zipModel);
	}

	@Override
	public void write(int bval) throws IOException{
		byte[] b = new byte[1];
		b[0] = (byte)bval;
		write(b, 0, 1);
	}

	@Override
	public void write(byte[] b) throws IOException{
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		crc.update(b, off, len);
		updateTotalBytesRead(len);
		super.write(b, off, len);
	}
}
