package me.dablakbandit.dabcore.block;

import java.util.Arrays;

import me.dablakbandit.dabcore.utils.ChunkLocation;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class AsyncBlock {

	private char[][] ids;

	private short[] count;
	private short[] air;
	private short[] relight;
	private int[][] biomes;

	private ChunkLocation location;
	private Chunk chunk;

	protected AsyncBlock(ChunkLocation chunk){
		this.location = chunk;
		ids = new char[16][];
		count = new short[16];
		air = new short[16];
		relight = new short[16];
	}

	public Chunk getChunk(){
		if (chunk == null) {
			ChunkLocation cl = getChunkLoc();
			chunk = Bukkit.getWorld(cl.world).getChunkAt(cl.x, cl.z);
		}
		return chunk;
	}

	public int getCount(int i){
		return count[i];
	}

	public int getAir(int i){
		return air[i];
	}

	public void setCount(int i, short value){
		count[i] = value;
	}

	public int getRelight(int i){
		return relight[i];
	}

	public int getTotalCount(){
		int total = 0;
		for(int i = 0; i < 16; i++)total += count[i];
		return total;
	}

	public int getTotalRelight(){
		if(getTotalCount() == 0 && biomes == null){
			Arrays.fill(count, (short) 1);
			Arrays.fill(relight, Short.MAX_VALUE);
			return Short.MAX_VALUE;
		}
		int total = 0;
		for(int i = 0; i < 16; i++)total += relight[i];
		return total;
	}

	public char[] getIdArray(int i){
		return ids[i];
	}

	public void clear(){
		ids = null;
		biomes = null;
	}

	public int[][] getBiomeArray(){
		return biomes;
	}

	public void setBlock(int x, int y, int z, int id, byte data){
		int i = y>>4;
		int j = (((y & 0xF) << 8) | (z << 4) | x);
		char[] vs = ids[i];
		if (vs == null) {
			vs = ids[i] = new char[4096];
			count[i]++;
		} else if (vs[j] == 0) {
			count[i]++;
		}
		switch (id) {
		case 0:
			air[i]++;
			vs[j] = (char) 1;
			return;
		case 10:
		case 11:
		case 39:
		case 40:
		case 50:
		case 51:
		case 74:
		case 76:
		case 89:
		case 122:
		case 124:
		case 138:
		case 169:
			relight[i]++;
		case 2:
		case 4:
		case 13:
		case 14:
		case 15:
		case 20:
		case 21:
		case 22:
		case 30:
		case 32:
		case 37:
		case 41:
		case 42:
		case 45:
		case 46:
		case 47:
		case 48:
		case 49:
		case 55:
		case 56:
		case 57:
		case 58:
		case 60:
		case 7:
		case 8:
		case 9:
		case 73:
		case 75:
		case 78:
		case 79:
		case 80:
		case 81:
		case 82:
		case 83:
		case 85:
		case 87:
		case 88:
		case 101:
		case 102:
		case 103:
		case 110:
		case 112:
		case 113:
		case 121:
		case 129:
		case 133:
		case 165:
		case 166:
		case 170:
		case 172:
		case 173:
		case 174:
		case 181:
		case 182:
		case 188:
		case 189:
		case 190:
		case 191:
		case 192:
			vs[j] = (char) (id << 4);
			return;
		case 130:
		case 62:
			relight[i]++;
		case 54:
		case 146:
		case 61:
		case 65:
		case 68:
			if(data < 2)data = 2;
		default:
			vs[j] = (char) ((id << 4) + data);
			return;
		}
	}

	public void setChunkLoc(ChunkLocation loc){
		this.location = loc;
		this.chunk = null;
	}

	public ChunkLocation getChunkLoc(){
		return this.location;
	}
}
