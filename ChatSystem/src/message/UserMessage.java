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
	
	public UserMessage(String text) throws IOException 
	{
		super();
		content = text.getBytes();
		buildHeader((byte)0,UserMessageType.TX.name(), content.length);
	}
	
	public UserMessage(byte[] bytes,UserMessageType type) throws IOException 
	{
		super();
		content = bytes;
		buildHeader((byte)0,UserMessageType.TX.name(), content.length);
	}
	
	public UserMessage(File f) throws IOException 
	{
		super();
		content = Files.readAllBytes(f.toPath());
		buildHeader((byte)0,UserMessageType.FL.name(), content.length);
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
