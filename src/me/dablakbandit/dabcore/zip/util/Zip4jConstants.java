package me.dablakbandit.dabcore.zip.util;

public interface Zip4jConstants{

	// Compression Types
	static final int COMP_STORE = 0;
	// static final int COMP_FILE_SHRUNK = 1;
	// static final int COMP_FILE_RED_COMP_FACTOR_1 = 2;
	// static final int COMP_FILE_RED_COMP_FACTOR_2 = 3;
	// static final int COMP_FILE_RED_COMP_FACTOR_3 = 4;
	// static final int COMP_FILE_RED_COMP_FACTOR_4 = 5;
	// static final int COMP_FILE_IMPLODED = 6;
	static final int COMP_DEFLATE = 8;
	// static final int COMP_FILE_ENHANCED_DEFLATED = 9;
	// static final int COMP_PKWARE_DATA_COMP_LIB_IMPL = 10;
	// static final int COMP_BZIP2 = 12;
	// static final int COMP_LZMA = 14;
	// static final int COMP_IBM_TERSE = 18;
	// static final int COMP_IBM_LZ77 =19;
	// static final int COMP_WAVPACK = 97;
	// static final int COMP_PPMD = 98;
	static final int COMP_AES_ENC = 99;

	// Compression level for deflate algorithm
	static final int DEFLATE_LEVEL_FASTEST = 1;
	static final int DEFLATE_LEVEL_FAST = 3;
	static final int DEFLATE_LEVEL_NORMAL = 5;
	static final int DEFLATE_LEVEL_MAXIMUM = 7;
	static final int DEFLATE_LEVEL_ULTRA = 9;

	// Encryption types
	static final int ENC_NO_ENCRYPTION = -1;
	static final int ENC_METHOD_STANDARD = 0;
	// static final int ENC_METHOD_STRONG = 1;
	static final int ENC_METHOD_AES = 99;

	// AES Key Strength
	static final int AES_STRENGTH_128 = 0x01;
	static final int AES_STRENGTH_192 = 0x02;
	static final int AES_STRENGTH_256 = 0x03;
}
