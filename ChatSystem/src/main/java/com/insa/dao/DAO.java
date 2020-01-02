package com.insa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.insa.message.Message;
import com.insa.message.UserMessage;
import com.insa.user.UserId;

public interface DAO {
	
	public void addMessage(UserMessage message) ; 
	
	public List<UserMessage> getHistory(UserId receiverId) ; 
	
	public void clearHistory() ; 

}
