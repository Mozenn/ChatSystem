package session;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;
import java.net.InetAddress;

import defo.User;
import localSystem.LocalSystem;
import message.SystemMessage;
import message.UserMessage;
import utility.NetworkUtility;

public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private int port;
	private UDPSessionListener listener;

	public LocalSession(User e, User r) throws IOException 
	{
		super(e,r);
		startSession();
	}
	
	
	@Override
	public void startSession() {

		socket = NetworkUtility.getUDPSocketWithRandomPort() ; 
		
		listener = new UDPSessionListener(this, socket);
	}
	
	// TODO : make runnable class for this 
	public void notifySS() throws IOException 
	{
		String n = "";
		for (int i = 0; i < User.MAX_NAME_SIZE - emitter.getUsername().length(); i++) 
		{
			n += " ";
		}
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(emitter);
		oo.close();

		byte[] serializedUser = bStream.toByteArray();
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedUser);
		InetAddress addr = InetAddress.getByAddress(receiver.getIpAddress());
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalSystem.LISTENING_PORT));
	}
	
	
	


	@Override
	public void closeSession() {
		// TODO Auto-generated method stub
		listener.stopRun();
		socket.close();
	}


	// TODO : make runnable class for this ? 
	@Override
	public void sendMessage(UserMessage m){
		// TODO Auto-generated method stub
		
		byte[] buffer;
		try {
			buffer = m.toByteArray();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}










