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
import com.chatsystem.localsystem.LocalSystem;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;
import com.chatsystem.utility.NetworkUtility;

import java.net.InetAddress;

final public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private LocalSessionListener listener;

	public LocalSession(User e, User r) throws IOException 
	{
		super(e,r);
		startSession();
	}
	
	public LocalSession(User e, User r, int receiverPort) throws IOException 
	{
		super(e,r,receiverPort);
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
		new NotifyStartSessionResponseTask((DatagramPacket)packetReceived) ; 
	}
	
	
	
	@Override
	public void closeSession() {
		listener.stopRun();
		socket.close();
		
		// Notify UI 
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

	@Override
	public void notifyCloseSession() {

		// TODO send close session message as runnable task 
		
	}
}










