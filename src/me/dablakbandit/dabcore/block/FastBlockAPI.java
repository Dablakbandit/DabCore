package me.dablakbandit.dabcore.block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.dablakbandit.dabcore.utils.NMSUtils;

public class FastBlockAPI{
	
	private static Class<?>			nms_world_class								= NMSUtils.getNMSClass("World");
	private static Class<?>			nms_player_connection_class					= NMSUtils.getNMSClass("PlayerConnection");
	private static Class<?>			nms_packet_class							= NMSUtils.getNMSClass("Packet");
	private static Class<?>			nms_entity_player_class						= NMSUtils.getNMSClass("EntityPlayer");
	private static Class<?>			obc_craft_world_class						= NMSUtils.getOBCClass("CraftWorld");
	private static Class<?>			obc_craft_chunk_class						= NMSUtils.getOBCClass("CraftChunk");
	private static Class<?>			obc_craft_player_class						= NMSUtils.getOBCClass("entity.CraftPlayer");
	private static Class<?>			nms_chunk_class								= NMSUtils.getNMSClass("Chunk");
	private static Class<?>			nms_block_class								= NMSUtils.getNMSClass("Block");
	private static Class<?>			nms_blocks_class							= NMSUtils.getNMSClass("Blocks");
	private static Class<?>			nms_block__tile_entity_class				= NMSUtils.getNMSClass("BlockTileEntity");
	private static Class<?>			nms_block_position_class					= NMSUtils.getNMSClass("BlockPosition");
	private static Class<?>			nms_iblock_data_class						= NMSUtils.getNMSClass("IBlockData");
	private static Class<?>			nms_chunk_section_class						= NMSUtils.getNMSClass("ChunkSection");
	private static Class<?>			nms_world_provider_class					= NMSUtils.getNMSClass("WorldProvider");
	private static Class<?>			nms_itile_entity_class						= NMSUtils.getNMSClass("ITileEntity");
	private static Class<?>			nms_tile_entity_class						= NMSUtils.getNMSClass("TileEntity");
	private static Class<?>			nms_packet_play_out_map_chunk_class			= NMSUtils.getNMSClass("PacketPlayOutMapChunk");
	private static Class<?>			nms_enum_tile_entity_state					= NMSUtils.getInnerClass(nms_chunk_class, "EnumTileEntityState");
	
	private static Method			player_method_get_handle					= NMSUtils.getMethod(obc_craft_player_class, "getHandle");
	private static Method			craft_chunk_method_get_handle				= NMSUtils.getMethod(obc_craft_chunk_class, "getHandle");
	private static Method			player_connection_method_send_packet		= NMSUtils.getMethod(nms_player_connection_class, "sendPacket", nms_packet_class);
	private static Method			world_method_get_handle						= NMSUtils.getMethod(obc_craft_world_class, "getHandle");
	private static Method			world_method_get_chunk						= NMSUtils.getMethod(nms_world_class, "getChunkAt", int.class, int.class);
	private static Method			world_method_s								= NMSUtils.getMethod(nms_world_class, "s", nms_block_position_class);
	private static Method			world_method_set_tile_entity				= NMSUtils.getMethod(nms_world_class, "setTileEntity", nms_block_position_class, nms_tile_entity_class);
	private static Method			block_method_get_combined					= NMSUtils.getMethod(nms_block_class, "getByCombinedId", int.class);
	private static Method			block_method_to_legacy_data					= NMSUtils.getMethod(nms_block_class, "toLegacyData", nms_iblock_data_class);
	private static Method			block_method_on_place						= NMSUtils.getMethod(nms_block_class, "onPlace", nms_world_class, nms_block_position_class, nms_iblock_data_class);
	private static Method			iblock_method_get_block						= NMSUtils.getMethod(nms_iblock_data_class, "getBlock");
	private static Method			tile_entity_method_invalidate_block_cache	= NMSUtils.getMethod(nms_tile_entity_class, "invalidateBlockCache");
	private static Method			itile_entity_method_a						= NMSUtils.getMethod(nms_itile_entity_class, "a", nms_world_class, int.class);
	private static Method			chunk_method_get_height_map					= NMSUtils.getMethod(nms_chunk_class, "r");
	private static Method			chunk_method_get_block_data					= NMSUtils.getMethod(nms_chunk_class, "getBlockData", nms_block_position_class);
	private static Method			chunk_method_get_sections					= NMSUtils.getMethod(nms_chunk_class, "getSections");
	private static Method			chunk_method_init_lighting					= NMSUtils.getMethod(nms_chunk_class, "initLighting");
	private static Method			chunk_method_a								= NMSUtils.getMethod(nms_chunk_class, "a", nms_block_position_class, nms_enum_tile_entity_state);
	private static Method			world_method_provider_m						= NMSUtils.getMethod(nms_world_provider_class, "m");
	private static Method			chunk_section_method_set_type				= NMSUtils.getMethod(nms_chunk_section_class, "setType", int.class, int.class, int.class, nms_iblock_data_class);
	private static Method			chunk_section_method_get_type				= NMSUtils.getMethod(nms_chunk_section_class, "getType", int.class, int.class, int.class);
	private static Method			block_method_remove							= NMSUtils.getMethod(nms_block_class, "remove", nms_world_class, nms_block_position_class, nms_iblock_data_class);
	
