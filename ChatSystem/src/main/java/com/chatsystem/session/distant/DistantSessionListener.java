package com.chatsystem.session.distant;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

final class DistantSessionListener extends Thread {
	
	private DistantSession session;
	private Socket socket;
	private AtomicBoolean run;
	
	public DistantSessionListener(DistantSession s, Socket socket) 
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
			
		}

	}
	
	@Override 
	public void run()
	{
		while(run.get()) 
		{
			
			try (DataInputStream dIn = new DataInputStream(socket.getInputStream());)
			{
				byte[] data = dIn.readAllBytes() ; // TODO test if that is blocking or not 
				
				UserMessage msg = null ; 
				
				try
				{
					msg = SerializationUtility.deserializeUserMessage(data);
				}
				catch(ClassCastException | JsonMappingException e) // Packet received is not a UserMessage  ;
				{
					try
					{
						SystemMessage sMsg = SerializationUtility.deserializeSystemMessage(data) ; 
						
						if(sMsg.getSubtype().equals(SystemMessage.SystemMessageType.CS))
						{
							System.out.println("DistantSessionListener close Session received") ; 
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
				} catch (IOException e) {
					e.printStackTrace();
					continue ; 
				}

				System.out.println("DistantSession Message Received");
				session.addMessage(msg);	// TODO implement observer 	
				
			} catch (IOException e1) {
				e1.printStackTrace();
			} 

		}
		
	}
	
}


