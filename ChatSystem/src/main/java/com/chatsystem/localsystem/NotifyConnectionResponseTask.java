package com.chatsystem.localsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.user.User;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

final class NotifyConnectionResponseTask implements Runnable {
	
	private LocalSystem localSystem ; 
	private Thread thread; 
	InetAddress addr ;
	
	public NotifyConnectionResponseTask(LocalSystem localSystem,DatagramPacket packet) throws IOException, ClassNotFoundException
	{
		this.localSystem = localSystem ; 
		
		User u = SerializationUtility.deserializeUser(SerializationUtility.deserializeSystemMessage(packet.getData()).getContent());
		addr = u.getIpAddress();
		
		//Debug 
		/*
		System.out.println("longueur : "+u.getId().length);
		System.out.println("longueur : "+u.getIpAddress().length);
		System.out.println("longueur : "+u.getUsername().length());
		*/
		
		// Start thread 
		thread = new Thread(this,"NotifyConnectionResponse") ; 
		thread.start();
	}
	
	@Override
	public void run() {
		
		
		byte[] serializedUser;
		try {
			serializedUser = SerializationUtility.serializeUser(localSystem.getUser());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		SystemMessage msg = null;
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.CR, serializedUser);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("system message creation failed") ; 
			return ; 
		}
		
		DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
		
		try {
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, addr, LocalSystem.LISTENING_PORT));
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