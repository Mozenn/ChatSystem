package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.session.Session;
import com.chatsystem.system.CommunicationSystem;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

final class NotifyStartDistantSessionTask implements Runnable{
	
	private Socket socket; 
	private Session parentSession ; 
	private Thread thread ; 
	
	public NotifyStartDistantSessionTask(Session parentSession, Socket socket)
	{
		this.socket = socket ; 
		this.parentSession = parentSession ; 
		
		thread = new Thread(this,"NotifyStartDistantSession") ; 
		thread.start();
	}
	
	public Thread getThread()
	{
		return this.thread ; 
	}

	@Override
	public void run() {
		
		try 
		{
			socket = new Socket(parentSession.getReceiver().getIpAddress(),CommunicationSystem.DISTANT_LISTENING_PORT) ; 
			
			byte[] serializedUser = null;
			
			try {
				serializedUser = SerializationUtility.serializeUser(parentSession.getEmitter());
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
		

		
	
		
	}

}
