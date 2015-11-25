package me.dablakbandit.dabcore.command.advanced;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class AdvancedCommandPart {

	private String arg;
	
	public AdvancedCommandPart(String arg){
		this.arg = arg;
	}
	
	public String getArg(){
		return arg;
	}
	
	public Map<String, AdvancedCommandPart> map = new HashMap<String, AdvancedCommandPart>();
	
	public abstract boolean needsPermission();
	public abstract String getPermission();
	public abstract void loadParts();
	public abstract boolean onCommandPart(CommandSender s, Command cmd, String Label, String[] args);
	
}
