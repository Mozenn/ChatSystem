package session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import defo.User;
import message.Message;
import message.UserMessage;

public abstract class Session {

	protected byte[] id;
	protected User emitter;
	protected User receiver;
	protected ArrayList<Message> messages;
	
	public Session()
	{
		
	}
	
	/*public Session(User u) 
	{
		emitter = u;
	}*/
	
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
	
	public void addMessage(Message m) 
	{
		messages.add(m);
		// TODO: save to database.
	}
	
	public abstract void sendMessage(UserMessage m);
	
	public abstract void startSession();
	
	public abstract void closeSession();
	
	
	
}
