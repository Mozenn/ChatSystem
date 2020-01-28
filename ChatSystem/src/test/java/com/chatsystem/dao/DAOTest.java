package com.chatsystem.dao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.chatsystem.dao.MessageDAO;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.ConfigurationUtility;

public class DAOTest {
	
	@Test 
	public void getHistoryTest() throws IOException
	{
		ConfigurationUtility.initializeApplicationFolder();
		
		UserMessage m1 = new UserMessage("data",new UserId("u1".getBytes()), new UserId("u2".getBytes())) ; 
		UserMessage m2 = new UserMessage("data",new UserId("u2".getBytes()), new UserId("u1".getBytes())) ; 
		
		MessageDAO dao = new MessageDAOSQLite() ; 
		
		dao.clearHistory(); 
		
		dao.addMessage(m1);
		dao.addMessage(m2);
		
		var messages = dao.getHistory(new UserId("u1".getBytes())) ; 
		
		//messages.forEach( m -> System.out.println(m.getDate().toString() + " " + m.getSubtype()));
		
		assertTrue(messages.contains(m1)) ;
		assertTrue(messages.contains(m2)) ;
		assertTrue(new String(messages.get(0).getContent()).equals("data"));
		
		ConfigurationUtility.clearApplicationFolder();
	}
	
}
