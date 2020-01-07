package com.chatsystem.session;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chatsystem.message.Message;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class LocalSessionListener extends Thread{
	
	private LocalSession session;
	private DatagramSocket socket;
	private AtomicBoolean run;
	
	public LocalSessionListener(LocalSession s, DatagramSocket socket) 
	{
		session = s;
		this.socket = socket;

		try {
			this.socket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		run = new AtomicBoolean(); 
		run.set(true);
		start();
	}
	
	public void stopRun() 
	{
		if(run.get())
		{
			run.set(false);
			//socket.close(); 
			
		}

	}
	
	public void run() 
	{
		while(run.get()) 
		{
			byte[] buffer = new byte[Message.MAX_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
			}
			catch(SocketTimeoutException e)
			{
				continue ; 
			}
			catch (IOException e) {
				e.printStackTrace();
				continue ; 
			} 

			
			UserMessage msg = null ; 
			
			try
			{
				msg = SerializationUtility.deserializeUserMessage(packet.getData());
			}
			catch(ClassCastException e) // Packet received is not a UserMessage  ;
			{
				try
				{
					SystemMessage sMsg = SerializationUtility.deserializeSystemMessage(packet.getData()) ; 
					
					if(sMsg.getSubtype().equals(SystemMessage.SystemMessageType.CS))
					{
						session.closeSession(); // TODO Use ObserverPattern 
						return ; 
					}
				}
				catch(ClassCastException | IOException e2)
				{
					continue ; 
				}
				continue ; 
			} catch (JsonParseException e) {
				e.printStackTrace();
				continue ; 
			} catch (JsonMappingException e) {
				e.printStackTrace();
				continue ; 
			} catch (IOException e) {
				e.printStackTrace();
				continue ; 
			}

			System.out.println("LocalSession Message Received");
			session.addMessage(msg);	// TODO implement observer 		
		}
		
		socket.close(); 
	}
	

}
