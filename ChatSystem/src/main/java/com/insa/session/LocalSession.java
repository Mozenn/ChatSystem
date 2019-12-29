package com.insa.session;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;

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
	}
	
	public void notifyStartSession() throws IOException 
	{
		new NotifyStartSessionTask(this,socket) ; 
	}
	
	
	
	@Override
	public void closeSession() {
		listener.stopRun();
		socket.close();
	}

	@Override
	public void sendMessage(String s){
		
		UserMessage m;
		
		try {
			m = new UserMessage(s, emitter.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		new SendMessageTask(this,socket,m);
	}

	@Override
	public void sendMessage(File f) {
		
		UserMessage m;
		
		try {
			m = new UserMessage(f, emitter.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		new SendMessageTask(this,socket,m);
	}
}










