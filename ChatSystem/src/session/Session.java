package session;

import java.util.ArrayList;

import defo.Message;
import defo.User;
import defo.UserMessage;

public abstract class Session {

	protected byte[] id;
	protected User emitter;
	protected User receiver;
	protected ArrayList<Message> messages;
	
	public Session()
	{
		
	}
	
	public Session(byte[] id, User e, User r) 
	{
		this.id = id;
		emitter = e;
		receiver = r;
		messages = new ArrayList<Message>();
	}
	
	public void addMessage(Message m) 
	{
		messages.add(m);
		// TODO: save to database.
	}
	
	public abstract void sendMessage(UserMessage m);
	
	public abstract void startSession();
	
	public abstract void closeSession();
	
	
	
}
