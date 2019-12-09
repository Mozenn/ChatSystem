package localSystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import defo.User;
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
		run = new AtomicBoolean() ; 
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
			System.out.println(packet.getAddress().toString());
			
			SystemMessage.SystemMessageType type ; 
			
			try
			{
				type = SystemMessage.SystemMessageType.valueOf(subtype);
			}
			catch(Exception e)
			{
				continue;
			}

			switch(type)
			{
				case SS: 
				try {
					thread.getLocalSystem().createSessionResponse(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break ;
				
				case CO:
				ObjectInputStream iStream;
				try {
					iStream = new ObjectInputStream(new ByteArrayInputStream(Message.extractContent(packet.getData())));
					User u = (User) iStream.readObject();
					iStream.close();
					thread.getLocalSystem().addLocalUser(u);
					thread.notifyConnectionResponse(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break ;
				
				case CR:
				try {
					iStream = new ObjectInputStream(new ByteArrayInputStream(Message.extractContent(packet.getData())));
					User u = (User) iStream.readObject();
					iStream.close();
					thread.getLocalSystem().addLocalUser(u);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break ;
				
				default : 
					break ; 
			}
		}
	}
	
	public void stopRun() 
	{
		run.set(false);
	}

	
	
	
	

}
