package com.insa.message;

import java.nio.file.Files;
import java.util.Arrays;

import com.insa.message.SystemMessage.SystemMessageType;

import java.io.File;
import java.io.IOException;

public class UserMessage extends Message{
	
	public enum UserMessageType
	{
		TX,
		FL
	}

	private UserMessageType subtype;
	private String senderId ; 
	
	public UserMessage(String text, String senderId) throws IOException 
	{
		super(text.getBytes());
		this.senderId = senderId ; 
		subtype = UserMessageType.TX;
	}
	/*
	public UserMessage(byte[] bytes,UserMessageType type) throws IOException 
	{
		super();
		content = bytes;
		subtype = UserMessageType.TX;
	} */
	
	public UserMessage(File f, String senderId) throws IOException 
	{
		super(Files.readAllBytes(f.toPath()) );
		subtype = UserMessageType.FL;
		this.senderId = senderId ; 
	}
	
	public UserMessageType getSubtype() {
		return subtype;
	}
	
	public String getSenderId() {
		return senderId;
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
