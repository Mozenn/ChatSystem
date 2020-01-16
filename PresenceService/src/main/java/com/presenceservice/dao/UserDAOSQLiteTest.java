package com.presenceservice.dao;

public final class UserDAOSQLiteTest extends UserDAOSQLite {
	
	private static final String DB_URL = "jdbc:sqlite:data/test.db" ;
	
	public UserDAOSQLiteTest()
	{
		super() ; 
	}
	
	@Override 
	protected String getDatabaseURL()
	{
		return DB_URL ; 
	}

}
