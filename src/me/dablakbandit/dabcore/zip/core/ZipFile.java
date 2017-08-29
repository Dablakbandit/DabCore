package me.dablakbandit.dabcore.zip.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.exception.ZipExceptionConstants;
import me.dablakbandit.dabcore.zip.io.ZipInputStream;
import me.dablakbandit.dabcore.zip.model.FileHeader;
import me.dablakbandit.dabcore.zip.model.UnzipParameters;
import me.dablakbandit.dabcore.zip.model.ZipModel;
import me.dablakbandit.dabcore.zip.model.ZipParameters;
import me.dablakbandit.dabcore.zip.progress.ProgressMonitor;
import me.dablakbandit.dabcore.zip.unzip.Unzip;
import me.dablakbandit.dabcore.zip.util.ArchiveMaintainer;
import me.dablakbandit.dabcore.zip.util.InternalZipConstants;
import me.dablakbandit.dabcore.zip.util.Zip4jUtil;
import me.dablakbandit.dabcore.zip.zip.ZipEngine;

public class ZipFile{

	private String file;
	private int mode;
	private ZipModel zipModel;
	private boolean isEncrypted;
	private ProgressMonitor progressMonitor;
	private boolean runInThread;
	private String fileNameCharset;

	public ZipFile(String zipFile) throws ZipException{
		this(new File(zipFile));
	}

	public ZipFile(File zipFile) throws ZipException{
		if(zipFile == null){ throw new ZipException("Input zip file parameter is not null", ZipExceptionConstants.inputZipParamIsNull); }

		this.file = zipFile.getPath();
		this.mode = InternalZipConstants.MODE_UNZIP;
		this.progressMonitor = new ProgressMonitor();
		this.runInThread = false;
	}

	public void createZipFile(File sourceFile, ZipParameters parameters) throws ZipException{
		ArrayList sourceFileList = new ArrayList();
		sourceFileList.add(sourceFile);
		createZipFile(sourceFileList, parameters, false, -1);
	}

	public void createZipFile(File sourceFile, ZipParameters parameters, boolean splitArchive, long splitLength) throws ZipException{

		ArrayList sourceFileList = new ArrayList();
		sourceFileList.add(sourceFile);
		createZipFile(sourceFileList, parameters, splitArchive, splitLength);
	}

	public void createZipFile(ArrayList sourceFileList, ZipParameters parameters) throws ZipException{
		createZipFile(sourceFileList, parameters, false, -1);
	}

	public void createZipFile(ArrayList sourceFileList, ZipParameters parameters, boolean splitArchive, long splitLength) throws ZipException{

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(file)){ throw new ZipException("zip file path is empty"); }

		if(Zip4jUtil.checkFileExists(file)){ throw new ZipException("zip file: " + file + " already exists. To add files to existing zip file use addFile method"); }

		if(sourceFileList == null){ throw new ZipException("input file ArrayList is null, cannot create zip file"); }

		if(!Zip4jUtil.checkArrayListTypes(sourceFileList, InternalZipConstants.LIST_TYPE_FILE)){ throw new ZipException("One or more elements in the input ArrayList is not of type File"); }

