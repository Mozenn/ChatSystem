package defo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class User{
	
	 private byte[] id;
	 private byte[] ipAddress;
	 private String username;
	 
	 public static final int MAX_NAME_SIZE = 20;
	 
	 public User(byte[] id, byte[] ip, String uname) 
	 {
		 this.id = id;
		 ipAddress = ip;
		 username = uname;
	 }
	 
	 
	 public User(byte[] content) 
	 {
		 id = extractID(content);
		 ipAddress = extractIPAddr(content);
		 username = new String(extractUsername(content)).trim();
	 }
	 
	 public byte[] getID() 
	 {
		 return id;
	 }
	 
	 public byte[] getIpAddress() {
		return ipAddress;
	 }

	 public String getUsername() {
		return username;
	 }
	 
	 public byte[] getSerialized() throws IOException 
	 {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(id);
		stream.write(ipAddress);
		stream.write(username.getBytes());
		return stream.toByteArray();
	 }
	 
	 private byte[] extractID(byte[] content) 
	 {
		 return Arrays.copyOfRange(content, 0, 6);
	 }
	 
	 private byte[] extractIPAddr(byte[] content) 
	 {
		 return Arrays.copyOfRange(content, 5, 9);
	 }
	 
	 private byte[] extractUsername(byte[] content) 
	 {
		 return Arrays.copyOfRange(content, 10, 33);
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
