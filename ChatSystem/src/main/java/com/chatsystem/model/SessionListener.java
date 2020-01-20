package com.chatsystem.model;

import java.util.EventListener;

import com.chatsystem.message.UserMessage;

/*
 * Observer pattern implementation specific to session events 
 */
public interface SessionListener extends EventListener{
	
	/*
	 * fire when a session has added or updated a message 
	 * @param 
	 * 		m new message added  
	 */
	public void messageAdded(UserMessage m) ; 
	
}
