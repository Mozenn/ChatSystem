package defo;

import java.io.IOException;

public class SystemMessage extends Message{
	
	public enum SystemMessageType
	{
		CO,
		SM,
		GU,
		CV,
		GH,
		SS
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
