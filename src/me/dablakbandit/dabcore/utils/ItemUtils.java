package me.dablakbandit.dabcore.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {

	private static boolean banner = getBanner();

	private static boolean getBanner(){
		try{
			Material m = Material.valueOf("BANNER");
			if(m!=null){
				return true;
			}
		}catch(Exception e){
		}
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
		return (boolean) hastag.invoke(is);
	}

	public static Method acm = NMSUtils.getMethod(cis, "asCraftMirror", nmis);

	public static ItemStack asCraftMirror(Object nis) throws Exception{
		return (ItemStack)acm.invoke(null, nis);
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
		if(m==null){
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
	
	@SuppressWarnings("unused")
	private static Class<?> nmrs = NMSUtils.getNMSClass("RegistrySimple"), nmrm = NMSUtils.getNMSClass("RegistryMaterials");
	private static Field nmrsc = NMSUtils.getField(nmrs, "c");
	
	public static String getMinecraftName(ItemStack is){
		String name = getItemName(is);
		try{
			Map<?, ?> m = (Map<?, ?>)nmrsc.get(registry);
			for(Entry<?, ?> e : m.entrySet()){
				Object item = e.getValue();
				String s = (String)gin.invoke(item);
				if(name.equals(s)){
					return e.getKey().toString();
				}
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
		return (boolean) nbtcie.invoke(tag);
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
		return (boolean) hkot.invoke(tag, "AttributeModifiers", 9);
	}

	public static Class<?> nbttl = NMSUtils.getNMSClass("NBTTagList");
	public static Method gl = NMSUtils.getMethod(nbttc, "getList", String.class, int.class);
	public static Method gb = NMSUtils.getMethod(nbttc, "getBoolean", String.class);
	public static Method sb = NMSUtils.getMethod(nbttc, "setBoolean", String.class, boolean.class);
	public static Method nbttla = NMSUtils.getMethod(nbttl, "add", nbtb);
	public static Constructor<?> nbttlc = NMSUtils.getConstructor(nbttl);

	public static Object getList(Object tag) throws Exception{
		return gl.invoke(tag, "AttributeModifiers", 10);
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

	public static int getSize(Object attribs) throws Exception{
		return (int)gs.invoke(attribs);
	}

	public static Method g = NMSUtils.getMethod(nbttl, "get", int.class);

	public static Object get(Object tlist, int i) throws Exception{
		return g.invoke(tlist, i);
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
			if(attrib!=null){
				UUID uuid = (UUID) ama.invoke(attrib);
				if(uuid.getLeastSignificantBits()!=0L&&uuid.getMostSignificantBits()!=0L){
					r.put(i, attrib);
				}
			}
		}
		return r;
	}

	public static boolean compareAtrributeTagList(Object tl1, Object tl2) throws Exception{
		int s1 = getSize(tl1);
		int s2 = getSize(tl2);
		if(s1==0&&s2==0){
			return true;
		}
		if(s1!=s2){
			return false;
		}
		for(int i = 0; i < s1; i++){
			Object compound1 = g.invoke(tl1, i);
			Object attrib1 = a.invoke(null, compound1);
			String n1 = getAttributeName(compound1), uuid1 = getAttributeUUID(attrib1).toString();
			int op1 = getAttributeOperation(attrib1);
			double am1 = getAttributeAmount(attrib1);
			for(int ii = 0; ii< s2; ii++){
				Object compound2 = g.invoke(tl2, ii);
				Object attrib2 = a.invoke(null, compound2);
				String n2 = getAttributeName(compound2);
				if(!n1.equals(n2)){
					return false;
				}
				String uuid2 = getAttributeUUID(attrib2).toString();
				if(!uuid1.equals(uuid2)){
					return false;
				}
				int op2 = getAttributeOperation(attrib2);
				if(op1!=op2){
					return false;
				}
				double am2 = getAttributeAmount(attrib2);
				if(am1!=am2){
					return false;
				}
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
				if(at!=null){
					name = at.getGeneric();
				}
			}catch(Exception e){}
			s = s + name + "," + getAttributeOperation(attrib) + "," + getAttributeAmount(attrib) + "," + getAttributeUUID(attrib).toString() + ";";
		}
		if(s.length()>0){
			s = s.substring(0, s.length()-1);
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
		MAX_HEALTH("generic.maxHealth"),
		FOLLOW_RANGE("generic.followRange"),
		MOVEMENT_SPEED("generic.movementSpeed"),
		KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
		ATTACK_DAMAGE("generic.attackDamage"),
		DAMAGE("generic.attackDamage"),
		JUMP_STRENGTH("horse.jumpStrength"),
		SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

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
			if(is1.getDurability()==is2.getDurability()){
				if(is1.hasItemMeta()&&is2.hasItemMeta()){
					ItemMeta im1 = is1.getItemMeta();
					ItemMeta im2 = is2.getItemMeta();
					if(im1.hasDisplayName()&&!im2.hasDisplayName()
							||!im1.hasDisplayName()&&im2.hasDisplayName()
							||im1.hasDisplayName()&&im2.hasDisplayName()&&!im1.getDisplayName().equals(im2.getDisplayName())){
						return false;
					}
					if(im1.hasLore()&&!im2.hasLore()
							||!im1.hasLore()&&im2.hasLore()
							||im1.hasLore()&&im2.hasLore()&&!im1.getLore().equals(im2.getLore())){
						return false;
					}
					if(im1.hasEnchants()&&!im2.hasEnchants()
							||!im1.hasEnchants()&&im2.hasEnchants()
							||im1.hasEnchants()&&im2.hasEnchants()&&!im1.getEnchants().equals(im2.getEnchants())){
						return false;
					}
					if(is1.getType().equals(Material.SKULL_ITEM)){
						SkullMeta sm1 = (SkullMeta)im1;
						SkullMeta sm2 = (SkullMeta)im2;
						if(sm1.hasOwner()&&!sm2.hasOwner()
								||!sm1.hasOwner()&&sm2.hasOwner()
								||sm1.hasOwner()&&sm2.hasOwner()&&!sm1.getOwner().equals(sm2.getOwner())){
							return false;
						}
					}
					if(banner&&is1.getType().equals(Material.BANNER)){
						org.bukkit.inventory.meta.BannerMeta bm1 = (org.bukkit.inventory.meta.BannerMeta)im1;
						org.bukkit.inventory.meta.BannerMeta bm2 = (org.bukkit.inventory.meta.BannerMeta)im2;
						if(bm1==null&&bm2!=null||bm1!=null&&bm2==null||!bm1.getBaseColor().equals(bm2.getBaseColor())){
							return false;
						}
						if(!(bm1.numberOfPatterns()==bm2.numberOfPatterns())){
							return false;
						}
						for(int i = 0; i < bm1.numberOfPatterns(); i++){
							if(!bm1.getPattern(i).equals(bm2.getPattern(i))){
								return false;
							}
						}
					}
				}else if(is1.hasItemMeta()&&!is2.hasItemMeta()
						||!is1.hasItemMeta()&&is2.hasItemMeta()){
					return false;
				}
				try{
					Object nis1 = getNMSCopy(is1);
					Object nis2 = getNMSCopy(is2);
					Object tis1 = getTag(nis1);
					Object tis2 = getTag(nis2);
					if(tis1!=null&&tis2==null){
						if(isEmpty(tis1))return true;
						return false;
					}
					if(tis1==null&&tis2!=null){
						if(isEmpty(tis2))return true;
						return false;
					}
					if(tis1==null&&tis2==null){
						return true;
					}
					boolean his1 = hasAttributeModifiersKey(tis1);
					boolean his2 = hasAttributeModifiersKey(tis2);
					if(his1&&!his2||!his1&&his2){
						return false;
					}
					Object tlis1 = getList(tis1);
					Object tlis2 = getList(tis2);
					if(!compareAtrributeTagList(tlis1, tlis2)){
						return false;
					}
					return true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean canMerge(ItemStack add, ItemStack to){
		if(add.getType().equals(to.getType())){
			if(add.getDurability()==to.getDurability()){
				if(add.hasItemMeta()&&to.hasItemMeta()){
					ItemMeta im1 = add.getItemMeta();
					ItemMeta im2 = to.getItemMeta();
					if(im1.hasDisplayName()&&!im2.hasDisplayName()
							||!im1.hasDisplayName()&&im2.hasDisplayName()
							||im1.hasDisplayName()&&im2.hasDisplayName()&&!im1.getDisplayName().equals(im2.getDisplayName())){
						return false;
					}
					if(im1.hasLore()&&!im2.hasLore()
							||!im1.hasLore()&&im2.hasLore()
							||im1.hasLore()&&im2.hasLore()&&!im1.getLore().equals(im2.getLore())){
						return false;
					}
					if(im1.hasEnchants()&&!im2.hasEnchants()
							||!im1.hasEnchants()&&im2.hasEnchants()
							||im1.hasEnchants()&&im2.hasEnchants()&&!im1.getEnchants().equals(im2.getEnchants())){
						return false;
					}
					if(to.getMaxStackSize()<=to.getAmount()){
						return false;
					}
					if(add.getType().equals(Material.SKULL_ITEM)){
						SkullMeta sm1 = (SkullMeta)im1;
						SkullMeta sm2 = (SkullMeta)im2;
						if(sm1.hasOwner()&&!sm2.hasOwner()
								||!sm1.hasOwner()&&sm2.hasOwner()
								||sm1.hasOwner()&&sm2.hasOwner()&&!sm1.getOwner().equals(sm2.getOwner())){
							return false;
						}
					}
					if(banner&&add.getType().equals(Material.BANNER)){
						org.bukkit.inventory.meta.BannerMeta bm1 = (org.bukkit.inventory.meta.BannerMeta)im1;
						org.bukkit.inventory.meta.BannerMeta bm2 = (org.bukkit.inventory.meta.BannerMeta)im2;
						if(bm1==null&&bm2!=null||bm1!=null&&bm2==null||!bm1.getBaseColor().equals(bm2.getBaseColor())){
							return false;
						}
						if(!(bm1.numberOfPatterns()==bm2.numberOfPatterns())){
							return false;
						}
						for(int i = 0; i < bm1.numberOfPatterns(); i++){
							if(!bm1.getPattern(i).equals(bm2.getPattern(i))){
								return false;
							}
						}
					}
				}else if(add.hasItemMeta()&&!to.hasItemMeta()
						||!add.hasItemMeta()&&to.hasItemMeta()){
					return false;
				}
				try{
					Object nadd = getNMSCopy(add);
					Object nto = getNMSCopy(to);
					boolean badd = hasTag(nadd);
					boolean bto = hasTag(nto);
					if(badd&&!bto||!badd&&bto){
						return false;
					}
					Object tadd = getTag(nadd);
					Object tto = getTag(nto);
					if(tadd!=null&&tto==null){
						if(isEmpty(tadd))return true;
						return false;
					}
					if(tadd==null&&tto!=null){
						if(isEmpty(tto))return true;
						return false;
					}
					if(tadd==null&&tto==null){
						return true;
					}
					boolean hadd = hasAttributeModifiersKey(tadd);
					boolean hto = hasAttributeModifiersKey(tto);
					if(hadd&&!hto||!hadd&&hto){
						return false;
					}
					Object tladd = getList(tadd);
					Object tlto = getList(tto);
					if(!compareAtrributeTagList(tladd, tlto)){
						return false;
					}
					return true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
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
		public int compare(ItemStack arg0, ItemStack arg1) {
			return arg0.getType().name().compareTo(arg1.getType().name());
		}
	}

	public static void sortByName(List<ItemStack> items){
		Collections.sort(items, new NameComparator());
	}

	private static class NameComparator implements Comparator<ItemStack>{
		@Override
		public int compare(ItemStack arg0, ItemStack arg1) {
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
		public int compare(ItemStack arg0, ItemStack arg1) {
			int i = arg1.getAmount()-arg0.getAmount();
			if(i==0){
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
		case DIAMOND_LEGGINGS:return true;
		default:return false;
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
		case DIAMOND_SWORD:return true;
		default:return false;
		}
	}

	public static boolean isDamaged(ItemStack is){
		return (isTool(is.getType())||isArmor(is.getType()))&&is.getDurability()!=is.getType().getMaxDurability();
	}
}
