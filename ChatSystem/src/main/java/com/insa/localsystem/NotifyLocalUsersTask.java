package com.insa.localsystem;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.insa.message.SystemMessage;
import com.insa.utility.SerializationUtility;

final class NotifyLocalUsersTask implements Runnable 
{

	private LocalSystem localSystem ; 
	private Thread thread; 
;
	
	public NotifyLocalUsersTask(LocalSystem localSystem) throws UnknownHostException
	{
		this.localSystem = localSystem ; 
	
		// Start Thread 
		this.thread = new Thread(this,"NotifyLocalUsers") ; 
		this.thread.start();
	}
	
	@Override
	public void run() {
		
		// Write User to byte and initial message 
		
		SystemMessage msg = null;

		byte[] serializedUser;
		try {
			serializedUser = SerializationUtility.serializeUser(localSystem.getUser()) ; 
			
			// Sending message 
			msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
			
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		
		
		// Send message with user as content 
					
		
		try(MulticastSocket socket = new MulticastSocket())
		{
			try {
				byte[] msgAsBytes = SerializationUtility.serializeMessage(msg); 
				socket.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
				socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, InetAddress.getByName(LocalSystem.MULTICAST_ADDR), LocalSystem.LISTENING_PORT));
				socket.leaveGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			socket.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 

		


	}
}
