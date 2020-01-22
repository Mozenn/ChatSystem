package com.chatsystem.system;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chatsystem.utility.LoggerUtility;



final class DistantCommunicationListener extends Thread {
	
	final private CommunicationSystem system ; 
	private ServerSocket serverSocket;
	private AtomicBoolean run;
	
	public DistantCommunicationListener(CommunicationSystem system) throws IOException 
	{
		this.system = system;
		
		this.serverSocket = new ServerSocket(0);
		this.serverSocket.setSoTimeout(1000);
		
		system.setDistantListeningPort(serverSocket.getLocalPort());
		
		run = new AtomicBoolean(); 
		run.set(true);
		start();
		
		LoggerUtility.getInstance().info("DistantCommunicationListener Start");
	}
	
	public void run() 
	{
		ExecutorService executorService = Executors.newFixedThreadPool(20); 
		
		while(run.get()) 
		{

			try {
				//System.out.println("DistantCommunicationListener wait receive");
				Socket clientSocket = serverSocket.accept();
				
				executorService.execute(new DistantCommunicationTask(clientSocket,system));
				
			} catch(SocketTimeoutException e)
			{
				continue ; 
			}catch (IOException e) { 
				e.printStackTrace();
				continue; 
			} 
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void stopRun() 
	{
		LoggerUtility.getInstance().info("DistantCommunicationListener Stop");
		run.set(false);
	}

}
