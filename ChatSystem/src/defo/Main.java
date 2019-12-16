package defo;

import java.util.Date;

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
		System.out.println("Current IP address : " + ip.getHostAddress());
		
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			
		byte[] mac = network.getHardwareAddress();
		
		User u = new User(mac,ip.getAddress(),"name") ; 
		
		LocalSystem locSys = new LocalSystem(u); 
		/*byte[] content = new String("poisqdfisiducfhckduifhskdicfh").getBytes();
		byte[] msg = new SystemMessage(SystemMessage.SystemMessageType.CO, content).toByteArray();
		System.out.println(new String(Message.extractSubtype(msg)));
		System.out.println(Message.extractSize(msg)[0]);
		System.out.println(new String(Message.extractTime(msg)));
		System.out.println(new String(Message.extractContent(msg)));*/
	}

}
