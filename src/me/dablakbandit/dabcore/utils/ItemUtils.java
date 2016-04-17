package me.dablakbandit.dabcore.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import me.dablakbandit.dabcore.json.JSONArray;
import me.dablakbandit.dabcore.json.JSONObject;
import me.dablakbandit.dabcore.nbt.NBTConstants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils{

	private static boolean banner = getBanner();

	private static boolean getBanner(){
		try{
			Material m = Material.valueOf("BANNER");
			if(m != null){ return true; }
		}catch(Exception e){}
		return false;
	}

	public static boolean hasBanner(){
		return banner;
	}

	public static Class<?> nmis = NMSUtils.getNMSClass("ItemStack"), cis = NMSUtils.getOBCClass("inventory.CraftItemStack");
	public static Method nmscopy = NMSUtils.getMethod(cis, "asNMSCopy", ItemStack.class);

	public static Object getNMSCopy(ItemStack is) throws Exception{
		return nmscopy.invoke(null, is);
	}

	public static Method hastag = NMSUtils.getMethod(nmis, "hasTag");

	public static boolean hasTag(Object is) throws Exception{
		return (boolean)hastag.invoke(is);
	}

	public static Method acm = NMSUtils.getMethod(cis, "asCraftMirror", nmis);

	public static ItemStack asCraftMirror(Object nis) throws Exception{
		return (ItemStack)acm.invoke(null, nis);
	}

	public static Method abc = NMSUtils.getMethod(cis, "asBukkitCopy", nmis);

	public static ItemStack asBukkitCopy(Object nmis) throws Exception{
		return (ItemStack)abc.invoke(null, nmis);
	}

	private static Class<?> ni = NMSUtils.getNMSClass("Item");

	private static Method gn = NMSUtils.getMethod(nmis, "getName");

	public static String getName(ItemStack is){
		try{
			return (String)gn.invoke(getNMSCopy(is));
		}catch(Exception e){
			return null;
		}
	}

	private static Method gi = NMSUtils.getMethod(nmis, "getItem"), ia = getA();

	public static Object getItem(Object nis) throws Exception{
		return gi.invoke(nis);
	}

	private static Method getA(){
		Method m = NMSUtils.getMethod(ni, "a", nmis);
		if(m == null){
			m = NMSUtils.getMethod(ni, "n", nmis);
		}
		return m;
	}

	public static String getRawName(ItemStack is){
		try{
			Object nis = getNMSCopy(is);
			return (String)ia.invoke(gi.invoke(nis), nis);
		}catch(Exception e){
			return null;
		}
	}

	private static Method gin = NMSUtils.getMethod(ni, "getName");

	public static String getItemName(ItemStack is){
		try{
			return (String)gin.invoke(gi.invoke(getNMSCopy(is)));
		}catch(Exception e){
			return null;
		}
	}

	private static Object registry = getRegistry();

	private static Object getRegistry(){
		try{
			return NMSUtils.getFieldSilent(ni, "REGISTRY").get(null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static Class<?> nmrs = NMSUtils.getNMSClass("RegistrySimple");
	private static Field nmrsc = NMSUtils.getField(nmrs, "c");

	public static String getMinecraftName(ItemStack is){
		String name = getItemName(is);
		try{
			Map<?, ?> m = (Map<?, ?>)nmrsc.get(registry);
			for(Entry<?, ?> e : m.entrySet()){
				Object item = e.getValue();
				String s = (String)gin.invoke(item);
				if(name.equals(s)){ return e.getKey().toString(); }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Class<?> nbttc = NMSUtils.getNMSClass("NBTTagCompound");
	public static Field tag = NMSUtils.getField(nmis, "tag");

	public static Object getTag(Object is) throws Exception{
		return tag.get(is);
	}

	public static void setTag(Object is, Object tag1) throws Exception{
		tag.set(is, tag1);
	}

	public static Method nbtcie = NMSUtils.getMethod(nbttc, "isEmpty");

	public static boolean isEmpty(Object tag) throws Exception{
		return (boolean)nbtcie.invoke(tag);
	}

	public static Field nbttcm = NMSUtils.getField(nbttc, "map");

	public static Object getMap(Object tag) throws Exception{
		return nbttcm.get(tag);
	}

	public static Class<?> nbtb = NMSUtils.getNMSClass("NBTBase");
	public static Method nbttcs = NMSUtils.getMethod(nbttc, "set", String.class, nbtb);
	public static Method nbttcss = NMSUtils.getMethod(nbttc, "setString", String.class, String.class);
	public static Method nbttcsi = NMSUtils.getMethod(nbttc, "setInt", String.class, int.class);
	public static Method nbttcsd = NMSUtils.getMethod(nbttc, "setDouble", String.class, double.class);
	public static Method nbttcsl = NMSUtils.getMethod(nbttc, "setLong", String.class, long.class);
	public static Method nbttcss1 = NMSUtils.getMethod(nbttc, "setShort", String.class, short.class);

	public static void set(Object tag, String key, Object value) throws Exception{
		nbttcs.invoke(tag, key, value);
	}

	public static void setString(Object tag, String key, String value) throws Exception{
		nbttcss.invoke(tag, key, value);
	}

	public static void setShort(Object tag, String key, short value) throws Exception{
		nbttcss1.invoke(tag, key, value);
	}

	public static void setInt(Object tag, String key, int i) throws Exception{
		nbttcsi.invoke(tag, key, i);
	}

	public static void setDouble(Object tag, String key, double d) throws Exception{
		nbttcsd.invoke(tag, key, d);
	}

	public static void setLong(Object tag, String key, long l) throws Exception{
		nbttcsl.invoke(tag, key, l);
	}

	public static Method nbttchk = NMSUtils.getMethod(nbttc, "hasKey", String.class);

	public static boolean hasKey(Object tag, String key) throws Exception{
		return (boolean)nbttchk.invoke(tag, key);
	}

	public static Method nbttcg = NMSUtils.getMethod(nbttc, "get", String.class);
	public static Method nbttcgs = NMSUtils.getMethod(nbttc, "getString", String.class);
	public static Method nbttcgi = NMSUtils.getMethod(nbttc, "getInt", String.class);
	public static Method nbttcgd = NMSUtils.getMethod(nbttc, "getDouble", String.class);
	public static Method nbttcgl = NMSUtils.getMethod(nbttc, "getLong", String.class);
	public static Method nbttcgs1 = NMSUtils.getMethod(nbttc, "getShort", String.class);

	public static Object get(Object tag, String key) throws Exception{
		return nbttcg.invoke(tag, key);
	}

	public static String getString(Object tag, String key) throws Exception{
		return (String)nbttcgs.invoke(tag, key);
	}

	public static int getInt(Object tag, String key) throws Exception{
		return (int)nbttcgi.invoke(tag, key);
	}

	public static double getDouble(Object tag, String key) throws Exception{
		return (double)nbttcgd.invoke(tag, key);
	}

	public static long getLong(Object tag, String key) throws Exception{
		return (long)nbttcgl.invoke(tag, key);
	}

	public static short getShort(Object tag, String key) throws Exception{
		return (short)nbttcgs1.invoke(tag, key);
	}

	public static Constructor<?> nbttcc = NMSUtils.getConstructor(nbttc);

	public static Object getNewNBTTagCompound() throws Exception{
		return nbttcc.newInstance();
	}

	public static Method hkot = NMSUtils.getMethod(nbttc, "hasKeyOfType", String.class, int.class);

	public static boolean hasAttributeModifiersKey(Object tag) throws Exception{
		return (boolean)hkot.invoke(tag, "AttributeModifiers", 9);
	}

	public static Class<?> nbttl = NMSUtils.getNMSClass("NBTTagList");
	public static Method gl = NMSUtils.getMethod(nbttc, "getList", String.class, int.class);
	public static Method gb = NMSUtils.getMethod(nbttc, "getBoolean", String.class);
	public static Method sb = NMSUtils.getMethod(nbttc, "setBoolean", String.class, boolean.class);
	public static Method nbttla = NMSUtils.getMethod(nbttl, "add", nbtb);
	public static Constructor<?> nbttlc = NMSUtils.getConstructor(nbttl);

	public static Object getList(Object tag) throws Exception{
		return gl.invoke(tag, "AttributeModifiers", 9);
	}

	public static Object getList(Object tag, String name, int id) throws Exception{
		return gl.invoke(tag, name, id);
	}

	public static boolean getUnbreakable(Object tag) throws Exception{
		return (boolean)gb.invoke(tag, "Unbreakable");
	}

	public static void setUnbreakable(Object tag, boolean value) throws Exception{
		sb.invoke(tag, "Unbreakable", value);
	}

	public static Object getNewNBTTagList() throws Exception{
		return nbttlc.newInstance();
	}

	public static void addToList(Object taglist, Object nbt) throws Exception{
		nbttla.invoke(taglist, nbt);
	}

	public static Method gs = NMSUtils.getMethod(nbttl, "size");

	public static int getSize(Object list) throws Exception{
		return (int)gs.invoke(list);
	}

	public static Method g = NMSUtils.getMethod(nbttl, "get", int.class);

	public static Object get(Object tlist, int i) throws Exception{
		return g.invoke(tlist, i);
	}

	public static Method gti = NMSUtils.getMethod(nbtb, "getTypeId");

	public static Class<?> nbtby = NMSUtils.getNMSClass("NBTTagByte");
	public static Class<?> nbtba = NMSUtils.getNMSClass("NBTTagByteArray");
	public static Class<?> nbtd = NMSUtils.getNMSClass("NBTTagDouble");
	public static Class<?> nbtf = NMSUtils.getNMSClass("NBTTagFloat");
	public static Class<?> nbti = NMSUtils.getNMSClass("NBTTagInt");
	public static Class<?> nbtia = NMSUtils.getNMSClass("NBTTagIntArray");
	public static Class<?> nbtl = NMSUtils.getNMSClass("NBTTagList");
	public static Class<?> nbtlo = NMSUtils.getNMSClass("NBTTagLong");
	public static Class<?> nbts = NMSUtils.getNMSClass("NBTTagShort");
	public static Class<?> nbtst = NMSUtils.getNMSClass("NBTTagString");

	public static Constructor<?> nbtbc = NMSUtils.getConstructor(nbtby, byte.class);
	public static Constructor<?> nbtbac = NMSUtils.getConstructor(nbtba, byte[].class);
	public static Constructor<?> nbtdc = NMSUtils.getConstructor(nbtd, double.class);
	public static Constructor<?> nbtfc = NMSUtils.getConstructor(nbtf, float.class);
	public static Constructor<?> nbtic = NMSUtils.getConstructor(nbti, int.class);
	public static Constructor<?> nbtiac = NMSUtils.getConstructor(nbtia, int[].class);
	public static Constructor<?> nbtlc = NMSUtils.getConstructor(nbtl);
	public static Constructor<?> nbtloc = NMSUtils.getConstructor(nbtlo, long.class);
	public static Constructor<?> nbtsc = NMSUtils.getConstructor(nbts, short.class);
	public static Constructor<?> nbtstc = NMSUtils.getConstructor(nbtst, String.class);

	public static Field nbtbd = NMSUtils.getField(nbtby, "data");
	public static Field nbtbad = NMSUtils.getField(nbtba, "data");
	public static Field nbtdd = NMSUtils.getField(nbtd, "data");
	public static Field nbtfd = NMSUtils.getField(nbtf, "data");
	public static Field nbtid = NMSUtils.getField(nbti, "data");
	public static Field nbtiad = NMSUtils.getField(nbtia, "data");
	public static Field nbtld = NMSUtils.getField(nbtl, "list");
	public static Field nbtlt = NMSUtils.getField(nbtl, "type");
	public static Field nbttcd = NMSUtils.getField(nbttc, "map");
	public static Field nbtlod = NMSUtils.getField(nbtlo, "data");
	public static Field nbtsd = NMSUtils.getField(nbts, "data");
	public static Field nbtstd = NMSUtils.getField(nbtst, "data");

	public static Object getNewNBTTagByte(byte value) throws Exception{
		return nbtbc.newInstance(value);
	}

	public static Object getNewNBTTagByteArray(byte[] value) throws Exception{
		return nbtbac.newInstance(value);
	}

	public static Object getData(Object nbt) throws Exception{
		int i = (int)((byte)gti.invoke(nbt));
		switch(i){
		case NBTConstants.TYPE_BYTE:
			return nbtbd.get(nbt);
		case NBTConstants.TYPE_SHORT:
			return nbtsd.get(nbt);
		case NBTConstants.TYPE_INT:
			return nbtid.get(nbt);
		case NBTConstants.TYPE_LONG:
			return nbtlod.get(nbt);
		case NBTConstants.TYPE_FLOAT:
			return nbtfd.get(nbt);
		case NBTConstants.TYPE_DOUBLE:
			return nbtdd.get(nbt);
		case NBTConstants.TYPE_BYTE_ARRAY:
			return nbtbad.get(nbt);
		case NBTConstants.TYPE_STRING:
			return nbtstd.get(nbt);
		case NBTConstants.TYPE_LIST:
			return convertListTagToValueList(nbt);
		case NBTConstants.TYPE_COMPOUND:
			return convertCompoundTagToValueMap(nbt);
		case NBTConstants.TYPE_INT_ARRAY:
			return nbtiad.get(nbt);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Object createData(Object value) throws Exception{
		if(value.getClass().equals(byte.class)){ return nbtbc.newInstance(value); }
		if(value.getClass().equals(byte[].class)){ return nbtbac.newInstance(value); }
		if(value.getClass().isAssignableFrom(Map.class)){ return convertValueMapToCompoundTag((Map<String, Object>)value); }
		if(value.getClass().equals(double.class)){ return nbtdc.newInstance(value); }
		if(value.getClass().equals(float.class)){ return nbtfc.newInstance(value); }
		if(value.getClass().equals(int.class)){ return nbtic.newInstance(value); }
		if(value.getClass().equals(int[].class)){ return nbtiac.newInstance(value); }
		if(value.getClass().isAssignableFrom(List.class)){ return convertValueListToListTag((List<Object>)value); }
		if(value.getClass().equals(long.class)){ return nbtloc.newInstance(value); }
		if(value.getClass().equals(short.class)){ return nbtsc.newInstance(value); }
		if(value.getClass().equals(String.class)){ return nbtstc.newInstance(value); }
		return null;
	}

	@SuppressWarnings({"unchecked"})
	public static Map<String, Object> convertCompoundTagToValueMap(Object nbt) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> map = (Map<String, Object>)getMap(nbt);
		for(Entry<String, Object> e : map.entrySet()){
			Object nbti = e.getValue();
			Object data = getData(nbti);
			if(data != null){
				ret.put(e.getKey(), data);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static List<Object> convertListTagToValueList(Object nbttl) throws Exception{
		List<Object> ret = new ArrayList<Object>();
		List<Object> list = (List<Object>)nbtld.get(nbttl);
		for(Object e : list){
			Object data = getData(e);
			if(data != null){
				ret.add(data);
			}
		}
		return ret;
	}

	public static Object convertValueMapToCompoundTag(Map<String, Object> map) throws Exception{
		Map<String, Object> value = new HashMap<String, Object>();
		for(Entry<String, Object> e : map.entrySet()){
			value.put(e.getKey(), createData(e.getValue()));
		}
		Object ret = getNewNBTTagCompound();
		nbttcm.set(ret, value);
		return ret;
	}

	public static Object convertValueListToListTag(List<Object> list) throws Exception{
		List<Object> value = new ArrayList<Object>();
		for(Object e : list){
			value.add(createData(e));
		}
		Object ret = getNewNBTTagList();
		nbttcm.set(ret, value);
		if(value.size() > 0){
			nbtlt.set(ret, (byte)gti.invoke(value.get(0)));
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static List<Object> convertListTagToJSON(Object nbttl, JSONArray ja, JSONArray helper) throws Exception{
		List<Object> ret = new ArrayList<Object>();
		List<Object> list = (List<Object>)nbtld.get(nbttl);
		for(Object e : list){
			Object data = getDataJSON(e, ja, helper);
			if(data != null){
				ja.put(data);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static void convertCompoundTagToJSON(Object nbt, JSONObject jo, JSONObject helper) throws Exception{
		Map<String, Object> map = (Map<String, Object>)getMap(nbt);
		for(Entry<String, Object> e : map.entrySet()){
			Object nbti = e.getValue();
			Object data = getDataJSON(e.getKey(), nbti, jo, helper);
			if(data != null){
				jo.put(e.getKey(), data);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static Object convertJSONToCompoundTag(JSONObject jo, JSONObject helper) throws Exception{
		Map<String, Object> value = new HashMap<String, Object>();
		Iterator<String> it = jo.keys();
		while(it.hasNext()){
			String e = it.next();
			value.put(e, createDataJSON(e, jo, helper));
		}
		Object ret = getNewNBTTagCompound();
		nbttcm.set(ret, value);
		return ret;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Object convertJSONToListTag(JSONArray ja, JSONArray helper) throws Exception{
		List value = new ArrayList();
		for(int i = 0; i < ja.length(); i++){
			value.add(createDataJSON(i, ja, helper));
		}
		Object ret = getNewNBTTagList();
		nbtld.set(ret, value);
		if(value.size() > 0){
			nbtlt.set(ret, (byte)gti.invoke(value.get(0)));
		}
		return ret;
	}

	public static Object getDataJSON(String key, Object nbt, JSONObject jo, JSONObject helper) throws Exception{
		int i = (int)((byte)gti.invoke(nbt));
		Object ret = null;
		Object help = i;
		switch(i){
		case NBTConstants.TYPE_BYTE:
			ret = nbtbd.get(nbt);
			break;
		case NBTConstants.TYPE_SHORT:
			ret = nbtsd.get(nbt);
			break;
		case NBTConstants.TYPE_INT:
			ret = nbtid.get(nbt);
			break;
		case NBTConstants.TYPE_LONG:
			ret = nbtlod.get(nbt);
			break;
		case NBTConstants.TYPE_FLOAT:
			ret = nbtfd.get(nbt);
			break;
		case NBTConstants.TYPE_DOUBLE:
			ret = nbtdd.get(nbt);
			break;
		case NBTConstants.TYPE_BYTE_ARRAY:
			ret = nbtbad.get(nbt);
			break;
		case NBTConstants.TYPE_STRING:
			ret = nbtstd.get(nbt);
			break;
		case NBTConstants.TYPE_LIST:{
			JSONArray ja1 = new JSONArray();
			JSONArray helper1 = new JSONArray();
			convertListTagToJSON(nbt, ja1, helper1);
			ret = ja1;
			help = helper1;
			break;
		}
		case NBTConstants.TYPE_COMPOUND:
			JSONObject jo1 = new JSONObject();
			JSONObject helper1 = new JSONObject();
			convertCompoundTagToJSON(nbt, jo1, helper1);
			ret = jo1;
			help = helper1;
			break;
		case NBTConstants.TYPE_INT_ARRAY:
			ret = nbtiad.get(nbt);
			break;
		}
		if(ret != null){
			helper.put(key, help);
		}
		return ret;
	}

	public static Object getDataJSON(Object nbt, JSONArray ja, JSONArray helper) throws Exception{
		int i = (int)((byte)gti.invoke(nbt));
		Object ret = null;
		Object help = i;
		switch(i){
		case NBTConstants.TYPE_BYTE:
			ret = nbtbd.get(nbt);
			break;
		case NBTConstants.TYPE_SHORT:
			ret = nbtsd.get(nbt);
			break;
		case NBTConstants.TYPE_INT:
			ret = nbtid.get(nbt);
			break;
		case NBTConstants.TYPE_LONG:
			ret = nbtlod.get(nbt);
			break;
		case NBTConstants.TYPE_FLOAT:
			ret = nbtfd.get(nbt);
			break;
		case NBTConstants.TYPE_DOUBLE:
			ret = nbtdd.get(nbt);
			break;
		case NBTConstants.TYPE_BYTE_ARRAY:
			ret = nbtbad.get(nbt);
			break;
		case NBTConstants.TYPE_STRING:
			ret = nbtstd.get(nbt);
			break;
		case NBTConstants.TYPE_LIST:{
			JSONArray ja1 = new JSONArray();
			JSONArray helper1 = new JSONArray();
			convertListTagToJSON(nbt, ja1, helper1);
			ret = ja1;
			break;
		}
		case NBTConstants.TYPE_COMPOUND:
			JSONObject jo1 = new JSONObject();
			JSONObject helper1 = new JSONObject();
			convertCompoundTagToJSON(nbt, jo1, helper1);
			ret = jo1;
			help = helper1;
			break;
		case NBTConstants.TYPE_INT_ARRAY:
			ret = nbtiad.get(nbt);
			break;
		}
		if(ret != null){
			helper.put(help);
		}
		return ret;
	}

	public static Object createDataJSON(String key, JSONObject jo, JSONObject helper) throws Exception{
		Object help = helper.get(key);
		Object ret = null;
		if(help instanceof JSONObject){
			JSONObject jo1 = jo.getJSONObject(key);
			JSONObject helper1 = (JSONObject)help;
			ret = convertJSONToCompoundTag(jo1, helper1);
		}else if(help instanceof JSONArray){
			JSONArray ja1 = jo.getJSONArray(key);
			JSONArray helper1 = (JSONArray)help;
			ret = convertJSONToListTag(ja1, helper1);
		}else{
			int i = (int)help;
			switch(i){
			case NBTConstants.TYPE_BYTE:
				ret = nbtbc.newInstance(getByte(jo.get(key)));
				break;
			case NBTConstants.TYPE_SHORT:
				ret = nbtsc.newInstance(getShort(jo.get(key)));
				break;
			case NBTConstants.TYPE_INT:
				ret = nbtic.newInstance(getInt(jo.get(key)));
				break;
			case NBTConstants.TYPE_LONG:
				ret = nbtlc.newInstance(getLong(jo.get(key)));
				break;
			case NBTConstants.TYPE_FLOAT:
				ret = nbtfc.newInstance(getFloat(jo.get(key)));
				break;
			case NBTConstants.TYPE_DOUBLE:
				ret = nbtdc.newInstance(getDouble(jo.get(key)));
				break;
			case NBTConstants.TYPE_BYTE_ARRAY:{
				JSONArray ja = jo.getJSONArray(key);
				byte[] b = new byte[ja.length()];
				for(int a = 0; a < ja.length(); a++){
					b[a] = getByte(ja.get(a));
				}
				return nbtbac.newInstance(b);
			}
			case NBTConstants.TYPE_STRING:
				ret = nbtstc.newInstance((String)jo.get(key));
				break;
			case NBTConstants.TYPE_INT_ARRAY:
				JSONArray ja = jo.getJSONArray(key);
				int[] b = new int[ja.length()];
				for(int a = 0; a < ja.length(); a++){
					b[a] = getInt(ja.get(a));
				}
				return nbtiac.newInstance(b);
			}
		}
		return ret;
	}

	private static byte getByte(Object o){
		if(o.getClass().equals(Integer.class)){ return (byte)(int)o; }
		if(o.getClass().equals(Short.class)){ return (byte)(short)o; }
		return (byte)o;
	}

	private static short getShort(Object o){
		if(o.getClass().equals(Integer.class)){ return (short)(int)o; }
		if(o.getClass().equals(Byte.class)){ return (short)(byte)o; }
		return (short)o;
	}

	private static int getInt(Object o){
		if(o.getClass().equals(Short.class)){ return (int)(short)o; }
		if(o.getClass().equals(Byte.class)){ return (int)(byte)o; }
		return (int)o;
	}

	private static double getDouble(Object o){
		if(o.getClass().equals(Float.class)){ return (double)(float)o; }
		if(o.getClass().equals(Long.class)){ return (double)(long)o; }
		return (double)o;
	}

	private static float getFloat(Object o){
		if(o.getClass().equals(Double.class)){ return (float)(double)o; }
		if(o.getClass().equals(Long.class)){ return (float)(long)o; }
		return (float)o;
	}

	private static long getLong(Object o){
		if(o.getClass().equals(Float.class)){ return (long)(float)o; }
		if(o.getClass().equals(Double.class)){ return (long)(double)o; }
		return (long)o;
	}

	public static Object createDataJSON(int key, JSONArray jo, JSONArray helper) throws Exception{
		Object help = helper.get(key);
		Object ret = null;
		if(help instanceof JSONObject){
			JSONObject jo1 = jo.getJSONObject(key);
			JSONObject helper1 = (JSONObject)help;
			return convertJSONToCompoundTag(jo1, helper1);
		}else if(help instanceof JSONArray){
			JSONArray ja1 = jo.getJSONArray(key);
			JSONArray helper1 = (JSONArray)help;
			return convertJSONToListTag(ja1, helper1);
		}else{
			int i = (int)help;
			switch(i){
			case NBTConstants.TYPE_BYTE:
				ret = nbtbc.newInstance(getByte(jo.get(key)));
				break;
			case NBTConstants.TYPE_SHORT:
				ret = nbtsc.newInstance(getShort(jo.get(key)));
				break;
			case NBTConstants.TYPE_INT:
				ret = nbtic.newInstance(getInt(jo.get(key)));
				break;
			case NBTConstants.TYPE_LONG:
				ret = nbtlc.newInstance(getLong(jo.get(key)));
				break;
			case NBTConstants.TYPE_FLOAT:
				ret = nbtfc.newInstance(getFloat(jo.get(key)));
				break;
			case NBTConstants.TYPE_DOUBLE:
				ret = nbtdc.newInstance(getDouble(jo.get(key)));
				break;
			case NBTConstants.TYPE_BYTE_ARRAY:{
				JSONArray ja = jo.getJSONArray(key);
				byte[] b = new byte[ja.length()];
				for(int a = 0; a < ja.length(); a++){
					b[a] = getByte(ja.get(a));
				}
				return nbtbac.newInstance(b);
			}
			case NBTConstants.TYPE_STRING:
				ret = nbtstc.newInstance((String)jo.get(key));
				break;
			case NBTConstants.TYPE_INT_ARRAY:
				JSONArray ja = jo.getJSONArray(key);
				int[] b = new int[ja.length()];
				for(int a = 0; a < ja.length(); a++){
					b[a] = getInt(ja.get(a));
				}
				return nbtiac.newInstance(b);
			}
		}
		return ret;
	}

	public static boolean compareBaseTag(Object tag, Object tag1) throws Exception{
		int i = (int)((byte)gti.invoke(tag));
		int i1 = (int)((byte)gti.invoke(tag1));
		if(i != i1)
			return false;
		switch(i){
		case NBTConstants.TYPE_BYTE:
			Byte b = (byte)nbtbd.get(tag);
			Byte b1 = (byte)nbtbd.get(tag1);
			return b.equals(b1);
		case NBTConstants.TYPE_SHORT:
			Short s = (short)nbtsd.get(tag);
			Short s1 = (short)nbtsd.get(tag1);
			return s.equals(s1);
		case NBTConstants.TYPE_INT:
			Integer a = (int)nbtid.get(tag);
			Integer a1 = (int)nbtid.get(tag1);
			return a.equals(a1);
		case NBTConstants.TYPE_LONG:
			Long l = (long)nbtlod.get(tag);
			Long l1 = (long)nbtlod.get(tag1);
			return l.equals(l1);
		case NBTConstants.TYPE_FLOAT:
			Float f = (float)nbtfd.get(tag);
			Float f1 = (float)nbtfd.get(tag1);
			return f.equals(f1);
		case NBTConstants.TYPE_DOUBLE:
			Double d = (double)nbtdd.get(tag);
			Double d1 = (double)nbtdd.get(tag1);
			return d.equals(d1);
		case NBTConstants.TYPE_BYTE_ARRAY:
			byte[] ba = (byte[])nbtbad.get(tag);
			byte[] ba1 = (byte[])nbtbad.get(tag1);
			return ba.equals(ba1);
		case NBTConstants.TYPE_STRING:
			String st = (String)nbtstd.get(tag);
			String st1 = (String)nbtstd.get(tag1);
			return st.equals(st1);
		case NBTConstants.TYPE_LIST:{
			return compareListTag(tag, tag1);
		}
		case NBTConstants.TYPE_COMPOUND:
			return compareCompoundTag(tag, tag1);
		case NBTConstants.TYPE_INT_ARRAY:
			int[] ia = (int[])nbtiad.get(tag);
			int[] ia1 = (int[])nbtiad.get(tag);
			return ia.equals(ia1);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean compareCompoundTag(Object tag, Object tag1) throws Exception{
		Map<String, Object> map = (Map<String, Object>)getMap(tag);
		Map<String, Object> map1 = (Map<String, Object>)getMap(tag1);
		if(map.size() != map1.size())
			return false;
		if(!map.keySet().containsAll(map1.keySet()))
			return false;
		for(Entry<String, Object> e : map.entrySet()){
			Object o = e.getValue();
			Object o1 = map1.get(e.getKey());
			if(!compareBaseTag(o, o1))
				return false;
		}
		return true;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static boolean compareListTag(Object tag, Object tag1) throws Exception{
		List list = (List)nbtld.get(tag);
		List list1 = (List)nbtld.get(tag1);
		if(list.size() != list1.size())
			return false;
		Collections.sort(list);
		Collections.sort(list1);
		Iterator it = list.iterator();
		Iterator it1 = list1.iterator();
		while(it.hasNext() && it1.hasNext()){
			Object o = it.next();
			Object o1 = it1.next();
			if(!compareBaseTag(o, o1))
				return false;
		}
		return true;
	}

	public static Class<?> am = NMSUtils.getNMSClass("AttributeModifier"), ga = NMSUtils.getNMSClass("GenericAttributes");
	public static Method a = NMSUtils.getMethod(ga, "a", nbttc), ama = NMSUtils.getMethod(am, "a");

	public static Object getAttribute(Object compound) throws Exception{
		return a.invoke(null, compound);
	}

	public static Map<Integer, Object> getAttributes(Object attribs) throws Exception{
		Map<Integer, Object> r = new HashMap<Integer, Object>();
		for(int i = 0; i < getSize(attribs); i++){
			Object compound = g.invoke(attribs, i);
			Object attrib = a.invoke(null, compound);
			if(attrib != null){
				UUID uuid = (UUID)ama.invoke(attrib);
				if(uuid.getLeastSignificantBits() != 0L && uuid.getMostSignificantBits() != 0L){
					r.put(i, attrib);
				}
			}
		}
		return r;
	}

	@Deprecated
	public static boolean compareAtrributeTagList(Object tl1, Object tl2) throws Exception{
		int s1 = getSize(tl1);
		int s2 = getSize(tl2);
		if(s1 == 0 && s2 == 0){ return true; }
		if(s1 != s2){ return false; }
		for(int i = 0; i < s1; i++){
			Object compound1 = g.invoke(tl1, i);
			Object attrib1 = a.invoke(null, compound1);
			String n1 = getAttributeName(compound1), uuid1 = getAttributeUUID(attrib1).toString();
			int op1 = getAttributeOperation(attrib1);
			double am1 = getAttributeAmount(attrib1);
			for(int ii = 0; ii < s2; ii++){
				Object compound2 = g.invoke(tl2, ii);
				Object attrib2 = a.invoke(null, compound2);
				String n2 = getAttributeName(compound2);
				if(!n1.equals(n2)){ return false; }
				String uuid2 = getAttributeUUID(attrib2).toString();
				if(!uuid1.equals(uuid2)){ return false; }
				int op2 = getAttributeOperation(attrib2);
				if(op1 != op2){ return false; }
				double am2 = getAttributeAmount(attrib2);
				if(am1 != am2){ return false; }
			}
		}
		return true;
	}

	public static String getAttributeName(Object compound) throws Exception{
		return getString(compound, "AttributeName");
	}

	public static String convertMapToString(Map<Integer, Object> attribs) throws Exception{
		String s = "";
		for(Entry<Integer, Object> entry : attribs.entrySet()){
			Object attrib = entry.getValue();
			String name = getAttributeNameFromAttribute(attrib);
			try{
				AttributeType at = AttributeType.valueOf(name);
				if(at != null){
					name = at.getGeneric();
				}
			}catch(Exception e){}
			s = s + name + "," + getAttributeOperation(attrib) + "," + getAttributeAmount(attrib) + "," + getAttributeUUID(attrib).toString() + ";";
		}
		if(s.length() > 0){
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public static Field amd = NMSUtils.getField(am, "d");

	public static UUID getAttributeUUID(Object attrib) throws Exception{
		return (UUID)amd.get(attrib);
	}

	public static Field amb = NMSUtils.getField(am, "b");

	public static int getAttributeOperation(Object attrib) throws Exception{
		return (int)amb.getInt(attrib);
	}

	public static Field amaf = NMSUtils.getField(am, "a");

	public static double getAttributeAmount(Object attrib) throws Exception{
		return (double)amaf.get(attrib);
	}

	public static Field amc = NMSUtils.getField(am, "c");

	public static String getAttributeNameFromAttribute(Object attrib) throws Exception{
		return (String)amc.get(attrib);
	}

	public enum AttributeType{
		MAX_HEALTH("generic.maxHealth"), FOLLOW_RANGE("generic.followRange"), MOVEMENT_SPEED("generic.movementSpeed"), KNOCKBACK_RESISTANCE("generic.knockbackResistance"), ATTACK_DAMAGE("generic.attackDamage"), DAMAGE("generic.attackDamage"), JUMP_STRENGTH("horse.jumpStrength"), SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

		public String generic;

		AttributeType(String generic){
			this.generic = generic;
		}

		public String getGeneric(){
			return this.generic;
		}
	}

	public static boolean compare(ItemStack is1, ItemStack is2){
		if(is1.getType().equals(is2.getType())){
			if(is1.getDurability() == is2.getDurability()){
				try{
					Object nis1 = getNMSCopy(is1);
					Object nis2 = getNMSCopy(is2);
					Object tis1 = getTag(nis1);
					Object tis2 = getTag(nis2);
					if(tis1 != null && tis2 == null){
						if(isEmpty(tis1))
							return true;
						return false;
					}
					if(tis1 == null && tis2 != null){
						if(isEmpty(tis2))
							return true;
						return false;
					}
					if(tis1 == null && tis2 == null){ return true; }
					return compareCompoundTag(tis1, tis2);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean canMerge(ItemStack add, ItemStack to){
		return compare(add, to);
	}

	public static boolean isModified(ItemStack is){
		ItemStack is1 = is.clone();
		is1.setAmount(1);
		ItemStack is2 = new ItemStack(is.getType(), 1, is.getDurability());
		return !is1.equals(is2);
	}

	public static void sortByMaterial(List<ItemStack> items){
		Collections.sort(items, new MaterialComparator());
	}

	private static class MaterialComparator implements Comparator<ItemStack>{
		@Override
		public int compare(ItemStack arg0, ItemStack arg1){
			return arg0.getType().name().compareTo(arg1.getType().name());
		}
	}

	public static void sortByName(List<ItemStack> items){
		Collections.sort(items, new NameComparator());
	}

	private static class NameComparator implements Comparator<ItemStack>{
		@Override
		public int compare(ItemStack arg0, ItemStack arg1){
			int i = 0;
			try{
				i = ChatColor.stripColor(getName(arg0)).compareTo(ChatColor.stripColor(getName(arg1)));
			}catch(Exception e){
				e.printStackTrace();
			}
			return i;
		}
	}

	public static void sortByAmount(List<ItemStack> items){
		Collections.sort(items, new AmountComparator());
	}

	private static class AmountComparator implements Comparator<ItemStack>{
		@Override
		public int compare(ItemStack arg0, ItemStack arg1){
			int i = arg1.getAmount() - arg0.getAmount();
			if(i == 0){
				try{
					i = ChatColor.stripColor(getName(arg0)).compareTo(ChatColor.stripColor(getName(arg1)));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return i;
		}
	}

	public static boolean isArmor(Material m){
		switch(m){
		case LEATHER_BOOTS:
		case LEATHER_CHESTPLATE:
		case LEATHER_HELMET:
		case LEATHER_LEGGINGS:
		case CHAINMAIL_BOOTS:
		case CHAINMAIL_CHESTPLATE:
		case CHAINMAIL_HELMET:
		case CHAINMAIL_LEGGINGS:
		case IRON_BOOTS:
		case IRON_CHESTPLATE:
		case IRON_HELMET:
		case IRON_LEGGINGS:
		case GOLD_BOOTS:
		case GOLD_CHESTPLATE:
		case GOLD_HELMET:
		case GOLD_LEGGINGS:
		case DIAMOND_BOOTS:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_HELMET:
		case DIAMOND_LEGGINGS:
			return true;
		default:
			return false;
		}
	}

	public static boolean isTool(Material m){
		switch(m){
		case FISHING_ROD:
		case FLINT_AND_STEEL:
		case SHEARS:
		case WOOD_AXE:
		case WOOD_HOE:
		case WOOD_PICKAXE:
		case WOOD_SPADE:
		case WOOD_SWORD:
		case IRON_AXE:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SPADE:
		case GOLD_AXE:
		case GOLD_HOE:
		case GOLD_PICKAXE:
		case GOLD_SPADE:
		case GOLD_SWORD:
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SPADE:
		case DIAMOND_SWORD:
			return true;
		default:
			return false;
		}
	}

	public static boolean isDamaged(ItemStack is){
		return (isTool(is.getType()) || isArmor(is.getType())) && is.getDurability() != is.getType().getMaxDurability();
	}
}
