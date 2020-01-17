package com.chatsystem.system;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.chatsystem.user.User;
import com.chatsystem.utility.SerializationUtility;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.time.Duration;
import java.net.ProxySelector;
import java.net.URI;
import java.io.OutputStream;

public class DistantUsersFetcher extends Thread{
	
	private AtomicBoolean run;
	private CommunicationSystem system ; 
	private Timestamp lastTimestamp ; 
	
	public DistantUsersFetcher(CommunicationSystem system) 
	{
		this.system = system ; 
		this.lastTimestamp = new Timestamp(new Date().getTime()) ; 
		
		run = new AtomicBoolean();
		run.set(true);
		start();
	}
	
	public void run() 
	{
		while(run.get()) 
		{		
			
			HttpClient client = HttpClient.newBuilder()
		            .version(HttpClient.Version.HTTP_2) // default
		            .followRedirects(HttpClient.Redirect.NORMAL) // Always redirect, except from HTTPS URLs to HTTP URLs.
		            .proxy(ProxySelector.getDefault())
		            .build();
			HttpRequest request = HttpRequest.newBuilder()
					.GET()
				    .uri(URI.create(system.PRESENCESERVICE_URL))
			    	.timeout(Duration.ofSeconds(10))
			    	.header("Content-Type", "application/json")
			    	.setHeader("IF_MODIFIED_SINCE", lastTimestamp.toString())
			    	.build();
			
	        try {
				HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
				
				if(response.statusCode() == 200)
				{
					List<User> users = SerializationUtility.deserializeUsers(response.body().getBytes()) ; 
					
					for(User u : users)
					{
						system.addDistantUser(u);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			lastTimestamp.setTime(new Date().getTime());
			
			try {
				sleep(5000) ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void stopRun() 
	{
		System.out.println("DistantUsersFetcher Stop");
		run.set(false);
	}
	
	

}