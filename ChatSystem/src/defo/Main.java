package defo;

import java.util.Date;
import java.util.Enumeration;

import localSystem.LocalSystem;
import message.Message;
import message.SystemMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + InetAddress.getLocalHost());
		InetAddress addr = null;
		System.out.println("Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();

                NetworkInterface e = n.nextElement();
                System.out.println("Interface: " + e.getName());
                Enumeration<InetAddress> a = e.getInetAddresses();

                        addr = a.nextElement();
                        System.out.println("  " + addr.getHostAddress());
                
        
		
        byte[] mac = new byte[]{25,36,45,89,75,02};
        System.out.println("hey1 " + addr);
 		
        
        byte[] ipe = addr.getAddress();
        
        System.out.println("hey " + ipe);
 		
		User u = new User(mac,ipe,"name") ; 
		System.out.println("loooooooool4 :" + u.getUsername());
		System.out.println("loooooooool5 :" + InetAddress.getByAddress(u.getIpAddress()));
		
		InetAddress addr1 = InetAddress.getByAddress(u.getIpAddress());
		System.out.println("loooooooool5 :" + addr1);
		
		System.out.println("loooooooool6 :" + u.getID());
		System.out.println("loooooooool :" + u.getSerialized());
		
		User machin = new User(u.getSerialized());
		System.out.println("loooooooool4 :" + machin.getUsername());
		System.out.println("loooooooool5 :" + machin.getIpAddress());
		System.out.println("loooooooool6 :" + machin.getID());

		

		
		
		LocalSystem locSys = new LocalSystem(u);
		/*byte[] content = new String("poisqdfisiducfhckduifhskdicfh").getBytes();
		byte[] msg = new SystemMessage(SystemMessage.SystemMessageType.CO, content).toByteArray();
		System.out.println(new String(Message.extractSubtype(msg)));
		System.out.println(Message.extractSize(msg)[0]);
		System.out.println(new String(Message.extractTime(msg)));
		System.out.println(new String(Message.extractContent(msg)));*/
	}

}
