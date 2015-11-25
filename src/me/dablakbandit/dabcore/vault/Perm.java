package me.dablakbandit.dabcore.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class Perm {

	public static Perm perm = new Perm();
	private Permission permission;
	
	private Perm(){
		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            this.permission = ((Permission)permissionProvider.getProvider());
        }
	}
	
	public static Perm getPerm(){
		return perm;
	}
	
	public Permission getPermission(){
		return this.permission;
	}
}
