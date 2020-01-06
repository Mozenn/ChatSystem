package com.chatsystem.model;

import java.util.Optional;

import com.chatsystem.user.User;

public interface SystemModel {
	
	public void addSystemListener(SystemListener sl) ; 
	
	public void removeSystemListener(SystemListener sl) ; 
	
	public SystemListener[] getSystemListeners() ; 
	
	Optional<User> getUser(); 

}
