package me.dablakbandit.dabcore.zip.unzip;

import java.io.File;
import java.util.ArrayList;

import me.dablakbandit.dabcore.zip.exception.ZipException;
import me.dablakbandit.dabcore.zip.io.ZipInputStream;
import me.dablakbandit.dabcore.zip.model.CentralDirectory;
import me.dablakbandit.dabcore.zip.model.FileHeader;
import me.dablakbandit.dabcore.zip.model.UnzipParameters;
import me.dablakbandit.dabcore.zip.model.ZipModel;
import me.dablakbandit.dabcore.zip.progress.ProgressMonitor;
import me.dablakbandit.dabcore.zip.util.InternalZipConstants;
import me.dablakbandit.dabcore.zip.util.Zip4jUtil;

public class Unzip{

	private ZipModel zipModel;

	public Unzip(ZipModel zipModel) throws ZipException{

		if(zipModel == null){ throw new ZipException("ZipModel is null"); }

		this.zipModel = zipModel;
	}

	public void extractAll(final UnzipParameters unzipParameters, final String outPath, final ProgressMonitor progressMonitor, boolean runInThread) throws ZipException{

		CentralDirectory centralDirectory = zipModel.getCentralDirectory();

		if(centralDirectory == null || centralDirectory.getFileHeaders() == null){ throw new ZipException("invalid central directory in zipModel"); }

		final ArrayList fileHeaders = centralDirectory.getFileHeaders();

		progressMonitor.setCurrentOperation(ProgressMonitor.OPERATION_EXTRACT);
		progressMonitor.setTotalWork(calculateTotalWork(fileHeaders));
		progressMonitor.setState(ProgressMonitor.STATE_BUSY);

		if(runInThread){
			Thread thread = new Thread(InternalZipConstants.THREAD_NAME){
				@Override
				public void run(){
					try{
						initExtractAll(fileHeaders, unzipParameters, progressMonitor, outPath);
						progressMonitor.endProgressMonitorSuccess();
					}catch(ZipException e){}
				}
			};
			thread.start();
		}else{
			initExtractAll(fileHeaders, unzipParameters, progressMonitor, outPath);
		}

	}

	private void initExtractAll(ArrayList fileHeaders, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String outPath) throws ZipException{

		for(int i = 0; i < fileHeaders.size(); i++){
			FileHeader fileHeader = (FileHeader)fileHeaders.get(i);
			initExtractFile(fileHeader, outPath, unzipParameters, null, progressMonitor);
			if(progressMonitor.isCancelAllTasks()){
				progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
				progressMonitor.setState(ProgressMonitor.STATE_READY);
				return;
			}
		}
	}

	public void extractFile(final FileHeader fileHeader, final String outPath, final UnzipParameters unzipParameters, final String newFileName, final ProgressMonitor progressMonitor, boolean runInThread) throws ZipException{
		if(fileHeader == null){ throw new ZipException("fileHeader is null"); }

		progressMonitor.setCurrentOperation(ProgressMonitor.OPERATION_EXTRACT);
		progressMonitor.setTotalWork(fileHeader.getCompressedSize());
		progressMonitor.setState(ProgressMonitor.STATE_BUSY);
		progressMonitor.setPercentDone(0);
		progressMonitor.setFileName(fileHeader.getFileName());

		if(runInThread){
			Thread thread = new Thread(InternalZipConstants.THREAD_NAME){
				@Override
				public void run(){
					try{
						initExtractFile(fileHeader, outPath, unzipParameters, newFileName, progressMonitor);
						progressMonitor.endProgressMonitorSuccess();
					}catch(ZipException e){}
				}
			};
			thread.start();
		}else{
			initExtractFile(fileHeader, outPath, unzipParameters, newFileName, progressMonitor);
			progressMonitor.endProgressMonitorSuccess();
		}

	}

	private void initExtractFile(FileHeader fileHeader, String outPath, UnzipParameters unzipParameters, String newFileName, ProgressMonitor progressMonitor) throws ZipException{

		if(fileHeader == null){ throw new ZipException("fileHeader is null"); }

		try{
			progressMonitor.setFileName(fileHeader.getFileName());

			if(!outPath.endsWith(InternalZipConstants.FILE_SEPARATOR)){
				outPath += InternalZipConstants.FILE_SEPARATOR;
			}

			// If file header is a directory, then check if the directory exists
			// If not then create a directory and return
			if(fileHeader.isDirectory()){
				try{
					String fileName = fileHeader.getFileName();
					if(!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)){ return; }
					String completePath = outPath + fileName;
					File file = new File(completePath);
					if(!file.exists()){
						file.mkdirs();
					}
				}catch(Exception e){
					progressMonitor.endProgressMonitorError(e);
					throw new ZipException(e);
				}
			}else{
				// Create Directories
				checkOutputDirectoryStructure(fileHeader, outPath, newFileName);

				UnzipEngine unzipEngine = new UnzipEngine(zipModel, fileHeader);
				try{
					unzipEngine.unzipFile(progressMonitor, outPath, newFileName, unzipParameters);
				}catch(Exception e){
					progressMonitor.endProgressMonitorError(e);
					throw new ZipException(e);
				}
			}
		}catch(ZipException e){
			progressMonitor.endProgressMonitorError(e);
			throw e;
		}catch(Exception e){
			progressMonitor.endProgressMonitorError(e);
			throw new ZipException(e);
		}
	}

	public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException{
		UnzipEngine unzipEngine = new UnzipEngine(zipModel, fileHeader);
		return unzipEngine.getInputStream();
	}

	private void checkOutputDirectoryStructure(FileHeader fileHeader, String outPath, String newFileName) throws ZipException{
		if(fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(outPath)){ throw new ZipException("Cannot check output directory structure...one of the parameters was null"); }

		String fileName = fileHeader.getFileName();

		if(Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)){
			fileName = newFileName;
		}

		if(!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)){
			// Do nothing
			return;
		}

		String compOutPath = outPath + fileName;
		try{
			File file = new File(compOutPath);
			String parentDir = file.getParent();
			File parentDirFile = new File(parentDir);
			if(!parentDirFile.exists()){
				parentDirFile.mkdirs();
			}
		}catch(Exception e){
			throw new ZipException(e);
		}
	}

	private long calculateTotalWork(ArrayList fileHeaders) throws ZipException{

		if(fileHeaders == null){ throw new ZipException("fileHeaders is null, cannot calculate total work"); }

		long totalWork = 0;

		for(int i = 0; i < fileHeaders.size(); i++){
			FileHeader fileHeader = (FileHeader)fileHeaders.get(i);
			if(fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getUnCompressedSize() > 0){
				totalWork += fileHeader.getZip64ExtendedInfo().getCompressedSize();
			}else{
				totalWork += fileHeader.getCompressedSize();
			}

		}

		return totalWork;
	}

}
