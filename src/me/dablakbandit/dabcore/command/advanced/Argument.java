package me.dablakbandit.dabcore.command.advanced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Argument{
	
	protected AdvancedCommand	command;
	protected String			arg;
	
	public Argument(AdvancedCommand cmd, String arg){
		this.command = cmd;
		this.arg = arg;
	}
	
	public String getArgument(){
		return arg;
	}
	
	public Map<String, Argument> map = new HashMap<String, Argument>();
	
	public boolean hasPermission(Player player){
		return true;
	};
	
	public void sendNoPermissionMessage(CommandSender s, Command cmd, String[] args){
		command.sendNoPermissionMessage(s, cmd, args);
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		return onCommandPart(s, cmd, label, args);
	}
	
	public abstract boolean onCommandPart(CommandSender s, Command cmd, String label, String[] args);
	
	public abstract List<String> getInfo();
	
}
