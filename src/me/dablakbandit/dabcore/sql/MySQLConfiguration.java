package me.dablakbandit.dabcore.sql;

import me.dablakbandit.dabcore.configuration.Configuration;

public class MySQLConfiguration {

	private String user, password, host, port, database;
	
	public MySQLConfiguration(Configuration file){
		if(!file.GetConfig().isSet("SQL.user")){
			file.GetConfig().set("SQL.user", "user");
		}
		if(!file.GetConfig().isSet("SQL.password")){
			file.GetConfig().set("SQL.password", "password");
		}
		if(!file.GetConfig().isSet("SQL.host")){
			file.GetConfig().set("SQL.host", "localhost");
		}
		if(!file.GetConfig().isSet("SQL.port")){
			file.GetConfig().set("SQL.port", "3306");
		}
		if(!file.GetConfig().isSet("SQL.database")){
			file.GetConfig().set("SQL.database", "db");
		}
		file.SaveConfig();
		this.user = file.GetConfig().getString("SQL.user");
		this.password = file.GetConfig().getString("SQL.password");
		this.host = file.GetConfig().getString("SQL.host");
		this.port = file.GetConfig().getString("SQL.port");
		this.database = file.GetConfig().getString("SQL.database");
	}
	
	public MySQL getMySQL(){
		return new MySQL(this.host, this.port, this.database, this.user, this.password);
	}
}
