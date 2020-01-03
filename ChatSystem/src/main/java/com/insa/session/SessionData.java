package com.insa.session;

import com.insa.user.User;

public class SessionData {
	
	private User user ; 
	
	private int port ; 
	
	public SessionData() 
	{
		user = new User() ; 
	}
	
	public SessionData(User u, int port)
	{
		this.user = u ; 
		this.port = port ; 
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
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
