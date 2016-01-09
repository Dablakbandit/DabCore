package me.dablakbandit.dabcore.command;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import me.dablakbandit.dabcore.utils.NMSUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public abstract class AbstractCommand implements CommandExecutor {

	protected final String command;
	protected final String description;
	protected final List<String> alias;
	protected final String usage;
	protected final String permMessage;

	protected static CommandMap cmap;

	public AbstractCommand(String command) {
		this(command, null, null, null, null);
	}

	public AbstractCommand(String command, String usage) {
		this(command, usage, null, null, null);
	}

	public AbstractCommand(String command, String usage, String description) {
		this(command, usage, description, null, null);
	}

	public AbstractCommand(String command, String usage, String description, String permissionMessage) {
		this(command, usage, description, permissionMessage, null);
	}

	public AbstractCommand(String command, String usage, String description, List<String> aliases) {
		this(command, usage, description, null, aliases);
	}

	public AbstractCommand(String command, String usage, String description, String permissionMessage, List<String> aliases) {
		this.command = command.toLowerCase();
		this.usage = usage;
		this.description = description;
		this.permMessage = permissionMessage;
		this.alias = aliases;
	}
	
	private static CommandMap commandMap = getCommandMap();
	private static Field knownCommands = getKnownCommands();

	@SuppressWarnings("unchecked")
	public void register() {
		ReflectCommand cmd = new ReflectCommand(this.command);
		if (this.alias != null) cmd.setAliases(this.alias);
		if (this.description != null) cmd.setDescription(this.description);
		if (this.usage != null) cmd.setUsage(this.usage);
		if (this.permMessage != null) cmd.setPermissionMessage(this.permMessage);
		try{
			Map<String, Command> commands = (Map<String, Command>) knownCommands.get(commandMap);
			commands.remove(this.command);
			knownCommands.set(commandMap, commands);
		}catch(Exception e){
			e.printStackTrace();
			System.out.print(commandMap.getClass().getName());
		}
		commandMap.register(this.command, "", cmd);
		cmd.setExecutor(this);
	}
	
	static Field getKnownCommands(){
		try{
			Class<?> clazz = commandMap.getClass();
			if(clazz.getSimpleName().equals("FakeSimpleCommandMap"))clazz = clazz.getSuperclass();
			return NMSUtils.getField(commandMap.getClass(), "knownCommands");
		}catch(Exception e){
			e.printStackTrace();
			System.out.print(commandMap.getClass().getName());
		}
		return null;
	}

	static CommandMap getCommandMap() {
		if (cmap == null) {
			try {
				final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				f.setAccessible(true);
				cmap = (CommandMap) f.get(Bukkit.getServer());
			} catch (Exception e) { e.printStackTrace(); }
			return cmap;
		} else {
			return cmap; 
		}
	}

	private final class ReflectCommand extends Command {
		private CommandExecutor exe = null;
		protected ReflectCommand(String command) { super(command); }
		public void setExecutor(CommandExecutor exe) { this.exe = exe; }
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			if (exe != null) { exe.onCommand(sender, this, commandLabel, args); }
			return false;
		}
	}

	public boolean isPlayer(CommandSender sender) { return (sender instanceof Player); }
	public boolean isAuthorized(CommandSender sender, String permission) { return sender.hasPermission(permission); }
	public boolean isAuthorized(Player player, String permission) { return player.hasPermission(permission); }
	public boolean isAuthorized(CommandSender sender, Permission perm) { return sender.hasPermission(perm); }
	public boolean isAuthorized(Player player, Permission perm) { return player.hasPermission(perm); }

	public abstract boolean onCommand(CommandSender s, Command cmd, String Label, String[] args);

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return null;
	}
}
