package com.insa.localcommunication;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.insa.message.SystemMessage;
import com.insa.user.User;
import com.insa.user.UserId;
import com.insa.utility.NetworkUtility;
import com.insa.utility.SerializationUtility;

public class SerializationTest {
	
	@Test 
	public void ValidSystemMessageSerialization() throws JsonParseException, JsonMappingException, IOException
	{
		String s = "hey" ; 
		
		SystemMessage messageBefore = new SystemMessage(SystemMessage.SystemMessageType.CO,s.getBytes()) ; 
		
		ObjectMapper o = new ObjectMapper() ;
		
		var content = o.writeValueAsString(messageBefore).getBytes() ; 
		
		SystemMessage messageAfter = SerializationUtility.deserializeSystemMessage(content) ;
		
		assertEquals(messageBefore.getDate(),messageAfter.getDate()); 
		assertEquals(messageBefore.getSubtype(),messageAfter.getSubtype()); 
		assertEquals(s,new String(messageAfter.getContent())); 
		
	     
	}
	
	@Test (expected = MismatchedInputException.class)
	public void InvalidSystemMessageSerialization() throws IOException
	{

		String s = "hey" ; 
		

		ObjectMapper o = new ObjectMapper() ;
		
		byte[] content;

		content = o.writeValueAsString(s).getBytes();

		
		SystemMessage messageAfter = SerializationUtility.deserializeSystemMessage(content) ;

	}
	
	@Test 
	public void ValidUserSerialization() throws JsonParseException, JsonMappingException, IOException
	{
		String s = "hey" ; 
		
		User userBefore = new User(new UserId("u1".getBytes()),NetworkUtility.getLocalIPAddress(),"username") ; 
		
		ObjectMapper o = new ObjectMapper() ;
		
		var content = o.writeValueAsString(userBefore).getBytes() ; 
		
		User userAfter = SerializationUtility.deserializeUser(content) ;
		
		assertEquals(userBefore.getId(),userAfter.getId()); 
		assertEquals(userBefore.getIpAddress(),userAfter.getIpAddress()); 
		assertEquals(userBefore.getUsername(),userAfter.getUsername()); 
		
	     
	}
	
	@Test (expected = MismatchedInputException.class)
	public void InvalidUserSerialization() throws IOException
	{

		String s = "hey" ; 
		

		ObjectMapper o = new ObjectMapper() ;
		
		byte[] content;

		content = o.writeValueAsString(s).getBytes();

		
		User user = SerializationUtility.deserializeUser(content) ;

	}

}
