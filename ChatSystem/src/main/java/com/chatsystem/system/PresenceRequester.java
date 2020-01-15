package com.chatsystem.system;

import java.util.concurrent.atomic.AtomicBoolean;import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.OutputStream;

public class PresenceRequester extends Thread{
	
	private AtomicBoolean run;
	
	public PresenceRequester() 
	{
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
				url = new URL("https://reqres.in/api/users");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpURLConnection con = null;
			
			try {
				con = (HttpURLConnection)url.openConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				con.setRequestMethod("HEAD");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try(OutputStream os = con.getOutputStream()) 
			{
			    os.write();           
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

}
