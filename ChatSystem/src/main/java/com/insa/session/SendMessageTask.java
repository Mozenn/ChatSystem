package com.insa.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.insa.message.UserMessage;

public class SendMessageTask implements Runnable{
	
	private final Session parentSession ; 
	private final DatagramSocket sendingSocket ; 
	private final UserMessage messageToSend ; 
	
	public SendMessageTask(Session parentSession, DatagramSocket sendingSocket, UserMessage message )
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
		try {
			buffer = messageToSend.toByteArray();
			InetAddress ipAdd = InetAddress.getByAddress(parentSession.getReceiver().getIpAddress()) ;  // TODO check if that is correct 
			packet = new DatagramPacket(buffer, buffer.length, ipAdd ,parentSession.getReceiverPort());
			
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		
		try {
			
			sendingSocket.send(packet); 
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("send failed") ; 
			return ; 
		}
		
	}

}
