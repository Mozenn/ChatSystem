package com.chatsystem.view;


public interface ChatEmitter {
	
	public void addChatListener(ChatListener cl) ; 
	
	public void removeChatListener(ChatListener cl) ; 
	
	public ChatListener[] getChatListeners() ; 
	
	public void clearChatListeners() ; 

}