	private static Constructor<?>	con_block_position							= NMSUtils.getConstructor(nms_block_position_class, int.class, int.class, int.class);
	private static Constructor<?>	con_chunk_section							= NMSUtils.getConstructor(nms_chunk_section_class, int.class, boolean.class);
	private static Constructor<?>	con_packet_play_out_map_chunk				= NMSUtils.getConstructor(nms_packet_play_out_map_chunk_class, nms_chunk_class, int.class);
	
	private static Field			chunksection_empty							= NMSUtils.getField(nms_chunk_class, "a");
	private static Field			block_field_air								= NMSUtils.getField(nms_blocks_class, "AIR");
	private static Field			world_field_block_provider					= NMSUtils.getField(nms_world_class, "worldProvider");
	private static Field			world_field_client_side						= NMSUtils.getField(nms_world_class, "isClientSide");
	private static Field			world_field_capture_block_states			= NMSUtils.getField(nms_world_class, "captureBlockStates");
	private static Field			chunk_field_must_save						= NMSUtils.getField(nms_chunk_class, "mustSave");
	private static Field			player_field_player_connection				= NMSUtils.getField(nms_entity_player_class, "playerConnection");
	
	private static Object			empty_chunksection							= NMSUtils.getFieldValue(chunksection_empty, null);
	private static Object			block_air									= NMSUtils.getFieldValue(block_field_air, null);
	private static Object			check										= NMSUtils.getEnum("CHECK", nms_enum_tile_entity_state);
	
	public static void init(){
		
	}
	
