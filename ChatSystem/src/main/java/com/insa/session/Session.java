package com.insa.session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.insa.message.Message;
import com.insa.message.UserMessage;
import com.insa.user.User;

public abstract class Session {

	protected byte[] id;
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(e.getID());
		stream.write(r.getID());
		id = stream.toByteArray();
		messages = new ArrayList<Message>();
	}
	
	public Session(User e, User r, int receiverPort) throws IOException 
	{
		emitter = e;
		receiver = r;
		this.receiverPort = receiverPort ; 
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(e.getID());
		stream.write(r.getID());
		id = stream.toByteArray();
		messages = new ArrayList<Message>();
	}
	
	public void addMessage(Message m) 
	{
		synchronized(messages)
		{
			messages.add(m);
		}

		// TODO: save to database.
	}
	
	public abstract void sendMessage(String s);
	
	public abstract void sendMessage(File f);
	
	public abstract void startSession();
	
	public abstract void closeSession();
	
	
	
}
