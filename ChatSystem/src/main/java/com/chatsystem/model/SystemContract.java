package com.chatsystem.model;

import java.io.File;
import java.sql.Timestamp;
import java.util.Optional;

import com.chatsystem.user.User;
import com.chatsystem.user.UserId;

public interface SystemContract extends SystemModel{
	
	public Optional<User> createLocalUser(String username) ; 
	
	public Optional<User> getUser() ; 
	
	public boolean changeUname(String newName) ; 
	
	public boolean startSession(User receiver) ; 
	
	public void closeSession(User receiver) ; 
	
	public void closeSessionNotified(User receiver) ; 
	
	public void sendMessage(User receiver, String Text) ; 
	
	public void sendFileMessage(User receiver, String filePath) ; 
	
	public void downloadFile(UserId senderId, Timestamp date) ; 
	
	public void changeDownloadPath(String newPath) ; 
	
	public void start();
	
	public void close();
	
	public boolean hasStarted() ; 

}
