package com.chatsystem.dao;

import java.io.IOException;

import com.chatsystem.utility.ConfigurationUtility;

public final class UserDAOFactory {
	
	public UserDAOFactory()
	{
		
	}
	
	public UserDAO getUserDAOInstance() throws IOException
	{
		
		UserDAO dao ; 
		
		if(ConfigurationUtility.isUsingEmbeddedDB())
		{
			dao = new UserDAOEmbedded() ; 
		}
		else
		{
			dao = new UserDAOMySQL() ; 
		}
		
		return dao ;
	}

}
