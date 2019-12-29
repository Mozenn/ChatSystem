package com.insa.localsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.insa.message.Message;
import com.insa.message.SystemMessage;
import com.insa.user.User;
import com.insa.utility.SerializationUtility;

final class LocalCommunicationListener extends Thread {
	
	final private LocalSystem system ; 
	private MulticastSocket socket;
	private AtomicBoolean run;
	
	public LocalCommunicationListener(LocalSystem system) throws IOException 
	{
		this.system = system;
		this.socket = new MulticastSocket(LocalSystem.LISTENING_PORT);
		socket.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		run = new AtomicBoolean(); 
		run.set(true);
		start();
	}
	
	public void run() 
	{
		while(run.get()) 
		{
			byte[] buffer = new byte[Message.MAX_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				System.out.println("wait receive");
				socket.receive(packet);
				System.out.println("receive done");
			} catch (IOException e) { // receive failed 
				e.printStackTrace();
				continue; 
			} 

			SystemMessage.SystemMessageType type ; 
			SystemMessage msg ; 
			try
			{
				msg = (SystemMessage)SerializationUtility.deserializeMessage(packet.getData()); 
				type = msg.getSubtype(); 
				System.out.println(type);
			}
			catch(ClassCastException | IOException e) // not a system message 
			{
				continue; 
			}
			
			switch(type)
			{
				case SS: // session started with current local user of localSystem  
				try {
					system.createSessionResponse(packet); // TODO implement observer pattern 
				} catch (IOException e) {
					e.printStackTrace();
					continue ; 
				}
				break ;
				
				case CO: // new user connection 
				try {
					// Deserialization 
					User u = SerializationUtility.deserializeUser(msg.getContent()) ; 
					
					system.addLocalUser(u);  // TODO implement observer pattern 
					system.notifyConnectionResponse(packet);
					
				} catch (IOException e) {
					e.printStackTrace();
					continue ; 
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					continue ; 
				}
				break ;
				
				case CR: // response to CO broadcast 
				User u;
				try {
					u = SerializationUtility.deserializeUser(msg.getContent());
				} catch (JsonParseException e) {
					e.printStackTrace();
					return ; 
				} catch (JsonMappingException e) {
					e.printStackTrace();
					return ; 
				} catch (IOException e) {
					e.printStackTrace();
					return ; 
				} 
					
					System.out.println("victoire");
					system.addLocalUser(u);// TODO implement observer pattern 
				break ;
				
				
				default : 
					break ; 
			}
		}
	}
	
	public void stopRun() throws UnknownHostException, IOException 
	{
		run.set(false);
		socket.leaveGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		socket.close();
	}

	
	
	
	

}
