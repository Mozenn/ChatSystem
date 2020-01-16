package com.chatsystem.message;

import java.nio.file.Files;
import java.util.Arrays;

import com.chatsystem.message.SystemMessage.SystemMessageType;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.SerializationUtility;

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
	private UserId receiverId ;
	private UserId senderId ;  
	
	public UserMessage()
	{
		super();
		this.subtype = UserMessageType.TX;
		this.receiverId = new UserId(); 
		this.senderId = new UserId(); 
	}
	
	public UserMessage(String text, UserId receiverId, UserId senderId ) 
	{
		super(text.getBytes());
		this.receiverId = receiverId ; 
		this.senderId = senderId ; 
		subtype = UserMessageType.TX;
	}
	
	public UserMessage(byte[] content,UserMessageType type, UserId receiverId, UserId senderId)
	{
		super(content);
		subtype = type;
		this.receiverId= receiverId ; 
		this.senderId = senderId ; 
	} 
	
	public UserMessage(FileWrapper f, UserId receiverId, UserId senderId) throws IOException 
	{
		super(SerializationUtility.serializeFileWrapper(f));
		subtype = UserMessageType.FL;
		this.receiverId = receiverId ; 
		this.senderId = senderId ;
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
