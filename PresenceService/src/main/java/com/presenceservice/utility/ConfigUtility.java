package com.presenceservice.utility;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigUtility {
	
	private ConfigUtility() {}
	
	public static Properties getConfigProperties() throws IOException
	{
		Properties properties = new Properties();
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "config.properties";
		FileInputStream in = new FileInputStream(path);
		properties.load(in);
		
		return properties ; 
	}
	
	public static void saveConfigProperties(Properties newProperties) throws IOException
	{
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "config.properties";
		FileOutputStream out = new FileOutputStream(path);
		newProperties.store(out, null);
	}
	
	public static String getConfigPath() 
	{
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() ;
		return path ; 
	}

}
