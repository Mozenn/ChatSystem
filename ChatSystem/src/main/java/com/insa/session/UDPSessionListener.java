package com.insa.session;

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.insa.message.Message;
import com.insa.message.UserMessage;
import com.insa.utility.SerializationUtility;

public class UDPSessionListener extends Thread{
	
	private LocalSession session;
	private DatagramSocket socket;
	private AtomicBoolean run;
	
	public UDPSessionListener(LocalSession s, DatagramSocket socket) 
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
			DatagramPacket packet = new DatagramPacket(buffer, 0);
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
