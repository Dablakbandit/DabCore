package me.dablakbandit.dabcore.jsonformatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.dablakbandit.dabcore.json.JSONArray;
import me.dablakbandit.dabcore.json.JSONObject;
import me.dablakbandit.dabcore.utils.ItemUtils;
import me.dablakbandit.dabcore.utils.NMSUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JSONFormatter {

	private JSONArray ja = new JSONArray();
	private Builder builder = new Builder();
	private String color = "";

	public JSONFormatter(){}

	public JSONFormatter append(JSONFormatter json){
		if(json.ja.length()==0)return this;
		try{
			for(int i = 0; i < json.ja.length(); i++){
				add(json.ja.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}

	public void clear(){
		ja = new JSONArray();
		builder = new Builder();
		color = "";
	}

	public JSONFormatter resetColors(){
		color = "";
		builder = new Builder();
		return this;
	}

	public String toJSON(){
		JSONObject jo = new JSONObject();
		try{
			if(ja.length()>0)jo.put("extra", ja);
			jo.put("text", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	public Object toSerialized(){
		try{
			return a.invoke(null, toJSON());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public JSONFormatter send(Player player){
		JSONFormatter.send(player, this);
		return this;
	}
	
	private void add(Object jo){
		if(jo!=null){
			ja.put(jo);
		}
	}
	
	public JSONFormatter appendNoConvert(String s){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			builder.append(c);
		}
		add(builder.toString(color));
		return this;
	}

	public JSONFormatter append(String s){
		s = ChatColor.translateAlternateColorCodes('&', s);
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toString(color));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toString(color));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toString(color));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toString(color));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toString(color));
					builder = new Builder(builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toString(color));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toString(color));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toString(color));
		return this;
	}
	
	public JSONFormatter appendHoverNoConvert(String s, String hover){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			builder.append(c);
		}
		add(builder.toStringHover(color, hover));
		return this;
	}

	public JSONFormatter appendHover(String s, String hover){
		s = ChatColor.translateAlternateColorCodes('&', s);
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toStringHover(color, hover));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toStringHover(color, hover));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toStringHover(color, hover));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toStringHover(color, hover));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toStringHover(color, hover));
					builder = new Builder(builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toStringHover(color, hover));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toStringHover(color, hover));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toStringHover(color, hover));
		return this;
	}
	
	public JSONFormatter appendCommandNoConvert(String s, String cmd){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			builder.append(c);
		}
		add(builder.toStringCommand(color, cmd));
		return this;
	}

	public JSONFormatter appendCommand(String s, String cmd){
		s = ChatColor.translateAlternateColorCodes('&', s);
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toStringCommand(color, cmd));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toStringCommand(color, cmd));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toStringCommand(color, cmd));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toStringCommand(color, cmd));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toStringCommand(color, cmd));
					builder = new Builder(builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toStringCommand(color, cmd));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toStringCommand(color, cmd));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toStringCommand(color, cmd));
		return this;
	}
	
	public JSONFormatter appendHoverCommandNoConvert(String s, String hover, String command){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			builder.append(c);
		}
		add(builder.toStringHoverCommand(color, hover, command));
		return this;
	}

	public JSONFormatter appendHoverCommand(String s, String hover, String cmd){
		s = ChatColor.translateAlternateColorCodes('&', s);
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder(builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toStringHoverCommand(color, hover, cmd));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toStringHoverCommand(color, hover, cmd));
		return this;
	}
	
	public JSONFormatter appendSuggestNoConvert(String s, String suggest){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			builder.append(c);
		}
		add(builder.toStringSuggest(color, suggest));
		return this;
	}

	public JSONFormatter appendSuggest(String s, String suggest){
		s = ChatColor.translateAlternateColorCodes('&', s);
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder(builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toStringSuggest(color, suggest));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toStringSuggest(color, suggest));
		return this;
	}
	
	public JSONFormatter appendHoverSuggestNoConvert(String s, String hover, String suggest){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			builder.append(c);
		}
		add(builder.toStringHoverSuggest(color, hover, suggest));
		return this;
	}

	public JSONFormatter appendHoverSuggest(String s, String hover, String suggest){
		s = ChatColor.translateAlternateColorCodes('&', s);
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder( builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toStringHoverSuggest(color, hover, suggest));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toStringHoverSuggest(color, hover, suggest));
		return this;
	}
	
	public JSONFormatter appendShowHover(String s, ItemStack is){
		builder = new Builder(builder);
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
			case '§':{
				ChatColor cc = ChatColor.getByChar(s.charAt(i+1)); 
				if(cc==null){
					builder.append(c);
					break;
				}
				switch(cc){
				case BOLD:
					add(builder.toStringShowItem(color, is));
					builder = new Builder(builder);
					builder.bold = true;
					break;
				case ITALIC:
					add(builder.toStringShowItem(color, is));
					builder = new Builder(builder);
					builder.italic = true;
					break;
				case MAGIC:
					add(builder.toStringShowItem(color, is));
					builder = new Builder(builder);
					builder.magic = true;
					break;
				case RESET:
					add(builder.toStringShowItem(color, is));
					builder = new Builder();
					color = "";
					break;
				case STRIKETHROUGH:
					add(builder.toStringShowItem(color, is));
					builder = new Builder( builder);
					builder.strikethrough = true;
					break;
				case UNDERLINE:
					add(builder.toStringShowItem(color, is));
					builder = new Builder(builder);
					builder.underline = true;
					break;
				default:{
					add(builder.toStringShowItem(color, is));
					builder = new Builder();
					color = cc.name().toLowerCase();
					break;
				}
				}
				i++;
				break;
			}
			default:{
				builder.append(c);
			}
			}
		}
		add(builder.toStringShowItem(color, is));
		return this;
	}
	
	public JSONFormatter appendItemHoverNoConvert(String s, ItemStack is){
		builder = new Builder(builder);
		return this;
	}

	private static Class<?> cs = NMSUtils.getNMSClassSilent("ChatSerializer", "IChatBaseComponent"), icbc = NMSUtils.getNMSClassSilent("IChatBaseComponent"), ppoc = NMSUtils.getNMSClassSilent("PacketPlayOutChat"),
			pc = NMSUtils.getNMSClassSilent("PlayerConnection"), p = NMSUtils.getNMSClassSilent("Packet"), ep = NMSUtils.getNMSClassSilent("EntityPlayer");
	private static Method a = NMSUtils.getMethodSilent(cs, "a", String.class), sp = NMSUtils.getMethodSilent(pc, "sendPacket", p);
	private static Field ppc = NMSUtils.getFieldSilent(ep, "playerConnection");
	private static Constructor<?> ppocc1 = NMSUtils.getConstructorSilent(ppoc, icbc), ppocc2 = NMSUtils.getConstructorSilent(ppoc);
	private static boolean b = cs!=null&&icbc!=null&&ppoc!=null&&pc!=null&&p!=null&&ep!=null&&a!=null&&sp!=null&&ppc!=null&&ppocc1!=null&&ppocc2!=null;

	private static void send(Player player, JSONFormatter jm){
		if(b){
			try{
				Object icbco = a.invoke(null, jm.toJSON());
				Object ppoco = ppocc1.newInstance(icbco);
				Object entityplayer = NMSUtils.getHandle(player);
				Object ppco = ppc.get(entityplayer);
				sp.invoke(ppco, ppoco);
			}catch(Exception e){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jm.toJSON());
			}
		}else{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jm.toJSON());
		}
	}

	public static void send(Player player, List<JSONFormatter> jfl){
		if(b){
			try{
				Object entityplayer = NMSUtils.getHandle(player);
				Object ppco = ppc.get(entityplayer);
				for(JSONFormatter jm : jfl){
					try{
						Object icbco = a.invoke(null, jm.toJSON());
						Object ppoco = ppocc1.newInstance(icbco);
						sp.invoke(ppco, ppoco);
					}catch(Exception e){
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jm.toJSON());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			for(JSONFormatter jm : jfl)Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jm.toJSON());
		}
	}

	private class Builder{

		private StringBuilder sb = new StringBuilder("");
		private boolean bold = false, italic = false, magic = false, strikethrough = false, underline = false, changed = false;

		public Builder(){
		}

		public Builder(Builder b){
			bold = b.bold;
			italic = b.italic;
			magic = b.magic;
			strikethrough = b.strikethrough;
			underline = b.underline;
		}

		public void append(char c){
			sb.append(c);
			changed = true;
		}

		public JSONObject toStringHoverCommand(String color, String hover, String cmd){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
				JSONObject jo1 = new JSONObject();
				jo1.put("action", "show_text");
				jo1.put("value", hover);
				jo.put("hoverEvent", jo1);
				JSONObject jo2 = new JSONObject();
				jo2.put("action", "run_command");
				jo2.put("value", "/" + cmd);
				jo.put("clickEvent", jo2);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}

		public JSONObject toStringCommand(String color, String cmd){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
				JSONObject jo1 = new JSONObject();
				jo1.put("action", "run_command");
				jo1.put("value", "/" + cmd);
				jo.put("clickEvent", jo1);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}

		public JSONObject toStringHover(String color, String hover){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
				JSONObject jo1 = new JSONObject();
				jo1.put("action", "show_text");
				jo1.put("value", hover);
				jo.put("hoverEvent", jo1);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}

		public JSONObject toStringHoverSuggest(String color, String hover, String suggest){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
				JSONObject jo1 = new JSONObject();
				jo1.put("action", "show_text");
				jo1.put("value", hover);
				jo.put("hoverEvent", jo1);
				JSONObject jo2 = new JSONObject();
				jo2.put("action", "suggest_command");
				jo2.put("value", "/" + suggest);
				jo.put("clickEvent", jo2);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}

		public JSONObject toStringSuggest(String color, String suggest){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
				JSONObject jo1 = new JSONObject();
				jo1.put("action", "suggest_command");
				jo1.put("value", "/" + suggest);
				jo.put("clickEvent", jo1);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}

		public JSONObject toString(String color){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}
		
		@SuppressWarnings("deprecation")
		public JSONObject toStringShowItem(String color, ItemStack is){
			String string = sb.toString();
			if(!changed)return null;
			if(string.length()==0)return null;
			JSONObject jo = new JSONObject();
			try{
				if(!color.equals(""))jo.put("color", color);
				if(bold)jo.put("bold", true);
				if(italic)jo.put("italic", true);
				if(magic)jo.put("obfuscated", true);
				if(strikethrough)jo.put("strikethrough", true);
				if(underline)jo.put("underlined", true);
				jo.put("text", string);
				JSONObject jo1 = new JSONObject();
				jo1.put("action", "show_item");
				String display = ItemUtils.getName(is);
				String raw = ItemUtils.getRawName(is);
				ItemMeta im = is.getItemMeta();
				List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
				Map<Enchantment, Integer> enchants = is.getItemMeta().getEnchants();
				boolean utag = !display.equals(raw)||lore.size()>0||enchants.size()>0;
				String tag = "";
				if(utag){
					tag = ",tag:{";
					if(!display.equals(raw)){
						tag = tag + "display:{Name:" + display;
						if(lore.size()>0){
							tag = tag + ",Lore:[";
							for(String s : lore){
								tag = tag + "\"" + s + "\",";
							}
							tag = tag.substring(0, tag.length()-1);
							tag = tag + "]";
						}
						tag = tag + "}";
					}else{
						if(lore.size()>0){
							tag = tag + "display:{Lore:[";
							for(String s : lore){
								tag = tag + "\"" + s + "\",";
							}
							tag = tag.substring(0, tag.length()-1);
							tag = tag + "]}";
						}
					}
					if(enchants.size()>0){
						if(tag.length()>6){
							tag = tag + ",";	
						}
						tag = tag + "ench:[";
						for(Entry<Enchantment, Integer> e : enchants.entrySet()){
							tag = tag + "{id:" + e.getKey().getId() + ",lvl:" + e.getValue() + "},";
						}
						tag = tag.substring(0, tag.length()-1);
						tag = tag + "]";
					}
					tag = tag + "}";
				}
				String name = ItemUtils.getMinecraftName(is);
				jo1.put("value", "{id:" + name + ",Count:" + is.getAmount() + tag + "}");
				jo.put("hoverEvent", jo1);
			}catch(Exception e){
				e.printStackTrace();
			}
			return jo;
		}
	}
}
