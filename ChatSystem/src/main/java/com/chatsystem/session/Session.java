package com.chatsystem.session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.Message;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;

public abstract class Session {

	protected User emitter;
	protected User receiver;
	protected ArrayList<UserMessage> messages;
	protected int receiverPort ; 
	
	protected Session()
	{
		
	}
	
	public Session(User e, User r) throws IOException 
	{
		emitter = e;
		receiver = r;
		messages = new ArrayList<UserMessage>();
	}
	
	public Session(User e, User r, int receiverPort) throws IOException 
	{
		emitter = e;
		receiver = r;
		this.receiverPort = receiverPort ; 
		messages = new ArrayList<UserMessage>();
	}

	public User getEmitter() {
		return emitter;
	}
	
	public User getReceiver() {
		return receiver;
	}
	
	public int getReceiverPort() {
		return receiverPort;
	}

	public void addMessage(UserMessage m) 
	{
		synchronized(messages)
		{
			messages.add(m);
		}

		DAO dao = new DAOSQLite() ; 
		
		dao.addMessage(m);
		
		// TODO Notify View 
	}
	
	public abstract void sendMessage(String s);
	
	public abstract void sendMessage(File f);
	
	public abstract void startSession();
	
	// Call on initiator session  
	public abstract void notifyStartSession() ;
	
	// Call on responding session 
	public abstract void notifyStartSessionResponse(Object packetReceived) ;
	
	// Notify session to send closeSession message 
	public abstract void notifyCloseSession() ;
	
	public abstract void closeSession();
	
	
	
}
