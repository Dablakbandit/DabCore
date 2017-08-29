package me.dablakbandit.dabcore.zip.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.util.InternalZipConstants;
import me.dablakbandit.dabcore.zip.util.Raw;
import me.dablakbandit.dabcore.zip.util.Zip4jUtil;

public class SplitOutputStream extends OutputStream{

	private RandomAccessFile raf;
	private long splitLength;
	private File zipFile;
	private File outFile;
	private int currSplitFileCounter;
	private long bytesWrittenForThisPart;

	public SplitOutputStream(String name) throws FileNotFoundException, ZipException{
		this(Zip4jUtil.isStringNotNullAndNotEmpty(name) ? new File(name) : null);
	}

	public SplitOutputStream(File file) throws FileNotFoundException, ZipException{
		this(file, -1);
	}

	public SplitOutputStream(String name, long splitLength) throws FileNotFoundException, ZipException{
		this(!Zip4jUtil.isStringNotNullAndNotEmpty(name) ? new File(name) : null, splitLength);
	}

	public SplitOutputStream(File file, long splitLength) throws FileNotFoundException, ZipException{
		if(splitLength >= 0 && splitLength < InternalZipConstants.MIN_SPLIT_LENGTH){ throw new ZipException("split length less than minimum allowed split length of " + InternalZipConstants.MIN_SPLIT_LENGTH + " Bytes"); }
		this.raf = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
		this.splitLength = splitLength;
		this.outFile = file;
		this.zipFile = file;
		this.currSplitFileCounter = 0;
		this.bytesWrittenForThisPart = 0;
	}

	@Override
	public void write(int b) throws IOException{
		byte[] buff = new byte[1];
		buff[0] = (byte)b;
		write(buff, 0, 1);
	}

	@Override
	public void write(byte[] b) throws IOException{
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		if(len <= 0){ return; }

		if(splitLength != -1){

			if(splitLength < InternalZipConstants.MIN_SPLIT_LENGTH){ throw new IOException("split length less than minimum allowed split length of " + InternalZipConstants.MIN_SPLIT_LENGTH + " Bytes"); }

			if(bytesWrittenForThisPart >= splitLength){
				startNextSplitFile();
				raf.write(b, off, len);
				bytesWrittenForThisPart = len;
			}else if(bytesWrittenForThisPart + len > splitLength){
				if(isHeaderData(b)){
					startNextSplitFile();
					raf.write(b, off, len);
					bytesWrittenForThisPart = len;
				}else{
					raf.write(b, off, (int)(splitLength - bytesWrittenForThisPart));
					startNextSplitFile();
					raf.write(b, off + (int)(splitLength - bytesWrittenForThisPart), (int)(len - (splitLength - bytesWrittenForThisPart)));
					bytesWrittenForThisPart = len - (splitLength - bytesWrittenForThisPart);
				}
			}else{
				raf.write(b, off, len);
				bytesWrittenForThisPart += len;
			}

		}else{
			raf.write(b, off, len);
			bytesWrittenForThisPart += len;
		}

	}

	private void startNextSplitFile() throws IOException{
		try{
			String zipFileWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(outFile.getName());
			File currSplitFile = null;
			String zipFileName = zipFile.getAbsolutePath();
			String parentPath = outFile.getParent() == null ? "" : outFile.getParent() + System.getProperty("file.separator");

			if(currSplitFileCounter < 9){
				currSplitFile = new File(parentPath + zipFileWithoutExt + ".z0" + (currSplitFileCounter + 1));
			}else{
				currSplitFile = new File(parentPath + zipFileWithoutExt + ".z" + (currSplitFileCounter + 1));
			}

			raf.close();

			if(currSplitFile.exists()){ throw new IOException("split file: " + currSplitFile.getName() + " already exists in the current directory, cannot rename this file"); }

			if(!zipFile.renameTo(currSplitFile)){ throw new IOException("cannot rename newly created split file"); }

			zipFile = new File(zipFileName);
			raf = new RandomAccessFile(zipFile, InternalZipConstants.WRITE_MODE);
			currSplitFileCounter++;
		}catch(ZipException e){
			throw new IOException(e.getMessage());
		}
	}

	private boolean isHeaderData(byte[] buff){
		if(buff == null || buff.length < 4){ return false; }

		int signature = Raw.readIntLittleEndian(buff, 0);
		long[] allHeaderSignatures = Zip4jUtil.getAllHeaderSignatures();
		if(allHeaderSignatures != null && allHeaderSignatures.length > 0){
			for(long allHeaderSignature : allHeaderSignatures){
				// Ignore split signature
				if(allHeaderSignature != InternalZipConstants.SPLITSIG && allHeaderSignature == signature){ return true; }
			}
		}

		return false;
	}

	public boolean checkBuffSizeAndStartNextSplitFile(int bufferSize) throws ZipException{
		if(bufferSize < 0){ throw new ZipException("negative buffersize for checkBuffSizeAndStartNextSplitFile"); }

		if(!isBuffSizeFitForCurrSplitFile(bufferSize)){
			try{
				startNextSplitFile();
				bytesWrittenForThisPart = 0;
				return true;
			}catch(IOException e){
				throw new ZipException(e);
			}
		}

		return false;
	}

	public boolean isBuffSizeFitForCurrSplitFile(int bufferSize) throws ZipException{
		if(bufferSize < 0){ throw new ZipException("negative buffersize for isBuffSizeFitForCurrSplitFile"); }

		if(splitLength >= InternalZipConstants.MIN_SPLIT_LENGTH){
			return bytesWrittenForThisPart + bufferSize <= splitLength;
		}else{
			// Non split zip -- return true
			return true;
		}
	}

	public void seek(long pos) throws IOException{
		raf.seek(pos);
	}

	@Override
	public void close() throws IOException{
		if(raf != null){
			raf.close();
		}
	}

	@Override
	public void flush() throws IOException{}

	public long getFilePointer() throws IOException{
		return raf.getFilePointer();
	}

	public boolean isSplitZipFile(){
		return splitLength != -1;
	}

	public long getSplitLength(){
		return splitLength;
	}

	public int getCurrSplitFileCounter(){
		return currSplitFileCounter;
	}
}
