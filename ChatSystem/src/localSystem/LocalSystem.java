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

import main.User;
import message.Message;
import message.SystemMessage;
import session.LocalSession;

final public class LocalSystem implements AutoCloseable{
	
	private ArrayList<User> localUsers;
	private ArrayList<User> distantUsers;
	private User user;
	
	private InetAddress centralSysIp;
	private int centralSysPort;
	
	private ArrayList<LocalSession> sessions;
	
	private LocalCommunicationListener listener ; 
	public static final int LISTENING_PORT = 8888; 
	public static final String MULTICAST_ADDR = "228.228.228.228"; // TODO use a non routable multicast address between 224.0.0.0 to 224.0.0.255 
	
	
	public LocalSystem() throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
	
		
		listener = new LocalCommunicationListener(this);
		
		notifyLocalUsers(); 
		
	}
	
	public LocalSystem(User u) throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		user = u ; 
		
		listener = new LocalCommunicationListener(this);
		
		notifyLocalUsers();
		
	}
	
	public void notifyLocalUsers() throws IOException 
	{
		new NotifyLocalUsersTask(this);
	}
	
	public void notifyConnectionResponse(DatagramPacket packet) throws IOException, ClassNotFoundException 
	{
		new NotifyConnectionResponseTask(this,packet);
	}
	
	public User getUser() 
	{
		synchronized(user)
		{
			return user;
		}

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
		
		synchronized(sessions)
		{
			sessions.add(new LocalSession(user, new User(c),packet.getPort())); // TODO check if getPort output the correct port 
		}
		
	}
	
	public void StartLocalSession(User receiver) throws IOException
	{
		LocalSession locSes = new LocalSession(user,receiver); 
		locSes.notifyStartSession(); // notify receiver system of session started 
		synchronized(sessions)
		{
			sessions.add(locSes); 
		}
		
	}
	
	public void addLocalUser(User u) 
	{
		synchronized(localUsers)
		{
			localUsers.add(u);
		}
		System.out.println(u.getUsername()); 
	}

	public void close() throws UnknownHostException, IOException
	{
		listener.stopRun();	
	}
	
	


}
