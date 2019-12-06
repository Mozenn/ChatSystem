package defo;

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
	
	public SystemMessage(SystemMessageType s, byte[] c) 
	{
		super();
		content = c;
		try {
			buildHeader((byte)1, s.name(), content.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
