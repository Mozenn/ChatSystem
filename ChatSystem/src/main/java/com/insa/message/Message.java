package com.insa.message;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public abstract class Message {
	
	public static final int MAX_SIZE = 1310720;
	
	protected byte[] content;
	protected Timestamp date ; 
	
	
	public byte[] getContent() {
		return content;
	}
	
	public Timestamp getDate()
	{
		return this.date ; 
	}
	
	protected Message()
	{
		this.content = new byte[512] ; 
		Date d = new Date();
		this.date = new Timestamp(d.getTime());
	}
	
	protected Message(byte[] content) 
	{
		this.content = content ; 
		Date d = new Date();
		this.date = new Timestamp(d.getTime());
	}
	
}
