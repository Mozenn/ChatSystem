package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.chatsystem.dao.MessageDAO;
import com.chatsystem.dao.MessageDAOSQLite;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SystemContract;
import com.chatsystem.session.Session;
import com.chatsystem.session.SessionData;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

/*
 * Represent a chat between two Users that are on two different networks 
 */
public class DistantSession extends Session {
	
	private Socket communicationSocket ; 
	private DistantSessionListener listener ; 
	
	/*
	 * @inheritDoc 
	 * Called by session initiator 
	 */
	public DistantSession(User emitter, User receiver, SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
		
	}
	
	/*
	 * @inheritDoc 
	 * Called by session receiver, after receiving SS SystemMessage  
	 */
	public DistantSession(User emitter, User receiver, int portToReach , SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
		
		initializeConnection(portToReach) ; 
	}
	
	public void initializeConnection(int portToReach) throws IOException
	{
		LoggerUtility.getInstance().info("DistantSession Address to reach " + receiver.getIpAddress().toString());
		communicationSocket = new Socket(receiver.getIpAddress(),portToReach) ; 
		
		startSession() ; 
	}

	@Override
	protected void startSession() {
		
		
		try {
			listener = new DistantSessionListener(this, communicationSocket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		MessageDAO dao;
		try {
			dao = new MessageDAOSQLite();
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		} 
		
		messages = (ArrayList<UserMessage>) dao.getHistory(receiver.getId()) ; 
		
		LoggerUtility.getInstance().info("DistantSession Started");

	}

	/*
	 * @inheritDoc 
	 * Called by session initiator 
	 */
	@Override
	public void notifyStartSession() {
		

		try (ServerSocket servSocket = new ServerSocket(0))
		{
			try 
			{
				Socket s = new Socket(getReceiver().getIpAddress(),getReceiver().getDistantPort()) ; 
				
				byte[] serializedData = null;
				
				serializedData = SerializationUtility.serializeSessionData(new SessionData(getEmitter(),servSocket.getLocalPort()));
	
				
				SystemMessage msg;
				
				try {
					msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedData);
				} catch (IOException e2) {
					e2.printStackTrace();
					return ; 
				}
				
				byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
				
				DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
				
				dOut.writeInt(msgAsBytes.length);
				dOut.write(msgAsBytes);
				dOut.flush();
				
				LoggerUtility.getInstance().info("NotifyStartDistantSession Port " + servSocket.getLocalPort());
				LoggerUtility.getInstance().info("NotifyStartDistantSession Address " + servSocket.getInetAddress());
				LoggerUtility.getInstance().info("NotifyStartDistantSession Sent");
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			
			try {
				LoggerUtility.getInstance().info("NotifyStartDistantSession Waiting for receiver Initialization");
				
				// Wait for receiver to initialize connection 
				communicationSocket = servSocket.accept() ;
				
				LoggerUtility.getInstance().info("NotifyStartDistantSession Connection Initialized");
				
			} catch (IOException e) {
				e.printStackTrace();
				return ; 
			} 
			
			
			startSession() ; 
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		


	}

	@Override
	public void notifyCloseSession() {
		
		try {
			new NotifyCloseDistantSessionTask(communicationSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		listener.stopRun();

	}

	/*
	 * Called when CS notify received 
	 */
	@Override
	protected void closeSession() {
		listener.stopRun();
		system.closeSession(receiver);
		
		LoggerUtility.getInstance().info("DistantSession Closed");
	}
	
	@Override
	public void sendMessage(String s) {

		LoggerUtility.getInstance().info("DistantSession Sending Text Message");
		
		 UserMessage m = new UserMessage(s, receiver.getId(), emitter.getId());
		 
		try {
			new SendDistantMessageTask(communicationSocket.getOutputStream(),m);
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		addMessage(m) ; 

	}

	@Override
	public void sendMessage(FileWrapper f) {
		
		LoggerUtility.getInstance().info("DistantSession Sending File Message");
		
		UserMessage m;
		
		try {
			m = new UserMessage(f, receiver.getId(), emitter.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		try {
			new SendDistantMessageTask(communicationSocket.getOutputStream(),m);
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		addMessage(m) ; 
	}


}
