package com.presenceservice.dao;

import java.io.IOException;
import java.util.Properties;

import com.presenceservice.utility.ConfigUtility;

public final class UserDAOSQLiteTest extends UserDAOSQLite {
	
	private static String DB_URL ;
	
	public UserDAOSQLiteTest() throws IOException
	{
		
		setupDatabase();
		
		createUsersTable(); // TODO create dao factory 
	}
	
	@Override
	protected void setupDatabase() throws IOException
	{
		Properties configProps = ConfigUtility.getConfigProperties() ; 
		
		DB_URL = configProps.getProperty("dbStartURL") + ConfigUtility.getConfigPath() + configProps.getProperty("dbTestName") ; 
		
		try {
			String driver = ConfigUtility.getConfigProperties().getProperty("driverClassName") ; 
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DAOConfigException("Driver not found", e) ; 
		} 
	}
	
	@Override 
	protected String getDatabaseURL()
	{
		return DB_URL ; 
	}

}
