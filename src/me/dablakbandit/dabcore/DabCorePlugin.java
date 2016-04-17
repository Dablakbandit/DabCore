package me.dablakbandit.dabcore;

import org.bukkit.plugin.java.JavaPlugin;

public class DabCorePlugin extends JavaPlugin{

	private static DabCorePlugin main;

	public static DabCorePlugin getInstance(){
		return main;
	}

	public void onLoad(){
		main = this;
	}

}
