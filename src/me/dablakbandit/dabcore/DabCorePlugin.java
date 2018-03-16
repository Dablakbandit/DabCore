package me.dablakbandit.dabcore;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.plugin.java.JavaPlugin;

import me.dablakbandit.dabcore.json.JSONObject;
import me.dablakbandit.dabcore.json.JSONTokener;
import me.dablakbandit.dabcore.metrics.Metrics;
import me.dablakbandit.dabcore.updater.PluginUpdater;
import me.dablakbandit.dabcore.utils.ItemUtils;

public class DabCorePlugin extends JavaPlugin{
	
	private static DabCorePlugin main;
	
	public static DabCorePlugin getInstance(){
		return main;
	}
	
	@Override
	public void onLoad(){
		DabCorePluginConfiguration.setup(this);
		PluginUpdater.getInstance().checkUpdate(this, "9994");
		main = this;
		new Metrics(this);
		ItemUtils.getInstance();
		try{
			File in = new File("./test.pin");
			FileInputStream fis = new FileInputStream(in);
			new JSONObject(new JSONTokener(fis));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEnable(){
		PluginUpdater.getInstance().start();
	}
	
}
