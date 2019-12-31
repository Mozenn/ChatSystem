package com.insa.message;

import java.nio.file.Files;
import java.util.Arrays;

import com.insa.message.SystemMessage.SystemMessageType;

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
	private byte[] receiverId ; 
	private byte[] senderId ; 
	
	public UserMessage()
	{
		super();
		this.subtype = UserMessageType.TX;
		this.receiverId = new byte[18]; 
		this.senderId = new byte[18]; 
	}
	
	public UserMessage(String text, byte[] receiverId, byte[] senderId ) throws IOException 
	{
		super(text.getBytes());
		this.receiverId = receiverId ; 
		this.senderId = senderId ; 
		subtype = UserMessageType.TX;
	}
	
	public UserMessage(byte[] content,UserMessageType type, byte[] receiverId, byte[] senderId) throws IOException 
	{
		super(content);
		subtype = type;
		this.receiverId= receiverId ; 
		this.senderId = senderId ; 
	} 
	
	public UserMessage(File f, byte[] receiverId, byte[] senderId) throws IOException 
	{
		super(Files.readAllBytes(f.toPath()) );
		subtype = UserMessageType.FL;
		this.receiverId = receiverId ; 
	}
	
	public UserMessageType getSubtype() {
		return subtype;
	}
	
	public byte[] getReceiverId() {
		return receiverId;
	}
	
	public byte[] getSenderId()
	{
		return this.senderId ; 
	}
	
	
	/*
	public UserMessage(byte[] tab) 
	{
		super();
		content = Message.extractContent(tab);
		this.header = Message.extractHeader(tab);	
	}
	*/
}
