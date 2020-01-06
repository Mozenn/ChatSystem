package com.chatsystem.model;

import java.util.EventListener;

import com.chatsystem.user.User;

public interface SystemListener extends EventListener{

	public void sessionStarted(SessionModel sm) ; 
	
	public void sessionClosed(SessionModel sm); 
	
	public void userConnection(User u); 
	
	public void userDisconnection(User u); 
}
