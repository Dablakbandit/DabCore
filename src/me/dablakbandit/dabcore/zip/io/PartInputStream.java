package me.dablakbandit.dabcore.zip.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import me.dablakbandit.dabcore.zip.crypto.AESDecrypter;
import me.dablakbandit.dabcore.zip.crypto.IDecrypter;
import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.unzip.UnzipEngine;
import me.dablakbandit.dabcore.zip.util.InternalZipConstants;
import me.dablakbandit.dabcore.zip.util.Zip4jConstants;

public class PartInputStream extends BaseInputStream{
	private RandomAccessFile raf;
	private long bytesRead, length;
	private UnzipEngine unzipEngine;
	private IDecrypter decrypter;
	private byte[] oneByteBuff = new byte[1];
	private byte[] aesBlockByte = new byte[16];
	private int aesBytesReturned = 0;
	private boolean isAESEncryptedFile = false;
	private int count = -1;

	public PartInputStream(RandomAccessFile raf, long start, long len, UnzipEngine unzipEngine){
		this.raf = raf;
		this.unzipEngine = unzipEngine;
		this.decrypter = unzipEngine.getDecrypter();
		this.bytesRead = 0;
		this.length = len;
		this.isAESEncryptedFile = unzipEngine.getFileHeader().isEncrypted() && unzipEngine.getFileHeader().getEncryptionMethod() == Zip4jConstants.ENC_METHOD_AES;
	}

	@Override
	public int available(){
		long amount = length - bytesRead;
		if(amount > Integer.MAX_VALUE){ return Integer.MAX_VALUE; }
		return (int)amount;
	}

	@Override
	public int read() throws IOException{
		if(bytesRead >= length){ return -1; }

		if(isAESEncryptedFile){
			if(aesBytesReturned == 0 || aesBytesReturned == 16){
				if(read(aesBlockByte) == -1){ return -1; }
				aesBytesReturned = 0;
			}
			return aesBlockByte[aesBytesReturned++] & 0xff;
		}else{
			return read(oneByteBuff, 0, 1) == -1 ? -1 : oneByteBuff[0] & 0xff;
		}
	}

	@Override
	public int read(byte[] b) throws IOException{
		return this.read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException{
		if(len > length - bytesRead){
			len = (int)(length - bytesRead);
			if(len == 0){
				checkAndReadAESMacBytes();
				return -1;
			}
		}

		if(unzipEngine.getDecrypter() instanceof AESDecrypter){
			if(bytesRead + len < length){
				if(len % 16 != 0){
					len = len - len % 16;
				}
			}
		}

		synchronized(raf){
			count = raf.read(b, off, len);
			if(count < len && unzipEngine.getZipModel().isSplitArchive()){
				raf.close();
				raf = unzipEngine.startNextSplitFile();
				if(count < 0){
					count = 0;
				}
				int newlyRead = raf.read(b, count, len - count);
				if(newlyRead > 0){
					count += newlyRead;
				}
			}
		}

		if(count > 0){
			if(decrypter != null){
				try{
					decrypter.decryptData(b, off, count);
				}catch(ZipException e){
					throw new IOException(e.getMessage());
				}
			}
			bytesRead += count;
		}

		if(bytesRead >= length){
			checkAndReadAESMacBytes();
		}

		return count;
	}

	protected void checkAndReadAESMacBytes() throws IOException{
		if(isAESEncryptedFile){
			if(decrypter != null && decrypter instanceof AESDecrypter){
				if(((AESDecrypter)decrypter).getStoredMac() != null){
					// Stored mac already set
					return;
				}
				byte[] macBytes = new byte[InternalZipConstants.AES_AUTH_LENGTH];
				int readLen = -1;
				readLen = raf.read(macBytes);
				if(readLen != InternalZipConstants.AES_AUTH_LENGTH){
					if(unzipEngine.getZipModel().isSplitArchive()){
						raf.close();
						raf = unzipEngine.startNextSplitFile();
						int newlyRead = raf.read(macBytes, readLen, InternalZipConstants.AES_AUTH_LENGTH - readLen);
						readLen += newlyRead;
					}else{
						throw new IOException("Error occured while reading stored AES authentication bytes");
					}
				}

				((AESDecrypter)unzipEngine.getDecrypter()).setStoredMac(macBytes);
			}
		}
	}

	@Override
	public long skip(long amount) throws IOException{
		if(amount < 0){ throw new IllegalArgumentException(); }
		if(amount > length - bytesRead){
			amount = length - bytesRead;
		}
		bytesRead += amount;
		return amount;
	}

	@Override
	public void close() throws IOException{
		raf.close();
	}

	@Override
	public void seek(long pos) throws IOException{
		raf.seek(pos);
	}

	@Override
	public UnzipEngine getUnzipEngine(){
		return this.unzipEngine;
	}
}
