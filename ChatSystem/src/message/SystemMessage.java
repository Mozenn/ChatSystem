package message;

import java.io.IOException;

public class SystemMessage extends Message{
	
	public enum SystemMessageType
	{
		CO("CO"), // connection
		CR("CR"), // connectionResponse
		GU("GU"), // getDistantUsers
		CV("CV"), // checkUsernameValidity
		SS("SS");  // startSession	
		
	    private String type;

		SystemMessageType(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}
	
	public SystemMessage(SystemMessageType s, byte[] c) throws IOException 
	{
		super();
		content = c;
		buildHeader((byte)1, s.name(), content.length);
	
	}
}
