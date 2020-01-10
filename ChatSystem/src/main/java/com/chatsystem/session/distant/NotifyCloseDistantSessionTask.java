package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

final class NotifyCloseDistantSessionTask implements Runnable {
	
	private Socket socket ; 
	
	public NotifyCloseDistantSessionTask(Socket socket)
	{
		this.socket = socket ; 
		
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
		
		try (DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());)
		{
			byte[] b = SerializationUtility.serializeMessage(msg); 
			
			dOut.write(b);
			dOut.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("NotifyCloseDistantSession failed") ; 
		}
		
		
		
		
		System.out.println("NotifyCloseDistantSession sent");
		
	}

}
