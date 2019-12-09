package localSystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

import message.SystemMessage;

public class NotifyConnectionResponseTask implements Runnable {
	
	private LocalSystem localSystem ; 
	
	public NotifyConnectionResponseTask(LocalSystem localSystem)
	{
		this.localSystem = localSystem ; 
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
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));

	}

}
