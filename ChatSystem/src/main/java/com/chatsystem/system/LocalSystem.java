package com.chatsystem.system;

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

import javax.swing.event.EventListenerList;

import com.chatsystem.message.UserMessage;
import com.chatsystem.model.SessionListener;
import com.chatsystem.model.SessionModel;
import com.chatsystem.model.SystemContract;
import com.chatsystem.model.SystemListener;
import com.chatsystem.session.LocalSession;
import com.chatsystem.session.Session;
import com.chatsystem.session.SessionData;
import com.chatsystem.system.NotifyLocalUsersTask.LocalNotifyType;
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
	
	private final EventListenerList listeners = new EventListenerList();
	
	private LocalCommunicationListener communicationListener ; 
	public static final int LISTENING_PORT = 8888; 
	public static final String MULTICAST_ADDR = "228.228.228.228"; // TODO use a non routable multicast address between 224.0.0.0 to 224.0.0.255 
	
	
	public LocalSystem() throws IOException 
	{
		localUsers = new HashMap<UserId,User>();
		distantUsers = new HashMap<UserId,User>();
		sessions = new HashMap<UserId,Session>() ;	
		
	}
	
	public void start()
	{
		System.out.println("LocalSystem Started") ; 
		
		try {
			communicationListener = new LocalCommunicationListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		notifyLocalUsers(); 
		
		RequestDistantUser();
	}
	
	public void close() 
	{
		
		new NotifyLocalUsersTask(this, LocalNotifyType.DISCONNECTION); 
		
		// TODO send Disconnection notify to central server
		
		communicationListener.stopRun();	
	}
	
	// ---------- COMMUNICATION 
	
	// CONNECTION 
	
	public void notifyLocalUsers() 
	{
		new NotifyLocalUsersTask(this,LocalNotifyType.CONNECTION);
	}
	
	public void notifyConnectionResponse(DatagramPacket packet) throws IOException, ClassNotFoundException 
	{
		new NotifyConnectionResponseTask(this,packet);
	}
	
	// SESSIONS 
	
	protected void createSessionResponse(DatagramPacket packet) throws IOException 
	{

		SessionData s = SerializationUtility.deserializeSessionData(SerializationUtility.deserializeSystemMessage(packet.getData()).getContent());
		LocalSession session = new LocalSession(user, s.getUser(), s.getPort(),this) ; 
		
		session.notifyStartSessionResponse(packet);
		
		synchronized(sessions)
		{
			sessions.put(s.getUser().getId(), session);
			fireSessionStarted(session); // notify view 
		}
		
	}
	
	@Override
	public boolean startLocalSession(User receiver) 
	{
		if(sessions.containsKey(receiver.getId()))
			return false ; 
		
		LocalSession locSes = null;
		try {
			locSes = new LocalSession(user,receiver,this);
		} catch (IOException e) {
			e.printStackTrace();
			return false ;
		} 
		
		locSes.notifyStartSession(); // notify receiver system of session started 
		synchronized(sessions)
		{
			sessions.put(receiver.getId(),locSes); 
			
			fireSessionStarted(locSes); // notify view 
		}
		
		return true ;
		
	}
	
	@Override
	public void closeSessionNotified(User receiver) 
	{
		synchronized(sessions)
		{
			fireSessionClosed(sessions.get(receiver.getId())) ; 
			sessions.get(receiver.getId()).notifyCloseSession() ; 
			sessions.remove(receiver.getId()) ; 
		}
	}
	
	public void closeSession(User receiver) 
	{
		synchronized(sessions)
		{
			fireSessionClosed(sessions.get(receiver.getId())) ; 
			sessions.remove(receiver.getId()) ; 
		}
	}
	
	@Override
	public void sendMessage(User receiver, String text) {
		
		synchronized(sessions)
		{
			sessions.get(receiver.getId()).sendMessage(text);
		}
	}

	@Override
	public void sendFileMessage(User receiver, File file) {
		
		synchronized(sessions)
		{
			sessions.get(receiver.getId()).sendMessage(file);
		}
	}
	
	// USERS 
	
	protected void addLocalUser(User u) 
	{
		synchronized(localUsers)
		{
			if(localUsers.containsKey(u.getId()))
				return ; 
			
			localUsers.put(u.getId(),u);
		}
		System.out.println("Local User added " + u.getUsername()); 
		
		fireuserConnection(u); // notify view 
	}
	
	protected void removeLocalUser(User u)
	{
		synchronized(localUsers)
		{
			localUsers.remove(u.getId());
		}
		System.out.println("Local User removed " + u.getUsername()); 
		
		fireuserDisconnection(u);
		
		synchronized(sessions)
		{
			if(sessions.containsKey(u.getId()))
			{
				fireSessionClosed(sessions.get(u.getId())); 
				
				sessions.remove(u.getId());
			}
		}
		

	}
	
	private void RequestDistantUser()
	{
		// TODO send request to central server as a runnable task 
		
		// if reception succeed, notify view 
	}
	
	// --------- LISTENERS 
	
	@Override
	public void addSystemListener(SystemListener sl) {
		
		listeners.add(SystemListener.class, sl);
	}

	@Override
	public void removeSystemListener(SystemListener sl) {
		listeners.remove(SystemListener.class, sl);
		
	}

	@Override
	public SystemListener[] getSystemListeners() {
		
		return listeners.getListeners(SystemListener.class); 
	}
	
	@Override
	public void clearSystemListeners()
	{
		for(var sl : getSystemListeners())
		{
			listeners.remove(SystemListener.class, sl);
		}
		
	}
	
	protected void fireSessionStarted(SessionModel sm)
	{
		for(SystemListener sl : getSystemListeners())
		{
			sl.sessionStarted(sm);
		}
	}
	
	protected void fireSessionClosed(SessionModel sm)
	{
		for(SystemListener sl : getSystemListeners())
		{
			sl.sessionClosed(sm);
		}
	}
	
	protected void fireuserConnection(User u)
	{
		for(SystemListener sl : getSystemListeners())
		{
			sl.userConnection(u);
		}
	}
	
	protected void fireuserDisconnection(User u)
	{
		for(SystemListener sl : getSystemListeners())
		{
			sl.userDisconnection(u);
		}
	}
	
	//---------- LOCAL USER 
	
	@Override
	public Optional<User> getUser() 
	{
		
		if(user == null)
		{
			try {
				LoadLocalUser();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}

		return Optional.ofNullable(user);

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
	
	@Override
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
		

		this.user = newUser ; 
		
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
	
	@Override
	public boolean changeUname(String newName) 
	{
		boolean res = false ; 
		
		// TODO 
		
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



