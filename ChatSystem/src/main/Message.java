package main;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class Message {
	
	public static final int MAX_SIZE = 1310720;
	public static final int HEADER_SIZE = 29;
	
	public static final int START_TYPE = 0;
	public static final int END_TYPE = 1;
	public static final int START_SUBTYPE = 1;
	public static final int END_SUBTYPE = 3;
	public static final int START_SIZE = 3;
	public static final int END_SIZE = 4;
	public static final int START_TIME = 4;
	public static final int END_TIME = 30;
	
	public byte[] content;
	protected byte[] header;
	
	public Message() 
	{

	}
	
	public void buildHeader(byte type, String subtype, int size) throws IOException 
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(type);
		stream.write(subtype.getBytes());
		stream.write(size);
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		stream.write(ts.toString().getBytes());
		header = stream.toByteArray();
	}
	
	public byte[] extractType() 
	{
		return Arrays.copyOfRange(header, START_TYPE, END_TYPE);
	}
	
	public byte[] extractSubtype() 
	{
		return Arrays.copyOfRange(header, START_SUBTYPE, END_SUBTYPE);
	}
	
	public byte[] extractSize() 
	{
		return Arrays.copyOfRange(header, START_SIZE, END_SIZE);
	}
	
	public byte[] extractTime() 
	{
		return Arrays.copyOfRange(header, START_TIME, END_TIME);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
