package me.dablakbandit.dabcore.block;

import org.bukkit.Location;
import org.bukkit.Material;

public class AsyncBlockPlacerAPI {

	@SuppressWarnings("deprecation")
	public static void setBlockAsync(Location loc, Material m){
		setBlock(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), (short)m.getId(), (byte)0);
	}
	
	@SuppressWarnings("deprecation")
	public static void setBlockAsync(Location loc, Material m, byte data){
		setBlock(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), (short)m.getId(), data);
	}
	
	public static void setBlock(String world, int x, int y, int z, short id){
		setBlock(world, x, y, z, id, (byte)0);
	}
	
	public static void setBlock(String world, int x, int y, int z, short id, byte data){
		AsyncBlockPlacer.getInstance().setBlock(world, x, y, z, id, data);
	}
	
}
