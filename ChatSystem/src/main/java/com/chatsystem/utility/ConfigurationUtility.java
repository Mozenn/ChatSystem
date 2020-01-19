package com.chatsystem.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class ConfigurationUtility {
	
	private ConfigurationUtility() {}
	
	public static Properties getAppProperties() throws IOException
	{	
		Properties properties = new Properties();

		String path = getConfigPath() + "app.properties" ;
		FileInputStream in = new FileInputStream(path);
		properties.load(in);
		
		return properties ; 
	}
	
	public static void saveAppProperties(Properties newProperties) throws IOException
	{
		String path = getConfigPath() + "app.properties";
		FileOutputStream out = new FileOutputStream(path);
		newProperties.store(out, null);
	}
	
	public static String getConfigPath() 
	{
		String path ; 
		
		if(isJUnitTest())
		{
			path = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ".chatsystem" + System.getProperty("file.separator") ; 
		}
		else
		{
			path = System.getProperty("user.home") + System.getProperty("file.separator") + ".chatsystem" + System.getProperty("file.separator") ;  
		}
		
		return path ; 
	}
	
	private static boolean isJUnitTest() {
	    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	    List<StackTraceElement> list = Arrays.asList(stackTrace);
	    for (StackTraceElement element : list) {
	        if (element.getClassName().startsWith("org.junit.")) {
	            return true;
	        }           
	    }
	    return false;
	}
	
	public static void initializeApplicationFolder() throws IOException
	{
		Path path = Path.of(getConfigPath()) ; 
		
		if(!Files.exists(path))
		{
			File folder = new File(getConfigPath()) ; 
			folder.mkdir() ; 
			
			// Create app.properties
			
			File appFile = new File(getConfigPath() + "app.properties") ; 
			
			appFile.createNewFile() ; 
			
			Properties prop = new Properties() ; 
			
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties")) ; 
			
			saveAppProperties(prop) ; 
			
			// Create data.db 
			
			File database = new File(getConfigPath() + prop.getProperty("dbName")) ; 
			
			database.createNewFile() ; 
		}
	}
	
	public static void clearApplicationFolder()
	{
		Path path = Path.of(getConfigPath()) ; 
		
		if(Files.exists(path))
		{
			deleteDirectory(new File(getConfigPath())) ; 
		}
	}
	
	private static boolean deleteDirectory(File directory)
	{
	    File[] allContents = directory.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directory.delete();
	}

}
