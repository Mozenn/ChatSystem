package message;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public abstract class Message implements Serializable{
	
	// TODO : move all helper to specific MessageUtility class 
	public static final int MAX_SIZE = 1310720;
	public static final int HEADER_SIZE = 29;
	
	public static final int START_TYPE = 0;
	public static final int END_TYPE = 1;
	public static final int START_SUBTYPE = 1;
	public static final int END_SUBTYPE = 3;
	public static final int START_SIZE = 3;
	public static final int END_SIZE = 4;
	public static final int START_TIME = 4;
	public static final int END_TIME = 27;
	public static final int START_CONTENT = 27;
	
	protected byte[] content;
	protected byte[] header;
	
	public byte[] getHeader() {
		return header;
	}

	public static byte[] extractType(byte[] m) 
	{
		return Arrays.copyOfRange(m, START_TYPE, END_TYPE);
	}
	
	public static byte[] extractSubtype(byte[] m) 
	{
		return Arrays.copyOfRange(m, START_SUBTYPE, END_SUBTYPE);
	}
	
	public static byte[] extractSize(byte[] m) 
	{
		return Arrays.copyOfRange(m, START_SIZE, END_SIZE);
	}
	
	public static byte[] extractTime(byte[] m) 
	{
		return Arrays.copyOfRange(m, START_TIME, END_TIME);
	}
	
	public static byte[] extractContent(byte[] m) 
	{
		return Arrays.copyOfRange(m, START_CONTENT, START_CONTENT + Message.extractSize(m)[0]);
	}
	
	public static byte[] extractHeader(byte[] m) 
	{
		return Arrays.copyOfRange(m, START_CONTENT, END_TIME);
	}
	
	public Message() 
	{

	}
	
	public byte[] toByteArray() throws IOException 
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(header);
		stream.write(content);
		return stream.toByteArray();
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
}
