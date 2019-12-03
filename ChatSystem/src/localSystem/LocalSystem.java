package localSystem;

import java.net.InetAddress;
import java.net.DatagramSocket;

import defo.User;

public class LocalSystem {
	
	User[] localUsers;
	User[] distantUsers;
	User user;
	InetAddress ip;
	int centralPort;
	
	public LocalSystem() 
	{
		// TODO : create localCommunication Thread and distant communication Thread
	}
	
	public void LoadUser() 
	{
		// TODO : load user from database using mac address as id
	}
	
	public void changeUname(String newName) 
	{
		// TODO : request name change
	}
	
	public void createSession(DatagramSocket socket) 
	{
		
	}
	
	
	


}
