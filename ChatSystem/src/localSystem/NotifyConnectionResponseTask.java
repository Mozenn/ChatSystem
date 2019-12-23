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

import main.User;
import message.Message;
import message.SystemMessage;
import utility.NetworkUtility;

final class NotifyConnectionResponseTask implements Runnable {
	
	private LocalSystem localSystem ; 
	private Thread thread; 
	InetAddress addr ;
	
	public NotifyConnectionResponseTask(LocalSystem localSystem,DatagramPacket packet) throws IOException, ClassNotFoundException
	{
		this.localSystem = localSystem ; 
		User u = new User(Message.extractContent(packet.getData()));
		addr = InetAddress.getByAddress(u.getIpAddress());
		
		//Debug 
		System.out.println("longueur : "+u.getID().length);
		System.out.println("longueur : "+u.getIpAddress().length);
		System.out.println("longueur : "+u.getUsername().length());
		
		// Start thread 
		thread = new Thread(this,"NotifyConnectionResponse") ; 
		thread.start();
	}
	
	@Override
	public void run() {
		
		
		byte[] serializedUser = null ;
		try {
			serializedUser = localSystem.getUser().getSerialized();
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		}
		SystemMessage msg = null;
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("system message creation failed") ; 
			return ; 
		}
		
		DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 	
		
		try {
			socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalSystem.LISTENING_PORT));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("send failed") ; 
			socket.close();
			return ; 
		}

		System.out.println("CR notify sent");

		socket.close();

	}

}
