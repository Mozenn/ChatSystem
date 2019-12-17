package localSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import defo.User;
import message.Message;
import message.SystemMessage;
import utility.NetworkUtility;

public class NotifyConnectionResponseTask implements Runnable {
	
	private LocalSystem localSystem ; 
	private Thread thread; 
	InetAddress addr ;
	
	public NotifyConnectionResponseTask(LocalSystem localSystem,DatagramPacket packet) throws IOException, ClassNotFoundException
	{
		this.localSystem = localSystem ; 
		User u = new User(Message.extractContent(packet.getData()));
		System.out.println("longueur : "+u.getID().length);
		System.out.println("longueur : "+u.getIpAddress().length);
		System.out.println("longueur : "+u.getUsername().length());
		addr = InetAddress.getByAddress(u.getIpAddress());
		thread = new Thread(this,"NotifyConnectionResponse") ; 
		thread.start();
	}
	
	@Override
	public void run() {
		try {			
			byte[] serializedUser = localSystem.getUser().getSerialized();
			SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
			DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
			System.out.println("nice");
			socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalSystem.LISTENING_PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

		

	}

}
