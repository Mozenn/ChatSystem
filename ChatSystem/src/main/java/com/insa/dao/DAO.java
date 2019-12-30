package com.insa.dao;

import com.insa.message.Message;

public interface DAO {
	
	public void connect(); 
	
	public void addMessage(Message message) ; 
	
	public void getHistory(String receiverId) ; // TODO change param type depending how message is stored 

}
