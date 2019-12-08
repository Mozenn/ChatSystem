package defo;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {
	
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
	 
	 private byte[] extractID(byte[] content) 
	 {
		 return Arrays.copyOfRange(content, 0, 7);
	 }
	 
	 private byte[] extractIPAddr(byte[] content) 
	 {
		 return Arrays.copyOfRange(content, 7, 12);
	 }
	 
	 private byte[] extractUsername(byte[] content) 
	 {
		 return Arrays.copyOfRange(content, 12, 33);
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
