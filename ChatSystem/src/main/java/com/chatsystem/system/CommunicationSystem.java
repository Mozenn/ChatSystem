package com.chatsystem.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
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
import java.util.Random;

import javax.swing.event.EventListenerList;

import com.chatsystem.dao.UserDAO;
import com.chatsystem.dao.UserDAOEmbedded;
import com.chatsystem.dao.UserDAOFactory;
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
import com.chatsystem.utility.SerializationException;
import com.chatsystem.utility.ConfigurationUtility;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

public class CommunicationSystem implements AutoCloseable , SystemContract{
	
	private HashMap<UserId,User> localUsers;
	private HashMap<UserId,User> distantUsers;
	private User user;
	private boolean bRunning = false ; 
	
	private String downloadPath  ; 
	
	private HashMap<UserId,Session> sessions;
	
	private final EventListenerList listeners = new EventListenerList();
	
	private LocalCommunicationListener localCommunicationListener ; 
	/*
	 * Port used to multicast in local network 
	 */
	public static final int LOCAL_LISTENING_PORT = 8888; // default port // TODO make a list with priority in case where the first is already taken ? 
	public static final String MULTICAST_ADDR = "228.228.228.228"; // TODO use a non routable multicast address between 224.0.0.0 to 224.0.0.255 
	
	private DistantCommunicationListener distantCommunicationListener; 
	private static final int DISTANT_LISTENING_PORT = 9999; // default port 
	
	public final String PRESENCESERVICE_URL ; 
	private DistantUsersFetcher distantUsersFetcher ; 
	
	public CommunicationSystem() throws IOException 
	{
		localUsers = new HashMap<UserId,User>();
		distantUsers = new HashMap<UserId,User>();
		sessions = new HashMap<UserId,Session>() ;	
		
		Properties properties = ConfigurationUtility.getAppProperties() ; 
		
		PRESENCESERVICE_URL = properties.getProperty("presenceServiceURL") ; 
		downloadPath = properties.getProperty("downloadPath") ; 
		if(downloadPath.equals("null"))
			downloadPath = System.getProperty("java.io.tmpdir") ; 
		
	}
	
	/*
	 * Start communications & notify connection 
	 */
	@Override 
	public void start()
	{
		if(ConfigurationUtility.isTesting())
		{
			try {
				distantCommunicationListener = new DistantCommunicationListener(this) ;
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			notifyStartWebService();
			
			distantUsersFetcher = new DistantUsersFetcher(this) ; 
			
			bRunning = true ; 
			
			LoggerUtility.getInstance().info("CommunicationSystem Test Started");
		}
		else
		{
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
			
			LoggerUtility.getInstance().info("CommunicationSystem Started");
		}

	}
	
	@Override
	public boolean hasStarted() {
		
		return bRunning;
	}
	
	/*
	 * Close all communications & notify disconnection 
	 */
	@Override 
	public void close() 
	{
		bRunning = false  ; 
		
		for(Session s : sessions.values())
		{
			closeSessionNotified(s.getReceiver());
		}
		
		var t = new NotifyLocalUsersTask(this, LocalNotifyType.DISCONNECTION); 
		
		notifyCloseWebService() ; 
		
		try {
			t.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} ; 
		
		
		if(ConfigurationUtility.isTesting())
		{
			distantCommunicationListener.stopRun();
			distantUsersFetcher.stopRun() ; 
		}
		else
		{
			localCommunicationListener.stopRun();	
			distantCommunicationListener.stopRun();
			distantUsersFetcher.stopRun() ; 
		}

	}
	
	// ===================  COMMUNICATION ==============================
	
	// CONNECTION 
	
	protected void notifyLocalUsers() 
	{
		new NotifyLocalUsersTask(this,LocalNotifyType.CONNECTION);
	}
	
	protected void notifyConnectionResponse(DatagramPacket packet) throws IOException, ClassNotFoundException 
	{
		new NotifyConnectionResponseTask(this,packet);
	}
	
	// ===================  SESSIONS ==============================
	
	/*
	 * Called when StartSession request has been received by localCommunicationListener 
	 */
	protected void onLocalSessionRequest(DatagramPacket packet) throws IOException 
	{

		SessionData s = SerializationUtility.deserializeSessionData(SerializationUtility.deserializeSystemMessage(packet.getData()).getContent());
		LocalSession session = new LocalSession(user, s.getUser(), s.getPort(),this) ; 
		
		session.StartSessionResponse(packet.getAddress(),packet.getPort());
		
		addSession(session) ; 
	}
	
	/*
	 * Called when StartSession request has been received by distantCommunicationListener 
	 */
	protected void onDistantSessionRequest(User user, int port) throws IOException
	{
		DistantSession session = new DistantSession(this.user,user,port,this) ; 
		
		addSession(session) ; 
	}
	
	protected void addSession(Session session) throws IOException
	{
		
		synchronized(sessions)
		{
			sessions.put(session.getReceiver().getId(), session);
			fireSessionStarted(session); // notify view 
			
			LoggerUtility.getInstance().info("CommunicationSystem Session Added");
		}
	}
	
	/*
	 * @throws NullPointerException if receiver is null 
	 */
	@Override
	public boolean startSession(User receiver) 
	{
		if(receiver == null)
			throw new NullPointerException() ; 
		
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
			
			LoggerUtility.getInstance().info("CommunicationSystem Session Added");
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
			
			LoggerUtility.getInstance().info("CommunicationSystem Session Added");
		}
		
		return true ;
		
	}
	
