package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.chatsystem.message.UserMessage;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

/*
 * Send Usermessage to receiver 
 */
final class SendDistantMessageTask implements Runnable {
	
	private final DataOutputStream outputStream ; 
	private final UserMessage messageToSend ; 

	
	public SendDistantMessageTask(OutputStream outputStream, UserMessage message )
	{
		
		this.messageToSend = message ; 
		this.outputStream = new DataOutputStream(outputStream) ; 
		
		Thread thread = new Thread(this,"SendDistantMessage") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		LoggerUtility.getInstance().info("SendDistantMessageTask Running");
		
		try 
		{
			byte[] b = SerializationUtility.serializeMessage(messageToSend); 
			
			outputStream.writeInt(b.length);
			outputStream.write(b);
			outputStream.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			LoggerUtility.getInstance().info("DistantSessionMessage Failed");
		}
		
		LoggerUtility.getInstance().info("DistantSessionMessage sent");
		
	}

}
