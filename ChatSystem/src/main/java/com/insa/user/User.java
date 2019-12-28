package com.insa.user;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

final public class User{
	
	 private final byte[] id;
	 private final byte[] ipAddress;
	 private String username;
	 
	 public static final int MAX_NAME_SIZE = 20;
	 
	 public User(byte[] id, byte[] ip, String uname) 
	 {
		 this.id = id;
		 this.ipAddress = ip;
		 this.username = uname;
	 }
	 
	 
	 public User(byte[] content) 
	 {
		 this.id = extractID(content);
		 this.ipAddress = extractIPAddr(content);
		 this.username = new String(extractUsername(content)).trim();
	 }
	 
	 @Override
	 public String toString()
	 {
		 return username ; 
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
	 
	 // TODO Use JSON 
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
