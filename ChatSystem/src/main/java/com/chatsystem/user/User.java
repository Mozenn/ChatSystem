package com.chatsystem.user;

import java.net.InetAddress;

final public class User{
	
	 private UserId id;  
	 private InetAddress ipAddress;  
	 private String username;
	 
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


	public void setUsername(String username) {
		this.username = username;
	}


	public UserId getId() {
		return id;
	}


	public void setId(UserId id) {
		this.id = id;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}
	 
	public void setIpAddress(InetAddress ipAddress) {
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
