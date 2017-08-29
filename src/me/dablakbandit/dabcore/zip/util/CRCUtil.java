package me.dablakbandit.dabcore.zip.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.progress.ProgressMonitor;

public class CRCUtil{

	private static final int BUF_SIZE = 1 << 14; // 16384

	public static long computeFileCRC(String inputFile) throws ZipException{
		return computeFileCRC(inputFile, null);
	}

	public static long computeFileCRC(String inputFile, ProgressMonitor progressMonitor) throws ZipException{

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(inputFile)){ throw new ZipException("input file is null or empty, cannot calculate CRC for the file"); }
		InputStream inputStream = null;
		try{
			Zip4jUtil.checkFileReadAccess(inputFile);

			inputStream = new FileInputStream(new File(inputFile));

			byte[] buff = new byte[BUF_SIZE];
			int readLen = -2;
			CRC32 crc32 = new CRC32();

			while((readLen = inputStream.read(buff)) != -1){
				crc32.update(buff, 0, readLen);
				if(progressMonitor != null){
					progressMonitor.updateWorkCompleted(readLen);
					if(progressMonitor.isCancelAllTasks()){
						progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
						progressMonitor.setState(ProgressMonitor.STATE_READY);
						return 0;
					}
				}
			}

			return crc32.getValue();
		}catch(IOException e){
			throw new ZipException(e);
		}catch(Exception e){
			throw new ZipException(e);
		}finally{
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(IOException e){
					throw new ZipException("error while closing the file after calculating crc");
				}
			}
		}
	}

}
