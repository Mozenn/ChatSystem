package com.chatsystem.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.chatsystem.system.CommunicationSystem;

/*
 * Method library providing utility method related to configuration 
 */
public final class ConfigurationUtility {
	
	private static Boolean bTest ; 
	private static Boolean bEmbedded ; 
	private static String path ; 
	
	private ConfigurationUtility() {}
	
	public static Properties getAppProperties() throws IOException
	{	
		Properties properties = new Properties();

		String path = getConfigPath() + "app.properties" ;
		FileInputStream in = new FileInputStream(path);
		properties.load(in);
		
		return properties ; 
	}
	
	/*
	 * Save changes made to newProperties to the app.properties file 
	 * @param the modified properties that need to be saved 
	 * @throws NullPointerException if newProperties is null 
	 */
	public static void saveAppProperties(Properties newProperties) throws IOException
	{
		if(newProperties == null)
			throw new NullPointerException() ; 
		
		String path = getConfigPath() + "app.properties";
		FileOutputStream out = new FileOutputStream(path);
		newProperties.store(out, null);
	}
	
	/*
	 * Get path where all configuration & data are stored 
	 */
	public static String getConfigPath() 
	{
		if(path == null)
		{
			if(isJUnitTest())
			{
			    int leftLimit = 97; // letter 'a'
			    int rightLimit = 122; // letter 'z'
			    Random random = new Random();
			 
			    String fileName = random.ints(leftLimit, rightLimit + 1)
			      .limit(5)
			      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			      .toString();
				
				path = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + fileName + System.getProperty("file.separator") ; 
			}
			else
			{
				if(isTesting())
					path = System.getProperty("user.home") + System.getProperty("file.separator") + ".chatsystemtest" + System.getProperty("file.separator") ;  
				else
					path = System.getProperty("user.home") + System.getProperty("file.separator") + ".chatsystem" + System.getProperty("file.separator") ;  
			}
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
	/*
	 * Whether the client is running as a testing version or standard version 
	 * a testing version has no local communication enabled nor message storing functionality 
	 */
	public static boolean isTesting()
	{
		
		
		if(bTest == null)
		{
			Properties prop = new Properties() ; 
			
			try {
				prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties")) ;
				
				bTest = prop.getProperty("test").equals("true") ? Boolean.valueOf(true) : Boolean.valueOf(false) ; 
				
			} catch (IOException e) {
				e.printStackTrace();
				bTest = Boolean.valueOf(false) ; 
			} 
			
			
			if(bTest)
				LoggerUtility.getInstance().severe("TESTING"); 
		}

		
		return bTest.booleanValue() ; 
	}
	
	public static boolean isUsingEmbeddedDB()
	{
		
		if(bEmbedded == null)
		{
			Properties prop = new Properties() ;
			
			try {
				prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties")) ;
				
				bEmbedded = prop.getProperty("embeddedDB").equals("true") ? Boolean.valueOf(true) : Boolean.valueOf(false) ; 
				
			} catch (IOException e) {
				e.printStackTrace();
				bEmbedded = Boolean.valueOf(false) ; 
			} 
		}
		
		return bEmbedded.booleanValue() ; 
	}
	
	/*
	 * Initialize application folder and files in the config path if not exist 
	 */
	public static void initializeApplicationFolder() throws IOException
	{
		if(isJUnitTest())
			clearApplicationFolder();
		
		Path path = Path.of(getConfigPath()) ; 
		
		if(!Files.exists(path))
		{
			File folder = new File(getConfigPath()) ; 
			folder.mkdir() ; 
			
			// Create app.properties
			
			File appFile = new File(getConfigPath() + "app.properties") ; 
			System.out.println(getConfigPath() + "app.properties") ; 
			
			appFile.createNewFile() ; 
			
			Properties prop = new Properties() ; 
			
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties")) ; 
			
			saveAppProperties(prop) ; 
			
			// Create data.db 
			
			File database = new File(getConfigPath() + prop.getProperty("dbEmbeddedName")) ; 
			
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
