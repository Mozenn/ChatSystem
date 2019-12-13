package localSystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.net.InetAddress;

import message.SystemMessage;
import utility.NetworkUtility;

public class NotifyLocalUsersTask implements Runnable 
{

	private LocalSystem localSystem ; 
	private Thread thread; 
;
	
	public NotifyLocalUsersTask(LocalSystem localSystem) throws UnknownHostException
	{
		this.localSystem = localSystem ; 
	
		thread = new Thread(this,"NotifyLocalUsers") ; 
		thread.start();
	}
	
	@Override
	public void run() {
		
		// Write User to byte 
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		
		try {

			ObjectOutput oo = new ObjectOutputStream(bStream); 
			oo.writeObject(localSystem.getUser());
			oo.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		

		byte[] serializedUser = bStream.toByteArray();
		
		SystemMessage msg = null;
		try {
			msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InetAddress addr = null;
		
		try {
			addr = InetAddress.getByName(LocalSystem.MULTICAST_ADDR);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket();
			socket.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		
		// TODO search how to broadcast 
		try {
			socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, InetAddress.getByName(LocalSystem.MULTICAST_ADDR), LocalSystem.LISTENING_PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
