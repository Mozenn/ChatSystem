package com.chatsystem.message;

import java.io.IOException;

public class SystemMessage extends Message{
	
	public enum SystemMessageType
	{
		CO("CO"), // connection
		CR("CR"), // connectionResponse
		DC("DC"), // disconnection 
		GU("GU"), // getDistantUsers
		CV("CV"), // checkUsernameValidity
		SS("SS"),  // startSession	
		SR("SR"),  // startSessionResponse
		CS("CS");  // closeSession 
		
	    private String type;

		SystemMessageType(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}
	
	private SystemMessageType subtype ; 
	
	public SystemMessageType getSubtype() {
		return subtype;
	}
	
	public SystemMessage() throws IOException 
	{
		super();
	
	}

	public SystemMessage(SystemMessageType s, byte[] c) throws IOException 
	{
		super(c);
		this.subtype = s ; 
	
	}
}
