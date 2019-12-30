package com.insa.user;


final public class User{
	
	 private byte[] id;
	 private byte[] ipAddress;
	 private String username;
	 
	 public static final int MAX_NAME_SIZE = 20;
	 
	 public User() {} 
	 
	 public User(byte[] id, byte[] ip, String uname) 
	 {
		 this.id = id;
		 this.ipAddress = ip;
		 this.username = uname;
	 }
	 
	 /*
	 @Override
	 public String toString()
	 {
		 return username ; 
	 }
*/

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public byte[] getId() {
		return id;
	}


	public void setId(byte[] id) {
		this.id = id;
	}

	public byte[] getIpAddress() {
		return ipAddress;
	}
	 
	public void setIpAddress(byte[] ipAddress) {
		this.ipAddress = ipAddress;
	}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
