package com.chatsystem.model;

import java.util.List;

import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;

/*
 * Observable part of the observer pattern implementation specific to session events 
 */
public interface SessionModel {
	
	public void addSessionListener(SessionListener sl) ; 
	
	public void removeSessionListener(SessionListener sl) ; 
	
	public SessionListener[] getSessionListeners() ; 
	
	public void clearSessionListeners() ; 
	
	public List<UserMessage> getMessages(); 
	
	public User getReceiver(); // TODO change this to UserModel ? 
	
	public User getEmitter();

}
