package com.insa.message;

import java.nio.file.Files;
import java.util.Arrays;

import com.insa.message.SystemMessage.SystemMessageType;
import com.insa.user.UserId;

import java.io.File;
import java.io.IOException;

public class UserMessage extends Message{
	
	public enum UserMessageType
	{
		TX("TX"),
		FL("FL");
		
	    private String type;

	    UserMessageType(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}

	private UserMessageType subtype;
	private UserId receiverId ; // TODO change to UserId 
	private UserId senderId ;  // TODO change to UserId 
	
	public UserMessage()
	{
		super();
		this.subtype = UserMessageType.TX;
		this.receiverId = new UserId(); 
		this.senderId = new UserId(); 
	}
	
	public UserMessage(String text, UserId receiverId, UserId senderId ) throws IOException 
	{
		super(text.getBytes());
		this.receiverId = receiverId ; 
		this.senderId = senderId ; 
		subtype = UserMessageType.TX;
	}
	
	public UserMessage(byte[] content,UserMessageType type, UserId receiverId, UserId senderId) throws IOException 
	{
		super(content);
		subtype = type;
		this.receiverId= receiverId ; 
		this.senderId = senderId ; 
	} 
	
	public UserMessage(File f, UserId receiverId, UserId senderId) throws IOException 
	{
		super(Files.readAllBytes(f.toPath()) );
		subtype = UserMessageType.FL;
		this.receiverId = receiverId ; 
	}
	
	public UserMessageType getSubtype() {
		return subtype;
	}
	
	public UserId getReceiverId() {
		return receiverId;
	}
	
	public UserId getSenderId()
	{
		return this.senderId ; 
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof UserMessage))
			return false ; 
		
		UserMessage m = (UserMessage) obj ; 
		
		return super.equals(obj) && this.senderId.equals(m.getSenderId()) && this.receiverId.equals(m.getReceiverId()) ;  
	}
	
	@Override 
	public int hashCode()
	{
		int res = super.hashCode() ; 
		res = 31 * res + senderId.hashCode() ; 
		res = 31 * res + receiverId.hashCode() ; 
		return res ; 
	}
	
	
}