	/*
	 * Close a session, notify view and notify receiver session 
	 * @throws NullPointerException if receiver is null 
	 */
	@Override
	public void closeSessionNotified(User receiver) 
	{
		if(receiver == null)
			throw new NullPointerException() ; 
		
		synchronized(sessions)
		{
			fireSessionClosed(sessions.get(receiver.getId())) ; 
			sessions.get(receiver.getId()).notifyCloseSession() ; 
			sessions.remove(receiver.getId()) ; 
		}
	}
	
	/*
	 * Close session without notifying receiver session
	 * @throws NullPointerException if receiver is null 
	 */
	@Override
	public void closeSession(User receiver) 
	{
		if(receiver == null)
			throw new NullPointerException() ; 
		
		synchronized(sessions)
		{
			fireSessionClosed(sessions.get(receiver.getId())) ; 
			sessions.remove(receiver.getId()) ; 
		}
	}
	
	/*
	 * @throws NullPointerException if receiver or text is null 
	 */
	@Override
	public void sendMessage(User receiver, String text) {
		
		if(receiver == null || text == null)
			throw new NullPointerException() ; 
		
		synchronized(sessions)
		{
			if(sessions.containsKey(receiver.getId()))
				sessions.get(receiver.getId()).sendMessage(text);
		}
	}

