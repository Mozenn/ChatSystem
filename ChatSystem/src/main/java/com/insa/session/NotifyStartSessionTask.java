package com.insa.session;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONObject;

import com.insa.localsystem.LocalSystem;
import com.insa.message.SystemMessage;

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
		
		// Serialization to Json 
		JSONObject u = new JSONObject();
		u.put("user", parentSession.getEmitter()) ; 
		byte[] serializedUser = u.toString().getBytes();
		
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
			sendingSocket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalSystem.LISTENING_PORT));
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
	}

}
