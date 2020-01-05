package com.chatsystem.localsystem;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.chatsystem.model.SystemContract;
import com.chatsystem.session.LocalSession;
import com.chatsystem.session.Session;
import com.chatsystem.session.SessionData;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

final public class LocalSystem implements AutoCloseable , SystemContract{
	
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
	
	/*
	public LocalSystem(User u) throws IOException 
	{
		localUsers = new HashMap<UserId,User>();
		distantUsers = new HashMap<UserId,User>();
		sessions = new HashMap<UserId,Session>() ;	
		
		user = u ; 
		
		listener = new LocalCommunicationListener(this);
		
		notifyLocalUsers();
		
		RequestDistantUser(); 
		
	}*/
	
	public void close() throws UnknownHostException, IOException
	{
		listener.stopRun();	
	}
	
	// ---------- COMMUNICATION 
	
	// CONNECTION 
	
	public void notifyLocalUsers() throws IOException 
	{
		new NotifyLocalUsersTask(this);
	}
	
	public void notifyConnectionResponse(DatagramPacket packet) throws IOException, ClassNotFoundException 
	{
		new NotifyConnectionResponseTask(this,packet);
	}
	
	// SESSIONS 
	
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
	
	public boolean startLocalSession(User receiver) 
	{
		LocalSession locSes = null;
		try {
			locSes = new LocalSession(user,receiver);
		} catch (IOException e) {
			e.printStackTrace();
			return false ;
		} 
		
		locSes.notifyStartSession(); // notify receiver system of session started 
		synchronized(sessions)
		{
			sessions.put(receiver.getId(),locSes); // TODO notify View ? 
		}
		
		return true ;
		
	}
	
	
	public void closeSession(User receiver) 
	{
		
	}
	
	// USERS 
	
	public void addLocalUser(User u) 
	{
		synchronized(localUsers)
		{
			localUsers.put(u.getId(),u);
		}
		System.out.println("Local User added " + u.getUsername()); 
		
		// TODO notify View  
	}
	
	public void RequestDistantUser()
	{
		// TODO send request to central system as a runnable task 
		
		// if reception succeed, notify view 
	}
	
	//---------- LOCAL USER 
	
	public Optional<User> getUser() 
	{
		
		synchronized(user)
		{
			if(user == null)
			{
				try {
					LoadLocalUser();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}

			return Optional.of(user);
			
		}

	}
	
	private boolean LoadLocalUser() throws IOException 
	{
		boolean result = false ; 
		
		File localUserFile = new File(LocalUserFilePath) ; 
		
		if(localUserFile.exists())
		{
			FileInputStream in = null ; 
			
		      try {
		    	  in = new FileInputStream(LocalUserFilePath);
		          
		    	  byte[] userAsBytes = new byte[(int) localUserFile.length()] ; 
		    			  
		    	  in.read(userAsBytes) ; 
		    	  
		    	  this.user = SerializationUtility.deserializeUser(userAsBytes) ; 
		    	  
		    	  result = true ; 

		       }
		      catch(Exception e)
		      {
		    	  result = false ; 
		      }
		      finally {
		    	   
		          if (in != null) {
		        	  in.close();
		          }
		       }
		      
		      
		}
	
		return result ; 
		
		
	}
	
	public Optional<User> createLocalUser(String username) 
	{
		
		InetAddress ipAdd;
		try {
			ipAdd = NetworkUtility.getLocalIPAddress();
		} catch (IOException e1) {
			e1.printStackTrace();
			return Optional.of(user); 
		} 
		
	    NetworkInterface ni;
	    byte[] id ; 
	    
		try {
			ni = NetworkInterface.getByInetAddress(ipAdd);
			id = ni.getHardwareAddress();
		} catch (SocketException e1) {
			e1.printStackTrace();
			return Optional.of(user); 
		}
	    
	    
		User newUser = new User(new UserId(id),ipAdd, username) ; 
		
		synchronized(user)
		{
			this.user = newUser ; 
		}
		
		File f = new File(LocalUserDirectoryPath) ; 
		
		f.mkdir() ; 
		
		try {
			saveLocalUser();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return Optional.of(user); 
	}
	
	private void saveLocalUser() throws IOException 
	{
		byte[] userAsBytes;
		try {
			userAsBytes = SerializationUtility.serializeUser(this.user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ; 
		} 
		
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
	
	public boolean changeUname(String newName) 
	{
		boolean res = false ; 
		
		// communicate to central system to check availability 
		
		// if not available 
			// notify view 
		
		// if available 
			// update localUser 
			// multicast notify change to all local users 
			// notify view 
		
		
		try {
			saveLocalUser();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return res ;
	}


}
