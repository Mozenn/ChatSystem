package com.chatsystem.system;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.SystemMessage.SystemMessageType;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;



final class NotifyLocalUsersTask implements Runnable 
{
	
	public enum LocalNotifyType
	{
		CONNECTION("CO"), 
		DISCONNECTION("DC"); 

	    private String type;

	    LocalNotifyType(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}

	private CommunicationSystem localSystem ; 
	private Thread thread; 
	private final LocalNotifyType type ;
	
	public Thread getThread()
	{
		return this.thread ; 
	}
;
	
	public NotifyLocalUsersTask(CommunicationSystem localSystem, LocalNotifyType type ) 
	{
		this.localSystem = localSystem ; 
	
		// Start Thread 
		this.thread = new Thread(this,"NotifyLocalUsers") ; 
		this.thread.start();
		this.type = type ; 
	}
	
	@Override
	public void run() {
		
		// Write User to byte and initial message 
		
		SystemMessage msg = null;

		byte[] serializedUser;
		try {
			serializedUser = SerializationUtility.serializeUser(localSystem.getUser().get()) ; 
			
			// Sending message 
			switch(type)
			{
				case CONNECTION :
					msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
					break ; 
				case DISCONNECTION:
					msg = new SystemMessage(SystemMessage.SystemMessageType.DC, serializedUser);
					break ; 
			}
			
			
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
		
		switch(type)
		{
			case CONNECTION :
				LoggerUtility.getInstance().info("CO notify multicasted");
				break ; 
			case DISCONNECTION:
				LoggerUtility.getInstance().info("DC notify multicasted");
				break ; 
		}
		
		

	}
}
