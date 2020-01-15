package com.presenceservice.dao;

import java.util.Optional;

import com.presenceservice.model.User;
import com.presenceservice.model.UserId;

public interface UserDAO {
	
	public Optional<User> getUser(UserId id) ; 
	
	public void addUser(User user) ; 
	
	public void updateUser(User user) ; 

}
