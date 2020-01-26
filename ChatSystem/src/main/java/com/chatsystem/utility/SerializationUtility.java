package com.chatsystem.utility;

import java.util.List;

import com.chatsystem.message.Message;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.session.SessionData;
import com.chatsystem.user.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

final public class SerializationUtility {
	
	private SerializationUtility() {}
	
	/*
	 * @throws NullPointerException if u is null 
	 */
	public static byte[] serializeUser(User u) 
	{
		if(u == null)
			throw new NullPointerException() ; 
		
		// Serialization to Json 
		Gson uJson = new Gson();
		return uJson.toJson(u).getBytes(); 
	}
	
	/*
	 * @throws NullPointerException if userAsByte is null 
	 * @throws SerializationException if serialization fails 
	 */
	public static User deserializeUser(byte[] userAsByte) throws SerializationException
	{
		if(userAsByte == null)
			throw new NullPointerException() ; 
		
		Gson o = new Gson () ; 
		try
		{
			User res =  o.fromJson(new String(userAsByte), User.class);
			
			return  res ; 
			
		} catch (JsonSyntaxException e)
		{
			throw new SerializationException(e) ; 
		}

	      
	}
	
	/*
	 * @throws NullPointerException if usersAsByte is null 
	 * @throws SerializationException if serialization fails 
	 */
	public static List<User> deserializeUsers(byte[] usersAsByte) throws SerializationException
	{
		if(usersAsByte == null)
			throw new NullPointerException() ; 
		
	    Gson o = new Gson () ; 
	      
		try
		{
			return  o.fromJson(new String(usersAsByte), new TypeToken<List<User>>(){}.getType()); 
			
		} catch (JsonSyntaxException e)
		{
			throw new SerializationException(e) ; 
		}
	}
	
	/*
	 * @throws NullPointerException if fw is null 
	 */
	public static byte[] serializeFileWrapper(FileWrapper fw) 
	{
		if(fw == null)
			throw new NullPointerException() ;
		
		// Serialization to Json 
		Gson fwJson = new Gson();
		return fwJson.toJson(fw).getBytes(); 
	}
	
	/*
	 * @throws NullPointerException if fileWrapperAsByte is null 
	 * @throws SerializationException if serialization fails 
	 */
	public static FileWrapper deserializeFileWrapper(byte[] fileWrapperAsByte) throws SerializationException
	{
		if(fileWrapperAsByte == null)
			throw new NullPointerException() ;
		
		Gson o = new Gson () ; 

		try
		{
		      return  o.fromJson(new String(fileWrapperAsByte), FileWrapper.class); 
			
		} catch (JsonSyntaxException e)
		{
			throw new SerializationException(e) ; 
		}
	}
	
	/*
	 * @throws NullPointerException if s is null 
	 */
	public static byte[] serializeSessionData(SessionData s) 
	{
		if(s == null)
			throw new NullPointerException() ;
		
		// Serialization to Json 
		Gson uJson = new Gson();
		return uJson.toJson(s).getBytes(); 
	}
	
	/*
	 * @throws NullPointerException if sessionDataAsBytes is null 
	 * @throws SerializationException if serialization fails 
	 */
	public static SessionData deserializeSessionData(byte[] sessionDataAsBytes) throws SerializationException
	{
		if(sessionDataAsBytes == null)
			throw new NullPointerException() ;
		
		Gson o = new Gson () ; 

		try
		{
	      return  o.fromJson(new String(sessionDataAsBytes), SessionData.class); 
			
		} catch (JsonSyntaxException e)
		{
			throw new SerializationException(e) ; 
		}
	}
	
	/*
	 * @throws NullPointerException if m is null 
	 */
	public static byte[] serializeMessage(Message m) 
	{
		if(m == null)
			throw new NullPointerException() ;
		
		// Serialization to Json 
		Gson uJson = new Gson();
		return uJson.toJson(m).getBytes(); 
	}
	
	/*
	 * @throws NullPointerException if messageAsByte is null 
	 * @throws SerializationException if serialization fails 
	 */
	public static UserMessage deserializeUserMessage(byte[] messageAsByte) throws SerializationException
	{
		if(messageAsByte == null)
			throw new NullPointerException() ;
		
		Gson o = new Gson () ; 

		try
		{
	      return  o.fromJson(new String(messageAsByte), UserMessage.class); 
			
		} catch (JsonSyntaxException e)
		{
			throw new SerializationException(e) ; 
		}
	}
	
	/*
	 * @throws NullPointerException if messageAsByte is null 
	 * @throws SerializationException if serialization fails 
	 */
	public static SystemMessage deserializeSystemMessage(byte[] messageAsByte) throws SerializationException
	{
		if(messageAsByte == null)
			throw new NullPointerException() ;
		
		Gson o = new Gson () ; 

		try
		{
	      return  o.fromJson(new String(messageAsByte), SystemMessage.class); 
			
		} catch (JsonSyntaxException e)
		{
			throw new SerializationException(e) ; 
		}
	}

}
