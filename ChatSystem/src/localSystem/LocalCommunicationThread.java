package localSystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import message.SystemMessage;

import java.net.DatagramPacket;

public class LocalCommunicationThread extends Thread implements AutoCloseable{
	
	private MulticastSocket socket;
	
	private LocalSystem system;
	private LocalCommunicationListener listener ; 
	private static final String multicastAddress = "228.228.228.228";
	
	public static final int PORT = 8888; 
	
	public LocalCommunicationThread(LocalSystem system) throws IOException 
	{
		this.system = system ;
		socket = new MulticastSocket(PORT);
		socket.joinGroup(InetAddress.getByName(multicastAddress));
		
		listener = new LocalCommunicationListener(this,socket);
		
		notifyLocalUsers(); 
	}
	
	public void notifyLocalUsers() throws IOException 
	{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(system.getUser());
		oo.close();

		byte[] serializedUser = bStream.toByteArray();
		
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.CO, serializedUser);
		InetAddress addr = InetAddress.getByName(multicastAddress);
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));
	}
	
	public void notifyConnectionResponse(DatagramPacket packet) throws IOException 
	{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(system.getUser());
		oo.close();

		byte[] serializedUser = bStream.toByteArray();
		
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.CR, serializedUser);
		InetAddress addr = packet.getAddress();
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));
	}
	
	public void close() throws UnknownHostException, IOException
	{
		listener.stopRun();
		socket.leaveGroup(InetAddress.getByName(multicastAddress));
		socket.close();	
	}
	
	public LocalSystem getLocalSystem() 
	{
		return system;
	}
	
	
	
	

}
