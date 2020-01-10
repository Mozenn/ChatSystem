package com.chatsystem.session.local;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.SystemContract;
import com.chatsystem.session.Session;
import com.chatsystem.user.User;
import com.chatsystem.utility.NetworkUtility;


final public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private LocalSessionListener listener;
	private int receiverPort ; 


	public LocalSession(User e, User r, SystemContract system) throws IOException 
	{
		super(e,r,system);
		startSession();
	}
	
	public LocalSession(User e, User r, int receiverPort, SystemContract system) throws IOException 
	{
		super(e,r,system);
		this.receiverPort = receiverPort ; 
 		startSession();
	}
	
	public int getReceiverPort() {
		return receiverPort;
	}
	
	protected void setReceiverPort(int port)
	{
		this.receiverPort = port ; 
	}
	
	
	@Override
	protected void startSession() {

		socket = NetworkUtility.getUDPSocketWithRandomPort() ; 
		
		listener = new LocalSessionListener(this, socket);
		
		System.out.println("LocalSession Started");
		
		DAO dao = new DAOSQLite() ; 
		
		messages = (ArrayList<UserMessage>) dao.getHistory(receiver.getId()) ; 
	}
	
	@Override
	public void notifyStartSession() 
	{
		new NotifyStartLocalSessionTask(this,socket.getLocalPort()) ; 
	}
	
	
	public void StartSessionResponse(InetAddress address, int port)
	{
		new NotifyStartLocalSessionResponseTask(address,port,socket.getLocalPort(),this) ; 
	}
	
	@Override
	public void notifyCloseSession() {

		new NotifyCloseLocalSessionTask(this); 
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
		
		new SendLocalMessageTask(this,socket,m);
		
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
		
		new SendLocalMessageTask(this,socket,m);
		
		addMessage(m) ; 
	}


}










