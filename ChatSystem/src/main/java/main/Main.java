package main;


import java.util.Date;
import java.util.Enumeration;

import com.insa.localsystem.LocalSystem;
import com.insa.message.Message;
import com.insa.message.SystemMessage;
import com.insa.user.User;
import com.insa.utility.NetworkUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;

import java.sql.Timestamp;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		    
		// TODO use getLocalIPAddress method 
		byte[] buf = new byte[4];
		byte[] data = new byte[] {'g','l','a'};
		
		MulticastSocket ms = new MulticastSocket(6666);
		
		ms.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		ms.send(new DatagramPacket(data, 3, InetAddress.getByName(LocalSystem.MULTICAST_ADDR), 6666));
		
		DatagramPacket r = new DatagramPacket(buf, 3);
        
        ms.receive(r);
        byte[] ipe = r.getAddress().getAddress();        
        
     // Obtenir l'adresse IP de la machine locale
	    NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByAddress(ipe));
	    byte[] mac = ni.getHardwareAddress();

        User u = new User(mac, ipe, "Ragnar Lodbrok");
        
        ms.leaveGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
        
		LocalSystem locSys = new LocalSystem(u);
		/*byte[] content = new String("poisqdfisiducfhckduifhskdicfh").getBytes();
		byte[] msg = new SystemMessage(SystemMessage.SystemMessageType.CO, content).toByteArray();
		System.out.println(new String(Message.extractSubtype(msg)));
		System.out.println(Message.extractSize(msg)[0]);
		System.out.println(new String(Message.extractTime(msg)));
		System.out.println(new String(Message.extractContent(msg)));*/
	}

}
