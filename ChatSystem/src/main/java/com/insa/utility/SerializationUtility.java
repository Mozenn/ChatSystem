package com.insa.utility;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insa.message.Message;
import com.insa.user.User;

final public class SerializationUtility {
	
	private SerializationUtility() {}
	
	public static byte[] serializeUser(User u) throws JsonProcessingException
	{
		// Serialization to Json 
		ObjectMapper uJson = new ObjectMapper();
		return uJson.writeValueAsString(u).getBytes(); 
	}
	
	public static User deserializeUser(byte[] userAsByte) throws JsonParseException, JsonMappingException, IOException
	{
	      ObjectMapper o = new ObjectMapper () ; 
	      return  o.readValue(userAsByte, User.class); 
	}
	
	public static byte[] serializeMessage(Message m) throws JsonProcessingException
	{
		// Serialization to Json 
		ObjectMapper uJson = new ObjectMapper();
		return uJson.writeValueAsString(m).getBytes(); 
	}
	
	public static Message deserializeMessage(byte[] messageAsByte) throws JsonParseException, JsonMappingException, IOException
	{
      ObjectMapper o = new ObjectMapper () ; 
      return  o.readValue(messageAsByte, Message.class); 
	}

}
