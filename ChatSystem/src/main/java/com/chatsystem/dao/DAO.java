package com.chatsystem.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.chatsystem.message.Message;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.UserId;

public interface DAO {
	
	public void addMessage(UserMessage message) ; 
	
	public List<UserMessage> getHistory(UserId receiverId) ; 
	
	public void clearHistory() ; 

}
