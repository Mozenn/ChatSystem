package session;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;

import defo.User;
import defo.UserMessage;

public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private UDPSessionListener listener;

	public LocalSession(byte[] id, User e, User r) 
	{
		super(id,e,r);
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
		// TODO : notify general listener of session start request
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










