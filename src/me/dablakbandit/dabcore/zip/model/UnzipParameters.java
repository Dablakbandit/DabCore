package me.dablakbandit.dabcore.zip.model;

public class UnzipParameters{

	private boolean ignoreReadOnlyFileAttribute;
	private boolean ignoreHiddenFileAttribute;
	private boolean ignoreArchiveFileAttribute;
	private boolean ignoreSystemFileAttribute;
	private boolean ignoreAllFileAttributes;
	private boolean ignoreDateTimeAttributes;

	public boolean isIgnoreReadOnlyFileAttribute(){
		return ignoreReadOnlyFileAttribute;
	}

	public void setIgnoreReadOnlyFileAttribute(boolean ignoreReadOnlyFileAttribute){
		this.ignoreReadOnlyFileAttribute = ignoreReadOnlyFileAttribute;
	}

	public boolean isIgnoreHiddenFileAttribute(){
		return ignoreHiddenFileAttribute;
	}

	public void setIgnoreHiddenFileAttribute(boolean ignoreHiddenFileAttribute){
		this.ignoreHiddenFileAttribute = ignoreHiddenFileAttribute;
	}

	public boolean isIgnoreArchiveFileAttribute(){
		return ignoreArchiveFileAttribute;
	}

	public void setIgnoreArchiveFileAttribute(boolean ignoreArchiveFileAttribute){
		this.ignoreArchiveFileAttribute = ignoreArchiveFileAttribute;
	}

	public boolean isIgnoreSystemFileAttribute(){
		return ignoreSystemFileAttribute;
	}

	public void setIgnoreSystemFileAttribute(boolean ignoreSystemFileAttribute){
		this.ignoreSystemFileAttribute = ignoreSystemFileAttribute;
	}

	public boolean isIgnoreAllFileAttributes(){
		return ignoreAllFileAttributes;
	}

	public void setIgnoreAllFileAttributes(boolean ignoreAllFileAttributes){
		this.ignoreAllFileAttributes = ignoreAllFileAttributes;
	}

	public boolean isIgnoreDateTimeAttributes(){
		return ignoreDateTimeAttributes;
	}

	public void setIgnoreDateTimeAttributes(boolean ignoreDateTimeAttributes){
		this.ignoreDateTimeAttributes = ignoreDateTimeAttributes;
	}

}
