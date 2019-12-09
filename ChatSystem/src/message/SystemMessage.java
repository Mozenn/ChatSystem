package message;

import java.io.IOException;

public class SystemMessage extends Message{
	
	public enum SystemMessageType
	{
		CO, // connection
		CR, // connectionResponse
		GU, // getDistantUsers
		CV, // checkUsernameValidity
		SS  // startSession	
	}
	
	public SystemMessage(SystemMessageType s, byte[] c) throws IOException 
	{
		super();
		content = c;
		buildHeader((byte)1, s.name(), content.length);
	
	}
}
