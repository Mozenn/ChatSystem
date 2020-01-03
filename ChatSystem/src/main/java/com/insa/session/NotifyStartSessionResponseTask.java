package com.insa.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.insa.message.SystemMessage;
import com.insa.utility.NetworkUtility;
import com.insa.utility.SerializationUtility;

public class NotifyStartSessionResponseTask implements Runnable {
	
	private DatagramPacket packetReceived ; 
	
	public NotifyStartSessionResponseTask(DatagramPacket packetReceived)
	{
		this.packetReceived = packetReceived  ; 
		
		Thread thread = new Thread(this,"NotifyStartSessionResponse") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		
		SystemMessage msg;
		
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.SR, new byte[8]); // content is not used 
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
