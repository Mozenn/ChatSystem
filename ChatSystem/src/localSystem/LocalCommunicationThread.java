package localSystem;

import java.net.DatagramSocket;

public class LocalCommunicationThread extends Thread{
	
	private DatagramSocket socket;
	private LocalSystem system;
	
	public static final int PORT = 8888; 
	
	public LocalCommunicationThread(LocalSystem system) 
	{
		this.system = system ; 
	}
	
	public void notifyLocalUsers() 
	{
		
	}
	
	public LocalSystem getLocalSystem() 
	{
		return system;
	}
	
	
	
	

}
