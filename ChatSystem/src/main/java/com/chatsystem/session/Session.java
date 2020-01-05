package com.chatsystem.session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.Message;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.SessionListener;
import com.chatsystem.model.SessionModel;
import com.chatsystem.user.User;

public abstract class Session implements SessionModel{

	protected User emitter;
	protected User receiver;
	protected ArrayList<UserMessage> messages;
	protected int receiverPort ; 
	
	private final EventListenerList listeners = new EventListenerList();
	
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
	
	public void addSessionListener(SessionListener sl) 
	{
		listeners.add(SessionListener.class, sl);
	}
	
	public void removeSessionListener(SessionListener sl) 
	{
		listeners.remove(SessionListener.class, sl);
	}
	
	public SessionListener[] getSessionListeners() 
	{
		return listeners.getListeners(SessionListener.class); 
	}
	
	
	protected void fireMessageReceived(UserMessage newMessage)
	{
		for(SessionListener sl : getSessionListeners())
		{
			sl.messageReceived(newMessage);
		}
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
	
	public List<UserMessage> getMessages()
	{
		return this.messages; 
	}

	public void addMessage(UserMessage m) 
	{
		synchronized(messages)
		{
			messages.add(m);
		}

		DAO dao = new DAOSQLite() ; 
		
		dao.addMessage(m);
		
		fireMessageReceived(m);
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
