package com.chatsystem.model;

import java.io.File;
import java.util.Optional;

import com.chatsystem.user.User;

public interface SystemContract extends SystemModel{
	
	public Optional<User> createLocalUser(String username) ; 
	
	public Optional<User> getUser() ; 
	
	public boolean changeUname(String newName) ; 
	
	public boolean startLocalSession(User receiver) ; 
	
	public void closeSession(User receiver) ; 
	
	public void closeSessionNotified(User receiver) ; 
	
	public void sendMessage(User receiver, String Text) ; 
	
	public void sendFileMessage(User receiver, File file) ; 
	
	public void start();
	
	public void close();

}
