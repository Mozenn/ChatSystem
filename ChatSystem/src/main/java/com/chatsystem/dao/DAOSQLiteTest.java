package com.chatsystem.dao;

import java.io.IOException;
import java.net.InetAddress;
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

import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;

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
	        pstmt.setBytes(1, message.getReceiverId().getId());
	        pstmt.setBytes(2, message.getSenderId().getId());
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
	
	public void addUser(User u)
	{
		String createStmt = "CREATE TABLE IF NOT EXISTS users (\n"
                + "    userid BLOB NOT NULL,\n"
                + "    inetaddress BLOB NOT NULL,\n"
                + "    username text NOT NULL,\n"
                + ");";  
		
		String insertStmt = "INSERT INTO users(userid,inetaddress,username) VALUES(?,?,?)" ; 
	
	try (Connection conn = DriverManager.getConnection(DB_URL);
	        Statement stmt = conn.createStatement()) {
		
	    // create a new table if not exist 
	    stmt.execute(createStmt);
	    
	    try(PreparedStatement pstmt = conn.prepareStatement(insertStmt))
	    {
	        pstmt.setBytes(1, u.getId().getId());
	        pstmt.setBytes(2, u.getIpAddress().getAddress());
	        pstmt.setString(3, u.getUsername());
	        pstmt.executeUpdate();
	    }
	    
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
	}
	
	public void removeUser(User u)
	{
		String deleteStmt = "DELETE FROM users where userid = ?" ;
		 
	    try(Connection conn = DriverManager.getConnection(DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(deleteStmt))
	    {
	        pstmt.setBytes(1, u.getId().getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
			e.printStackTrace();
			return ; 
		}
	}
	
	public void updateUser(User u)
	{
		String updateStmt = "UPDATE users SET inetaddress = ?, username = ? WHERE userid = ?" ;
		 
	    try(Connection conn = DriverManager.getConnection(DB_URL);
	    		PreparedStatement pstmt = conn.prepareStatement(updateStmt))
	    {
	        pstmt.setBytes(1, u.getIpAddress().getAddress());
	        pstmt.setString(2, u.getUsername());
	        pstmt.setBytes(3, u.getId().getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
			e.printStackTrace();
			return ; 
		}
	}
	
	
	public void clearUser() {
		
		
		String deleteStmt = "DELETE FROM users" ; 
	
	try (Connection conn = DriverManager.getConnection(DB_URL);
	        Statement stmt = conn.createStatement()) {
		
	    stmt.execute(deleteStmt);
	    
	    
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
	
	}
	
	public User getUser(UserId id) {
		
		// query all messages 
		
		String query = "SELECT inetaddress, username FROM users WHERE userid = ?" ;  
		
		User userToAdd = null ; 
		
       try (Connection conn = DriverManager.getConnection(DB_URL);
    		   PreparedStatement pstmt = conn.prepareStatement(query)){
    	   pstmt.setBytes(1,id.getId());  
    	   ResultSet rs = pstmt.executeQuery();
    	   
    	   
           // loop through the result set
           if (rs.next()) { 
         	  InetAddress inetAddressResult = InetAddress.getByAddress(rs.getBytes("inetaddress")) ;
         	  String usernamedResult = rs.getString("username") ;
            	  
        	  userToAdd = new User(id,inetAddressResult,usernamedResult); 
              }
          } catch (SQLException e) {
              e.printStackTrace();
          } catch (IOException e) {
			e.printStackTrace();
		}
       
       
       return userToAdd ; 
	}

}
