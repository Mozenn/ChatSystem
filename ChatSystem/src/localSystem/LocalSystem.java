package localSystem;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;

import defo.User;
import message.Message;
import message.SystemMessage;
import session.LocalSession;

public class LocalSystem implements AutoCloseable{
	
	private ArrayList<User> localUsers;
	private ArrayList<User> distantUsers;
	private User user;
	
	private InetAddress centralSysIp;
	private int centralSysPort;
	
	ArrayList<LocalSession> sessions;
	
	LocalCommunicationListener listener ; 
	public static final int LISTENING_PORT = 8888; 
	public static final String MULTICAST_ADDR = "228.228.228.228";
	
	
	public LocalSystem() throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		multicastSocket = new MulticastSocket(LISTENING_PORT);
		multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_ADDR));
		
		listener = new LocalCommunicationListener(this,MULTICAST_ADDR);
		
		notifyLocalUsers(); 
		
	}
	
	public LocalSystem(User u) throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		user = u ; 
		
		// TODO start listener 
		
	}
	
	public void notifyLocalUsers() throws IOException 
	{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(system.getUser());
		oo.close();

		byte[] serializedUser = bStream.toByteArray();
		
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		InetAddress addr = InetAddress.getByName(multicastAddress);
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));
	}
	
	public void notifyConnectionResponse(DatagramPacket packet) throws IOException 
	{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(system.getUser());
		oo.close();

		byte[] serializedUser = bStream.toByteArray();
		
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.CR, serializedUser);
		InetAddress addr = packet.getAddress();
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));
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
	
	public void StartLocalSession(User receiver) throws IOException
	{
		LocalSession locSes = new LocalSession(user,receiver);  
		sessions.add(locSes); 
	}
	
	public void addLocalUser(User u) 
	{
		localUsers.add(u);
		System.out.println(u.getUsername()); 
	}

	public void close() throws UnknownHostException, IOException
	{
		listener.stopRun();
		multicastSocket.leaveGroup(InetAddress.getByName(multicastAddress));
		multicastSocket.close();	
	}
	
	


}
