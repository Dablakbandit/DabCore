package me.dablakbandit.dabcore.zip.crypto;

import me.dablakbandit.dabcore.zip.crypto.engine.ZipCryptoEngine;
import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.exception.ZipExceptionConstants;
import me.dablakbandit.dabcore.zip.model.FileHeader;
import me.dablakbandit.dabcore.zip.util.InternalZipConstants;

public class StandardDecrypter implements IDecrypter{

	private FileHeader fileHeader;
	private byte[] crc = new byte[4];
	private ZipCryptoEngine zipCryptoEngine;

	public StandardDecrypter(FileHeader fileHeader, byte[] headerBytes) throws ZipException{
		if(fileHeader == null){ throw new ZipException("one of more of the input parameters were null in StandardDecryptor"); }

		this.fileHeader = fileHeader;
		this.zipCryptoEngine = new ZipCryptoEngine();
		init(headerBytes);
	}

	@Override
	public int decryptData(byte[] buff) throws ZipException{
		return decryptData(buff, 0, buff.length);
	}

	@Override
	public int decryptData(byte[] buff, int start, int len) throws ZipException{
		if(start < 0 || len < 0){ throw new ZipException("one of the input parameters were null in standard decrpyt data"); }

		try{
			for(int i = start; i < start + len; i++){
				int val = buff[i] & 0xff;
				val = (val ^ zipCryptoEngine.decryptByte()) & 0xff;
				zipCryptoEngine.updateKeys((byte)val);
				buff[i] = (byte)val;
			}
			return len;
		}catch(Exception e){
			throw new ZipException(e);
		}
	}

	public void init(byte[] headerBytes) throws ZipException{
		byte[] crcBuff = fileHeader.getCrcBuff();
		crc[3] = (byte)(crcBuff[3] & 0xFF);
		crc[2] = (byte)(crcBuff[3] >> 8 & 0xFF);
		crc[1] = (byte)(crcBuff[3] >> 16 & 0xFF);
		crc[0] = (byte)(crcBuff[3] >> 24 & 0xFF);

		if(crc[2] > 0 || crc[1] > 0 || crc[0] > 0){ throw new IllegalStateException("Invalid CRC in File Header"); }

		if(fileHeader.getPassword() == null || fileHeader.getPassword().length <= 0){ throw new ZipException("Wrong password!", ZipExceptionConstants.WRONG_PASSWORD); }

		zipCryptoEngine.initKeys(fileHeader.getPassword());

		try{
			int result = headerBytes[0];
			for(int i = 0; i < InternalZipConstants.STD_DEC_HDR_SIZE; i++){
				// Commented this as this check cannot always be trusted
				// New functionality: If there is an error in extracting a
				// password protected file,
				// "Wrong Password?" text is appended to the exception message
				// if(i+1 == InternalZipConstants.STD_DEC_HDR_SIZE &&
				// ((byte)(result ^ zipCryptoEngine.decryptByte()) != crc[3]) &&
				// !isSplit)
				// throw new ZipException("Wrong password!",
				// ZipExceptionConstants.WRONG_PASSWORD);

				zipCryptoEngine.updateKeys((byte)(result ^ zipCryptoEngine.decryptByte()));
				if(i + 1 != InternalZipConstants.STD_DEC_HDR_SIZE){
					result = headerBytes[i + 1];
				}
			}
		}catch(Exception e){
			throw new ZipException(e);
		}
	}

}
