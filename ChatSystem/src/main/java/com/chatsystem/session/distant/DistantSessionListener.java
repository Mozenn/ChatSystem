package com.chatsystem.session.distant;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationException;
import com.chatsystem.utility.SerializationUtility;

final class DistantSessionListener extends Thread {
	
	private DistantSession session;
	private DataInputStream stream;
	private AtomicBoolean run;
	
	/*
	 * @throws NullPointerException if stream is null 
	 */
	public DistantSessionListener(DistantSession s, InputStream stream) 
	{
		if(stream == null)
			throw new NullPointerException() ; 
		
		session = s;
		this.stream = new DataInputStream(stream);
		/*
		try {
			this.socket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}*/

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
				LoggerUtility.getInstance().info("DistantSessionListener Reading") ; 
				
				
				byte[] data = null;
				
				try
				{
					int length = stream.readInt();   
					// read length of incoming message
					if(length>0) {
						LoggerUtility.getInstance().info("DistantSessionListener Received") ; 
						data = new byte[length];
					    stream.readFully(data, 0, data.length); // read the message

					}
					else
					{
						//LoggerUtility.getInstance().info("Empty");
						try {
							sleep(1000) ;
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
						continue ; 
						
					}
				} catch( IOException e)
				{
					e.printStackTrace();
					continue ; 
				}

				UserMessage msg = null ; 
				
				try
				{
					msg = SerializationUtility.deserializeUserMessage(data);
				}
				catch(ClassCastException | SerializationException e) // Packet received is not a UserMessage  ;
				{
					try
					{
						SystemMessage sMsg = SerializationUtility.deserializeSystemMessage(data) ; 
						
						if(sMsg.getSubtype().equals(SystemMessage.SystemMessageType.CS))
						{
							LoggerUtility.getInstance().info("DistantSessionListener close Session received") ; 
							session.closeSession(); 
							return ; 
						}
					}
					catch(ClassCastException | SerializationException e2)
					{
						continue ; 
					}
					continue ; 
				} 

				LoggerUtility.getInstance().info("DistantSession Message Received");
				session.addMessage(msg);	
				
				try {
					sleep(1000) ;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 

			}
		
	}
	
}


