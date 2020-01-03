package com.insa.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.insa.localsystem.LocalSystem;
import com.insa.message.Message;
import com.insa.message.SystemMessage;
import com.insa.utility.NetworkUtility;
import com.insa.utility.SerializationUtility;

class NotifyStartSessionTask implements Runnable{
	
	private final Session parentSession ; 
	private final int sessionPort ; 
	
	public NotifyStartSessionTask(Session parentSession, int sessionPort)
	{
		this.parentSession = parentSession  ; 
		this.sessionPort = sessionPort ; 
		
		Thread thread = new Thread(this,"NotifyStartSession") ; 
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
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, addr, LocalSystem.LISTENING_PORT));
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
			socket.receive(packet);
		} catch (SocketTimeoutException e) { // TODO modify this to check if received message is SR and limit to 1sec at same time 
			socket.close();
			parentSession.closeSession();

		}catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}

}
