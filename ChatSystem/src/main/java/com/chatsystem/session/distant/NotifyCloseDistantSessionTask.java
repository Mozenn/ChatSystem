package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

/*
 * Send CS notify to receiver 
 */
final class NotifyCloseDistantSessionTask implements Runnable {
	
	private DataOutputStream outputStream ; 
	
	/*
	 * @throws NullPointerException if outputStream is null 
	 */
	public NotifyCloseDistantSessionTask(OutputStream outputStream)
	{
		if(outputStream == null)
			throw new NullPointerException() ; 
		
		this.outputStream = new DataOutputStream(outputStream) ; 
		
		Thread thread = new Thread(this,"NotifyCloseDistantSession") ; 
		thread.start();
	}

	@Override
	public void run() {
		
		SystemMessage msg;
		
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.CS, new byte[10]); // content not used 
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
		
		try
		{
			byte[] b = SerializationUtility.serializeMessage(msg); 
			
			outputStream.writeInt(b.length);
			outputStream.write(b);
			outputStream.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			LoggerUtility.getInstance().info("NotifyCloseDistantSession Failed");
		}
		
		LoggerUtility.getInstance().info("NotifyCloseDistantSession Sent");
		
	}

}
