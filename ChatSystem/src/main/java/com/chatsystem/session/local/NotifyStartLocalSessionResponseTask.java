package com.chatsystem.session.local;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.session.Session;
import com.chatsystem.session.SessionData;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;

public class NotifyStartLocalSessionResponseTask implements Runnable {
	
	private InetAddress targetAddress ; 
	private int targetPort ; 
	private final int sessionPort ; 
	private final Session parentSession ; 
	
	public NotifyStartLocalSessionResponseTask(InetAddress targetAddress, int targetPort, int sessionPort, Session parentSession)
	{
		this.targetAddress = targetAddress  ; 
		this.targetPort = targetPort  ; 
		this.sessionPort = sessionPort ; 
		this.parentSession = parentSession ; 
		
		Thread thread = new Thread(this,"NotifyStartSessionResponse") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		byte[] serializedData = null;
		

		serializedData = SerializationUtility.serializeSessionData(new SessionData(parentSession.getEmitter(),sessionPort));

		
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
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, targetAddress, targetPort));
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
			return ; 
		}
		
		LoggerUtility.getInstance().info("NotifyStartLocalSessionResponse sent");

		
		socket.close();
	}

}
