package localSystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import message.SystemMessage;
import utility.NetworkUtility;

public class NotifyConnectionResponseTask implements Runnable {
	
	private LocalSystem localSystem ; 
	private Thread thread; 
	InetAddress addr ;
	
	public NotifyConnectionResponseTask(LocalSystem localSystem,String multicastAddr) throws UnknownHostException
	{
		this.localSystem = localSystem ; 
		this.addr = InetAddress.getByName(LocalSystem.MULTICAST_ADDR) ; 
	
		thread = new Thread(this,"NotifyLocalUsers") ; 
		thread.start();
	}
	
	@Override
	public void run() {
		
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(localSystem.getUser());
		oo.close();

		byte[] serializedUser = bStream.toByteArray();
		
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		InetAddress addr = InetAddress.getByName(multicastAddress);
		
		DatagramSocket socket = NetworkUtility.getUDPSocketWithRandomPort() ; 
		
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));

	}

}
