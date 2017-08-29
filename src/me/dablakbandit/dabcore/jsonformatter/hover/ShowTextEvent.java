package me.dablakbandit.dabcore.jsonformatter.hover;

import me.dablakbandit.dabcore.json.JSONObject;

public class ShowTextEvent extends HoverEvent{
	
	private JSONObject object = new JSONObject();
	
	public ShowTextEvent(String text){
		try{
			object.put("action", "show_text");
			object.put("value", text);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public JSONObject getEvent(){
		return object;
	}
	
}
