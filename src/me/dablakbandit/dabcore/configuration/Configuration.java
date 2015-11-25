package me.dablakbandit.dabcore.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.dablakbandit.dabcore.utils.ItemUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {
	private JavaPlugin plugin;
	private FileConfiguration conf = null;
	private File file = null;
	private String fname = null;

	public Configuration(String filename){
		fname = filename;
	}
	
	public Configuration(JavaPlugin plugin, String filename){
		this.plugin = plugin;
		fname = filename;
	}

	public void ReloadConfig(){
		if(file==null){
			file = plugin != null ? new File(plugin.getDataFolder(), fname) : new File(fname);
		}
		conf = YamlConfiguration.loadConfiguration(file);

		InputStream isDefaults = plugin != null ? plugin.getResource(fname) : null;
		if(isDefaults!=null){
			@SuppressWarnings("deprecation")
			YamlConfiguration confDefault = YamlConfiguration.loadConfiguration(isDefaults);
			conf.setDefaults(confDefault);
		}
	}

	public FileConfiguration GetConfig(){
		if(conf==null){
			ReloadConfig();
		}
		return conf;
	}

	public boolean SaveConfig(){
		if(conf==null||file==null){
			return 	false;
		}

		try{
			conf.save(file);
			return true;
		}catch(IOException ex){
			System.out.print("[" + (plugin !=null ? plugin.getName() : "Configuration") + "] Error saving configuration file: '" + fname + "'!");
			return false;
		}
	}

	public File getFile(){
		if(file==null){
			file = plugin != null ? new File(plugin.getDataFolder(), fname) : new File(fname);
		}
		return file;
	}
	
	protected List<ItemStack> getItems(Configuration config, String path1){
		List<ItemStack> items = new ArrayList<ItemStack>();
		if(config.GetConfig().isSet(path1)){
			for(String s : config.GetConfig().getConfigurationSection(path1).getKeys(false)){
				String path = path1 + "." + s;
				try{
					Material m = Material.valueOf(config.GetConfig().getString(path + ".Material"));
					if(m!=null){
						ItemStack is = new ItemStack(m);
						is.setAmount(config.GetConfig().getInt(path + ".Amount"));
						is.setDurability((short)config.GetConfig().getInt(path + ".Durability"));
						ItemMeta im = is.getItemMeta();
						if(config.GetConfig().isSet(path + ".CustomName")){
							im.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.GetConfig().getString(path + ".CustomName")));
						}
						if(config.GetConfig().isSet(path + ".Lore")){
							im.setLore(b(config.GetConfig().getStringList(path + ".Lore")));
						}
						if(config.GetConfig().isSet(path + ".Enchants")){
							for(String s1 : config.GetConfig().getConfigurationSection(path + ".Enchants").getKeys(false)){
								String p1 = path + ".Enchants." + s1;
								Enchantment e = Enchantment.getByName(config.GetConfig().getString(p1 + ".Enchantment"));
								int val = config.GetConfig().getInt(p1 + ".Level");
								im.addEnchant(e, val, true);
							}
						}
						if(is.getDurability()==3&&is.getType().equals(Material.SKULL_ITEM)){
							if(config.GetConfig().isSet(path + ".Meta")){
								((SkullMeta)im).setOwner(config.GetConfig().getString(path + ".Meta"));
							}
						}else if(ItemUtils.hasBanner()&&is.getType().equals(org.bukkit.Material.BANNER)){
							if(config.GetConfig().isSet(path + ".Banner")){
								if(config.GetConfig().isSet(path + ".Banner.Base")){
									((org.bukkit.inventory.meta.BannerMeta)im).setBaseColor(DyeColor.valueOf(config.GetConfig().getString(path + ".Banner.Base")));
								}
								if(config.GetConfig().isSet(path + ".Banner.Patterns")){
									for(String s1 : config.GetConfig().getConfigurationSection(path + ".Banner.Patterns").getKeys(false)){
										String p1 = path + ".Banner.Patterns" + s1;
										DyeColor dc = DyeColor.valueOf(config.GetConfig().getString(p1 + ".DyeColor"));
										org.bukkit.block.banner.PatternType pt = org.bukkit.block.banner.PatternType.valueOf(config.GetConfig().getString(p1 + ".Type"));
										((org.bukkit.inventory.meta.BannerMeta)im).addPattern(new org.bukkit.block.banner.Pattern(dc, pt));
									}
								}
							}
						}else if(m.equals(Material.LEATHER_BOOTS)||m.equals(Material.LEATHER_CHESTPLATE)||m.equals(Material.LEATHER_HELMET)||m.equals(Material.LEATHER_LEGGINGS)){
							if(config.GetConfig().isSet(path + ".Meta")){
								LeatherArmorMeta lam = (LeatherArmorMeta)im;
								lam.setColor(Color.fromRGB(config.GetConfig().getInt(path + ".Meta")));
							}
						}else if(m.equals(Material.ENCHANTED_BOOK)){
							if(config.GetConfig().isSet(path1 + ".Meta")){
								EnchantmentStorageMeta esm = (EnchantmentStorageMeta)im;
								for(String p1 : config.GetConfig().getConfigurationSection(path1 + ".Meta").getKeys(false)){
									esm.addEnchant(Enchantment.getByName(config.GetConfig().getString(path + ".Meta" + p1 + ".Name")), config.GetConfig().getInt(path1 + ".Meta" + path + ".Level"), true);
								}
							}
						}
						is.setItemMeta(im);
						try{
							Object nis = ItemUtils.getNMSCopy(is);
							Object tag = ItemUtils.getTag(nis);
							if(tag==null){
								tag = ItemUtils.getNewNBTTagCompound();
							}
							if(config.GetConfig().isSet(path + ".Unbreakable")){
								ItemUtils.setUnbreakable(tag, config.GetConfig().getBoolean(path + ".Unbreakable"));
							}
							if(config.GetConfig().isSet(path + ".Tags")){
								Object taglist;
								if(ItemUtils.hasAttributeModifiersKey(tag)){
									taglist = ItemUtils.getList(tag);
								}else{
									taglist = ItemUtils.getNewNBTTagList();
								}
								for(String p1 : config.GetConfig().getConfigurationSection(path + ".Tags").getKeys(false)){
									String p2 = path + ".Tags." + p1;
									Object compound = ItemUtils.getNewNBTTagCompound();
									ItemUtils.setString(compound, "Name", config.GetConfig().getString(p2 + ".Name"));
									ItemUtils.setString(compound, "AttributeName", config.GetConfig().getString(p2 + ".AttributeName"));
									ItemUtils.setDouble(compound, "Amount", config.GetConfig().getDouble(p2 + ".Amount"));
									ItemUtils.setInt(compound, "Operation", config.GetConfig().getInt(p2 + ".Operation"));
									UUID uuid = UUID.fromString(config.GetConfig().getString(p2 + ".UUID"));
									ItemUtils.setLong(compound, "UUIDMost", uuid.getMostSignificantBits());
									ItemUtils.setLong(compound, "UUIDLeast", uuid.getLeastSignificantBits());
									ItemUtils.addToList(taglist, compound);
								}
								ItemUtils.set(tag, "AttributeModifiers", taglist);
							}
							ItemUtils.setTag(nis, tag);
							is = ItemUtils.asCraftMirror(nis);
						}catch(Exception e){
							e.printStackTrace();
						}
						items.add(is);
					}
				}catch(Exception e){
					System.out.print("[GrandExchange] Problems in itemlist.yml at " + path1 + s);
				}
			}
		}
		return items;
	}
	
	@SuppressWarnings("unused")
	private List<String> a(List<String> a){
		List<String> ret = new ArrayList<String>();
		for(String s : a){
			ret.add(s.replaceAll("§", "&"));
		}
		return ret;
	}

	private List<String> b(List<String> b){
		List<String> ret = new ArrayList<String>();
		for(String s : b){
			ret.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		return ret;
	}

	public static final class Section{

		public final Object get(Configuration config, String s){
			return config.GetConfig().get(s);
		}

		public final void set(Configuration config, String s, Object o, boolean save){
			config.GetConfig().set(s, o);
			if(save)config.SaveConfig();
		}
	}

	public static final class DefinedSection{

		private String sec;

		public DefinedSection(String s){
			sec = s;
		}

		public final Object get(Configuration config){
			return config.GetConfig().get(sec);
		}
		
		public final Object get(Configuration config, String add){
			return config.GetConfig().get(sec+add);
		}
		
		public final String getSection(){
			return sec;
		}
		
		public final boolean isSet(Configuration config){
			return config.GetConfig().isSet(sec);
		}
		
		public final boolean isSet(Configuration config, String add){
			return config.GetConfig().isSet(sec + add);
		}
		
		public final void set(Configuration config, Object o){
			config.GetConfig().set(sec, o);
		}
		
		public final void set(Configuration config, String add, Object o){
			config.GetConfig().set(sec+add, o);
		}

		public final void set(Configuration config, Object o, boolean save){
			config.GetConfig().set(sec, o);
			if(save)config.SaveConfig();
		}
		
		public final void set(Configuration config, String add, Object o, boolean save){
			config.GetConfig().set(sec+add, o);
			if(save)config.SaveConfig();
		}
	}
}
