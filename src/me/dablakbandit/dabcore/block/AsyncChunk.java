package me.dablakbandit.dabcore.block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.dablakbandit.dabcore.DabCorePlugin;
import me.dablakbandit.dabcore.utils.ChunkLocation;
import me.dablakbandit.dabcore.utils.NMSUtils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class AsyncChunk implements Listener {

	private static AsyncChunk instance = new AsyncChunk();

	public static AsyncChunk getInstance(){
		return instance;
	}

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
	private Method areNeighborsLoaded = NMSUtils.getMethod(chunk, "areNeighborsLoaded", int.class);
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


	private AsyncChunk(){
		TaskManager.getInstance().task(new Runnable(){
			@Override
			public void run(){
				Bukkit.getPluginManager().registerEvents(AsyncChunk.this, DabCorePlugin.getInstance());
			}
		});
	}

	@SuppressWarnings("deprecation")
	public Collection<AsyncBlock> sendChunk( Collection<AsyncBlock> fcs){
		HashMap<AsyncBlock, Object> packets = new HashMap<AsyncBlock, Object>();
		HashMap<String, ArrayList<AsyncBlock>> map = new HashMap<String, ArrayList<AsyncBlock>>();

		for(AsyncBlock fc : fcs){
			String world = fc.getChunkLoc().world;
			ArrayList<AsyncBlock> list = map.get(world);
			if(list == null){
				list = new ArrayList<AsyncBlock>();
				map.put(world, list);
			}
			list.add(fc);
		}
		int view = Bukkit.getServer().getViewDistance();
		try{
			for(Player player : Bukkit.getOnlinePlayers()){
				String world = player.getWorld().getName();
				ArrayList<AsyncBlock> list = map.get(world);
				if(list == null){
					continue;
				}
				Location loc = player.getLocation();
				int cx = loc.getBlockX() >> 4;
				int cz = loc.getBlockZ() >> 4;
				Object entity = getHandlePlayer.invoke(player);

				for(AsyncBlock fc : list){
					int dx = Math.abs(cx - fc.getChunkLoc().x);
					int dz = Math.abs(cz - fc.getChunkLoc().z);
					if((dx > view)||(dz > view)){
						continue;
					}
					Object con = playerConnection.get(entity);
					Object packet = packets.get(fc);
					if(packet == null){
						Object c = getHandleChunk.invoke(fc.getChunk());
						packet = MapChunk.newInstance(c, true, 65535);
						packets.put(fc, packet);
					}
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
		HashSet<AsyncBlock> chunks = new HashSet<AsyncBlock>();
		for(AsyncBlock fc : fcs){
			Chunk chunk = fc.getChunk();
			chunk.unload(true, false);
			chunk.load();
			ChunkLocation loc = fc.getChunkLoc();
			chunk.getWorld().refreshChunk(loc.x, loc.z);
			if(!fixLighting(fc, true)){
				chunks.add(fc);
			}
		}
		return chunks;
	}

	public boolean fixLighting(AsyncBlock fc, boolean fixAll){
		try{
			AsyncBlock bc = (AsyncBlock) fc;
			Chunk chunk = bc.getChunk();
			if(!chunk.isLoaded())chunk.load(false);

			Object c = getHandleChunk.invoke(chunk);

			if(!(boolean) areNeighborsLoaded.invoke(c, 1))return false;

			initLighting.invoke(c);

			if((bc.getTotalRelight() == 0 && !fixAll))return true;

			Object[] sections = (Object[])fsections.get(c);
			Object w = fworld.get(c);

			int x1 = chunk.getX() << 4;
			int x2 = chunk.getZ() << 4;

			for(int j = 0; j < sections.length; j++){
				Object section = sections[j];
				if(section == null){
					continue;
				}
				if((bc.getRelight(j) == 0 && !fixAll)||bc.getCount(j) == 0||(bc.getCount(j) >= 4096&&bc.getAir(j) == 0))continue;
				char[] array = getIdArray(section);
				int l = Cache.RANDOM.random(2);
				for(int k = 0; k < array.length; k++){
					int i = array[k];
					if(i < 16)continue;
					short id = Cache.CACHE_ID[i];
					switch(id){
					default:
						if(!fixAll)continue;
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
						int x = Cache.CACHE_X[j][k];
						int y = Cache.CACHE_Y[j][k];
						int z = Cache.CACHE_Z[j][k];
						if(isSurrounded(sections, x, y, z))continue;
						Object pos = blockPositionConstructor.newInstance(x1 + x, y, x2 + z);
						X.invoke(w, pos);
					}
				}
			}
			return true;
		}catch(Throwable e){
			e.printStackTrace();
		}
		return false;
	}

	private boolean isSurrounded(Object[] sections, int x, int y, int z){
		return isSolid(getId(sections, x, y + 1, z))
				&& isSolid(getId(sections, x + 1, y - 1, z))
				&& isSolid(getId(sections, x - 1, y, z))
				&& isSolid(getId(sections, x, y, z + 1))
				&& isSolid(getId(sections, x, y, z - 1));
	}

	@SuppressWarnings("deprecation")
	private boolean isSolid(int i){
		if(i == 0)return false;
		return Material.getMaterial(i).isOccluding();
	}

	private int getId(Object[] sections, int x, int y, int z){
		if(x < 0||x > 15||z < 0||z > 15)return 1;
		if(y < 0||y > 255)return 1;
		int i = Cache.CACHE_I[y][x][z];
		Object section = sections[i];
		if(section == null){
			return 0;
		}
		char[] array = getIdArray(section);
		int j = Cache.CACHE_J[y][x][z];
		return array[j] >> 4;
	}

	@SuppressWarnings("unchecked")
	public boolean setComponents(AsyncBlock fc){
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
				int j = Cache.CACHE_I[ly][lx][lz];
				int k = Cache.CACHE_J[ly][lx][lz];
				char[] array = fs.getIdArray(j);
				if(array == null)continue;
				if(array[k] != 0)iter.remove();
			}

			for(int i = 0; i < 16; i++){
				if((entities[i] != null)&&(fs.getCount(i) >= 4096)){
					entities[i].clear();
				}
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

	private Object newChunkSection( int i, boolean flag, char[] ids){
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

	private char[] getIdArray( Object obj){
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

	public AsyncBlock getChunk( ChunkLocation wrap){
		return new AsyncBlock(wrap);
	}

	private HashMap<ChunkLocation, AsyncBlock> toLight = new HashMap<ChunkLocation, AsyncBlock>();

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		if(toLight.size() == 0)return;
		Chunk chunk = event.getChunk();
		ChunkLocation loc = new ChunkLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
		for(int x = -1; x <= 1; x++){
			for(int z = -1; z <= 1; z++){
				ChunkLocation a = new ChunkLocation(loc.world, loc.x + x, loc.z + z);
				if(toLight.containsKey(a)){
					if(fixLighting(toLight.get(a), true)){
						toLight.remove(a);
						return;
					}
				}
			}
		}
	}

	private ConcurrentHashMap<ChunkLocation, AsyncBlock> blocks = new ConcurrentHashMap<ChunkLocation, AsyncBlock>();

	public boolean setBlock( String world, int x, int y, int z, short id, byte data){
		if((y > 255)||(y < 0))return false;
		ChunkLocation wrap = new ChunkLocation(world, x >> 4, z >> 4);
		x = x & 15;
		z = z & 15;
		AsyncBlock result = blocks.get(wrap);
		if(result == null){
			result = getChunk(wrap);
			result.setBlock(x, y, z, id, data);
			AsyncBlock previous = blocks.put(wrap, result);
			if(previous == null)return true;
			blocks.put(wrap, previous);
			result = previous;
		}
		result.setBlock(x, y, z, id, data);
		return true;
	}

	public AsyncBlock next(){
		try{
			if(blocks.size() == 0)return null;
			Iterator<Entry<ChunkLocation, AsyncBlock>> iter = blocks.entrySet().iterator();
			AsyncBlock toReturn = iter.next().getValue();
			if(Queue.getInstance().isWaiting())return null;
			iter.remove();
			execute(toReturn);
			return toReturn;
		}catch(Throwable e){
			e.printStackTrace();
			return null;
		}
	}

	private ArrayDeque<AsyncBlock> toUpdate = new ArrayDeque<AsyncBlock>();

	private boolean execute(AsyncBlock fc){
		if(fc == null)return false;
		Chunk chunk = fc.getChunk();
		chunk.load(true);
		if(!setComponents(fc))return false;
		toUpdate.add(fc);
		Queue.getInstance().addTask(new Runnable(){
			@Override
			public void run(){
				if(toUpdate.size() == 0)return;
				for(AsyncBlock fc : sendChunk(toUpdate))toLight.put(fc.getChunkLoc(), fc);
				toUpdate.clear();
			}
		});
		return true;
	}

	public void setChunk(AsyncBlock chunk){
		blocks.put(chunk.getChunkLoc(), (AsyncBlock) chunk);
	}

}
