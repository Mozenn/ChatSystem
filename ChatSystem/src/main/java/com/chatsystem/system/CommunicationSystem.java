package com.chatsystem.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.time.Duration;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.swing.event.EventListenerList;

import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SessionModel;
import com.chatsystem.model.SystemContract;
import com.chatsystem.model.SystemListener;
import com.chatsystem.session.Session;
import com.chatsystem.session.SessionData;
import com.chatsystem.session.distant.DistantSession;
import com.chatsystem.session.local.LocalSession;
import com.chatsystem.system.NotifyLocalUsersTask.LocalNotifyType;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.PropertiesUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

final public class CommunicationSystem implements AutoCloseable , SystemContract{
	
	private HashMap<UserId,User> localUsers;
	private HashMap<UserId,User> distantUsers;
	private User user;
	private boolean bRunning = false ; 
	
	private String downloadPath  ; 
	
	private HashMap<UserId,Session> sessions;
	
	private final EventListenerList listeners = new EventListenerList();
	
	private LocalCommunicationListener localCommunicationListener ; 
	public static final int LOCAL_LISTENING_PORT = 8888; 
	public static final String MULTICAST_ADDR = "228.228.228.228"; // TODO use a non routable multicast address between 224.0.0.0 to 224.0.0.255 
	
	private DistantCommunicationListener distantCommunicationListener; 
	public static final int DISTANT_LISTENING_PORT = 8888; 
	
	public final String PRESENCESERVICE_URL ; 
	private DistantUsersFetcher distantUsersFetcher ; 
	
	public CommunicationSystem() throws IOException 
	{
		localUsers = new HashMap<UserId,User>();
		distantUsers = new HashMap<UserId,User>();
		sessions = new HashMap<UserId,Session>() ;	
		
		Properties properties = PropertiesUtility.getAppProperties() ; 
		
		PRESENCESERVICE_URL = properties.getProperty("presenceServiceURL") ; 
		downloadPath = properties.getProperty("downloadPath") ; 
		if(downloadPath.equals("null"))
			downloadPath = System.getProperty("java.io.tmpdir") ; 
	}
	
