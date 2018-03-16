package me.dablakbandit.dabcore;

import java.lang.reflect.Field;

import org.bukkit.plugin.Plugin;

import me.dablakbandit.dabcore.configuration.PluginConfiguration;

public class DabCorePluginConfiguration extends PluginConfiguration{
	
	private static DabCorePluginConfiguration	configuration;
	
	// @formatter:off
	public static IntegerPath					UPDATE_CHECK			= new IntegerPath("Update_Check", 3600);
	
	private DabCorePluginConfiguration(Plugin plugin){
		super(plugin);
	}
	
	public static void setup(Plugin plugin){
		configuration = new DabCorePluginConfiguration(plugin);
		load();
	}
	
	public static void load(){
		configuration.plugin.reloadConfig();
		try{
			boolean save = false;
			for(Field f : DabCorePluginConfiguration.class.getDeclaredFields()){
				if(Path.class.isAssignableFrom(f.getType())){
					Path p = (Path)f.get(null);
					if(!save)
						save = p.retrieve(configuration.plugin.getConfig());
					else
						p.retrieve(configuration.plugin.getConfig());
				}
			}
			if(save){
				configuration.plugin.saveConfig();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void reload(){
		load();
	}
	
}
