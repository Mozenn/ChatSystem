package com.insa.session;

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

import com.insa.dao.DAO;
import com.insa.dao.DAOSQLite;
import com.insa.localsystem.LocalSystem;
import com.insa.message.SystemMessage;
import com.insa.message.UserMessage;
import com.insa.user.User;
import com.insa.utility.NetworkUtility;

import java.net.InetAddress;

final public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private UDPSessionListener listener;

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
		
		listener = new UDPSessionListener(this, socket);
		
		System.out.println("LocalSession Started");
		
		DAO dao = new DAOSQLite() ; 
		
		messages = (ArrayList<UserMessage>) dao.getHistory(receiver.getId()) ; 
	}
	
	public void notifyStartSession() 
	{
		new NotifyStartSessionTask(this,socket.getLocalPort()) ; 
	}
	
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
}










