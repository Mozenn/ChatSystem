package localSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import defo.Message;
import defo.SystemMessage;
import defo.UserMessage;

public class LocalCommunicationListener extends Thread {
	
	private LocalCommunicationThread thread;
	private DatagramSocket socket;
	private AtomicBoolean run;
	
	public LocalCommunicationListener(LocalCommunicationThread thread, DatagramSocket socket) 
	{
		this.thread = thread;
		this.socket = socket;
		run.set(true);
		start();
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
			String subtype = new String(Message.extractSubtype(buffer));
			SystemMessage.SystemMessageType type = SystemMessage.SystemMessageType.valueOf(subtype);
			switch(type)
			{
				case SS: 
					thread.getLocalSystem(). // TODO ON EST LA
			}
		}
	}
	
	public void stopRun() 
	{
		run.set(false);
	}

	
	
	
	

}
