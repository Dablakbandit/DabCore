package me.dablakbandit.dabcore.jsonformatter.hover;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dablakbandit.dabcore.json.JSONObject;
import me.dablakbandit.dabcore.utils.ItemUtils;

public class ShowItemEvent extends HoverEvent{
	
	private JSONObject object = new JSONObject();
	
	@SuppressWarnings("deprecation")
	public ShowItemEvent(ItemStack is){
		try{
			object.put("action", "show_item");
			String display = ItemUtils.getName(is);
			String raw = ItemUtils.getRawName(is);
			ItemMeta im = is.getItemMeta();
			List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
			Map<Enchantment, Integer> enchants = is.getItemMeta().getEnchants();
			boolean utag = !display.equals(raw) || lore.size() > 0 || enchants.size() > 0;
			String tag = "";
			if(utag){
				tag = ",tag:{";
				if(!display.equals(raw)){
					tag = tag + "display:{Name:" + display;
					if(lore.size() > 0){
						tag = tag + ",Lore:[";
						for(String s : lore){
							tag = tag + "\"" + s + "\",";
						}
						tag = tag.substring(0, tag.length() - 1);
						tag = tag + "]";
					}
					tag = tag + "}";
				}else{
					if(lore.size() > 0){
						tag = tag + "display:{Lore:[";
						for(String s : lore){
							tag = tag + "\"" + s + "\",";
						}
						tag = tag.substring(0, tag.length() - 1);
						tag = tag + "]}";
					}
				}
				if(enchants.size() > 0){
					if(tag.length() > 6){
						tag = tag + ",";
					}
					tag = tag + "ench:[";
					for(Entry<Enchantment, Integer> e : enchants.entrySet()){
						tag = tag + "{id:" + e.getKey().getId() + ",lvl:" + e.getValue() + "},";
					}
					tag = tag.substring(0, tag.length() - 1);
					tag = tag + "]";
				}
				tag = tag + "}";
			}
			String name = ItemUtils.getMinecraftName(is);
			object.put("value", "{id:" + name + ",Count:" + is.getAmount() + tag + "}");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public JSONObject getEvent(){
		return object;
	}
	
}
