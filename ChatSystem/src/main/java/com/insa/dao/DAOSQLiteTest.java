package com.insa.dao;

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

import com.insa.message.UserMessage;

public class DAOSQLiteTest implements DAO {
	
	private static final String DB_URL = "jdbc:sqlite:data/test.db" ;

	@Override
	public void addMessage(UserMessage message)  {
		
		
		String createStmt = "CREATE TABLE IF NOT EXISTS messages (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    receiverid BLOB NOT NULL,\n"
                + "    senderid BLOB NOT NULL,\n"
                + "    timestamp text NOT NULL,\n"
                + "    type text NOT NULL,\n"
                + "    content BLOB\n"
                + ");";  
		
		String insertStmt = "INSERT INTO messages(receiverid,senderid,timestamp,type,content) VALUES(?,?,?,?,?)" ; 
	
	try (Connection conn = DriverManager.getConnection(DB_URL);
	        Statement stmt = conn.createStatement()) {
		
	    // create a new table if not exist 
	    stmt.execute(createStmt);
	    
	    try(PreparedStatement pstmt = conn.prepareStatement(insertStmt))
	    {
	        pstmt.setBytes(1, message.getReceiverId());
	        pstmt.setBytes(2, message.getSenderId());
	        pstmt.setString(3, message.getDate().toString());
	        pstmt.setString(4, message.getSubtype().toString());
	        pstmt.setBytes(5, message.getContent());
	        pstmt.executeUpdate();
	    }
	    
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
		
	}

	@Override
	public List<UserMessage> getHistory(byte[] receiverId) {

		List<UserMessage> messages = new ArrayList<UserMessage>();
		
		// query all messages 
		
		String query = "SELECT receiverid, senderid, timestamp, type, content FROM messages WHERE receiverid = ? or senderid = ?" ;  
		
       try (Connection conn = DriverManager.getConnection(DB_URL);
    		   PreparedStatement pstmt = conn.prepareStatement(query)){
    	   pstmt.setBytes(1,receiverId); 
    	   pstmt.setBytes(2,receiverId); 
    	   ResultSet rs = pstmt.executeQuery();
    	   
    	   
    	   
              // loop through the result set
              while (rs.next()) { 
            	  byte[] receiverIdResult = rs.getBytes("receiverid") ;
            	  byte[] senderIdResult = rs.getBytes("senderid") ;
            	  Timestamp ts = Timestamp.valueOf(rs.getString("timestamp")) ; 
            	  byte[] content = rs.getBytes("content") ; 
            	  String type = rs.getString("type") ;
            	  
            	  var messageToAdd = new UserMessage(content,UserMessage.UserMessageType.valueOf(type),receiverIdResult, senderIdResult); 
            	  messageToAdd.setDate(ts);
            	  
            	  messages.add(messageToAdd);
              }
          } catch (SQLException e) {
              e.printStackTrace();
          } catch (IOException e) {
			e.printStackTrace();
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
		
	    // create a new table if not exist 
	    stmt.execute(deleteStmt);
	    
	    
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
	
	}

}
