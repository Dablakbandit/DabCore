package me.dablakbandit.dabcore.nbt;

public enum NBTCompression{
	UNCOMPRESSED(0), GZIP(1), ZLIB(2), FROM_BYTE(-1);
	
	private final int compressionId;
	
	NBTCompression(int compressionId){
		this.compressionId = compressionId;
	}
	
	public int getCompressionId(){
		return compressionId;
	}
	
	public static NBTCompression fromId(int id){
		for(NBTCompression value : values())
			if(value.compressionId == id)
				return value;
		throw new IllegalArgumentException("[JNBT] No " + NBTCompression.class.getSimpleName() + " enum constant with id: " + id);
	}
}
