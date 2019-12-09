package localSystem;

import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

import defo.User;
import message.Message;
import session.LocalSession;

public class LocalSystem {
	
	private ArrayList<User> localUsers;
	private ArrayList<User> distantUsers;
	private User user;
	private InetAddress centralSysIp;
	private int centralSysPort;
	ArrayList<LocalSession> sessions;
	
	LocalCommunicationThread localCom ; 
	
	public LocalSystem() throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		localCom = new LocalCommunicationThread(this) ; 
		
	}
	
	public LocalSystem(User u) throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		user = u ; 
		
		localCom = new LocalCommunicationThread(this) ; 
		
	}
	
	public User getUser() 
	{
		return user;
	}
	
	public void LoadUser() 
	{
		// TODO : load user from database using mac address as id
	}
	
	public void changeUname(String newName) 
	{
		// TODO : request name change
	}
	
	protected void createSessionResponse(DatagramPacket packet) throws IOException 
	{
		byte[] c = Message.extractContent(packet.getData());
		sessions.add(new LocalSession(user, new User(c)));
	}
	
	public void createLocalSession(User receiver) throws IOException
	{
		LocalSession locSes = new LocalSession(user,receiver);  
	}
	
	public void addLocalUser(User u) 
	{
		localUsers.add(u);
		System.out.println(u.getUsername()); 
	}

	
	
	


}
