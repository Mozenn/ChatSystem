package com.presenceservice.model;

import java.util.Arrays;

public class UserId { 
	
	private byte[] id ; 
	
	public UserId()
	{
		this.id = new byte[8]  ; 
	}
	
	public UserId(byte[] id)
	{
		this.id = id  ; 
	}
	
	public byte[] getId()
	{
		return this.id ; 
	}
	
	public void setId(byte[] id) {
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
