package com.chatsystem.system;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.system.NotifyLocalUsersTask.LocalNotifyType;
import com.chatsystem.utility.SerializationUtility;

final class NotifyChangeUsernameLocalTask implements Runnable{
	
	private CommunicationSystem localSystem ; 
	private Thread thread; 
	
	public Thread getThread()
	{
		return this.thread ; 
	}
;
	
	public NotifyChangeUsernameLocalTask(CommunicationSystem localSystem) 
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
			serializedUser = SerializationUtility.serializeUser(localSystem.getUser().get()) ; 

			msg = new SystemMessage(SystemMessage.SystemMessageType.CU, serializedUser);

		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		
		
		// Send message with user as content 
					
		
		try(MulticastSocket socket = new MulticastSocket())
		{
			try {
				byte[] msgAsBytes = SerializationUtility.serializeMessage(msg); 
				socket.joinGroup(InetAddress.getByName(CommunicationSystem.MULTICAST_ADDR));
				socket.send(new DatagramPacket(msgAsBytes, msgAsBytes.length, InetAddress.getByName(CommunicationSystem.MULTICAST_ADDR), CommunicationSystem.LOCAL_LISTENING_PORT));
				socket.leaveGroup(InetAddress.getByName(CommunicationSystem.MULTICAST_ADDR));
				
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
		
		System.out.println("CU notify multicasted");
		
		

	}

}
