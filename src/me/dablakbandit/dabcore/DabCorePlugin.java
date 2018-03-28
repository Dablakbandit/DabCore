package me.dablakbandit.dabcore;

import org.bukkit.plugin.java.JavaPlugin;

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
	}
	
	@Override
	public void onEnable(){
		PluginUpdater.getInstance().start();
	}
	
}
