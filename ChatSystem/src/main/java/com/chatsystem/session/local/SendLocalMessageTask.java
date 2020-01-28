package com.chatsystem.session.local;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.chatsystem.message.UserMessage;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

public class SendLocalMessageTask implements Runnable{
	
	private final LocalSession parentSession ; 
	private final DatagramSocket sendingSocket ; 
	private final UserMessage messageToSend ; 
	
	public SendLocalMessageTask(LocalSession parentSession, DatagramSocket sendingSocket, UserMessage message )
	{
		this.parentSession = parentSession  ; 
		this.sendingSocket = sendingSocket ; 
		this.messageToSend = message ; 
		
		Thread thread = new Thread(this,"NotifyStartSession") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		byte[] buffer;
		
		DatagramPacket packet ; 

		buffer = SerializationUtility.serializeMessage(messageToSend); 
		InetAddress ipAdd = parentSession.getReceiver().getIpAddress() ;  
		packet = new DatagramPacket(buffer, buffer.length, ipAdd ,parentSession.getReceiverPort());
			

		
		
		try {
			
			sendingSocket.send(packet); 
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("send failed") ; 
			return ; 
		}
		
		LoggerUtility.getInstance().info("SendLocalSessionMessage sent");
		
	}

}
