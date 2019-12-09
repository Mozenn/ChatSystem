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
	
	public LocalSystem() 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		localCom = new LocalCommunicationThread(this) ; 
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
