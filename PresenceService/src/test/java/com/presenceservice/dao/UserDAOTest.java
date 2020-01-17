package com.presenceservice.dao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import com.presenceservice.model.User;
import com.presenceservice.model.UserId;

public class UserDAOTest {
	
	@Test 
	public void StoreUserTest() throws IOException 
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
		
		UserDAO dao = new UserDAOSQLiteTest() ; 
		System.out.println("User Table created") ; 
		
		dao.clearUser();
		
		dao.addUser(u1);
		dao.addUser(u2);
		
		var r1 = dao.getUser(uId1) ; 
		var r2 = dao.getUser(uId2) ; 
		
		assertTrue(r1.get().equals(u1)) ; 
		assertTrue(r2.get().equals(u2)) ; 
		
	}

}
