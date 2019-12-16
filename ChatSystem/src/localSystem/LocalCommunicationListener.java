package localSystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import defo.User;
import message.Message;
import message.SystemMessage;
import message.UserMessage;

public class LocalCommunicationListener extends Thread {
	
	LocalSystem system ; 
	private MulticastSocket socket;
	private AtomicBoolean run;
	
	public LocalCommunicationListener(LocalSystem system) throws IOException 
	{
		this.system = system;
		this.socket = new MulticastSocket(LocalSystem.LISTENING_PORT);
		socket.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		run = new AtomicBoolean(); 
		run.set(true);
		start();
	}
	
	public void run() 
	{
		while(run.get()) 
		{
			byte[] buffer = new byte[Message.MAX_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				System.out.println("1");
				socket.receive(packet);
				System.out.println("2");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String subtype = new String(Message.extractSubtype(packet.getData()));
			System.out.println(subtype);
			System.out.println(packet.getAddress().toString());
			SystemMessage.SystemMessageType type ; 
			
			try
			{
				type = SystemMessage.SystemMessageType.valueOf(subtype);
			}
			catch(Exception e)
			{
				System.out.println("ça dégage");
				continue;
			}
			switch(type)
			{
				case SS: 
				try {
					system.createSessionResponse(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break ;
				
				case CO:
				ObjectInputStream iStream;
				try {
					
					/*iStream = new ObjectInputStream(new ByteArrayInputStream(Message.extractContent(packet.getData())));*/
					User u = new User(Message.extractContent(packet.getData()));/*(User) iStream.readObject();
					iStream.close();*/
					system.addLocalUser(u);
					system.notifyConnectionResponse(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break ;
				
				case CR:
					User u = new User(Message.extractContent(packet.getData()));
					system.addLocalUser(u);
				break ;
				
				
				default : 
					break ; 
			}
		}
	}
	
	public void stopRun() throws UnknownHostException, IOException 
	{
		run.set(false);
		socket.leaveGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		socket.close();
	}

	
	
	
	

}
