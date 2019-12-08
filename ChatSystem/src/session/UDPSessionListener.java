package session;

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicBoolean;

import message.Message;
import message.UserMessage;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UserMessage msg = new UserMessage(buffer);
			session.addMessage(msg);			
		}
	}
	

}
