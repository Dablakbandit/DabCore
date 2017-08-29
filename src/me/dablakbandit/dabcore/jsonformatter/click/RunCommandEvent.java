package me.dablakbandit.dabcore.jsonformatter.click;

import me.dablakbandit.dabcore.json.JSONObject;

public class RunCommandEvent extends ClickEvent{
	
	private JSONObject object = new JSONObject();
	
	public RunCommandEvent(String command){
		if(!command.startsWith("/"))
			command = "/" + command;
		try{
			object.put("action", "run_command");
			object.put("value", command);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public JSONObject getEvent(){
		return object;
	}
	
}
