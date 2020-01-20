package com.chatsystem.session;

import com.chatsystem.user.User;

/*
 * Wrapper class to be serialized in Json format and sent via Message 
 */
public class SessionData {
	
	private User user ; 
	
	private int port ; 
	
	public SessionData() 
	{
		user = new User() ; 
	}
	
	/*
	 * @throw NullPointerException if u is null 
	 * @throw IllegalArgumentException if port is not in the range 1024-65535 
	 */
	public SessionData(User u, int port)
	{
		if(u == null)
			throw new NullPointerException() ; 
		
		if(port < 1024 || port > 655535)
			throw new IllegalArgumentException("port must be in 1024-65535 range") ; 
		
		this.user = u ; 
		this.port = port ; 
	}

	public User getUser() {
		return user;
	}

	/*
	 * @throw NullPointerException if user is null 
	 */
	public void setUser(User user) {
		
		if(user == null)
			throw new NullPointerException() ; 
		
		this.user = user;
	}

	public int getPort() {
		return port;
	}

	/*
	 * @throw IllegalArgumentException if port is not in the range 1024-65535 
	 */
	public void setPort(int port) {
		
		if(port < 1024 || port > 655535)
			throw new IllegalArgumentException("port must be in 1024-65535 range") ; 
		
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof SessionData))
			return false ; 
		
		SessionData s = (SessionData) obj ; 
		
		return port == s.getPort() && user.equals(s.getUser()) ;  
	}
	
	@Override 
	public int hashCode()
	{
		int res = 31 * Integer.hashCode(port) + user.hashCode();
		return res  ; 
	}

}
