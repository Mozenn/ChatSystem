package localSystem;

import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

import defo.Message;
import defo.User;
import session.LocalSession;

public class LocalSystem {
	
	ArrayList<User> localUsers;
	ArrayList<User> distantUsers;
	User user;
	InetAddress ip;
	int centralPort;
	ArrayList<LocalSession> sessions;
	
	public LocalSystem() 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
	}
	
	public void LoadUser() 
	{
		// TODO : load user from database using mac address as id
	}
	
	public void changeUname(String newName) 
	{
		// TODO : request name change
	}
	
	public void createSession(DatagramPacket packet) throws IOException 
	{
		byte[] c = Message.extractContent(packet.getData());
		sessions.add(new LocalSession(user, new User(c)));
	}

	
	
	


}
