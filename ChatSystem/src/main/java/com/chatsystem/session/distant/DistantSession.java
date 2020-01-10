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
import com.chatsystem.model.SystemContract;
import com.chatsystem.session.Session;
import com.chatsystem.session.local.NotifyCloseLocalSessionTask;
import com.chatsystem.session.local.SendLocalMessageTask;
import com.chatsystem.user.User;
import com.chatsystem.utility.NetworkUtility;

public class DistantSession extends Session {
	
	private Socket socket ; 
	private DistantSessionListener listener ; 
	
	public DistantSession(User emitter, User receiver, SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
	}
	
	public DistantSession(User emitter, User receiver, Socket socket , SystemContract system) throws IOException
	{
		super(emitter,receiver,system) ; 
		this.socket = socket ; 
		startSession() ; 
	}

	@Override
	protected void startSession() {
		
		
		listener = new DistantSessionListener(this, socket);
		
		System.out.println("DistantSession Started");
		
		DAO dao = new DAOSQLite() ; 
		
		messages = (ArrayList<UserMessage>) dao.getHistory(receiver.getId()) ; 

	}

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

	}
	
	@Override
	public void sendMessage(String s) {
		
		UserMessage m;
		
		try {
			m = new UserMessage(s, receiver.getId(), emitter.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		new SendDistantMessageTask(socket,m);
		
		addMessage(m) ; 

	}

	@Override
	public void sendMessage(File f) {
		
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
