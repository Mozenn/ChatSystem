package com.presenceservice.dao;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.presenceservice.model.User;
import com.presenceservice.model.UserId;
import com.presenceservice.utility.ConfigUtility;

public class UserDAOSQLite implements UserDAO {
	
	private static String DB_URL;

	public UserDAOSQLite() throws IOException 
	{
		setupDatabase() ; 
	
		createUsersTable()  ; 
	}
	 
	protected void setupDatabase() throws IOException
	{
		Properties configProps = ConfigUtility.getConfigProperties() ; 
		
		DB_URL = configProps.getProperty("dbStartURL") + ConfigUtility.getConfigPath() + configProps.getProperty("dbName") ; 
		
		try {
			String driver = ConfigUtility.getConfigProperties().getProperty("driverClassName") ; 
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DAOConfigException("Driver not found", e) ; 
		} 
	}
	
	private void createUsersTable() 
	{
		String createStmt = "CREATE TABLE IF NOT EXISTS users (\n"
                + "    userid BLOB NOT NULL,\n"
                + "    inetaddress BLOB NOT NULL,\n"
                + "    username text NOT NULL\n"
                + ");";  
		
		try (Connection conn = DriverManager.getConnection(DB_URL );
		        Statement stmt = conn.createStatement()) {
			
		    // create a new table if not exist 
		    stmt.execute(createStmt);
		    
		} catch (SQLException e) {
		   throw new DAOException("User table creation failed", e) ; 
		}
	}
	
	public void addUser(User u)
	{
		
		String insertStmt = "INSERT INTO users(userid,inetaddress,username) VALUES(?,?,?)" ; 

	    
	    try(Connection conn = DriverManager.getConnection(DB_URL ); 
	    		PreparedStatement pstmt = conn.prepareStatement(insertStmt))
	    {
	        pstmt.setBytes(1, u.getId().getId());
	        pstmt.setBytes(2, u.getIpAddress().getAddress());
	        pstmt.setString(3, u.getUsername());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	 throw new DAOException("User insertion failed", e) ; 
		}

	}
	
	public void removeUser(User u)
	{
		String deleteStmt = "DELETE FROM users where userid = ?" ;
		 
	    try(Connection conn = DriverManager.getConnection(DB_URL );
	    		PreparedStatement pstmt = conn.prepareStatement(deleteStmt))
	    {
	        pstmt.setBytes(1, u.getId().getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	 throw new DAOException("User removal failed", e) ; 
		}
	}
	
	public void updateUser(User u)
	{
		String updateStmt = "UPDATE users SET inetaddress = ?, username = ? WHERE userid = ?" ;
		 
	    try(Connection conn = DriverManager.getConnection(DB_URL );
	    		PreparedStatement pstmt = conn.prepareStatement(updateStmt))
	    {
	        pstmt.setBytes(1, u.getIpAddress().getAddress());
	        pstmt.setString(2, u.getUsername());
	        pstmt.setBytes(3, u.getId().getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	 throw new DAOException("User update failed", e) ; 
		}
	}
	
	
	public void clearUser() {
		
		
		String deleteStmt = "DELETE FROM users" ; 
	
	try (Connection conn = DriverManager.getConnection(DB_URL );
	        Statement stmt = conn.createStatement()) {
		
	    stmt.execute(deleteStmt);
	    
	    
	} catch (SQLException e) {
		 throw new DAOException("User table clear failed", e) ; 
	}
	
	}
	
	@Override
	public Optional<User> getUser(UserId id) {
		
		String query = "SELECT inetaddress, username FROM users WHERE userid = ?" ;  
		
		Optional<User> userToAdd = null ; 
		
       try (Connection conn = DriverManager.getConnection(DB_URL );
    		   PreparedStatement pstmt = conn.prepareStatement(query)){
    	   pstmt.setBytes(1,id.getId());  
    	   ResultSet rs = pstmt.executeQuery();
    	   
    	   
           // loop through the result set
           if (rs.next()) { 
         	  InetAddress inetAddressResult = InetAddress.getByAddress(rs.getBytes("inetaddress")) ;
         	  String usernamedResult = rs.getString("username") ;
            	  
        	  User u = new User(id,inetAddressResult,usernamedResult); 
        	  userToAdd = Optional.ofNullable(u) ; 
              }
          } catch (SQLException e) {
        	  throw new DAOException("Selection failed", e) ; 
          } catch (IOException e) {
        	  throw new DAOException("invalid InetAddress", e) ; 
		}
       
       
       return userToAdd ; 
	}

	@Override
	public List<User> getAllUsers() {
		
		String query = "SELECT userid, inetaddress, username FROM users" ; 
		
		ArrayList<User> res = new ArrayList<User>() ; 
		
       try (Connection conn = DriverManager.getConnection(DB_URL );
    		   Statement stmt = conn.createStatement()){

    	   ResultSet rs = stmt.executeQuery(query);
    	   
    	   
           if (rs.next()) { 
        	   UserId uId = new UserId(rs.getBytes("userid")) ; 
         	  InetAddress inetAddressResult = InetAddress.getByAddress(rs.getBytes("inetaddress")) ;
         	  String usernamedResult = rs.getString("username") ;
            	  
        	  res.add(new User(uId,inetAddressResult,usernamedResult)) ;
              }
           
          } catch (SQLException e) {
        	  throw new DAOException("Selection failed", e) ; 
          } catch (IOException e) {
        	  throw new DAOException("invalid InetAddress", e) ; 
		}
       
       
       return res ; 
	}

	@Override
	public boolean isUsernameAvailable(String username) {
		
		String query = "SELECT * FROM users WHERE username = ?" ;  
		
       try (Connection conn = DriverManager.getConnection(DB_URL );
    		   PreparedStatement pstmt = conn.prepareStatement(query)){
    	   pstmt.setString(1,username);  
    	   ResultSet rs = pstmt.executeQuery();
    	   
           if (rs.next()) { 
        	   return false ; 
           }
           else
           {
        	   return true ; 
           }
       } catch (SQLException e) {
    	   throw new DAOException("Selection failed", e) ; 
	}

	}


}
