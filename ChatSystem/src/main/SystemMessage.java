package main;

public class SystemMessage extends Message{
	
	public enum SystemMessageType
	{
		CO,
		SM,
		GU,
		CV,
		GH
	}

	public SystemMessageType subtype;
	
	public SystemMessage(SystemMessageType s, byte[] c) 
	{
		super();
		subtype = s;
		content = c;
		size = c.length;	
	}
}
