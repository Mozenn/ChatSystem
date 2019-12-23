package session;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.concurrent.ThreadLocalRandom;
import java.net.InetAddress;

import localSystem.LocalSystem;
import main.User;
import message.SystemMessage;
import message.UserMessage;
import utility.NetworkUtility;

final public class LocalSession extends Session{
	
	private DatagramSocket socket;
	private UDPSessionListener listener;

	public LocalSession(User e, User r) throws IOException 
	{
		super(e,r);
		startSession();
	}
	
	public LocalSession(User e, User r, int receiverPort) throws IOException 
	{
		super(e,r,receiverPort);
		startSession();
	}
	
	
	@Override
	public void startSession() {

		socket = NetworkUtility.getUDPSocketWithRandomPort() ; 
		
		listener = new UDPSessionListener(this, socket);
	}
	
	// TODO : make runnable class for this 
	// TODO to review 
	public void notifyStartSession() throws IOException 
	{
		/*
		String n = "";
		for (int i = 0; i < User.MAX_NAME_SIZE - emitter.getUsername().length(); i++) 
		{
			n += " ";
		}
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(emitter);
		oo.close();

		byte[] serializedUser = bStream.toByteArray(); */
		
		// TODO serialize emitter 
		byte[] serializedUser = new byte[0] ; 
		
		SystemMessage msg = new SystemMessage(SystemMessage.SystemMessageType.SS, serializedUser);
		InetAddress addr = InetAddress.getByAddress(receiver.getIpAddress());
		socket.send(new DatagramPacket(msg.toByteArray(), msg.toByteArray().length, addr, LocalSystem.LISTENING_PORT));
	}
	
	
	


	@Override
	public void closeSession() {
		listener.stopRun();
		socket.close();
	}


	// TODO : make runnable class for this ? 
	@Override
	public void sendMessage(String s){
		
		UserMessage m;
		
		try {
			m = new UserMessage(s);
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		byte[] buffer;
		
		DatagramPacket packet ; 
		try {
			buffer = m.toByteArray();
			InetAddress ipAdd = InetAddress.getByAddress(receiver.getIpAddress()) ;  // TODO check if that is correct 
			packet = new DatagramPacket(buffer, buffer.length, ipAdd ,receiverPort);
			
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		
		try {
			
			socket.send(packet); 
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("send failed") ; 
			return ; 
		}
	}

	// TODO : make runnable class for this ? 
	@Override
	public void sendMessage(File f) {
		
		UserMessage m;
		
		try {
			m = new UserMessage(f);
		} catch (IOException e1) {
			e1.printStackTrace();
			return ; 
		} 
		
		byte[] buffer;
		
		DatagramPacket packet ; 
		try {
			buffer = m.toByteArray();
			InetAddress ipAdd = InetAddress.getByAddress(receiver.getIpAddress()) ;  // TODO check if that is correct 
			packet = new DatagramPacket(buffer, buffer.length, ipAdd ,receiverPort);
			
		} catch (IOException e) {
			e.printStackTrace();
			return ; 
		}
		
		
		try {
			
			socket.send(packet); 
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("send failed") ; 
			return ; 
		}
	}
}










