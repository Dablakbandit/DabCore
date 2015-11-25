package me.dablakbandit.dabcore.block;

import me.dablakbandit.dabcore.utils.ChunkLocation;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

public class AsyncBlockPlacer {

	@SuppressWarnings("deprecation")
	public static void setBlockAsync(Location loc, Material m){
		Queue.getInstance().setBlock(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), (short)m.getId());
	}

	@SuppressWarnings("deprecation")
	public static void setBlockAsync(Location loc, Material m, byte data){
		Queue.getInstance().setBlock(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), (short)m.getId(), data);
	}

	public static void setBlockAsync(String world, int x, int y, int z, short id, byte data) {
		Queue.getInstance().setBlock(world, x, y, z, id, data);
	}

	public static AsyncBlock createChunk(){
		return AsyncChunk.getInstance().getChunk(new ChunkLocation(null, 0, 0));
	}

	public static void setChunkAsync(AsyncBlock data, ChunkLocation location){
		data.setChunkLoc(location);
		data.addToQueue();
	}

	public static void setChunkAsync(AsyncBlock data, Chunk chunk){
		ChunkLocation loc = new ChunkLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
		data.setChunkLoc(loc);
		data.addToQueue();
	}

	public static void fixLighting(ChunkLocation loc, boolean fixAll){
		AsyncChunk.getInstance().fixLighting(AsyncChunk.getInstance().getChunk(loc), fixAll);
	}

	public static void fixLighting(Chunk chunk, boolean fixAll){
		ChunkLocation loc = new ChunkLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
		AsyncChunk.getInstance().fixLighting(AsyncChunk.getInstance().getChunk(loc), fixAll);
	}

	public static void addTask(Runnable whenDone){
		Queue.getInstance().addTask(whenDone);
	}
}
