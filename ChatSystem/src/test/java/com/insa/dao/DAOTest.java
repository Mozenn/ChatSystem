package com.insa.dao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.insa.message.UserMessage;

public class DAOTest {
	
	@Test 
	public void getHistoryTest() throws IOException
	{
		UserMessage m1 = new UserMessage("data","u1".getBytes(),"u2".getBytes()) ; 
		UserMessage m2 = new UserMessage("data","u2".getBytes(),"u1".getBytes()) ; 
		
		DAO dao = new DAOSQLiteTest() ; 
		
		dao.clearHistory(); 
		
		dao.addMessage(m1);
		dao.addMessage(m2);
		
		var messages = dao.getHistory("u1".getBytes()) ; 
		
		messages.forEach( m -> System.out.println(m.getDate().toString() + " " + m.getSubtype()));
		
		assertTrue(messages.contains(m1)) ;
		assertTrue(messages.contains(m2)) ;
	}

}
