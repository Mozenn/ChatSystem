package com.chatsystem.user;

import java.util.Arrays;

import com.chatsystem.message.Message;

/*
 * Identify a User 
 * UserId must be unique  
 */
public class UserId { 
	
	private byte[] id ; 
	
	public UserId()
	{
		this.id = new byte[8]  ; 
	}
	
	/*
	 * @throws NullPointerException if id is null 
	 */
	public UserId(byte[] id)
	{
		if(id == null)
			throw new NullPointerException("Id can't be null") ; 
		
		this.id = id  ; 
	}
	
	public byte[] getId()
	{
		return this.id ; 
	}
	
	/*
	 * @throws NullPointerException if id is null 
	 */
	public void setId(byte[] id) {
		
		if(id == null)
			throw new NullPointerException("Id can't be null") ; 
		
		this.id = id;
	}
	
	@Override
	public String toString()
	{
		return id.toString() ; 
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof UserId))
			return false ; 
		
		UserId uId = (UserId) obj ; 
		
		return Arrays.equals(this.id,uId.getId()) ;  
	}
	
	@Override 
	public int hashCode()
	{
		return Arrays.hashCode(id) ; 
	}
	
	

}
