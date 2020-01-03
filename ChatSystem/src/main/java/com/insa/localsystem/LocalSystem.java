package com.insa.localsystem;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;

import com.insa.session.LocalSession;
import com.insa.session.Session;
import com.insa.session.SessionData;
import com.insa.user.User;
import com.insa.user.UserId;
import com.insa.utility.NetworkUtility;
import com.insa.utility.SerializationUtility;

final public class LocalSystem implements AutoCloseable{
	
	private HashMap<UserId,User> localUsers;
	private HashMap<UserId,User> distantUsers;
	private User user;
	
	private InetAddress centralSysIp;
	private int centralSysPort;
	private final String LocalUserFilePath = "data/localuser" ; 
	private final String LocalUserDirectoryPath = "data/" ; 
	
	private HashMap<UserId,Session> sessions;
	
	private LocalCommunicationListener listener ; 
	public static final int LISTENING_PORT = 8888; 
	public static final String MULTICAST_ADDR = "228.228.228.228"; // TODO use a non routable multicast address between 224.0.0.0 to 224.0.0.255 
	
	
	public LocalSystem() throws IOException 
	{
		localUsers = new HashMap<UserId,User>();
		distantUsers = new HashMap<UserId,User>();
		sessions = new HashMap<UserId,Session>() ;	
		
		System.out.println("LocalSystem Started") ; 
		
		LoadLocalUser(); 
		
		listener = new LocalCommunicationListener(this);
		
		notifyLocalUsers(); 
		
		RequestDistantUser();
		
	}
	
	public LocalSystem(User u) throws IOException 
	{
		localUsers = new HashMap<UserId,User>();
		distantUsers = new HashMap<UserId,User>();
		sessions = new HashMap<UserId,Session>() ;	
		
		user = u ; 
		
		listener = new LocalCommunicationListener(this);
		
		notifyLocalUsers();
		
		RequestDistantUser(); 
		
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
	
	public void LoadLocalUser() throws IOException 
	{
		File localUserFile = new File(LocalUserFilePath) ; 
		
		if(localUserFile.exists())
		{
			FileInputStream in = null ; 
			
		      try {
		    	  in = new FileInputStream(LocalUserFilePath);
		          
		    	  byte[] userAsBytes = new byte[(int) localUserFile.length()] ; 
		    			  
		    	  in.read(userAsBytes) ; 
		    	  
		    	  this.user = SerializationUtility.deserializeUser(userAsBytes) ; 

		       }
		      finally {
		    	   
		          if (in != null) {
		        	  in.close();
		          }
		       }
		}
		else 
		{
			createLocalUser(); 
		}
	}
	
	private void createLocalUser() throws IOException
	{
		String username = "hey" ; // TODO ask username through UI 
		
		InetAddress ipAdd = NetworkUtility.getLocalIPAddress(); 
		
	    NetworkInterface ni = NetworkInterface.getByInetAddress(ipAdd);
	    byte[] id = ni.getHardwareAddress();
	    
		User newUser = new User(new UserId(id),ipAdd, username) ; 
		
		this.user = newUser ; 
		
		File f = new File(LocalUserDirectoryPath) ; 
		
		f.mkdir() ; 
		
		saveLocalUser(); 
	}
	
	private void saveLocalUser() throws IOException
	{
		byte[] userAsBytes = SerializationUtility.serializeUser(this.user); 
		
		// Write user to file 
		
		FileOutputStream out = null;
		
	      try {
	          out = new FileOutputStream(LocalUserFilePath);
	          
             out.write(userAsBytes);

	       }
	      finally {
	    	   
	          if (out != null) {
	             out.close();
	          }
	       }
	}
	
	public void RequestDistantUser()
	{
		// TODO send request to central system as a runnable task 
	}
	
	public void changeUname(String newName) throws IOException 
	{
		// TODO : request name change
		
		// communicate to central system to check availability 
		
		// if not available 
			// notify UI 
		
		// if available 
			// update localUser 
			// multicast notify change to all local users 
		
		
		saveLocalUser(); 
	}
	
	protected void createSessionResponse(DatagramPacket packet) throws IOException 
	{

		SessionData s = SerializationUtility.deserializeSessionData(SerializationUtility.deserializeSystemMessage(packet.getData()).getContent());
		LocalSession session = new LocalSession(user, s.getUser(), s.getPort()) ; 
		
		synchronized(sessions)
		{
			sessions.put(s.getUser().getId(), session);
			session.notifyStartSessionResponse(packet);
		}
		
	}
	
	public void StartLocalSession(User receiver) throws IOException
	{
		LocalSession locSes = new LocalSession(user,receiver); 
		locSes.notifyStartSession(); // notify receiver system of session started 
		synchronized(sessions)
		{
			sessions.put(receiver.getId(),locSes); 
		}
		
	}
	
	public void addLocalUser(User u) 
	{
		synchronized(localUsers)
		{
			localUsers.put(u.getId(),u);
		}
		System.out.println("Local User added " + u.getUsername()); 
		
		// TODO notify GUI 
	}

	public void close() throws UnknownHostException, IOException
	{
		listener.stopRun();	
	}
	
	


}
