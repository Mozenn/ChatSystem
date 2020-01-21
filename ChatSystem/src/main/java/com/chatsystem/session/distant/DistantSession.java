package com.chatsystem.session.distant;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SystemContract;
import com.chatsystem.session.Session;
import com.chatsystem.session.local.NotifyCloseLocalSessionTask;
import com.chatsystem.session.local.SendLocalMessageTask;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.NetworkUtility;

public class DistantSession extends Session {
	
	private Socket socket ; 
	private DistantSessionListener listener ; 
	
	/*
	 * Called by session initiator 
	 */
	public DistantSession(User emitter, User receiver, SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
	}
	
	/*
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
		
		var t = new NotifyStartDistantSessionTask(this, socket) ; 
		
		try {
			t.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		startSession() ; 

	}

	@Override
	public void notifyCloseSession() {
		
		new NotifyCloseDistantSessionTask(socket); 
		listener.stopRun();

	}

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
