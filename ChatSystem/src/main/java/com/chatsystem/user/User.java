package com.chatsystem.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * Represent a user of the application 
 * A user is linked to a local machine through the MAC address 
 */
public class User{
	
	/*
	 * Identify the user 
	 * Taken from the MAC address of the local machine 
	 */
	 private UserId id;  
	 private InetAddress ipAddress;  
	 private String username;
	 private int localPort ; 
	 private int distantPort ; 
	 
	 /*
	  * Maximum username length 
	  */
	 public static final int MAX_NAME_SIZE = 20;
	 
	 public User() throws UnknownHostException 
	 {
		 this.id = new UserId("default".getBytes());
		 this.ipAddress = InetAddress.getLocalHost();
		 this.username = "default";
		 this.localPort = 8888 ; 
		 this.distantPort = 9999 ; 
	 } 
	 
	 /*
	  * @throws NullPointerException if id, ip or uname is null 
	  */
	 public User(UserId id, InetAddress ip, String uname) 
	 {
		 if(id == null || ip == null || uname == null)
			 throw new NullPointerException() ; 
		 
		 this.id = id;
		 this.ipAddress = ip;
		 this.username = uname;
		 this.localPort = 8888 ; 
		 this.distantPort = 9999 ; 
	 }
	 
	 /*
	  * @throws NullPointerException if id, ip or uname is null 
	  * @throws IllegalArgumentException if localPort or distantPort is not in range 1024-65335 
	  */
	 public User(UserId id, InetAddress ip, String uname, int localPort, int distantPort) 
	 {
		 if(id == null || ip == null || uname == null)
			 throw new NullPointerException() ; 
		 
		 if(localPort < 1024 || localPort > 65335 || distantPort < 1024 || distantPort > 65335)
			 throw new IllegalArgumentException("a port must be in range 1024-65335") ; 
					
		 
		 this.id = id;
		 this.ipAddress = ip;
		 this.username = uname;
		 this.distantPort = distantPort ; 
		 this.localPort = localPort ; 
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
			 throw new NullPointerException() ; 
		
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
			 throw new NullPointerException() ; 
		
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
			 throw new NullPointerException() ; 
		
		this.ipAddress = ipAddress;
	}
	
	public int getLocalPort() {
		return localPort;
	}


	public void setLocalPort(int localPort) {
		
		this.localPort = localPort;
	}

	public int getDistantPort() {
		return distantPort;
	}

	public void setDistantPort(int distantPort) {
		
		
		this.distantPort = distantPort;
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
