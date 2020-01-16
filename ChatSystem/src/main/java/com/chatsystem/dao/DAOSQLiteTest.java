package com.chatsystem.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.PropertiesUtility;

public final class DAOSQLiteTest extends DAOSQLite {
	
	private static String DB_URL ;

	public DAOSQLiteTest() throws IOException
	{
		super();
		
		loadDatabaseURL() ; 
	}
	
	@Override 
	protected void loadDatabaseURL() throws IOException
	{
		Properties appProps = PropertiesUtility.getAppProperties() ; 
		
		DB_URL = appProps.getProperty("dbURLTest") ; 
	}
	
	@Override 
	protected String getDatabaseURL() 
	{

		return DB_URL ; 
	}

}
