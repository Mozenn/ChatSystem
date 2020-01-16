package com.chatsystem.system;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
			
			URL url = null;
			
			try {
				url = new URL(system.PRESENCESERVICE_URL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			HttpURLConnection con = null;
			
			try {
				con = (HttpURLConnection)url.openConnection();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				con.setRequestMethod("HEAD");
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			
			try(OutputStream os = con.getOutputStream()) 
			{
			    //os.write();           
			} catch (IOException e) {
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
