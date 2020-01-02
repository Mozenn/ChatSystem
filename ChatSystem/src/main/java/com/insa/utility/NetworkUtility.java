package com.insa.utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.concurrent.ThreadLocalRandom;

import com.insa.localsystem.LocalSystem;
import com.insa.session.UDPSessionListener;

public final class NetworkUtility {
	
	private NetworkUtility() {}
	
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
	
	/*
	 * Sends and receives UDP Packet on same socket to get local ip address 
	 */
	public static InetAddress getLocalIPAddress() throws IOException
	{
		byte[] buf = new byte[4];
		byte[] data = new byte[] {'g','l','a'};
		
		MulticastSocket ms = new MulticastSocket(6666);
		
		ms.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		ms.send(new DatagramPacket(data, 3, InetAddress.getByName(LocalSystem.MULTICAST_ADDR), 6666));
		
		DatagramPacket r = new DatagramPacket(buf, 3);
        
        ms.receive(r);
        
        ms.leaveGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
        
        return r.getAddress();   
	}

}
