package com.chatsystem.session.distant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.session.Session;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

/*
 * Send SS notify to receiver 
 * The local User is included is the message content 
 */
final class NotifyStartDistantSessionTask implements Runnable{
	
	private Socket socket; 
	private Session parentSession ; 
	private Thread thread ; 
	
	/*
	 * @throws NullPointerException if parentSession 
	 */
	public NotifyStartDistantSessionTask(Session parentSession, Socket socket)
	{
		if(parentSession == null)
			throw new NullPointerException() ; 
		
		this.socket = socket ; 
		this.parentSession = parentSession ; 
		
		thread = new Thread(this,"NotifyStartDistantSession") ; 
		thread.start();
	}
	
	public Thread getThread()
	{
		return this.thread ; 
	}

	@Override
	public void run() {
		
		try 
		{
			socket = new Socket(parentSession.getReceiver().getIpAddress(),parentSession.getReceiver().getDistantPort()) ; 
			
			byte[] serializedUser = null;
			

			serializedUser = SerializationUtility.serializeUser(parentSession.getEmitter());

			
			SystemMessage msg;
			
			try {
				msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedUser);
			} catch (IOException e2) {
				e2.printStackTrace();
				return ; 
			}
			
			byte[] msgAsBytes = SerializationUtility.serializeMessage(msg);
			
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			
			dOut.write(msgAsBytes);
			
			LoggerUtility.getInstance().info("NotifyStartDistantSession Sent");
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		

		
	
		
	}

}
