package com.presenceservice.model;

import java.net.InetAddress;

/*
 * Represent a user of the application 
 * A user is linked to a local machine through the MAC address 
 */
final public class User{
	
	/*
	 * Identify the user 
	 * Taken from the MAC address of the local machine 
	 */
	 private UserId id;  
	 private InetAddress ipAddress;  
	 private String username;
	 
	 /*
	  * Maximum username length 
	  */
	 public static final int MAX_NAME_SIZE = 20;
	 
	 public User() {} 
	 
	 public User(UserId id, InetAddress ip, String uname) 
	 {
		 this.id = id;
		 this.ipAddress = ip;
		 this.username = uname;
	 }
	 
	 
	 @Override
	 public String toString()
	 {
		 return username ; 
	 }
	  

	public String getUsername() {
		return username;
	}

	/*
	 * @throws NullPointerException if username is null 
	 */
	public void setUsername(String username) {
		
		if(username == null)
			throw new NullPointerException("Username can't be null") ; 
		
		this.username = username;
	}


	public UserId getId() {
		return id;
	}

	/*
	 * @throws NullPointerException if id is null 
	 */
	public void setId(UserId id) {
		if(id == null)
			throw new NullPointerException("UserId can't be null") ; 
		
		this.id = id;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}
	 
	/*
	 * @throws NullPointerException if ipAddress is null 
	 */
	public void setIpAddress(InetAddress ipAddress) {
		if(ipAddress == null)
			throw new NullPointerException("ipAddress can't be null") ; 
		this.ipAddress = ipAddress;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof User))
			return false ; 
		
		User u = (User) obj ; 
		
		return this.id.equals(u.getId()) ;  
	}
	
	@Override 
	public int hashCode()
	{
		return id.hashCode() ; 
	}
	
}