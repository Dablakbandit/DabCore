package me.dablakbandit.dabcore.zip.model;

import java.util.TimeZone;

import me.dablakbandit.dabcore.zip.util.InternalZipConstants;
import me.dablakbandit.dabcore.zip.util.Zip4jConstants;
import me.dablakbandit.dabcore.zip.util.Zip4jUtil;

public class ZipParameters implements Cloneable{

	private int compressionMethod;
	private int compressionLevel;
	private boolean encryptFiles;
	private int encryptionMethod;
	private boolean readHiddenFiles;
	private char[] password;
	private int aesKeyStrength;
	private boolean includeRootFolder;
	private String rootFolderInZip;
	private TimeZone timeZone;
	private int sourceFileCRC;
	private String defaultFolderPath;
	private String fileNameInZip;
	private boolean isSourceExternalStream;

	public ZipParameters(){
		compressionMethod = Zip4jConstants.COMP_DEFLATE;
		encryptFiles = false;
		readHiddenFiles = true;
		encryptionMethod = Zip4jConstants.ENC_NO_ENCRYPTION;
		aesKeyStrength = -1;
		includeRootFolder = true;
		timeZone = TimeZone.getDefault();
	}

	public int getCompressionMethod(){
		return compressionMethod;
	}

	public void setCompressionMethod(int compressionMethod){
		this.compressionMethod = compressionMethod;
	}

	public boolean isEncryptFiles(){
		return encryptFiles;
	}

	public void setEncryptFiles(boolean encryptFiles){
		this.encryptFiles = encryptFiles;
	}

	public int getEncryptionMethod(){
		return encryptionMethod;
	}

	public void setEncryptionMethod(int encryptionMethod){
		this.encryptionMethod = encryptionMethod;
	}

	public int getCompressionLevel(){
		return compressionLevel;
	}

	public void setCompressionLevel(int compressionLevel){
		this.compressionLevel = compressionLevel;
	}

	public boolean isReadHiddenFiles(){
		return readHiddenFiles;
	}

	public void setReadHiddenFiles(boolean readHiddenFiles){
		this.readHiddenFiles = readHiddenFiles;
	}

	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

	public char[] getPassword(){
		return password;
	}

	public void setPassword(String password){
		if(password == null){ return; }
		setPassword(password.toCharArray());
	}

	public void setPassword(char[] password){
		this.password = password;
	}

	public int getAesKeyStrength(){
		return aesKeyStrength;
	}

	public void setAesKeyStrength(int aesKeyStrength){
		this.aesKeyStrength = aesKeyStrength;
	}

	public boolean isIncludeRootFolder(){
		return includeRootFolder;
	}

	public void setIncludeRootFolder(boolean includeRootFolder){
		this.includeRootFolder = includeRootFolder;
	}

	public String getRootFolderInZip(){
		return rootFolderInZip;
	}

	public void setRootFolderInZip(String rootFolderInZip){
		if(Zip4jUtil.isStringNotNullAndNotEmpty(rootFolderInZip)){

			if(!rootFolderInZip.endsWith("\\") && !rootFolderInZip.endsWith("/")){
				rootFolderInZip = rootFolderInZip + InternalZipConstants.FILE_SEPARATOR;
			}

			rootFolderInZip = rootFolderInZip.replaceAll("\\\\", "/");

			// if (rootFolderInZip.endsWith("/")) {
			// rootFolderInZip = rootFolderInZip.substring(0,
			// rootFolderInZip.length() - 1);
			// rootFolderInZip = rootFolderInZip + "\\";
			// }
		}
		this.rootFolderInZip = rootFolderInZip;
	}

	public TimeZone getTimeZone(){
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone){
		this.timeZone = timeZone;
	}

	public int getSourceFileCRC(){
		return sourceFileCRC;
	}

	public void setSourceFileCRC(int sourceFileCRC){
		this.sourceFileCRC = sourceFileCRC;
	}

	public String getDefaultFolderPath(){
		return defaultFolderPath;
	}

	public void setDefaultFolderPath(String defaultFolderPath){
		this.defaultFolderPath = defaultFolderPath;
	}

	public String getFileNameInZip(){
		return fileNameInZip;
	}

	public void setFileNameInZip(String fileNameInZip){
		this.fileNameInZip = fileNameInZip;
	}

	public boolean isSourceExternalStream(){
		return isSourceExternalStream;
	}

	public void setSourceExternalStream(boolean isSourceExternalStream){
		this.isSourceExternalStream = isSourceExternalStream;
	}

}
