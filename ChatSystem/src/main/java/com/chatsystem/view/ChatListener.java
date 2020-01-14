package com.chatsystem.view;

import java.sql.Timestamp;
import java.util.EventListener;

import javax.swing.DefaultListModel;

import com.chatsystem.user.User;
import com.chatsystem.user.UserId;

public interface ChatListener extends EventListener {
	
	void fileDownloaded(UserId sender, Timestamp receptDate) ; 
	
	void messageSent(User currentReceiver, String text) ; 
	
	void fileMessageSent(User currentReceiver, DefaultListModel<String> pathList) ; 

}
