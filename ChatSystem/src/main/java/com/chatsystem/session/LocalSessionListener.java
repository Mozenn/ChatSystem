package com.chatsystem.session;

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chatsystem.message.Message;
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
		run.set(true);
		start();
	}
	
	public void stopRun() 
	{
		run.set(false);
	}
	
	public void run() 
	{
		while(run.get()) 
		{
			byte[] buffer = new byte[Message.MAX_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
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
				// TODO Check if packet is CloseSession message, if so, call close session (using observer pattern ?) 
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

			
			session.addMessage(msg);	// TODO implement observer 		
		}
	}
	

}
