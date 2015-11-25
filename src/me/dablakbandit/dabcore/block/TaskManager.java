package me.dablakbandit.dabcore.block;

import me.dablakbandit.dabcore.DabCorePlugin;

import org.bukkit.plugin.Plugin;

public class TaskManager {

	private static TaskManager instance = new TaskManager(DabCorePlugin.getInstance());

	public static TaskManager getInstance(){
		return instance;
	}

	private Plugin plugin;

	private TaskManager(Plugin plugin){
		this.plugin = plugin;
	}

	public int repeat( Runnable r, int interval){
		return plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, r, interval, interval);
	}

	public void task( Runnable r){
		if(r == null)return;
		plugin.getServer().getScheduler().runTask(plugin, r).getTaskId();
	}
}
