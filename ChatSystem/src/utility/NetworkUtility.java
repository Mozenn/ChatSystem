package utility;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ThreadLocalRandom;

import session.UDPSessionListener;

public final class NetworkUtility {
	
	public static DatagramSocket getUDPSocketWithRandomPort()
	{
		boolean b = false;
		DatagramSocket socket = null ; 
		
		do
		{
			try 
			{
				socket = new DatagramSocket();
				b = true;
			}
			catch(SocketException e) 
			{
			}
		}while(!b);
		
		return socket;
	}

}
