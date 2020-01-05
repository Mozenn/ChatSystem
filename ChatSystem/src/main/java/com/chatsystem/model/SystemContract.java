package com.chatsystem.model;

import java.util.Optional;

import com.chatsystem.user.User;

public interface SystemContract {
	
	public Optional<User> createLocalUser(String username) ; 
	
	public Optional<User> getUser() ; 
	
	public boolean changeUname(String newName) ; 
	
	public boolean startLocalSession(User receiver) ; 
	
	public void closeSession(User receiver) ; 

}
