package me.dablakbandit.dabcore.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Eco {

	public static Eco manager = new Eco();
	private Economy economy;
	
	private Eco(){
		try{
			RegisteredServiceProvider<Economy> rsp1 = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			economy = rsp1.getProvider();
		}catch(NullPointerException a){

		}
	}
	
	public static Eco getInstance(){
		return manager;
	}
	
	public Economy getEconomy(){
		return economy;
	}
}
