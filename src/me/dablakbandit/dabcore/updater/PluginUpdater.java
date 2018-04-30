package me.dablakbandit.dabcore.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.dablakbandit.dabcore.DabCorePlugin;
import me.dablakbandit.dabcore.DabCorePluginConfiguration;

public class PluginUpdater implements Listener{
	
	private static PluginUpdater updater = new PluginUpdater();
	
	public static PluginUpdater getInstance(){
		return updater;
	}
	
	private PluginUpdater(){
		
	}
	
	public Map<String, String>	versions	= new HashMap<String, String>();
	public Map<String, String>	messages	= new HashMap<String, String>();
	public Map<String, String>	resources	= new HashMap<String, String>();
	
	public void start(){
		Bukkit.getPluginManager().registerEvents(this, DabCorePlugin.getInstance());
		if(messages.size() == 0){ return; }
		for(String s : messages.values()){
			Bukkit.getConsoleSender().sendMessage(s);
		}
		delay(3);
		int times = DabCorePluginConfiguration.UPDATE_CHECK.get();
		if(times > 86400){
			times = 86400;
			System.out.print("[DabCore] Update time may not be longer than a day.");
		}else if(times < 900){
			times = 900;
			System.out.print("[DabCore] Update time may not be less then 15 minutes.");
		}
		int time = 20 * times;
		Bukkit.getScheduler().runTaskTimerAsynchronously(DabCorePlugin.getInstance(), new Runnable(){
			@Override
			public void run(){
				System.out.print("[DabCore] Checking for " + versions.size() + " updates.");
				for(String s : new ArrayList<String>(versions.keySet())){
					Plugin plugin = Bukkit.getPluginManager().getPlugin(s);
					if(plugin != null){
						checkUpdate(plugin, resources.get(plugin.getName()));
					}
				}
			}
		}, time, time);
	}
	
	private void delay(int times){
		if(times <= 0){ return; }
		long next = System.currentTimeMillis() + 1000;
		System.out.print("[DabCore] Continuing in " + times);
		while(System.currentTimeMillis() < next)
			;
		delay(times - 1);
	}
	
	public void checkUpdate(Plugin plugin, String id){
		String name = plugin.getName();
		String current_version = versions.get(name);
		if(current_version == null){
			current_version = plugin.getDescription().getVersion();
			versions.put(name, current_version);
		}
		System.out.print("[DabCore] Checking update for " + name + " " + current_version);
		resources.put(name, id);
		try{
			URL checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + id);
			URLConnection con = checkURL.openConnection();
			con.setConnectTimeout(2000);
			String new_version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			if(new_version.contains("[")){
				new_version = new_version.substring(0, new_version.indexOf('['));
			}
			if(!new_version.equals(current_version)){
				versions.put(name, new_version);
				messages.put(name, "[DabCore] " + ChatColor.GREEN + "Plugin " + name + " has an update! Old: " + current_version + ", New: " + new_version);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		if(p.isOp()){
			for(String s : messages.values()){
				p.sendMessage(s);
			}
		}
	}
	
}
