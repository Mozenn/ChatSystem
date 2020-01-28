package com.chatsystem.system;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.chatsystem.message.SystemMessage;
import com.chatsystem.user.User;
import com.chatsystem.utility.LoggerUtility;
import com.chatsystem.utility.SerializationUtility;

public class NotifyChangeUsernameWebServiceTask implements Runnable{
	
	private CommunicationSystem localSystem ; 
	private User user ; 
	private Thread thread; 
	
	public Thread getThread()
	{
		return this.thread ; 
	}
;
	
	public NotifyChangeUsernameWebServiceTask(CommunicationSystem localSystem, User user) 
	{
		this.localSystem = localSystem ; 
		this.user = user ; 
		// Start Thread 
		this.thread = new Thread(this,"NotifyLocalUsers") ; 
		this.thread.start();
	}
	
	@Override
	public void run() {
		
		String userAsString;

		userAsString = new String(SerializationUtility.serializeUser(user));

		
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userAsString))
                .uri(URI.create(localSystem.PRESENCESERVICE_URL))
                .header("Content-Type", "application/json")
                .setHeader("COMMUNICATION", "CU")
                .timeout(Duration.ofSeconds(2))
                .build();
        
			
			try {
				LoggerUtility.getInstance().info("CommunicationSystem NotifyWebService CU Sent");
				httpClient.send(request, BodyHandlers.ofString());
				
				
				LoggerUtility.getInstance().info("CommunicationSystem NotifyWebService CU Received");
			} catch (HttpTimeoutException e) {
				return ; 
			} catch (ConnectException e){
				return ; 
			}
			catch (IOException | InterruptedException e) {
				e.printStackTrace();
				
			}
		

	}

}
