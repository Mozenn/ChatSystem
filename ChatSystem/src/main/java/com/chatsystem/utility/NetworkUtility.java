package com.chatsystem.utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.concurrent.ThreadLocalRandom;

import com.chatsystem.session.local.LocalSessionListener;
import com.chatsystem.system.CommunicationSystem;

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
		
		ms.joinGroup(InetAddress.getByName(CommunicationSystem.MULTICAST_ADDR));
		ms.send(new DatagramPacket(data, 3, InetAddress.getByName(CommunicationSystem.MULTICAST_ADDR), 6666));
		
		DatagramPacket r = new DatagramPacket(buf, 3);
        
        ms.receive(r);
        
        ms.leaveGroup(InetAddress.getByName(CommunicationSystem.MULTICAST_ADDR));
        
        return r.getAddress();   
	}
	
	public static String getLocalSubnetMask() throws SocketException, IOException
	{
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(NetworkUtility.getLocalIPAddress());
		
		int netPrefix = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength() ; 
		
		
		String maskString = null ; 
		
	    try {
	        // Since this is for IPv4, it's 32 bits, so set the sign value of
	        // the int to "negative"...
	        int shiftby = (1<<31);
	        // For the number of bits of the prefix -1 (we already set the sign bit)
	        for (int i=netPrefix-1; i>0; i--) {
	            // Shift the sign right... Java makes the sign bit sticky on a shift...
	            // So no need to "set it back up"...
	            shiftby = (shiftby >> 1);
	        }
	        // Transform the resulting value in xxx.xxx.xxx.xxx format, like if
	        /// it was a standard address...
	        maskString = Integer.toString((shiftby >> 24) & 255) + "." + Integer.toString((shiftby >> 16) & 255) + "." + Integer.toString((shiftby >> 8) & 255) + "." + Integer.toString(shiftby & 255);
	        
	        
	    }
	        catch(Exception e){e.printStackTrace();
	    }
	    
		
		return maskString ; 
	}

}
