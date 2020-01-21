package com.chatsystem.base;


import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.stream.IntStream;

import com.chatsystem.controller.Controller;
import com.chatsystem.dao.DAO;
import com.chatsystem.dao.DAOSQLite;
import com.chatsystem.message.Message;
import com.chatsystem.message.SystemMessage;
import com.chatsystem.message.UserMessage;
import com.chatsystem.system.CommunicationSystem;
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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;

import java.sql.Timestamp;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		 
		
		Controller controller = new Controller() ; 

        
	}

}
