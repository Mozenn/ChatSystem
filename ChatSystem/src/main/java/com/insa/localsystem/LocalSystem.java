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

import com.insa.session.LocalSession;
import com.insa.user.User;
import com.insa.utility.NetworkUtility;
import com.insa.utility.SerializationUtility;

final public class LocalSystem implements AutoCloseable{
	
	private ArrayList<User> localUsers;
	private ArrayList<User> distantUsers;
	private User user;
	
	private InetAddress centralSysIp;
	private int centralSysPort;
	private final String LocalUserFilePath = "data/localuser" ; 
	private final String LocalUserDirectoryPath = "data/" ; 
	
	private ArrayList<LocalSession> sessions;
	
	private LocalCommunicationListener listener ; 
	public static final int LISTENING_PORT = 8888; 
	public static final String MULTICAST_ADDR = "228.228.228.228"; // TODO use a non routable multicast address between 224.0.0.0 to 224.0.0.255 
	
	
	public LocalSystem() throws IOException 
	{
		localUsers = new ArrayList<User>();
		distantUsers = new ArrayList<User>();
		sessions = new ArrayList<LocalSession>();	
		
		LoadLocalUser(); 
		
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
		
		byte[] ipAdd = NetworkUtility.getLocalIPAddress(); 
		
	    NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByAddress(ipAdd));
	    String id = ni.getHardwareAddress().toString();
		
		User newUser = new User(id,ipAdd, username) ; 
		
		this.user = newUser ; 
		
		byte[] userAsBytes = SerializationUtility.serializeUser(newUser); 
		
		File f = new File(LocalUserDirectoryPath) ; 
		
		f.mkdir() ; 
		
		
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
	
	public void changeUname(String newName) throws IOException 
	{
		// TODO : request name change
		
		
		saveLocalUser(); 
	}
	
	protected void createSessionResponse(DatagramPacket packet) throws IOException 
	{
		// Deserialization 
		User u = SerializationUtility.deserializeUser(SerializationUtility.deserializeMessage(packet.getData()).getContent());
		
		synchronized(sessions)
		{
			sessions.add(new LocalSession(user, u,packet.getPort())); // TODO check if getPort output the correct port 
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
