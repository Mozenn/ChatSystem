package com.chatsystem.base;


import java.util.Date;
import java.util.Enumeration;

import com.chatsystem.controller.Controller;
import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.localsystem.LocalSystem;
import com.chatsystem.message.Message;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;
import com.chatsystem.utility.NetworkUtility;
import com.chatsystem.utility.SerializationUtility;
import com.chatsystem.view.View;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

public class App {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		 
		/*
		byte[] buf = new byte[4];
		byte[] data = new byte[] {'g','l','a'};
		
		MulticastSocket ms = new MulticastSocket(6666);
		
		ms.joinGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
		ms.send(new DatagramPacket(data, 3, InetAddress.getByName(LocalSystem.MULTICAST_ADDR), 6666));
		
		DatagramPacket r = new DatagramPacket(buf, 3);
        
        ms.receive(r);
        byte[] ipe = r.getAddress().getAddress();   
        String ad = r.getAddress().toString() ; 
        //var t = InetAddress.getByAddress(ad.getBytes()) ; 
        System.out.println(ad) ; 
        
     // Obtenir l'adresse IP de la machine locale
	    NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByAddress(ipe));
	    byte[] mac = ni.getHardwareAddress();
	    
	    String s = mac.toString();
	    
	    byte[] mac2 = s.getBytes();
	    
	    System.out.println(mac.equals(mac2)); 
	    
        //User u = new User(mac, ipe, "Ragnar Lodbrok");
        
        ms.leaveGroup(InetAddress.getByName(LocalSystem.MULTICAST_ADDR));
        
		//LocalSystem locSys = new LocalSystem(u);
        */
		
		      
		// LocalSystem locSys = new LocalSystem();
		
		Controller controller = new Controller() ; 

        
	}

}
