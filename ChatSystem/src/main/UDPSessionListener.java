package main;

import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.atomic.AtomicBoolean;

public class UDPSessionListener extends Thread{
	
	private LocalSession session;
	private AtomicBoolean run;
	
	public UDPSessionListener(LocalSession s) 
	{
		session = s;
		run.set(true);
	}
	
	public void stopRun() 
	{
		run.set(false);
	}
	
	public void Run() 
	{
		byte[] buffer = new byte[Message.MAX_SIZE];
		while(run.get()) 
		{
			DatagramPacket packet = new DatagramPacket(buffer, 0);
			try {
				session.socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
	

}
