package com.chatsystem.system;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationException;
import com.chatsystem.utility.SerializationUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializationTest {
	
	@Test 
	public void ValidSystemMessageSerialization() throws  IOException, InterruptedException
	{
		String s = "hey" ; 
		
		SystemMessage messageBefore = new SystemMessage(SystemMessage.SystemMessageType.CO,s.getBytes()) ; 
		
		
		var content = SerializationUtility.serializeMessage(messageBefore) ; 
		
		
		SystemMessage messageAfter = SerializationUtility.deserializeSystemMessage(content) ;
		
		
		System.out.println(messageBefore.getDate().toString()) ; 
		System.out.println(messageAfter.getDate().toString()) ; 
		assertEquals(messageBefore.getDate(),messageAfter.getDate()); 
		assertEquals(messageBefore.getSubtype(),messageAfter.getSubtype()); 
		assertEquals(s,new String(messageAfter.getContent())); 
		
	     
	}
	
	@Test (expected = SerializationException.class)
	public void InvalidSystemMessageSerialization() throws IOException
	{

		var content = "hey".getBytes() ; 

		
		SystemMessage messageAfter = SerializationUtility.deserializeSystemMessage(content) ;

	}
	
	@Test 
	public void ValidUserMessageSerialization()
	{
		String s = "hey" ; 
		
		UserId id1 = new UserId("id1".getBytes()) ; 
		UserId id2 = new UserId("id2".getBytes()) ; 
		
		UserMessage messageBefore = new UserMessage(s,id1,id2) ; 
		
		
		var content = SerializationUtility.serializeMessage(messageBefore) ; 
		
		UserMessage messageAfter = SerializationUtility.deserializeUserMessage(content) ;
		
		assertEquals(messageBefore.getDate(),messageAfter.getDate()); 
		assertEquals(messageBefore.getSubtype(),messageAfter.getSubtype()); 
		assertEquals(s,new String(messageAfter.getContent())); 
		assertEquals(id1,messageAfter.getReceiverId()); 
		assertEquals(id2,messageAfter.getSenderId()); 
		
	     
	}
	
	@Test (expected = SerializationException.class)
	public void InvalidUserMessageSerialization() throws IOException
	{

		
		byte[] content;

		content = "content".getBytes();

		
		UserMessage messageAfter = SerializationUtility.deserializeUserMessage(content) ;

	}
	
	@Test 
	public void ValidUserSerialization() throws IOException
	{
		String s = "hey" ; 
		
		User userBefore = new User(new UserId("u1".getBytes()),NetworkUtility.getLocalIPAddress(),"username") ; 
		
		var content = SerializationUtility.serializeUser(userBefore) ; 
		
		User userAfter = SerializationUtility.deserializeUser(content) ;
		
		assertEquals(userBefore.getId(),userAfter.getId()); 
		assertEquals(userBefore.getIpAddress(),userAfter.getIpAddress()); 
		assertEquals(userBefore.getUsername(),userAfter.getUsername()); 
		
	     
	}
	
	@Test (expected = SerializationException.class)
	public void InvalidUserSerialization() throws IOException
	{
		
		byte[] content;

		content = "content".getBytes() ; 

		
		User user = SerializationUtility.deserializeUser(content) ;

	}
	
	@Test 
	public void ValidFileWrapperSerialization() throws IOException 
	{
		String s = "fileName" ; 
		File f = new File("resources/fileIcon.png"); 
		
		FileWrapper fileWrapperBefore = new FileWrapper(s,f) ; 
		
		var content = SerializationUtility.serializeFileWrapper(fileWrapperBefore) ; 
		
		FileWrapper fileWrapperAfter = SerializationUtility.deserializeFileWrapper(content) ;
		
		assertEquals(fileWrapperAfter.getFileName(),fileWrapperBefore.getFileName()); 
		assertTrue(Arrays.equals(fileWrapperAfter.getFileContent(), fileWrapperBefore.getFileContent())); 
		
	     
	}
	
	@Test (expected = SerializationException.class)
	public void InvalidFileWrapperSerialization() throws IOException
	{
		
		byte[] content;

		content = "content".getBytes() ; 

		
		FileWrapper fileWrapperAfter = SerializationUtility.deserializeFileWrapper(content) ;

	}

}
