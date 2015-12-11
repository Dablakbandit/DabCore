package me.dablakbandit.dabcore.block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import me.dablakbandit.dabcore.utils.ChunkLocation;
import me.dablakbandit.dabcore.utils.NMSUtils;
import me.dablakbandit.dabcore.utils.PseudoRandom;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class AsyncBlockPlacer {
	
	private static AsyncBlockPlacer blockplacer = new AsyncBlockPlacer();
	
	public static AsyncBlockPlacer getInstance(){
		return blockplacer;
	}
	
	public static PseudoRandom RANDOM = new PseudoRandom();	
	
	//BLOCK/CHUNK
	private Class<?> entityPlayer = NMSUtils.getNMSClass("EntityPlayer");
	private Class<?> mapChunk = NMSUtils.getNMSClass("PacketPlayOutMapChunk");
	private Class<?> packet = NMSUtils.getNMSClass("Packet");
	private Class<?> connection = NMSUtils.getNMSClass("PlayerConnection");
	private Class<?> chunk = NMSUtils.getNMSClass("Chunk");
	private Class<?> craftPlayer = NMSUtils.getOBCClass("entity.CraftPlayer");
	private Class<?> craftChunk = NMSUtils.getOBCClass("CraftChunk");
	private Class<?> world = NMSUtils.getNMSClass("World");
	private Class<?> blockPosition = NMSUtils.getNMSClass("BlockPosition");
	private Class<?> chunkSection = NMSUtils.getNMSClass("ChunkSection");

	private Method getHandlePlayer = NMSUtils.getMethod(craftPlayer, "getHandle");
	private Method getHandleChunk = NMSUtils.getMethod(craftChunk, "getHandle");
	private Method X = NMSUtils.getMethod(world, "x", blockPosition);
	private Method getIdArray = NMSUtils.getMethod(chunkSection, "getIdArray");
	private Method sendPacket = NMSUtils.getMethod(connection, "sendPacket", packet);
	private Method initLighting = NMSUtils.getMethod(chunk, "initLighting");
	private Method getX = NMSUtils.getMethod(blockPosition, "getX");
	private Method getY = NMSUtils.getMethod(blockPosition, "getY");
	private Method getZ = NMSUtils.getMethod(blockPosition, "getZ");

	private Constructor<?> MapChunk = NMSUtils.getConstructor(mapChunk, chunk, boolean.class, int.class);
	private Constructor<?> blockPositionConstructor = NMSUtils.getConstructor(blockPosition, int.class, int.class, int.class);
	private Constructor<?> chunkSectionConstructor = NMSUtils.getConstructor(chunkSection, int.class, boolean.class, char[].class);

	private Field playerConnection = NMSUtils.getField(entityPlayer, "playerConnection");
	private Field fsections = NMSUtils.getField(chunk, "sections");
	private Field fworld = NMSUtils.getField(chunk, "world");
	private Field ftileEntities = NMSUtils.getField(chunk, "tileEntities");
	private Field fentitySlices = NMSUtils.getField(chunk, "entitySlices");
	//END
		
	
	//BLOCK PLACER	
	public boolean setBlock(String world, int x, int y, int z, short id, byte data){
		if((y > 255)||(y < 0))return false;
		ChunkLocation wrap = new ChunkLocation(world, x >> 4, z >> 4);
		x = x & 15;
		z = z & 15;
		AsyncBlock result = getChunk(wrap);
		result.setBlock(x, y, z, id, data);
		execute(result);
		return true;
	}
	
	private AsyncBlock getChunk(ChunkLocation wrap){
		return new AsyncBlock(wrap);
	}
	
	private boolean execute(AsyncBlock fc){
		if(fc == null)return false;
		Chunk chunk = fc.getChunk();
		chunk.load(true);
		if(!setComponents(fc))return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean setComponents(AsyncBlock fc){
		try{
			AsyncBlock fs = (AsyncBlock) fc;
			Chunk chunk = fs.getChunk();
			World world = chunk.getWorld();
			boolean flag = world.getEnvironment() == Environment.NORMAL;
			Object c = getHandleChunk.invoke(chunk);
			Object[] sections = (Object[]) fsections.get(c);
			HashMap<?, ?> tiles = (HashMap<?, ?>) ftileEntities.get(c);
			List<?>[] entities = (List<?>[]) fentitySlices.get(c);
			Set<Entry<?, ?>> entryset = (Set<Entry<?, ?>>)(Set<?>)tiles.entrySet();
			Iterator<Entry<?, ?>> iter = entryset.iterator();
			while(iter.hasNext()){
				Entry<?, ?> tile = iter.next();
				Object pos = tile.getKey();
				int lx = (int) getX.invoke(pos) & 15;
				int ly = (int) getY.invoke(pos);
				int lz = (int) getZ.invoke(pos) & 15;
				int j = ly>>4;
				int k = (((ly & 0xF) << 8) | (lz << 4) | lx);
				char[] array = fs.getIdArray(j);
				if(array == null)continue;
				if(array[k] != 0)iter.remove();
			}
			for(int i = 0; i < 16; i++){
				if((entities[i] != null)&&(fs.getCount(i) >= 4096))entities[i].clear();
			}
			for(int j = 0; j < sections.length; j++){
				if(fs.getCount(j) == 0)continue;
				char[] newArray = fs.getIdArray(j);
				if(newArray == null)continue;
				Object section = sections[j];
				if((section == null)||(fs.getCount(j) >= 4096)){
					section = sections[j] = newChunkSection(j << 4, flag, newArray);
					continue;
				}
				char[] currentArray = getIdArray(section);
				boolean fill = true;
				for(int k = 0; k < newArray.length; k++){
					char n = newArray[k];
					if(n == 0){
						fill = false;
						continue;
					}
					switch(n){
					case 0:
						fill = false;
						continue;
					case 1:
						fill = false;
						currentArray[k] = 0;
						continue;
					default:
						currentArray[k] = n;
						continue;
					}
				}
				if(fill)fs.setCount(j, Short.MAX_VALUE);
			}
			fs.clear();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private Object newChunkSection(int i, boolean flag, char[] ids){
		try{
			return chunkSectionConstructor.newInstance(i, flag, ids);
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		return null;
	}
	//END
	
	
	//UPDATE CHUNK	
	@SuppressWarnings("deprecation")
	public void updateChunk(Chunk chunk){
		int view = Bukkit.getServer().getViewDistance()+2;
		int amount = 0 ;
		try{
			Object c = getHandleChunk.invoke(chunk);
			Object packet  = MapChunk.newInstance(c, true, 65535);
			for(Player player : Bukkit.getOnlinePlayers()){
				String world = player.getWorld().getName();
				if(world.equals(chunk.getWorld().getName())){
					Location loc = player.getLocation();
					int cx = loc.getBlockX() >> 4;
					int cz = loc.getBlockZ() >> 4;
					Object entity = getHandlePlayer.invoke(player);
					int dx = Math.abs(cx - chunk.getX());
					int dz = Math.abs(cz - chunk.getZ());
					if((dx > view)||(dz > view))continue;
					amount++;
					Object con = playerConnection.get(entity);
					sendPacket.invoke(con, packet);
				}
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}catch(InstantiationException e){
			e.printStackTrace();
		}
		chunk.unload(true, false);
		fixLighting(chunk);
		if(amount>0){
			chunk.load();
			chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
		}
	}
	//END
	
	
	//FIX CHUNK LIGHTING
	public void fixLighting(Chunk chunk){
		try{
			Object c = getHandleChunk.invoke(chunk);

			initLighting.invoke(c);

			Object[] sections = (Object[])fsections.get(c);
			Object w = fworld.get(c);

			int x1 = chunk.getX() << 4;
			int x2 = chunk.getZ() << 4;

			for(int j = 0; j < sections.length; j++){
				Object section = sections[j];
				if(section == null){
					continue;
				}
				char[] array = getIdArray(section);
				int l = RANDOM.random(2);
				for(int k = 0; k < array.length; k++){
					int i = array[k];
					if(i < 16)continue;
					int id = i >> 4;
					switch(id){
					default:
						if((k & 1) == l){
							l = 1 - l;
							continue;
						}
					case 10:
					case 11:
					case 39:
					case 40:
					case 50:
					case 51:
					case 62:
					case 74:
					case 76:
					case 89:
					case 122:
					case 124:
					case 130:
					case 138:
					case 169:
						Object pos = blockPositionConstructor.newInstance(x1, 0, x2);
						X.invoke(w, pos);
					}
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
		return;
	}
	//END
	
	
	//CHUNK UPDATE/LIGHTING
	private char[] getIdArray(Object obj){
		try{
			return (char[]) getIdArray.invoke(obj);
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		return null;
	}
	//END
	
	class BlockRunnable implements Runnable{
		
		private AsyncBlock block;
		
		BlockRunnable(AsyncBlock block){
			this.block = block;
		}

		@Override
		public void run() {
			execute(block);
		}
	}
}