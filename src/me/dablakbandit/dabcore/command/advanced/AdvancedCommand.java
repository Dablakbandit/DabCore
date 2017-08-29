package me.dablakbandit.dabcore.command.advanced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dablakbandit.dabcore.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AdvancedCommand extends AbstractCommand{

	public AdvancedCommand(String command) {
		this(command, null, null, null, null);
	}

	public AdvancedCommand(String command, String usage) {
		this(command, usage, null, null, null);
	}

	public AdvancedCommand(String command, String usage, String description) {
		this(command, usage, description, null, null);
	}

	public AdvancedCommand(String command, String usage, String description, String permissionMessage) {
		this(command, usage, description, permissionMessage, null);
	}

	public AdvancedCommand(String command, String usage, String description, List<String> aliases) {
		this(command, usage, description, null, aliases);
	}

	public AdvancedCommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
		super(command, usage, description, permissionMessage, aliases);
		createArguments();
		register();
	}
	
	public Map<String, Argument> map = new HashMap<String, Argument>();

	public boolean hasPermission(CommandSender s){
		return true;
	};
	
	public abstract boolean onCommand(CommandSender s, Command cmd, String Label);
	public abstract void sendNoPermissionMessage(CommandSender s, Command cmd, String[] args);
	public abstract void sendUnknownMessage(CommandSender s, Command cmd, String[] args);
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		if(args.length==0){
			if(!hasPermission(s)){
				sendNoPermissionMessage(s, cmd, args);
				return false;
			}
			return onCommand(s, cmd, label, args);
		}
		Argument acp = map.get(args[0]);
		if(acp==null){
			sendUnknownMessage(s, cmd, args);
			return false;
		}
		return acp.onCommand(s, cmd, label, args);
	}
	
	public abstract void createArguments();
	
	protected String getFullCommand(Command cmd, String[] args){
		String command = cmd.getLabel();
		if(args.length>0){
			for(int i = 0; i < args.length; i++)command = command + " " + args[i];
		}
		return command;
	}
}
