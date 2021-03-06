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
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;

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
		
		serializedData = SerializationUtility.serializeSessionData(new SessionData(parentSession.getEmitter(),sessionPort));

		
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
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, addr, parentSession.getReceiver().getLocalPort()));
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		LoggerUtility.getInstance().info("NotifyStartLocalSession sent");
		
		
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

			LoggerUtility.getInstance().info("NotifyStartLocalSession wait receive");
			
			while(!bStop && t - System.currentTimeMillis() < 1000)
			{
				socket.receive(packet);
				
				SystemMessage receivedMsg = SerializationUtility.deserializeSystemMessage(packet.getData()); 
				
				if(receivedMsg.getSystemMessageType().equals(SystemMessage.SystemMessageType.SR)) // session valid 
				{
					SessionData s = SerializationUtility.deserializeSessionData(receivedMsg.getContent());
					
					parentSession.setReceiverPort(s.getPort()) ; 
					
					LoggerUtility.getInstance().info("NotifyStartLocalSession session confirmed");
					socket.close();
					return ; 
				}
					
			}

			LoggerUtility.getInstance().info("NotifyStartLocalSession not received");
			socket.close();
			parentSession.closeSession();
			
		} catch (SocketTimeoutException e) { 
			socket.close();
			LoggerUtility.getInstance().info("NotifyStartLocalSession not received");
			parentSession.closeSession();

		}catch (IOException e) {
			e.printStackTrace();
			
			socket.close();
			parentSession.closeSession();
		}
		
	}

}
