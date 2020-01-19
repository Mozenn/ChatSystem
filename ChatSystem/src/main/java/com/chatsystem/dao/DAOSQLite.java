package com.chatsystem.dao;

import java.io.IOException;
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
import com.chatsystem.user.UserId;
import com.chatsystem.utility.ConfigurationUtility;

public class DAOSQLite implements DAO {
	
	private static String DB_URL ;
	
	public DAOSQLite() throws IOException 
	{
		Properties appProps = ConfigurationUtility.getAppProperties() ; 
		
		DB_URL = appProps.getProperty("dbStartURL") + ConfigurationUtility.getConfigPath() + appProps.getProperty("dbName") ; 
		
		String driver = appProps.getProperty("driverClassName") ; 
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			throw new DAOConfigurationException("Driver not Found", e1) ; 
		}
		
		createMessagesTable();
	}
	
	private void createMessagesTable() throws IOException
	{
		
		String createStmt = "CREATE TABLE IF NOT EXISTS messages (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    receiverid BLOB NOT NULL,\n"
                + "    senderid BLOB NOT NULL,\n"
                + "    timestamp text NOT NULL,\n"
                + "    type text NOT NULL,\n"
                + "    content BLOB\n"
                + ");";  
		
		try (Connection conn = DriverManager.getConnection(DB_URL);
		        Statement stmt = conn.createStatement()) {
			
		    // create a new table if not exist 
		    stmt.execute(createStmt);
		    
		} catch (SQLException e) {
			throw new DAOException(e) ; 
		} 
	}

	@Override
	public void addMessage(UserMessage message)  {
		

		
		String insertStmt = "INSERT INTO messages(receiverid,senderid,timestamp,type,content) VALUES(?,?,?,?,?)" ; 
	
	    try(Connection conn = DriverManager.getConnection(DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(insertStmt))
	    {
	        pstmt.setBytes(1, message.getReceiverId().getId());
	        pstmt.setBytes(2, message.getSenderId().getId());
	        pstmt.setString(3, message.getDate().toString());
	        pstmt.setString(4, message.getSubtype().toString());
	        pstmt.setBytes(5, message.getContent());
	        pstmt.executeUpdate();
	    }catch (SQLException e) {
	    	throw new DAOException("Message Insertion failed", e) ; 
		}
		
	}

	@Override
	public List<UserMessage> getHistory(UserId receiverId) {

		List<UserMessage> messages = new ArrayList<UserMessage>();
		
		// query all messages 
		
		String query = "SELECT receiverid, senderid, timestamp, type, content FROM messages WHERE receiverid = ? or senderid = ?" ;  
		
       try (Connection conn = DriverManager.getConnection(DB_URL);
    		   PreparedStatement pstmt = conn.prepareStatement(query)){
    	   pstmt.setBytes(1,receiverId.getId()); 
    	   pstmt.setBytes(2,receiverId.getId()); 
    	   ResultSet rs = pstmt.executeQuery();
    	   
    	   
    	   
              // loop through the result set
              while (rs.next()) { 
            	  UserId receiverIdResult = new UserId(rs.getBytes("receiverid")) ;
            	  UserId senderIdResult = new UserId(rs.getBytes("senderid")) ;
            	  Timestamp ts = Timestamp.valueOf(rs.getString("timestamp")) ; 
            	  byte[] content = rs.getBytes("content") ; 
            	  String type = rs.getString("type") ;
            	  
            	  var messageToAdd = new UserMessage(content,UserMessage.UserMessageType.valueOf(type),receiverIdResult, senderIdResult); 
            	  messageToAdd.setDate(ts);
            	  
            	  messages.add(messageToAdd);
              }
          } catch (SQLException e) {
        	  throw new DAOException("Query failed", e) ; 
          } 
		
		
		// sort messages using timestamp 
		Collections.sort(messages); 
       
       
       return messages ; 
	}

	@Override
	public void clearHistory() {
		
		
		String deleteStmt = "DELETE FROM messages" ; 
	
	try (Connection conn = DriverManager.getConnection(DB_URL);
	        Statement stmt = conn.createStatement()) {
		
	    stmt.execute(deleteStmt);
	    
	    
	} catch (SQLException e) {
		throw new DAOException("Clear Message Table failed", e) ; 
	}
	
	}

}
