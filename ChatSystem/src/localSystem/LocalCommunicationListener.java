package localSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import message.Message;
import message.SystemMessage;
import message.UserMessage;

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
				try {
					thread.getLocalSystem().createSession(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				case CO:
					
			}
		}
	}
	
	public void stopRun() 
	{
		run.set(false);
	}

	
	
	
	

}