	/*
	 * @throws NullPointerException if receiver or filePath is null 
	 */
	@Override
	public void sendFileMessage(User receiver, String filePath) {
		
		if(receiver == null || filePath == null)
			throw new NullPointerException() ; 
		
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
	
	/*
	 * @throws NullPointerException if senderId or date is null 
	 */
	@Override
	public void downloadFile(UserId senderId, Timestamp date) 
	{
		if(senderId == null || date == null)
			throw new NullPointerException() ; 
		
		LoggerUtility.getInstance().info("CommunicationSystem DownloadFile Start");
		
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
			} catch (SerializationException e) {
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
	
	/*
	 * @throws NullPointerException if newPath or date is null 
	 */
	@Override
	public void changeDownloadPath(String newPath)
	{
		if(newPath == null)
			throw new NullPointerException() ;
		
		this.downloadPath = newPath ; 
		
		Properties properties ; 
		
		try {
			properties = ConfigurationUtility.getAppProperties() ;
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		} 
		
		properties.setProperty("downloadPath", newPath) ; 
		
		try {
			ConfigurationUtility.saveAppProperties(properties) ;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		LoggerUtility.getInstance().info("CommunicationSystem DownloadPath Changed");
		
	}
	
	@Override 
	public String getDownloadPath()
	{
		return this.downloadPath ; 
	}
	
	// ===================  USERS ==============================
	
	/*
	 * updateDistantUser is called if u is already stored 
	 */
	protected void addLocalUser(User u) 
	{
		if( u.getId().equals(this.user.getId()))
			return ; 
		
		synchronized(localUsers)
		{
			
			if(localUsers.containsKey(u.getId()) && localUsers.get(u.getId()).getUsername().equals(u.getUsername()))
			{
				return ; 
			}
			else if(distantUsers.containsKey(u.getId())) // if webservice has sent back list of distant user before local user response has been received 
			{
				removeDistantUser(u);
			}
			else if(localUsers.containsKey(u.getId()))
			{
				updateLocalUser(u);
				return ; 
			}
			
			localUsers.put(u.getId(),u);
		}
		LoggerUtility.getInstance().info("CommunicationSystem Local User added " + u.getUsername());
		
		fireuserConnection(u); // notify view 
	}
	
	
	protected void updateLocalUser(User u)
	{
		if( u.getId().equals(this.user.getId()))
			return ; 
		
		if(!localUsers.containsKey(u.getId()) || localUsers.get(u.getId()).getUsername().equals(u.getUsername()))
			return ; 
		
		localUsers.put(u.getId(),u);
		
		fireUsernameChanged(u);
		
		LoggerUtility.getInstance().info("CommunicationSystem Local User updated " + u.getUsername());
		
	}
	
	protected void removeLocalUser(User u)
	{
		synchronized(localUsers)
		{
			localUsers.remove(u.getId());
		}
		
		LoggerUtility.getInstance().info("CommunicationSystem Local User removed " + u.getUsername());
		
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
		if( u.getId().equals(this.user.getId()))
			return ; 
		
		synchronized(distantUsers)
		{

			if( (distantUsers.containsKey(u.getId()) && distantUsers.get(u.getId()).getUsername().equals(u.getUsername())) // if user already contained && username has not changed 
					|| localUsers.containsKey(u.getId())) // or if user is in local network 
				return ; 
			else if(distantUsers.containsKey(u.getId()))
			{
				updateDistantUser(u) ; 
				return ; 
			}
			
			distantUsers.put(u.getId(),u);
		}
		
		LoggerUtility.getInstance().info("CommunicationSystem Distant User added " + u.getUsername());
		
		fireuserConnection(u); // notify view 
	}
	
	protected void updateDistantUser(User u)
	{
		if( u.getId().equals(this.user.getId()))
			return ; 
		else if(!distantUsers.containsKey(u.getId()) || distantUsers.get(u.getId()).getUsername().equals(u.getUsername()))
			return ; 
		
		distantUsers.put(u.getId(),u);
		
		fireUsernameChanged(u);
		
		LoggerUtility.getInstance().info("CommunicationSystem Distant User updated " + u.getUsername());
		
	}
	
	protected void removeDistantUser(User u)
	{
		synchronized(distantUsers)
		{
			distantUsers.remove(u.getId());
		}
		
		LoggerUtility.getInstance().info("CommunicationSystem Distant User removed " + u.getUsername());
		
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
	
	protected void UpdateDistantUsers(List<User> updatedUsers)
	{
		// add new users 
		
		for(User u : updatedUsers)
		{
			if(distantUsers.containsKey(u.getId()))
			{
				if(!distantUsers.get(u.getId()).getUsername().equals(u.getUsername()))
				{
					addDistantUser(u);
				}
			}
			else
			{
				addDistantUser(u);
			}
		}
		
		// remove missing users 
		
		for(User u : distantUsers.values())
		{
			if(!updatedUsers.contains(u))
			{
				removeDistantUser(u);
			}
		}
	}
	
	private void notifyStartWebService()
	{
		
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		String userAsString;

		userAsString = new String(SerializationUtility.serializeUser(this.user));

		
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userAsString))
                .uri(URI.create(PRESENCESERVICE_URL))
                .header("Content-Type", "application/json")
                .setHeader("COMMUNICATION", "CO")
                .timeout(Duration.ofSeconds(2))
                .build();
        
        try {
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			
			LoggerUtility.getInstance().info("CommunicationSystem NotifyWebService CO Sent ");
			
			if(response.statusCode() == 200)
			{
				List<User> users = SerializationUtility.deserializeUsers(response.body().getBytes()) ; 
				
				for(User u : users)
				{
					addDistantUser(u);
				}
				LoggerUtility.getInstance().info("CommunicationSystem NotifyWebService CO Received ");
			}
		}catch (HttpTimeoutException e) {
			return ; 
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

		userAsString = new String(SerializationUtility.serializeUser(this.user));

		
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userAsString))
                .uri(URI.create(PRESENCESERVICE_URL))
                .timeout(Duration.ofSeconds(2)) 
                .header("Content-Type", "application/json")
                .setHeader("COMMUNICATION", "DC")
                .build();
        
        try {
        	LoggerUtility.getInstance().info("CommunicationSystem NotifyWebService DC Sent");
			httpClient.send(request, BodyHandlers.ofString());
		} catch (HttpTimeoutException e) {
			return ; 
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		} catch (InterruptedException e) {
			e.printStackTrace();
			return ; 
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
	
	protected void fireUsernameChanged(User u)
	{
		for(SystemListener sl : getSystemListeners())
		{
			sl.usernameChanged(u);
		}
	}
	
	// ===================  LOCAL USER ==============================
	
	@Override
	public Optional<User> getUser() 
	{
		if(ConfigurationUtility.isTesting())
		{
			if(user == null)
			{
			    int leftLimit = 97; // letter 'a'
			    int rightLimit = 122; // letter 'z'
			    Random random = new Random();
			 
			    String id = random.ints(leftLimit, rightLimit + 1)
			      .limit(5)
			      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			      .toString();
			    
			    String username = random.ints(leftLimit, rightLimit + 1)
					      .limit(7)
					      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					      .toString();
				
				try {
					this.user = new User(new UserId(id.getBytes()),InetAddress.getLocalHost(),username) ;
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} 
			}
		}
		else
		{
			if(user == null)
			{
				try {
					LoadLocalUser();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}


		return Optional.ofNullable(user);

	}
	
	private boolean LoadLocalUser() throws IOException 
	{
		boolean result = false ; 
		
		Properties props = ConfigurationUtility.getAppProperties() ; 
		
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
		    
		    
			User newUser = new User(new UserId(id),ipAdd, username,LOCAL_LISTENING_PORT, DISTANT_LISTENING_PORT) ; 
			

			this.user = newUser ; 
			
			try {
				saveLocalUser();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			UserDAO dao ; 
			
			try {
				UserDAOFactory factory = new UserDAOFactory() ;
				dao = factory.getUserDAOInstance() ; 
			} catch (IOException e) {
				e.printStackTrace();
				return Optional.ofNullable(user); 
			} 
			
			dao.addUser(newUser);
		}
		
		
		return Optional.ofNullable(user); 
	}
	
	/*
	 * Create a new temporary user to send to webservice to check username availability 
	 */
	private User getDummyLocalUser()
	{
		if(ConfigurationUtility.isTesting())
		{
			InetAddress ipAdd;
			try {
				ipAdd = NetworkUtility.getLocalIPAddress();
			} catch (IOException e1) {
				e1.printStackTrace();
				return null ;
			} 
			
		    byte[] id = this.user.getId().getId();  
			
			return new User(new UserId(id),ipAdd, "name") ; 
		}
		else
		{
			InetAddress ipAdd;
			try {
				ipAdd = NetworkUtility.getLocalIPAddress();
			} catch (IOException e1) {
				e1.printStackTrace();
				return null ;
			} 
			
		    NetworkInterface ni;
		    byte[] id ; 
		    
			try {
				ni = NetworkInterface.getByInetAddress(ipAdd);
				id = ni.getHardwareAddress();
			} catch (SocketException e1) {
				e1.printStackTrace();
				return null ;
			}
		    
		    
			return new User(new UserId(id),ipAdd, "name") ; 
		}

	}
	
	private void saveLocalUser() throws IOException 
	{
		Properties props = ConfigurationUtility.getAppProperties() ; 
		
		byte[] userAsBytes;

		userAsBytes = SerializationUtility.serializeUser(this.user);

		
		props.setProperty("localUser", new String(userAsBytes))  ;
		ConfigurationUtility.saveAppProperties(props);
	}
	
	/*
	 * @throws NullPointerException if newName is null 
	 */
	@Override
	public boolean changeUname(String newName) 
	{
		if(newName == null)
			throw new NullPointerException() ; 
		
		boolean res = checkUsernameAvailability(newName) ; 
		

		if(!res)
		{
			return res ; 
		}
		else
		{
			user.setUsername(newName);
			
			
			// multicast username change 
			new NotifyChangeUsernameLocalTask(this) ; 
			
			new NotifyChangeUsernameWebServiceTask(this, this.user) ; 
			
			// notify view 
			fireUsernameChanged(this.user) ; 
			
			UserDAO dao ; 
			
			try {
				UserDAOFactory factory = new UserDAOFactory() ;
				dao = factory.getUserDAOInstance() ; 
			} catch (IOException e) {
				e.printStackTrace();
				return false ;
			} 
			
			dao.updateUser(this.user);
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
		
		UserDAO dao;
		try {
			
			UserDAOFactory factory = new UserDAOFactory() ;
			dao = factory.getUserDAOInstance() ; 
		} catch (IOException e) {
			e.printStackTrace();
			return true ; 
		} 
		
		return dao.isUsernameAvailable(username) ; 
		
	}
	
	/*
	 * Set distantListeningPort following binding of serversocket in DistantCommunicationListener 
	 */
	protected void setDistantListeningPort(int newPort)
	{
		this.user.setDistantPort(newPort);
	}






}



