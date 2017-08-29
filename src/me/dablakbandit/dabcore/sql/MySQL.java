package me.dablakbandit.dabcore.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends Database{
	private final String	user;
	private final String	database;
	private final String	password;
	private final String	port;
	private final String	hostname;
	private Connection		connection;

	public MySQL(String hostname, String port, String database, String username, String password){
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
		this.connection = null;
	}

	public Connection openConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/"
					+ this.database + "?useUnicode=true&characterEncoding=utf-8", this.user, this.password);
		}catch(SQLException e){
			System.out.print(e.getMessage());
			closeConnection();
		}catch(ClassNotFoundException e){
			System.out.print("JDBC Driver not found!");
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
				System.out.print("Error closing the MySQL Connection!");
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
