package com.chatsystem.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;


public class NotifyCloseLocalSessionTask implements Runnable{
	
	private final Session parentSession ; 
	
	public NotifyCloseLocalSessionTask(Session parentSession)
	{
		this.parentSession = parentSession  ; 
		
		Thread thread = new Thread(this,"NotifyCloseSession") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		SystemMessage msg;
		
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.CS, new byte[10]); // content not used 
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		
		 InetAddress addr = parentSession.getReceiver().getIpAddress();
		 
		 DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
		
		try {
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, addr, parentSession.getReceiverPort()));
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		System.out.println("NotifyCloseSession sent");
		
		
	}

}
