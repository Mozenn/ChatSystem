package com.insa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.insa.message.Message;
import com.insa.message.UserMessage;

public interface DAO {
	
	public void addMessage(UserMessage message) ; 
	
	public List<UserMessage> getHistory(byte[] receiverId) ; 
	
	public void clearHistory() ; 

}
