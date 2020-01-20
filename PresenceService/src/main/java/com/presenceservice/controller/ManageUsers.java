package com.presenceservice.controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.presenceservice.model.*;
import com.presenceservice.utility.ConfigUtility;
import com.presenceservice.utility.LoggerUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.presenceservice.dao.*;


/**
 * TODO Document 
 */
@WebServlet(urlPatterns = "/users", loadOnStartup = 1)
public class ManageUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static HashMap<User,Boolean> users = new HashMap<User,Boolean>() ; // true if online, false if not 
	
	private Timestamp lastModificationDate ; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageUsers() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	
    	try {
			ConfigUtility.initializeConfigFolder();
		} catch (IOException e2) {
			e2.printStackTrace();
			return ; 
		}
    	
    	UserDAO dao = null;
		try {
			dao = new UserDAOSQLite();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
    	
    	List<User> usersResult = dao.getAllUsers();
    	/*
    	try {
			User u1 = new User(new UserId("id".getBytes()),InetAddress.getLocalHost(),"hey") ;
			User u2 = new User(new UserId("id2".getBytes()),InetAddress.getLocalHost(),"hey2") ;
			
			usersResult.add(u1) ; 
			usersResult.add(u2) ; 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}  ; 
    	*/
    	for(User u : usersResult)
    	{
    		users.put(u,false) ; 
    	} 
    	
		Date d = new Date();
		this.lastModificationDate = new Timestamp(d.getTime());
		
		LoggerUtility.getInstance().info("Server Started");

    }
    

	/**
	 * @return list of online users if it has been modified since the date specified in the request or no date is specified 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LoggerUtility.getInstance().info("GET Received");
		
		String tsString = request.getHeader("IF_MODIFIED_SINCE") ; 
		
		if(tsString != null)
		{
			Timestamp ts = Timestamp.valueOf(tsString) ; 
			
			LoggerUtility.getInstance().info("Time received " + ts.toString());
			LoggerUtility.getInstance().info("Last Modification Time : " + lastModificationDate.toString());
			
			if(lastModificationDate.before(ts))
			{
				response.setStatus(420);
				LoggerUtility.getInstance().info("No sending needed");
			}
			else
			{
				writeUsers(response) ; 
			}
				
		}
		else
		{
			writeUsers(response) ; 
		}
 
	}
	
	
	/*
	 * Writes all online users as a Json String in the given response 
	 * @param the response to be filled
	 */
	protected void writeUsers(HttpServletResponse response) throws IOException 
	{
		LoggerUtility.getInstance().info("Sending updated users list");
		
		List<User> onlineUsers = new ArrayList<User>();
		
		for(User u : users.keySet())
		{
			LoggerUtility.getInstance().info("User "+ u.getUsername() + " connectedState" + users.get(u));
			if(users.get(u))
				onlineUsers.add(u) ; 
		}
		
		ObjectMapper uJson = new ObjectMapper();
		
		String usersJsonString = uJson.writeValueAsString(onlineUsers) ; 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try(var out = response.getWriter() )
		{
			out.println(usersJsonString);
			out.flush();
		}
	}

	/**
	 * require the request to have a "COMMUNICATION" header of type CO(connection), DC(disconnection) or CU(changeUsername) 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LoggerUtility.getInstance().info("POST Received");
		
		String type = request.getHeader("COMMUNICATION") ; 
		
		if(type == null) 
			return ; 
		
		if(type.equals("CO")) // connection notify  
		{
			LoggerUtility.getInstance().info("CO");
			
			String userJsonString = request.getReader().readLine() ; 
			
			if(userJsonString == null) 
				return ; 
			
			ObjectMapper uJson = new ObjectMapper();
			
			User u = null ; 
			
			try {
				u = uJson.readValue(userJsonString, User.class) ; 
			} catch(JsonMappingException | JsonParseException e)
			{
				e.printStackTrace();
				return ; 
			}
			
			
			if(users.containsKey(u))
			{
				Boolean isOnline = users.get(u) ; 
				if(!isOnline)
				{
					isOnline = true ; 
					lastModificationDate.setTime(new Date().getTime()); 
				}
			}
			else
			{
				users.put(u,true) ; 
				System.out.println("User added : " + u) ; 
				lastModificationDate.setTime(new Date().getTime()); 
				
				UserDAO dao = new UserDAOSQLite() ; 
				
				dao.addUser(u);
			}
			
			
			doGet(request, response);
		}
		else if(type.equals("DC"))  // disconnection notify 
		{
			LoggerUtility.getInstance().info("DC");
			
			String userJsonString = request.getReader().readLine() ; 
			
			if(userJsonString == null) 
				return ; 
			
			ObjectMapper uJson = new ObjectMapper();
			
			User u = null ; 
			
			try {
				u = uJson.readValue(userJsonString, User.class) ; 
			} catch(JsonMappingException | JsonParseException e)
			{
				e.printStackTrace();
				return ; 
			}
			
			
			if(users.containsKey(u))
			{
				Boolean isOnline = users.get(u) ; 
				if(!isOnline)
				{
					isOnline = false ; 
					lastModificationDate.setTime(new Date().getTime()); 
				}
			}
		}
		else if(type.equals("CU")) // checkusername request 
		{
			LoggerUtility.getInstance().info("CU");
			
			String username = request.getReader().readLine() ; 
			
			boolean isAvailable = true ; 
			User uToUpdate = null ; 
			
			for(User u : users.keySet())
			{
				if(u.getUsername().equals(username))
				{
					isAvailable = false ;
					uToUpdate = u ; 
				}

			}
			
			if(!isAvailable)
				response.setStatus(400);
			else
			{
				// update user 
				
				uToUpdate.setUsername(username);
				
				UserDAO dao = new UserDAOSQLite() ; 
				dao.updateUser(uToUpdate);
				
				lastModificationDate.setTime(new Date().getTime()); 
			}
		}
		

	}

}
