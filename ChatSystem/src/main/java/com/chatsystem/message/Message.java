package com.chatsystem.message;

import java.util.Arrays;
import java.util.Date;
import java.sql.Timestamp;

public abstract class Message implements Comparable<Message>{
	
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
	
	public void setDate(Timestamp date)
	{
		this.date = date ; 
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
	
	@Override 
	public int compareTo(Message o)
	{
		return date.compareTo(o.getDate());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Message))
			return false ; 
		
		Message m = (Message) obj ; 
		
		return this.date.equals(m.getDate()) && Arrays.equals(this.content,m.getContent()) ;  
	}
	
	@Override 
	public int hashCode()
	{
		return 31 * Arrays.hashCode(content) + date.hashCode() ; 
	}
	
	@Override 
	public String toString()
	{
		return "Timestamp : " + date.toString(); 
	}
	
}
