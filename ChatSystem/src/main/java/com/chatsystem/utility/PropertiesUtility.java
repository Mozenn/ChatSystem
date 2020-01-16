package com.chatsystem.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtility {
	
	private PropertiesUtility() {}
	
	public static Properties getAppProperties() throws IOException
	{
		Properties properties = new Properties();
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "app.properties";
		System.out.println("PATH BEGIN "+ path) ; 
		FileInputStream in = new FileInputStream(path);
		properties.load(in);
		
		return properties ; 
	}
	
	public static void saveAppProperties(Properties newProperties) throws IOException
	{
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "app.properties";
		System.out.println("PATH "+ path) ; 
		FileOutputStream out = new FileOutputStream(path);
		newProperties.store(out, null);
	}

}
