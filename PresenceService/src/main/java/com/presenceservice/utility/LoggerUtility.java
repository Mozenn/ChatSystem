package com.presenceservice.utility;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * Logger that log both on console and file in the loggerPath specified in the config.properties file or temp folder if not specified 
 */
public class LoggerUtility {
	
	private static LoggerUtility instance ; 
	
    private static Logger logger;   
       private final int limit = 1024*10000; //10 MB

	private LoggerUtility()
	{
	    logger = Logger.getLogger("Logger");  
	    
	    FileHandler fh;  

	    try {  

	    	String path = ConfigUtility.getConfigProperties().getProperty("loggerPath") ; 
	    	
	    	if(path.equals("null"))
	    	{
	    		fh = new FileHandler(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "presenceservice.log",limit,2);
	    	}
	    	else
	    	{
	    		fh = new FileHandler(path + System.getProperty("file.separator") + "presenceservice.log",limit,2);
	    	}
	          
	        
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter); 
	        
	        logger.addHandler(fh);

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
    public static LoggerUtility getInstance() {
        if(instance == null) {
            instance = new LoggerUtility();
        }
        return instance;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warning(String message) {
        logger.warning(message);        
    }

    public void severe(String message) {
        logger.severe(message);
    }
	
	
}
