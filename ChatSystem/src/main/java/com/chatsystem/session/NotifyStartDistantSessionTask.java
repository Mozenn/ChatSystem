package com.chatsystem.session;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.system.CommunicationSystem;
import com.chatsystem.user.User;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

final class NotifyStartDistantSessionTask implements Runnable{
	
	private int sessionPort ; 
	private Session parentSession ; 
	
	public NotifyStartDistantSessionTask(Session parentSession, int sessionPort)
	{
		this.sessionPort = sessionPort ; 
		this.parentSession = parentSession ; 
		
		Thread thread = new Thread(this,"NotifyStartDistantSession") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		try (Socket socket = new Socket(parentSession.getReceiver().getIpAddress(),CommunicationSystem.DISTANT_LISTENING_PORT) ;)
		{
			byte[] serializedData = null;
			
			try {
				serializedData = SerializationUtility.serializeSessionData(new SessionData(parentSession.getEmitter(),sessionPort));
			} catch (JsonProcessingException e3) {
				e3.printStackTrace();
				return ; 
			} 
			
			SystemMessage msg;
			
			try {
				msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedData);
			} catch (IOException e2) {
				e2.printStackTrace();
				return ; 
			}
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			
			dOut.write(msgAsBytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		

		
	
		
	}

}
