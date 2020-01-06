package com.chatsystem.model;

import java.util.EventListener;

import com.chatsystem.message.UserMessage;

public interface SessionListener extends EventListener{
	
	public void messageAdded(UserMessage m) ; 
	
}
