package com.insa.dao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLiteTest;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;

public class DAOTest {
	
	@Test 
	public void getHistoryTest() throws IOException
	{
		UserMessage m1 = new UserMessage("data",new UserId("u1".getBytes()), new UserId("u2".getBytes())) ; 
		UserMessage m2 = new UserMessage("data",new UserId("u2".getBytes()), new UserId("u1".getBytes())) ; 
		
		DAO dao = new DAOSQLiteTest() ; 
		
		dao.clearHistory(); 
		
		dao.addMessage(m1);
		dao.addMessage(m2);
		
		var messages = dao.getHistory(new UserId("u1".getBytes())) ; 
		
		messages.forEach( m -> System.out.println(m.getDate().toString() + " " + m.getSubtype()));
		
		assertTrue(messages.contains(m1)) ;
		assertTrue(messages.contains(m2)) ;
		assertTrue(new String(messages.get(0).getContent()).equals("data"));
		System.out.println(new String(messages.get(0).getContent()));
	}
	
	@Test 
	public void StoreUserTest() 
	{
		UserId uId1 = new UserId("id1".getBytes()) ;
		UserId uId2 = new UserId("id2".getBytes()) ;
		
		User u1 = null ; 
		User u2 = null ; 
		
		try {
			u1 = new User(uId1,InetAddress.getLocalHost(),"u1")  ;
			u2 = new User(uId2,InetAddress.getLocalHost(),"u2")  ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return ; 
		} 
		
		DAOSQLiteTest dao = new DAOSQLiteTest() ; 
		
		dao.clearUser();
		
		dao.addUser(u1);
		dao.addUser(u2);
		
	}
}
