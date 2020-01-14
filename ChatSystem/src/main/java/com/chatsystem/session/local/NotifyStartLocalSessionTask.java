package com.chatsystem.session.local;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.chatsystem.message.Message;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.session.SessionData;
import com.chatsystem.system.CommunicationSystem;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

class NotifyStartLocalSessionTask implements Runnable{
	
	private final LocalSession parentSession ; 
	private final int sessionPort ; 
	
	public NotifyStartLocalSessionTask(LocalSession parentSession, int sessionPort)
	{
		this.parentSession = parentSession  ; 
		this.sessionPort = sessionPort ; 
		
		Thread thread = new Thread(this,"NotifyStartLocalSession") ; 
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
			msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedData);
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		
		 InetAddress addr = parentSession.getReceiver().getIpAddress();
		 
		 DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
		
		try {
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, addr, CommunicationSystem.LOCAL_LISTENING_PORT));
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		System.out.println("NotifyStartSession sent");
		
		
		try {
			socket.setSoTimeout(1000); // wait 1s max 
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		byte[] buffer = new byte[Message.MAX_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			boolean bStop = false ; 
			
			long t = System.currentTimeMillis();
			System.out.println("NotifyStartSession wait receive");
			while(!bStop && t - System.currentTimeMillis() < 1000)
			{
				socket.receive(packet);
				
				SystemMessage receivedMsg = SerializationUtility.deserializeSystemMessage(packet.getData()); 
				
				if(receivedMsg.getSubtype().equals(SystemMessage.SystemMessageType.SR)) // session valid 
				{
					SessionData s = SerializationUtility.deserializeSessionData(receivedMsg.getContent());
					
					parentSession.setReceiverPort(s.getPort()) ; 
					
					System.out.println("NotifyStartSession session confirmed");
					socket.close();
					return ; 
				}
					
			}

			System.out.println("NotifyStartSession not received");
			socket.close();
			parentSession.closeSession();
			
		} catch (SocketTimeoutException e) { 
			socket.close();
			System.out.println("NotifyStartSession not received");
			parentSession.closeSession();

		}catch (IOException e) {
			e.printStackTrace();
			
			socket.close();
			parentSession.closeSession();
		}
		
	}

}