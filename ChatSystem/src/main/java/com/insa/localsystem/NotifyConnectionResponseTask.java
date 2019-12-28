package com.insa.localsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.JSONObject;

import com.insa.message.Message;
import com.insa.message.SystemMessage;
import com.insa.user.User;
import com.insa.utility.NetworkUtility;

final class NotifyConnectionResponseTask implements Runnable {
	
	private LocalSystem localSystem ; 
	private Thread thread; 
	InetAddress addr ;
	
	public NotifyConnectionResponseTask(LocalSystem localSystem,DatagramPacket packet) throws IOException, ClassNotFoundException
	{
		this.localSystem = localSystem ; 
		
		JSONObject userJson = new JSONObject(new String(Message.extractContent(packet.getData()))) ; 
		User u = (User) userJson.get("user");
		addr = InetAddress.getByAddress(u.getIpAddress());
		
		//Debug 
		System.out.println("longueur : "+u.getId().length());
		System.out.println("longueur : "+u.getIpAddress().length);
		System.out.println("longueur : "+u.getUsername().length());
		
		// Start thread 
		thread = new Thread(this,"NotifyConnectionResponse") ; 
		thread.start();
	}
	
	@Override
	public void run() {
		
		
		byte[] serializedUser = null ;
		// Serialization to Json 
		JSONObject u = new JSONObject();
		u.put("user", localSystem.getUser()) ; 
		serializedUser = u.toString().getBytes();
		
		SystemMessage msg = null;
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("system message creation failed") ; 
			return ; 
		}
		
		DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
		
		try {
			socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalSystem.LISTENING_PORT));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("send failed") ; 
			socket.close();
			return ; 
		}

		System.out.println("CR notify sent");

		socket.close();

	}

}
