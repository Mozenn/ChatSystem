package com.insa.localsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import com.insa.message.Message;
import com.insa.message.SystemMessage;
import com.insa.user.User;

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
			String subtype = new String(Message.extractSubtype(packet.getData()));
			System.out.println(subtype);
			System.out.println(packet.getAddress().toString());
			SystemMessage.SystemMessageType type ; 
			
			try
			{
				type = SystemMessage.SystemMessageType.valueOf(subtype);
			}
			catch(Exception e) // not a system message 
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
					JSONObject userJson = new JSONObject(new String(Message.extractContent(packet.getData()))) ; 
					User u = (User) userJson.get("user");
					
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
					JSONObject userJson = new JSONObject(new String(Message.extractContent(packet.getData()))) ; 
					User u = (User) userJson.get("user");
					
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