	public void start()
	{
		System.out.println("CommunicationSystem Started") ; 
		
		try {
			localCommunicationListener = new LocalCommunicationListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			distantCommunicationListener = new DistantCommunicationListener(this) ;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		notifyLocalUsers(); 
		
		notifyStartWebService();
		
		distantUsersFetcher = new DistantUsersFetcher(this) ; 
		
		bRunning = true ; 
	}
	
	@Override
	public boolean hasStarted() {
		
		return bRunning;
	}
	
	public void close() 
	{
		bRunning = false  ; 
		
		var t = new NotifyLocalUsersTask(this, LocalNotifyType.DISCONNECTION); 
		
		notifyCloseWebService() ; 
		
		try {
			t.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} ; 
		
		localCommunicationListener.stopRun();	
		distantCommunicationListener.stopRun();
		distantUsersFetcher.stopRun() ; 
	}
	
	// ===================  COMMUNICATION ==============================
	
	// CONNECTION 
	
	public void notifyLocalUsers() 
	{
		new NotifyLocalUsersTask(this,LocalNotifyType.CONNECTION);
	}
	
	public void notifyConnectionResponse(DatagramPacket packet) throws IOException, ClassNotFoundException 
	{
		new NotifyConnectionResponseTask(this,packet);
	}
	
	/// ===================  SESSIONS ==============================
	
	protected void onLocalSessionRequest(DatagramPacket packet) throws IOException 
	{

		SessionData s = SerializationUtility.deserializeSessionData(SerializationUtility.deserializeSystemMessage(packet.getData()).getContent());
		LocalSession session = new LocalSession(user, s.getUser(), s.getPort(),this) ; 
		
		session.StartSessionResponse(packet.getAddress(),packet.getPort());
		
		addSession(session) ; 
	}
	
	protected void onDistantSessionRequest(User user, Socket clientSocket) throws IOException
	{
		DistantSession session = new DistantSession(this.user,user,clientSocket,this) ; 
		
		addSession(session) ; 
	}
	
	protected void addSession(Session session) throws IOException
	{
		
		synchronized(sessions)
		{
			sessions.put(session.getReceiver().getId(), session);
			fireSessionStarted(session); // notify view 
		}
	}
	
	@Override
	public boolean startSession(User receiver) 
	{
		if(sessions.containsKey(receiver.getId()))
			return false ; 
		
		if(localUsers.containsKey(receiver.getId()))
		{
			return startLocalSession(receiver) ; 
		}
		else if(distantUsers.containsKey(receiver.getId()))
		{
			return startDistantSession(receiver) ; 
		}
		
		return false ; 
		
	}
	
	
	private boolean startLocalSession(User receiver) 
	{
		
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
	
	private boolean startDistantSession(User receiver) 
	{
		
		DistantSession distSes = null;

		try {
			distSes = new DistantSession(user,receiver,this);
		} catch (IOException e) {
			e.printStackTrace();
			return false ; 
		}

		
		distSes.notifyStartSession(); // notify receiver distant listener of session started 
		synchronized(sessions)
		{
			sessions.put(receiver.getId(),distSes); 
			
			fireSessionStarted(distSes); // notify view 
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
	
	@Override
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
	public void sendFileMessage(User receiver, String filePath) {
		
		File file = new File(filePath) ; 
		System.out.println(filePath) ; 

		if(file.exists() && file.isFile())
		{
			FileWrapper fileWrapper;
			try {
				fileWrapper = new FileWrapper(file.getName(),file);
			} catch (IOException e) {
				e.printStackTrace();
				return ; 
			} 
			
			synchronized(sessions)
			{ 
				sessions.get(receiver.getId()).sendMessage(fileWrapper);
			}
		}
		

	}
	
	
	// ===================  DOWNLOAD ==============================
	
	@Override
	public void downloadFile(UserId senderId, Timestamp date) 
	{
		Optional<UserMessage> m ; 
		synchronized(sessions)
		{
			m = sessions.get(senderId).getMessage(date) ; 
		}
		
		if(m.isPresent())
		{
			FileWrapper fw = null ; 
			try {
				fw = SerializationUtility.deserializeFileWrapper(m.get().getContent()) ;
			} catch (JsonParseException e) {
				e.printStackTrace();
				return ; 
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return ; 
			} catch (IOException e) {
				e.printStackTrace();
				return ; 
			} 
			
			File downloadedFile = new File(downloadPath+"/" + fw.getFileName()) ; 
			
			try {
				downloadedFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return ; 
			} 
			
			try (FileOutputStream stream = new FileOutputStream(downloadedFile)) {
			    stream.write(fw.getFileContent());
			} catch (IOException e) {
				e.printStackTrace();
				return ; 
			}
		}
		
	}
	
	@Override
	public void changeDownloadPath(String newPath)
	{
		this.downloadPath = newPath ; 
		
		Properties properties ; 
		
		try {
			properties = PropertiesUtility.getAppProperties() ;
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		} 
		
		properties.setProperty("downloadPath", newPath) ; 
		
		try {
			PropertiesUtility.saveAppProperties(properties) ;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println("CommunicationSystem DownloadPath Changed") ; 
		
	}
	
	@Override 
	public String getDownloadPath()
	{
		return this.downloadPath ; 
	}
	
	// ===================  USERS ==============================
	
	protected void addLocalUser(User u) 
	{
		synchronized(localUsers)
		{
			
			if( u.getId().equals(this.user.getId()))
				return ; 
			
			if(localUsers.containsKey(u.getId()) && localUsers.get(u.getId()).getUsername().equals(u.getUsername()))
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
	
	protected void addDistantUser(User u) 
	{
		synchronized(distantUsers)
		{
			if( u.getId().equals(this.user.getId()))
				return ; 
			
			if(distantUsers.containsKey(u.getId()) && distantUsers.get(u.getId()).getUsername().equals(u.getUsername())) // if user already contained && username has not changed 
				return ; 
			
			distantUsers.put(u.getId(),u);
		}
		System.out.println("Distant User added " + u.getUsername()); 
		
		fireuserConnection(u); // notify view 
	}
	
	protected void removeDistantUser(User u)
	{
		synchronized(distantUsers)
		{
			distantUsers.remove(u.getId());
		}
		System.out.println("Distant User removed " + u.getUsername()); 
		
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
	
	private void notifyStartWebService()
	{
		
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		String userAsString;
		try {
			userAsString = new String(SerializationUtility.serializeUser(this.user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ; 
		}  
		
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userAsString))
                .uri(URI.create(PRESENCESERVICE_URL))
                .header("Content-Type", "application/json")
                .setHeader("COMMUNICATION", "CO")
                .build();
        
        try {
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			
			if(response.statusCode() == 200)
			{
				List<User> users = SerializationUtility.deserializeUsers(response.body().getBytes()) ; 
				
				for(User u : users)
				{
					addDistantUser(u);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
	}
	
	private void notifyCloseWebService()
	{
		
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		String userAsString;
		try {
			userAsString = new String(SerializationUtility.serializeUser(this.user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ; 
		}  
		
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userAsString))
                .uri(URI.create(PRESENCESERVICE_URL))
                .timeout(Duration.ofSeconds(2)) 
                .header("Content-Type", "application/json")
                .setHeader("COMMUNICATION", "DC")
                .build();
        
        try {
			httpClient.send(request, BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        
	}

	
	// ===================  LISTENER ==============================
	
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
	
	// ===================  LOCAL USER ==============================
	
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
		
		Properties props = PropertiesUtility.getAppProperties() ; 
		
		String userAsJsonString = props.getProperty("localUser")  ;
		
		if(!userAsJsonString.equals("null"))
		{	 
			
		    this.user = SerializationUtility.deserializeUser(userAsJsonString.getBytes()) ; 
		    	 
		    result = true ; 
		}
		      
		return result ; 
		
	}
	
	@Override
	public Optional<User> createLocalUser(String username) 
	{
		if(checkUsernameAvailability(username))
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
			
			try {
				saveLocalUser();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		return Optional.ofNullable(user); 
	}
	
	private void saveLocalUser() throws IOException 
	{
		Properties props = PropertiesUtility.getAppProperties() ; 
		
		byte[] userAsBytes;
		try {
			userAsBytes = SerializationUtility.serializeUser(this.user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ; 
		} 
		
		props.setProperty("localUser", new String(userAsBytes))  ;
		PropertiesUtility.saveAppProperties(props);
	}
	
	@Override
	public boolean changeUname(String newName) 
	{
		boolean res = checkUsernameAvailability(newName) ; 
		

		if(!res)
		{
			return res ; 
		}
		else
		{
			user.setUsername(newName);
			
			
			// multicast username change 
			new NotifyChangeUsernameTask(this) ; 
			
			
			//TODO notify view 
			
		}


		
		
		try {
			saveLocalUser();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return res ;
	}
	
	private boolean checkUsernameAvailability(String username )
	{
		boolean res = false ; 
		
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(username))
                .uri(URI.create(PRESENCESERVICE_URL))
                .header("Content-Type", "text/plain")
                .setHeader("COMMUNICATION", "CU")
                .build();
        

			HttpResponse<String> response;
			
			try {
				response = httpClient.send(request, BodyHandlers.ofString());
				
				res = response.statusCode() == 200 ; 
			} catch (ConnectException e){
				res = true ; 
			}
			catch (IOException | InterruptedException e) {
				e.printStackTrace();
				
			}
			
		return res ;
	}






}



