package com.insa.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.insa.localsystem.LocalSystem;
import com.insa.message.SystemMessage;
import com.insa.utility.SerializationUtility;

class NotifyStartSessionTask implements Runnable{
	
	private final Session parentSession ; 
	private final DatagramSocket sendingSocket ; 
	
	public NotifyStartSessionTask(Session parentSession, DatagramSocket sendingSocket)
	{
		this.parentSession = parentSession  ; 
		this.sendingSocket = sendingSocket ; 
		
		Thread thread = new Thread(this,"NotifyStartSession") ; 
		thread.start();
	}

	@Override
	public void run() {
		
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
		
		InetAddress addr;
		
		try {
			addr = InetAddress.getByAddress(parentSession.getReceiver().getIpAddress());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return  ; 
		}
		
		try {
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			sendingSocket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, addr, LocalSystem.LISTENING_PORT));
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		System.out.println("NotifyStartSession sent");
		
	}

}
