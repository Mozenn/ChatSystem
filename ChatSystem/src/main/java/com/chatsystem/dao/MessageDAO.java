package com.chatsystem.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.chatsystem.message.Message;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.UserId;

public interface MessageDAO {
	
	public void addMessage(UserMessage message) ; 
	
	/*
	 * @return list of all message where id is either the sender or the receiver 
	 */
	public List<UserMessage> getHistory(UserId receiverId) ; 
	
	/*
	 * Remove every messages stored in the DB 
	 */
	public void clearHistory() ; 

}
