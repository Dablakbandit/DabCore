package me.dablakbandit.dabcore.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
