package com.chatsystem.model;


public interface SystemListener {

	public void sessionCreated(SessionModel sm) ; 
	
	public void sessionClosed(SessionModel sm); 
}
