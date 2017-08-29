package me.dablakbandit.dabcore.jsonformatter.click;

import me.dablakbandit.dabcore.json.JSONObject;

public class OpenUrlEvent extends ClickEvent{
	
	private JSONObject object = new JSONObject();
	
	public OpenUrlEvent(String suggest){
		try{
			object.put("action", "open_url");
			object.put("value", suggest);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public JSONObject getEvent(){
		return object;
	}
	
}
