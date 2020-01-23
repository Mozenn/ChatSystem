package com.chatsystem.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.session.SessionData;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

final class DistantCommunicationTask implements Runnable {
	
	    private Socket clientSocket ; 
	    final private CommunicationSystem system ; 

	    public DistantCommunicationTask(Socket clientSocket, CommunicationSystem system)
	    {
	        this.clientSocket = clientSocket ;  
	        this.system = system ; 
	    }

	@Override
	public void run() {
		
		LoggerUtility.getInstance().info("DistantCommunicationListenerTask Running ");
		
		try ( DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());)
		{
			
			SystemMessage msg ; 
			try
			{
				int length = dIn.readInt();   
				byte[] message = null ; 
				// read length of incoming message
				if(length>0) {
				     message = new byte[length];
				    dIn.readFully(message, 0, message.length); // read the message
				    msg = SerializationUtility.deserializeSystemMessage(message);
				}
				else
				{
					LoggerUtility.getInstance().info("Empty");
					return ; 
				}
				
			}
			catch(ClassCastException | IOException e) // not a system message 
			{
				e.printStackTrace();
				return ; 
			}
			switch(msg.getSubtype())
			{
				case CO:
				{
					try {
						// Deserialization 
						User u = SerializationUtility.deserializeUser(msg.getContent()) ; 
						LoggerUtility.getInstance().info("DistantCommunicationListener CO received ");
						
						system.addDistantUser(u);
						
					} catch (IOException e) {
						e.printStackTrace();
					} 
					break ;
				}
				case DC:
				{
					try {
						// Deserialization 
						User u = SerializationUtility.deserializeUser(msg.getContent()) ; 
						
						LoggerUtility.getInstance().info("DistantCommunicationListener DC received ");
						system.removeDistantUser(u); 
						
					} catch (IOException e) {
						e.printStackTrace();
					} 
					break ;
				}
				case SS:
				{
					SessionData sd = SerializationUtility.deserializeSessionData(msg.getContent());
					
					LoggerUtility.getInstance().info("DistantCommunicationListener SS received ");
					
					system.onDistantSessionRequest(sd.getUser(), sd.getPort());
				}
				default:
					break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		LoggerUtility.getInstance().info("DistantCommunicationListenerTask Quit ");
		
	}

}
