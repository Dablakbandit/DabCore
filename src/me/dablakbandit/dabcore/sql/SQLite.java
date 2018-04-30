package me.dablakbandit.dabcore.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLite extends Database{
	
	private JavaPlugin		plugin;
	private final String	database;
	private Connection		connection;
	
	public SQLite(String database){
		this(null, database);
	}
	
	public SQLite(JavaPlugin plugin, String database){
		this.plugin = plugin;
		this.database = database;
		this.connection = null;
	}
	
	public Connection openConnection(){
		try{
			Class.forName("org.sqlite.JDBC");
			File f = (plugin == null ? new File(database) : new File(plugin.getDataFolder(), database));
			if(!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			if(!f.exists())
				f.createNewFile();
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + f);
		}catch(SQLException e){
			System.out.print(e.getMessage());
			closeConnection();
		}catch(ClassNotFoundException e){
			System.out.print("JDBC Driver not found!");
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.connection;
	}
	
	public boolean checkConnection(){
		return this.connection != null;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public void closeConnection(){
		if(this.connection != null){
			try{
				this.connection.close();
			}catch(SQLException e){
				System.out.print("Error closing the SQLite Connection!");
				e.printStackTrace();
			}
		}
	}
	
	public ResultSet querySQL(String query){
		Connection c = null;
		if(checkConnection()){
			c = getConnection();
		}else{
			c = openConnection();
		}
		Statement s = null;
		try{
			s = c.createStatement();
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		ResultSet ret = null;
		try{
			ret = s.executeQuery(query);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public void updateSQL(String update){
		Connection c = null;
		if(checkConnection()){
			c = getConnection();
		}else{
			c = openConnection();
		}
		Statement s = null;
		try{
			s = c.createStatement();
			s.executeUpdate(update);
		}catch(SQLException e1){
			e1.printStackTrace();
		}
	}
}
