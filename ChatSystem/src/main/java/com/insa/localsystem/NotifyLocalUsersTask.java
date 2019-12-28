package com.insa.localsystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.insa.message.SystemMessage;
import com.insa.utility.NetworkUtility;

import java.net.InetAddress;

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
		
		// Write User to byte 
		
		SystemMessage msg = null;

		byte[] serializedUser;
		try {
			serializedUser = localSystem.getUser().getSerialized();
			msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		
		
		// Send message with user as content 
			
		InetAddress addr = null;
		
		try {
			addr = InetAddress.getByName(LocalSystem.MULTICAST_ADDR);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return ; 
		}
		
		
		try(MulticastSocket socket = new MulticastSocket())
		{
			try {
				socket.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
				socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, InetAddress.getByName(LocalSystem.MULTICAST_ADDR), LocalSystem.LISTENING_PORT));
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
