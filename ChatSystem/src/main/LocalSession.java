package main;

import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class LocalSession extends Session{
	
	DatagramSocket socket;

	public LocalSession(byte[] id, User e, User r) 
	{
		super(id,e,r);
	}
	
	
	@Override
	public void sendMessage(UserMessage m) {
		// TODO Auto-generated method stub
		
		

	}

}










