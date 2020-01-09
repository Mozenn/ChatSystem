package com.chatsystem.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

public class NotifyStartLocalSessionResponseTask implements Runnable {
	
	private DatagramPacket packetReceived ; 
	private final int sessionPort ; 
	private final Session parentSession ; 
	
	public NotifyStartLocalSessionResponseTask(DatagramPacket packetReceived,int sessionPort, Session parentSession)
	{
		this.packetReceived = packetReceived  ; 
		this.sessionPort = sessionPort ; 
		this.parentSession = parentSession ; 
		
		Thread thread = new Thread(this,"NotifyStartSessionResponse") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		byte[] serializedData = null;
		
		try {
			serializedData = SerializationUtility.serializeSessionData(new SessionData(parentSession.getEmitter(),sessionPort));
		} catch (JsonProcessingException e3) {
			e3.printStackTrace();
			return ; 
		} 
		
		SystemMessage msg;
		
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.SR, serializedData); 
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		 
		 DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
		
		try {
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, packetReceived.getAddress(), packetReceived.getPort()));
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
			return ; 
		}
		
		System.out.println("NotifyStartSessionResponse sent");
		
		socket.close();
	}

}
