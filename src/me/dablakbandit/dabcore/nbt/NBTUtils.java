package me.dablakbandit.dabcore.nbt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public final class NBTUtils{
	public static final Pattern NBT_FILE_NAME_PATTERN = Pattern.compile("^(.*)\\.dat$");
	
	private NBTUtils(){
		throw new AssertionError("Not instantiable");
	}
	
	public static List<CompoundTag> readNBTFiles(Collection<File> files, NBTCompression compression) throws IOException{
		List<CompoundTag> result = new ArrayList<>(files.size());
		
		for(File file : files){
			CompoundTag tag = readNBTFile(file, compression);
			result.add(tag);
		}
		
		return result;
	}
	
	public static CompoundTag readNBTFile(File levelFile, NBTCompression compression) throws IOException{
		try(NBTInputStream in = new NBTInputStream(new BufferedInputStream(new FileInputStream(levelFile)), compression)){
			return (CompoundTag)in.readTag();
		}
	}
}
