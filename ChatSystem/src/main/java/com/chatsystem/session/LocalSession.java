package com.chatsystem.session;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.SystemContract;
import com.chatsystem.system.LocalSystem;
import com.chatsystem.user.User;
import com.chatsystem.utility.NetworkUtility;

import java.net.InetAddress;

final public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private LocalSessionListener listener;


	public LocalSession(User e, User r, SystemContract system) throws IOException 
	{
		super(e,r,system);
		startSession();
	}
	
	public LocalSession(User e, User r, int receiverPort, SystemContract system) throws IOException 
	{
		super(e,r,receiverPort,system);
		startSession();
	}
	
	
	@Override
	public void startSession() {

		socket = NetworkUtility.getUDPSocketWithRandomPort() ; 
		
		listener = new LocalSessionListener(this, socket);
		
		System.out.println("LocalSession Started");
		
		DAO dao = new DAOSQLite() ; 
		
		messages = (ArrayList<UserMessage>) dao.getHistory(receiver.getId()) ; 
	}
	
	@Override
	public void notifyStartSession() 
	{
		new NotifyStartSessionTask(this,socket.getLocalPort()) ; 
	}
	
	@Override
	public void notifyStartSessionResponse(Object packetReceived)
	{
		new NotifyStartSessionResponseTask((DatagramPacket)packetReceived,socket.getLocalPort(),this) ; 
	}
	
	@Override
	public void notifyCloseSession() {

		new NotifyCloseSessionTask(this); 
		listener.stopRun();
	}
	
	@Override
	protected void closeSession() {
		listener.stopRun();
		/*
		if(!socket.isClosed())
			socket.close();
		*/ 
		system.closeSession(receiver);
	}

	@Override
	public void sendMessage(String s){
		
		UserMessage m;
		
		try {
			m = new UserMessage(s, receiver.getId(), emitter.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		new SendMessageTask(this,socket,m);
		
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
		
		new SendMessageTask(this,socket,m);
		
		addMessage(m) ; 
	}


}










