package me.dablakbandit.dabcore.command.advanced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dablakbandit.dabcore.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
		register();
	}
	
	public Map<String, AdvancedCommandPart> map = new HashMap<String, AdvancedCommandPart>();
	
	public abstract boolean needsPermission();
	public abstract String getPermission();
	public abstract void loadParts();
	public abstract boolean onCommandPart(CommandSender s, Command cmd, String Label, String[] args);
	public abstract void sendPermissionMessage(CommandSender s);
	public abstract void sendUnknownMessage(CommandSender s);
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String Label, String[] args){
		if(args.length==0){
			if(needsPermission()&&!s.hasPermission(getPermission())){
				sendPermissionMessage(s);
				return false;
			}
			return onCommandPart(s, cmd, Label, args);
		}
		AdvancedCommandPart acp = map.get(args[0]);
		if(acp==null){
			sendUnknownMessage(s);
			return false;
		}
		return acp.onCommandPart(s, cmd, Label, args);
	}
}
