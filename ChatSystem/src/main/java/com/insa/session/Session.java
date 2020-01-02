package com.insa.session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.insa.dao.DAO;
import com.insa.dao.DAOSQLite;
import com.insa.message.Message;
import com.insa.message.UserMessage;
import com.insa.user.User;

public abstract class Session {

	protected User emitter;
	protected User receiver;
	protected ArrayList<Message> messages;
	protected int receiverPort ; 
	
	protected Session()
	{
		
	}
	
	public Session(User e, User r) throws IOException 
	{
		emitter = e;
		receiver = r;
		messages = new ArrayList<Message>();
	}
	
	public Session(User e, User r, int receiverPort) throws IOException 
	{
		emitter = e;
		receiver = r;
		this.receiverPort = receiverPort ; 
		messages = new ArrayList<Message>();
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
		
		// TODO Notify UI 
	}
	
	public abstract void sendMessage(String s);
	
	public abstract void sendMessage(File f);
	
	public abstract void startSession();
	
	public abstract void notifyStartSession() ;
	
	public abstract void closeSession();
	
	
	
}
