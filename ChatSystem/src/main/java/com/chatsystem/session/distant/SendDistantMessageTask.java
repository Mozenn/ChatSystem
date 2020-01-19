package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.chatsystem.message.UserMessage;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

final class SendDistantMessageTask implements Runnable {
	
	private final Socket sendingSocket ; 
	private final UserMessage messageToSend ; 

	
	public SendDistantMessageTask(Socket sendingSocket, UserMessage message )
	{
		this.messageToSend = message ; 
		this.sendingSocket = sendingSocket ; 
	}

	@Override
	public void run() {
		
		try (DataOutputStream dOut = new DataOutputStream(sendingSocket.getOutputStream());)
		{
			byte[] b = SerializationUtility.serializeMessage(messageToSend); 
			
			dOut.write(b);
			dOut.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			LoggerUtility.getInstance().info("DistantSessionMessage Failed");
		}
		
		LoggerUtility.getInstance().info("DistantSessionMessage sent");
		
	}

}
