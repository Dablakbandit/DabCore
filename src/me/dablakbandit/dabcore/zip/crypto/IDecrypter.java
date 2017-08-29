package me.dablakbandit.dabcore.zip.crypto;

import me.dablakbandit.dabcore.zip.exception.ZipException;

public interface IDecrypter{

	public int decryptData(byte[] buff, int start, int len) throws ZipException;

	public int decryptData(byte[] buff) throws ZipException;

}
