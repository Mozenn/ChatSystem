package com.chatsystem.session;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.event.EventListenerList;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.Message;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SessionListener;
import com.chatsystem.model.SessionModel;
import com.chatsystem.model.SystemContract;
import com.chatsystem.system.CommunicationSystem;
import com.chatsystem.user.User;
import com.chatsystem.utility.ConfigurationUtility;
import com.chatsystem.utility.LoggerUtility;

public abstract class Session implements SessionModel{

	protected User emitter;
	protected User receiver;
	protected ArrayList<UserMessage> messages;
	protected SystemContract system ; 
	
	private final EventListenerList listeners = new EventListenerList();
	
	protected Session()
	{
		
	}
	
	/*
	 * @throws NullPointerException if emitter, receiver or system is null  
	 */
	public Session(User emitter, User receiver, SystemContract system) throws IOException 
	{
		if(emitter == null || receiver == null || system == null)
			throw new NullPointerException() ; 
		
		this.emitter = emitter;
		this.receiver = receiver;
		messages = new ArrayList<UserMessage>();
		this.system = system ; 
	}
	
	@Override
	public void addSessionListener(SessionListener sl) 
	{
		listeners.add(SessionListener.class, sl);
	}
	
	@Override
	public void removeSessionListener(SessionListener sl) 
	{
		listeners.remove(SessionListener.class, sl);
	}
	
	@Override
	public SessionListener[] getSessionListeners() 
	{
		return listeners.getListeners(SessionListener.class); 
	}
	
	@Override
	public void clearSessionListeners()
	{
		for(var sl : getSessionListeners())
		{
			listeners.remove(SessionListener.class, sl);
		}
		
	}
	
	
	protected void fireMessageAdded(UserMessage newMessage)
	{
		for(SessionListener sl : getSessionListeners())
		{
			sl.messageAdded(newMessage);
		}
	}

	public User getEmitter() {
		return emitter;
	}
	
	public User getReceiver() {
		return receiver;
	}
	
	public List<UserMessage> getMessages()
	{
		return this.messages; 
	}
	
	/*
	 * @throws
	 */
	public void addMessage(UserMessage m) 
	{
		if(ConfigurationUtility.isTesting())
		{
			synchronized(messages)
			{
				messages.add(m);
			}
			
			fireMessageAdded(m);
			
			LoggerUtility.getInstance().info("Session Message Added");
		}
		else
		{
			synchronized(messages)
			{
				messages.add(m);
			}

			DAO dao;
			try {
				dao = new DAOSQLite();
			} catch (IOException e) {
				e.printStackTrace();
				return ; 
			} 
			
			dao.addMessage(m);
			
			fireMessageAdded(m);
			
			LoggerUtility.getInstance().info("Session Message Added");
		}

	}
	
	public Optional<UserMessage> getMessage(Timestamp date) 
	{
		return messages.stream()
				.filter(message -> message.getDate().equals(date))
				.findAny() ; 
	}
	
	public abstract void sendMessage(String s);
	
	public abstract void sendMessage(FileWrapper f);
	
	protected abstract void startSession();
	
	// Call on initiator session  
	public abstract void notifyStartSession() ;
	
	// Notify session to send closeSession message 
	public abstract void notifyCloseSession() ;
	
	protected abstract void closeSession();
	
	
	
}
