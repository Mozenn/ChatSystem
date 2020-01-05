package com.chatsystem.model;

public interface SystemModel {
	
	public void addSystemListener(SystemListener sl) ; 
	
	public void removeSystemListener(SystemListener sl) ; 
	
	public SystemListener[] getSystemListeners() ; 

}
