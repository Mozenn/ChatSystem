package main;

public class User {
	
	 byte[] id;
	 byte[] ipAddress;
	 String username;
	 
	 public User(byte[] id, byte[] ip, String uname) 
	 {
		 this.id = id;
		 ipAddress = ip;
		 username = uname;
	 }
}