	public static void setBlockFast(World world, int x, int y, int z, int blockId, byte data){
		try{
			Object nms_world = world_method_get_handle.invoke(world);
			Object nms_chunk = world_method_get_chunk.invoke(nms_world, x >> 4, z >> 4);
			Object nms_bp = con_block_position.newInstance(x, y, z);
			int combined = blockId + (data << 12);
			Object nms_ibd = block_method_get_combined.invoke(null, combined);
			int i = x & 0xF;
			int j = y;
			int k = z & 0xF;
			int l = k << 4 | i;
			
			int[] height_map = (int[])chunk_method_get_height_map.invoke(nms_chunk);
			
			if(j >= height_map[l] - 1){
				height_map[l] = 64537;
			}
			Object nms_ibd1 = chunk_method_get_block_data.invoke(nms_chunk, nms_bp);
			if(nms_ibd == nms_ibd1){ return; }
			Object nms_block = iblock_method_get_block.invoke(nms_ibd);
			Object nms_block1 = iblock_method_get_block.invoke(nms_ibd1);
			
			Object[] sections = (Object[])chunk_method_get_sections.invoke(nms_chunk);
			
			Object chunk_section = sections[(j >> 4)];
			if(chunk_section == empty_chunksection){
				if(nms_block == block_air){ return; }
				Object world_provider = world_field_block_provider.get(nms_world);
				chunk_section = con_chunk_section.newInstance(j >> 4 << 4, world_method_provider_m.invoke(world_provider));
				sections[(j >> 4)] = chunk_section;
			}
			chunk_section_method_set_type.invoke(chunk_section, i, j & 0xF, k, nms_ibd);
			if(nms_block != nms_block1){
				if((boolean)world_field_client_side.get(nms_world)){
					block_method_remove.invoke(nms_block1, nms_world, nms_bp, nms_ibd1);
				}else if(nms_itile_entity_class.isAssignableFrom(nms_block1.getClass())){
					world_method_s.invoke(nms_world, nms_bp);
				}
			}
			Object block_type = chunk_section_method_get_type.invoke(chunk_section, i, j & 0xF, k);
			Object nms_block2 = iblock_method_get_block.invoke(block_type);
			if(nms_block2 != nms_block){ return; }
			if(nms_itile_entity_class.isAssignableFrom(nms_block1.getClass())){
				Object tile_entity = chunk_method_a.invoke(nms_chunk, nms_bp, check);
				if(tile_entity != null){
					tile_entity_method_invalidate_block_cache.invoke(tile_entity);
				}
			}
			if(!(boolean)world_field_client_side.get(nms_world) && nms_block1 != nms_block && !(boolean)world_field_capture_block_states.get(nms_world) || nms_block__tile_entity_class.isAssignableFrom(nms_block.getClass())){
				block_method_on_place.invoke(nms_block, nms_world, nms_bp, nms_ibd);
			}
			
			if(nms_itile_entity_class.isAssignableFrom(nms_block.getClass())){
				Object tile_entity = chunk_method_a.invoke(nms_chunk, nms_bp, check);
				if(tile_entity == null){
					itile_entity_method_a.invoke(nms_block, nms_world, block_method_to_legacy_data.invoke(nms_block, nms_ibd));
					world_method_set_tile_entity.invoke(nms_world, nms_bp, tile_entity);
				}
				if(tile_entity != null){
					tile_entity_method_invalidate_block_cache.invoke(tile_entity);
				}
			}
			chunk_field_must_save.set(nms_chunk, true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void setBlockFast(Location l, int blockId, byte data){
		setBlockFast(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), blockId, data);
	}
	
	@SuppressWarnings("deprecation")
	public static void setBlockFast(Location l, Material m, byte data){
		setBlockFast(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), m.getId(), data);
	}
	
	protected static int getLower(int x, int z){
		if(x < z){ return x; }
		return z;
	}
	
	protected static int getGreater(int x, int z){
		if(x < z){ return z; }
		return x;
	}
	
	public static void updateLight(World world, int Blockx1, int Blockz1, int Blockx2, int Blockz2){
		int cx1 = FastBlockAPI.getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = FastBlockAPI.getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = FastBlockAPI.getLower(Blockx1, Blockx2) >> 4;
		int cz2 = FastBlockAPI.getLower(Blockz1, Blockz2) >> 4;
		
		try{
			Object nms_world = world_method_get_handle.invoke(world);
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					Object nms_chunk = world_method_get_chunk.invoke(nms_world, i, j);
					updateLight(nms_chunk);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateLight(Location from, Location to){
		updateLight(from.getWorld(), from.getBlockX(), from.getBlockZ(), to.getBlockX(), to.getBlockZ());
	}
	
	private static void updateLight(Object nms_chunk) throws Exception{
		chunk_method_init_lighting.invoke(nms_chunk);
	}
	
	public static void updateChunk(World world, int Blockx1, int Blockz1, int Blockx2, int Blockz2){
		int cx1 = FastBlockAPI.getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = FastBlockAPI.getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = FastBlockAPI.getLower(Blockx1, Blockx2) >> 4;
		int cz2 = FastBlockAPI.getLower(Blockz1, Blockz2) >> 4;
		try{
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					updateChunk(world, world.getChunkAt(i, j));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateChunk(Location from, Location to){
		updateChunk(from.getWorld(), from.getBlockX(), from.getBlockZ(), to.getBlockX(), to.getBlockZ());
	}
	
	private static void updateChunk(World world, Chunk chunk) throws Exception{
		Object nms_chunk = craft_chunk_method_get_handle.invoke(chunk);
		Object packet = con_packet_play_out_map_chunk.newInstance(nms_chunk, 65535);
		for(Player p : world.getPlayers()){
			Object nms_player = player_method_get_handle.invoke(p);
			Object con = player_field_player_connection.get(nms_player);
			player_connection_method_send_packet.invoke(con, packet);
		}
	}
	
	public static void updateChunkAndLight(World world, int Blockx1, int Blockz1, int Blockx2, int Blockz2){
		int cx1 = FastBlockAPI.getGreater(Blockx1, Blockx2) >> 4;
		int cz1 = FastBlockAPI.getGreater(Blockz1, Blockz2) >> 4;
		int cx2 = FastBlockAPI.getLower(Blockx1, Blockx2) >> 4;
		int cz2 = FastBlockAPI.getLower(Blockz1, Blockz2) >> 4;
		try{
			for(int i = cx2; i <= cx1; i++){
				for(int j = cz2; j <= cz1; j++){
					Chunk chunk = world.getChunkAt(i, j);
					Object nms_chunk = craft_chunk_method_get_handle.invoke(chunk);
					updateLight(nms_chunk);
					updateChunk(world, chunk);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void updateChunkAndLight(Location from, Location to){
		updateChunkAndLight(from.getWorld(), from.getBlockX(), from.getBlockZ(), to.getBlockX(), to.getBlockZ());
	}
	
}
