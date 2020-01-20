package com.presenceservice.utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class ConfigUtility {
	
	private ConfigUtility() {}
	
	/*
	 * Load the config.properties file located in the config path 
	 * @return the loaded config.properties as Properties 
	 */
	public static Properties getConfigProperties() throws IOException
	{
		Properties properties = new Properties();

		String path = getConfigPath() + "config.properties" ;
		FileInputStream in = new FileInputStream(path);
		properties.load(in);
		
		return properties ; 
	}
	
	/*
	 * Save changes made to the config.properties 
	 * @param the modified properties that need to be saved 
	 */
	public static void saveConfigProperties(Properties newProperties) throws IOException
	{
		String path = getConfigPath() + "config.properties";
		FileOutputStream out = new FileOutputStream(path);
		newProperties.store(out, null);
	}
	
	public static String getConfigPath() 
	{
		String path ; 
		
		if(isJUnitTest())
		{
			path = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ".presenceservice" + System.getProperty("file.separator") ; 
		}
		else
		{
			path = System.getProperty("user.home") + System.getProperty("file.separator") + ".presenceservice" + System.getProperty("file.separator") ;  
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
	 * Initialize application folder and files in the config path if not exist 
	 */
	public static void initializeConfigFolder() throws IOException
	{
		Path path = Path.of(getConfigPath()) ; 
		
		if(!Files.exists(path))
		{
			File folder = new File(getConfigPath()) ; 
			folder.mkdir() ; 
			
			// Create app.properties
			
			File appFile = new File(getConfigPath() + "config.properties") ; 
			
			appFile.createNewFile() ; 
			
			Properties prop = new Properties() ; 
			
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) ; 
			
			saveConfigProperties(prop) ; 
			
			// Create data.db 
			
			File database = new File(getConfigPath() + prop.getProperty("dbName")) ; 
			
			database.createNewFile() ; 
		}
	}
	
	
	/*
	 * clear application folder on disk 
	 */
	public static void clearConfigFolder()
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
