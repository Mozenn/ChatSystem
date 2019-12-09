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
import localSystem.LocalCommunicationThread;
import message.SystemMessage;
import message.UserMessage;

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
		// TODO Auto-generated method stub
		
		boolean b = false;
		int num;
		while(!b) 
		{
			num = ThreadLocalRandom.current().nextInt(1024,65535);
			try 
			{
				socket = new DatagramSocket(num);
				b = true;
			}
			catch(SocketException e) 
			{
				e.printStackTrace();
			}
		}
		listener = new UDPSessionListener(this, socket);
	}
	
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
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalCommunicationThread.PORT));
	}
	
	
	


	@Override
	public void closeSession() {
		// TODO Auto-generated method stub
		listener.stopRun();
		socket.close();
	}



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










