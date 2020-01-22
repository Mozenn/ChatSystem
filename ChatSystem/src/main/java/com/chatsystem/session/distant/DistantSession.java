package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SystemContract;
import com.chatsystem.session.Session;
import com.chatsystem.session.local.NotifyCloseLocalSessionTask;
import com.chatsystem.session.local.SendLocalMessageTask;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

/*
 * Represent a chat between two Users that are on two different networks 
 */
public class DistantSession extends Session {
	
	private Socket socket ; 
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
	public DistantSession(User emitter, User receiver, Socket socket , SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
		this.socket = socket ; 
		startSession() ; 
	}

	@Override
	protected void startSession() {
		
		
		listener = new DistantSessionListener(this, socket);
		
		DAO dao;
		try {
			dao = new DAOSQLite();
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
		
		try 
		{
			socket = new Socket(getReceiver().getIpAddress(),getReceiver().getDistantPort()) ; 
			
			byte[] serializedUser = null;
			
			try {
				serializedUser = SerializationUtility.serializeUser(getEmitter());
			} catch (JsonProcessingException e3) {
				e3.printStackTrace();
				return ; 
			} 
			
			SystemMessage msg;
			
			try {
				msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedUser);
			} catch (IOException e2) {
				e2.printStackTrace();
				return ; 
			}
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			
			dOut.write(msgAsBytes);
			
			LoggerUtility.getInstance().info("NotifyStartDistantSession Sent");
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		startSession() ; 

	}

	@Override
	public void notifyCloseSession() {
		
		new NotifyCloseDistantSessionTask(socket); 
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

		 UserMessage m = new UserMessage(s, receiver.getId(), emitter.getId());
		
		new SendDistantMessageTask(socket,m);
		
		addMessage(m) ; 

	}

	@Override
	public void sendMessage(FileWrapper f) {
		
		UserMessage m;
		
		try {
			m = new UserMessage(f, receiver.getId(), emitter.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		new SendDistantMessageTask(socket,m);
		
		addMessage(m) ; 
	}


}
