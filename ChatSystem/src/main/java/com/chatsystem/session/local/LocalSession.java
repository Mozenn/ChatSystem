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
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SystemContract;
import com.chatsystem.session.Session;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.NetworkUtility;

/*
 * Represent a chat between two Users that are on same network
 */
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
		
		DAO dao;
		try {
			dao = new DAOSQLite();
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		} 
		
		messages = (ArrayList<UserMessage>) dao.getHistory(receiver.getId()) ; 
		
		LoggerUtility.getInstance().info("LocalSession Started");
	}
	
	/*
	 * Notify receiver of session started 
	 */
	@Override
	public void notifyStartSession() 
	{
		new NotifyStartLocalSessionTask(this,socket.getLocalPort()) ; 
	}
	
	/*
	 * Notify initiator of session confirmed
	 */
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

		system.closeSession(receiver);
		
		LoggerUtility.getInstance().info("LocalSession Closed");
	}

	@Override
	public void sendMessage(String s){
		
		UserMessage m = new UserMessage(s, receiver.getId(), emitter.getId());
	
		
		new SendLocalMessageTask(this,socket,m);
		
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
		
		new SendLocalMessageTask(this,socket,m);
		
		addMessage(m) ; 
		
	}


}










