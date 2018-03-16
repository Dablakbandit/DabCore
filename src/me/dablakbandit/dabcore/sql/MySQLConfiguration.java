package me.dablakbandit.dabcore.sql;

import org.bukkit.configuration.file.FileConfiguration;

import me.dablakbandit.dabcore.configuration.Configuration;

public class MySQLConfiguration{
	
	private String user, password, host, port, database, extra;
	
	public MySQLConfiguration(Configuration file){
		FileConfiguration conf = file.GetConfig();
		if(!conf.isSet("SQL.user")){
			conf.set("SQL.user", "user");
		}
		if(!conf.isSet("SQL.password")){
			conf.set("SQL.password", "password");
		}
		if(!conf.isSet("SQL.host")){
			conf.set("SQL.host", "localhost");
		}
		if(!conf.isSet("SQL.port")){
			conf.set("SQL.port", "3306");
		}
		if(!conf.isSet("SQL.database")){
			conf.set("SQL.database", "db");
		}
		if(!conf.isSet("SQL.extra")){
			conf.set("SQL.extra", "?useUnicode=true&characterEncoding=utf-8");
		}
		file.SaveConfig();
		this.user = conf.getString("SQL.user");
		this.password = conf.getString("SQL.password");
		this.host = conf.getString("SQL.host");
		this.port = conf.getString("SQL.port");
		this.database = conf.getString("SQL.database");
		this.extra = conf.getString("SQL.extra");
	}
	
	public MySQL getMySQL(){
		return new MySQL(this.host, this.port, this.database, this.user, this.password, this.extra);
	}
}
