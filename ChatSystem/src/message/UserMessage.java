package message;

import java.nio.file.Files;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class UserMessage extends Message{
	
	public enum UserMessageType
	{
		TX,
		FL
	}

	public UserMessageType subtype;
	
	public UserMessage(String t) 
	{
		super();
		content = t.getBytes();
		try {
			buildHeader((byte)0,UserMessageType.TX.name(), content.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	//TODO : handle exception
	public UserMessage(File f) throws IOException 
	{
		super();
		content = Files.readAllBytes(f.toPath());
		try {
			buildHeader((byte)0,UserMessageType.FL.name(), content.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public UserMessage(byte[] tab) 
	{
		super();
		content = Message.extractContent(tab);
		this.header = Message.extractHeader(tab);	
	}
}
