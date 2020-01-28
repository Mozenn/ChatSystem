package com.chatsystem.dao;

import java.util.List;
import java.util.Optional;

import com.chatsystem.user.User;
import com.chatsystem.user.UserId;

public interface UserDAO {
	
	public Optional<User> getUser(UserId id) ; 
	
	public List<User> getAllUsers() ; 
	
	public void addUser(User user) ;
	
	public void removeUser(User u) ; 
	
	/*
	 * Remove all user entry from the database 
	 */
	public void clearUser() ; 
	
	public void updateUser(User user) ; 
	
	public boolean isUsernameAvailable(String username) ; 

}
