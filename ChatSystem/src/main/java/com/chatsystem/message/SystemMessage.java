package com.chatsystem.message;

import java.io.IOException;

/*
 * Type of Message used by communication systems to communicate with other local or distant client 
 */
public class SystemMessage extends Message{
	
	/*
	 * Allow to check the type of message received 
	 */
	public enum SystemMessageType
	{
		CO("CO"), // connection
		CR("CR"), // connectionResponse
		DC("DC"), // disconnection 
		SS("SS"),  // startSession	
		SR("SR"),  // startSessionResponse
		CU("CU"),   // changeUsername
		CS("CS");  // closeSession 
		
	    private String type;

		SystemMessageType(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}
	
	private SystemMessageType systemMessageType ; 
	
	public SystemMessageType getSystemMessageType() {
		return systemMessageType;
	}
	
	public void setSystemMessageType(SystemMessageType type)
	{
		this.systemMessageType = type ; 
	}
	
	public SystemMessage() throws IOException 
	{
		super();
	
	}
	
	/*
	 * @throws NullPointerException if type is null 
	 */
	public SystemMessage(SystemMessageType type, byte[] c) throws IOException 
	{
		super(c);
		
		if(type == null)
			throw new NullPointerException() ; 
		
		this.systemMessageType = type ; 
	
	}
}
