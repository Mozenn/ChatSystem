package com.chatsystem.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.session.SessionData;
import com.chatsystem.user.User;
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
		
		try (DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());)
		{
			SystemMessage msg ; 
			try
			{
				msg = SerializationUtility.deserializeSystemMessage(dIn.readAllBytes());
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
						
						System.out.println("DistantCommunicationListener CO received ");
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
						
						System.out.println("DistantCommunicationListener DC received ");
						system.removeDistantUser(u); 
						
					} catch (IOException e) {
						e.printStackTrace();
					} 
					break ;
				}
				case SS:
				{
					User u = SerializationUtility.deserializeUser(msg.getContent());
					
					system.onDistantSessionRequest(u, clientSocket);
				}
				default:
					break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		
	}

}
