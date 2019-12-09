package defo;

import java.util.Date;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.sql.Timestamp;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		String truc = "babozo";
		//System.out.println(truc.getBytes().length);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		stream.write(4);
		
		System.out.println(InetAddress.getLocalHost()); 
		
		
		User t = new User(stream.toByteArray(),InetAddress.getLocalHost().getAddress(),"hey");

		
		// Serialize to a byte array
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream); 
		oo.writeObject(t);
		oo.close();

		byte[] serializedMessage = bStream.toByteArray();
		
		
		ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(serializedMessage));
		User t2 = (User) iStream.readObject();
		iStream.close();
		
		InetAddress i = InetAddress.getByAddress(t2.getIpAddress()); 
		
		System.out.println(t2.getUsername() + " " + i + " " + t2.getID()); 
	}

}
