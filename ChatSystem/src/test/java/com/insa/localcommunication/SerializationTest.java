package com.insa.localcommunication;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.insa.message.SystemMessage;
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
	public void InvalidMessageSerialization() throws IOException
	{

		String s = "hey" ; 
		

		ObjectMapper o = new ObjectMapper() ;
		
		byte[] content;

		content = o.writeValueAsString(s).getBytes();

		
		SystemMessage messageAfter = SerializationUtility.deserializeSystemMessage(content) ;


		
	     
	}

}