		createNewZipModel();
		this.zipModel.setSplitArchive(splitArchive);
		this.zipModel.setSplitLength(splitLength);
		addFiles(sourceFileList, parameters);
	}

	public void createZipFileFromFolder(String folderToAdd, ZipParameters parameters, boolean splitArchive, long splitLength) throws ZipException{

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(folderToAdd)){ throw new ZipException("folderToAdd is empty or null, cannot create Zip File from folder"); }

		createZipFileFromFolder(new File(folderToAdd), parameters, splitArchive, splitLength);

	}

	public void createZipFileFromFolder(File folderToAdd, ZipParameters parameters, boolean splitArchive, long splitLength) throws ZipException{

		if(folderToAdd == null){ throw new ZipException("folderToAdd is null, cannot create zip file from folder"); }

		if(parameters == null){ throw new ZipException("input parameters are null, cannot create zip file from folder"); }

		if(Zip4jUtil.checkFileExists(file)){ throw new ZipException("zip file: " + file + " already exists. To add files to existing zip file use addFolder method"); }

		createNewZipModel();
		this.zipModel.setSplitArchive(splitArchive);
		if(splitArchive){
			this.zipModel.setSplitLength(splitLength);
		}

		addFolder(folderToAdd, parameters, false);
	}

	public void addFile(File sourceFile, ZipParameters parameters) throws ZipException{
		ArrayList sourceFileList = new ArrayList();
		sourceFileList.add(sourceFile);
		addFiles(sourceFileList, parameters);
	}

	public void addFiles(ArrayList sourceFileList, ZipParameters parameters) throws ZipException{

		checkZipModel();

		if(this.zipModel == null){ throw new ZipException("internal error: zip model is null"); }

		if(sourceFileList == null){ throw new ZipException("input file ArrayList is null, cannot add files"); }

		if(!Zip4jUtil.checkArrayListTypes(sourceFileList, InternalZipConstants.LIST_TYPE_FILE)){ throw new ZipException("One or more elements in the input ArrayList is not of type File"); }

		if(parameters == null){ throw new ZipException("input parameters are null, cannot add files to zip"); }

		if(progressMonitor.getState() == ProgressMonitor.STATE_BUSY){ throw new ZipException("invalid operation - Zip4j is in busy state"); }

		if(Zip4jUtil.checkFileExists(file)){
			if(zipModel.isSplitArchive()){ throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files"); }
		}

		ZipEngine zipEngine = new ZipEngine(zipModel);
		zipEngine.addFiles(sourceFileList, parameters, progressMonitor, runInThread);
	}

	public void addFolder(String path, ZipParameters parameters) throws ZipException{
		if(!Zip4jUtil.isStringNotNullAndNotEmpty(path)){ throw new ZipException("input path is null or empty, cannot add folder to zip file"); }

		addFolder(new File(path), parameters);
	}

	public void addFolder(File path, ZipParameters parameters) throws ZipException{
		if(path == null){ throw new ZipException("input path is null, cannot add folder to zip file"); }

		if(parameters == null){ throw new ZipException("input parameters are null, cannot add folder to zip file"); }

		addFolder(path, parameters, true);
	}

	private void addFolder(File path, ZipParameters parameters, boolean checkSplitArchive) throws ZipException{

		checkZipModel();

		if(this.zipModel == null){ throw new ZipException("internal error: zip model is null"); }

		if(checkSplitArchive){
			if(this.zipModel.isSplitArchive()){ throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files"); }
		}

		ZipEngine zipEngine = new ZipEngine(zipModel);
		zipEngine.addFolderToZip(path, parameters, progressMonitor, runInThread);

	}

	public void addStream(InputStream inputStream, ZipParameters parameters) throws ZipException{
		if(inputStream == null){ throw new ZipException("inputstream is null, cannot add file to zip"); }

		if(parameters == null){ throw new ZipException("zip parameters are null"); }

		this.setRunInThread(false);

		checkZipModel();

		if(this.zipModel == null){ throw new ZipException("internal error: zip model is null"); }

		if(Zip4jUtil.checkFileExists(file)){
			if(zipModel.isSplitArchive()){ throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files"); }
		}

		ZipEngine zipEngine = new ZipEngine(zipModel);
		zipEngine.addStreamToZip(inputStream, parameters);
	}

	private void readZipInfo() throws ZipException{

		if(!Zip4jUtil.checkFileExists(file)){ throw new ZipException("zip file does not exist"); }

		if(!Zip4jUtil.checkFileReadAccess(this.file)){ throw new ZipException("no read access for the input zip file"); }

		if(this.mode != InternalZipConstants.MODE_UNZIP){ throw new ZipException("Invalid mode"); }

		RandomAccessFile raf = null;
		try{
			raf = new RandomAccessFile(new File(file), InternalZipConstants.READ_MODE);

			if(zipModel == null){

				HeaderReader headerReader = new HeaderReader(raf);
				zipModel = headerReader.readAllHeaders(this.fileNameCharset);
				if(zipModel != null){
					zipModel.setZipFile(file);
				}
			}
		}catch(FileNotFoundException e){
			throw new ZipException(e);
		}finally{
			if(raf != null){
				try{
					raf.close();
				}catch(IOException e){
					// ignore
				}
			}
		}
	}

	public void extractAll(String destPath) throws ZipException{
		extractAll(destPath, null);

	}

	public void extractAll(String destPath, UnzipParameters unzipParameters) throws ZipException{

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(destPath)){ throw new ZipException("output path is null or invalid"); }

		if(!Zip4jUtil.checkOutputFolder(destPath)){ throw new ZipException("invalid output path"); }

		if(zipModel == null){
			readZipInfo();
		}

		// Throw an exception if zipModel is still null
		if(zipModel == null){ throw new ZipException("Internal error occurred when extracting zip file"); }

		if(progressMonitor.getState() == ProgressMonitor.STATE_BUSY){ throw new ZipException("invalid operation - Zip4j is in busy state"); }

		Unzip unzip = new Unzip(zipModel);
		unzip.extractAll(unzipParameters, destPath, progressMonitor, runInThread);

	}

	public void extractFile(FileHeader fileHeader, String destPath) throws ZipException{
		extractFile(fileHeader, destPath, null);
	}

	public void extractFile(FileHeader fileHeader, String destPath, UnzipParameters unzipParameters) throws ZipException{
		extractFile(fileHeader, destPath, unzipParameters, null);
	}

	public void extractFile(FileHeader fileHeader, String destPath, UnzipParameters unzipParameters, String newFileName) throws ZipException{

		if(fileHeader == null){ throw new ZipException("input file header is null, cannot extract file"); }

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(destPath)){ throw new ZipException("destination path is empty or null, cannot extract file"); }

		readZipInfo();

		if(progressMonitor.getState() == ProgressMonitor.STATE_BUSY){ throw new ZipException("invalid operation - Zip4j is in busy state"); }

		fileHeader.extractFile(zipModel, destPath, unzipParameters, newFileName, progressMonitor, runInThread);

	}

	public void extractFile(String fileName, String destPath) throws ZipException{
		extractFile(fileName, destPath, null);
	}

	public void extractFile(String fileName, String destPath, UnzipParameters unzipParameters) throws ZipException{
		extractFile(fileName, destPath, unzipParameters, null);
	}

	public void extractFile(String fileName, String destPath, UnzipParameters unzipParameters, String newFileName) throws ZipException{

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)){ throw new ZipException("file to extract is null or empty, cannot extract file"); }

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(destPath)){ throw new ZipException("destination string path is empty or null, cannot extract file"); }

		readZipInfo();

		FileHeader fileHeader = Zip4jUtil.getFileHeader(zipModel, fileName);

		if(fileHeader == null){ throw new ZipException("file header not found for given file name, cannot extract file"); }

		if(progressMonitor.getState() == ProgressMonitor.STATE_BUSY){ throw new ZipException("invalid operation - Zip4j is in busy state"); }

		fileHeader.extractFile(zipModel, destPath, unzipParameters, newFileName, progressMonitor, runInThread);

	}

	public void setPassword(String password) throws ZipException{
		if(!Zip4jUtil.isStringNotNullAndNotEmpty(password)){ throw new NullPointerException(); }
		setPassword(password.toCharArray());
	}

	public void setPassword(char[] password) throws ZipException{
		if(zipModel == null){
			readZipInfo();
			if(zipModel == null){ throw new ZipException("Zip Model is null"); }
		}

		if(zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null){ throw new ZipException("invalid zip file"); }

		for(int i = 0; i < zipModel.getCentralDirectory().getFileHeaders().size(); i++){
			if(zipModel.getCentralDirectory().getFileHeaders().get(i) != null){
				if(((FileHeader)zipModel.getCentralDirectory().getFileHeaders().get(i)).isEncrypted()){
					((FileHeader)zipModel.getCentralDirectory().getFileHeaders().get(i)).setPassword(password);
				}
			}
		}
	}

	public List getFileHeaders() throws ZipException{
		readZipInfo();
		if(zipModel == null || zipModel.getCentralDirectory() == null){ return null; }
		return zipModel.getCentralDirectory().getFileHeaders();
	}

	public FileHeader getFileHeader(String fileName) throws ZipException{
		if(!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)){ throw new ZipException("input file name is emtpy or null, cannot get FileHeader"); }

		readZipInfo();
		if(zipModel == null || zipModel.getCentralDirectory() == null){ return null; }

		return Zip4jUtil.getFileHeader(zipModel, fileName);
	}

	public boolean isEncrypted() throws ZipException{
		if(zipModel == null){
			readZipInfo();
			if(zipModel == null){ throw new ZipException("Zip Model is null"); }
		}

		if(zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null){ throw new ZipException("invalid zip file"); }

		ArrayList fileHeaderList = zipModel.getCentralDirectory().getFileHeaders();
		for(int i = 0; i < fileHeaderList.size(); i++){
			FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
			if(fileHeader != null){
				if(fileHeader.isEncrypted()){
					isEncrypted = true;
					break;
				}
			}
		}

		return isEncrypted;
	}

	public boolean isSplitArchive() throws ZipException{

		if(zipModel == null){
			readZipInfo();
			if(zipModel == null){ throw new ZipException("Zip Model is null"); }
		}

		return zipModel.isSplitArchive();

	}

	public void removeFile(String fileName) throws ZipException{

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)){ throw new ZipException("file name is empty or null, cannot remove file"); }

		if(zipModel == null){
			if(Zip4jUtil.checkFileExists(file)){
				readZipInfo();
			}
		}

		if(zipModel.isSplitArchive()){ throw new ZipException("Zip file format does not allow updating split/spanned files"); }

		FileHeader fileHeader = Zip4jUtil.getFileHeader(zipModel, fileName);
		if(fileHeader == null){ throw new ZipException("could not find file header for file: " + fileName); }

		removeFile(fileHeader);
	}

	public void removeFile(FileHeader fileHeader) throws ZipException{
		if(fileHeader == null){ throw new ZipException("file header is null, cannot remove file"); }

		if(zipModel == null){
			if(Zip4jUtil.checkFileExists(file)){
				readZipInfo();
			}
		}

		if(zipModel.isSplitArchive()){ throw new ZipException("Zip file format does not allow updating split/spanned files"); }

		ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
		archiveMaintainer.initProgressMonitorForRemoveOp(zipModel, fileHeader, progressMonitor);
		archiveMaintainer.removeZipFile(zipModel, fileHeader, progressMonitor, runInThread);
	}

	public void mergeSplitFiles(File outputZipFile) throws ZipException{
		if(outputZipFile == null){ throw new ZipException("outputZipFile is null, cannot merge split files"); }

		if(outputZipFile.exists()){ throw new ZipException("output Zip File already exists"); }

		checkZipModel();

		if(this.zipModel == null){ throw new ZipException("zip model is null, corrupt zip file?"); }

		ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
		archiveMaintainer.initProgressMonitorForMergeOp(zipModel, progressMonitor);
		archiveMaintainer.mergeSplitZipFiles(zipModel, outputZipFile, progressMonitor, runInThread);
	}

	public void setComment(String comment) throws ZipException{
		if(comment == null){ throw new ZipException("input comment is null, cannot update zip file"); }

		if(!Zip4jUtil.checkFileExists(file)){ throw new ZipException("zip file does not exist, cannot set comment for zip file"); }

		readZipInfo();

		if(this.zipModel == null){ throw new ZipException("zipModel is null, cannot update zip file"); }

		if(zipModel.getEndCentralDirRecord() == null){ throw new ZipException("end of central directory is null, cannot set comment"); }

		ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
		archiveMaintainer.setComment(zipModel, comment);
	}

	public String getComment() throws ZipException{
		return getComment(null);
	}

	public String getComment(String encoding) throws ZipException{
		if(encoding == null){
			if(Zip4jUtil.isSupportedCharset(InternalZipConstants.CHARSET_COMMENTS_DEFAULT)){
				encoding = InternalZipConstants.CHARSET_COMMENTS_DEFAULT;
			}else{
				encoding = InternalZipConstants.CHARSET_DEFAULT;
			}
		}

		if(Zip4jUtil.checkFileExists(file)){
			checkZipModel();
		}else{
			throw new ZipException("zip file does not exist, cannot read comment");
		}

		if(this.zipModel == null){ throw new ZipException("zip model is null, cannot read comment"); }

		if(this.zipModel.getEndCentralDirRecord() == null){ throw new ZipException("end of central directory record is null, cannot read comment"); }

		if(this.zipModel.getEndCentralDirRecord().getCommentBytes() == null || this.zipModel.getEndCentralDirRecord().getCommentBytes().length <= 0){ return null; }

		try{
			return new String(this.zipModel.getEndCentralDirRecord().getCommentBytes(), encoding);
		}catch(UnsupportedEncodingException e){
			throw new ZipException(e);
		}
	}

	private void checkZipModel() throws ZipException{
		if(this.zipModel == null){
			if(Zip4jUtil.checkFileExists(file)){
				readZipInfo();
			}else{
				createNewZipModel();
			}
		}
	}

	private void createNewZipModel(){
		zipModel = new ZipModel();
		zipModel.setZipFile(file);
		zipModel.setFileNameCharset(fileNameCharset);
	}

	public void setFileNameCharset(String charsetName) throws ZipException{
		if(!Zip4jUtil.isStringNotNullAndNotEmpty(charsetName)){ throw new ZipException("null or empty charset name"); }

		if(!Zip4jUtil.isSupportedCharset(charsetName)){ throw new ZipException("unsupported charset: " + charsetName); }

		this.fileNameCharset = charsetName;
	}

	public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException{
		if(fileHeader == null){ throw new ZipException("FileHeader is null, cannot get InputStream"); }

		checkZipModel();

		if(zipModel == null){ throw new ZipException("zip model is null, cannot get inputstream"); }

		Unzip unzip = new Unzip(zipModel);
		return unzip.getInputStream(fileHeader);
	}

	public boolean isValidZipFile(){
		try{
			readZipInfo();
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public ArrayList getSplitZipFiles() throws ZipException{
		checkZipModel();
		return Zip4jUtil.getSplitZipFiles(zipModel);
	}

	public ProgressMonitor getProgressMonitor(){
		return progressMonitor;
	}

	public boolean isRunInThread(){
		return runInThread;
	}

	public void setRunInThread(boolean runInThread){
		this.runInThread = runInThread;
	}

	public File getFile(){
		return new File(this.file);
	}
}
