package com.presenceservice.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.presenceservice.model.*;
import com.presenceservice.utility.ConfigUtility;
import com.presenceservice.utility.LoggerUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * TODO Document 
 */
@WebServlet(urlPatterns = "/users", loadOnStartup = 1)
public class ManageUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static List<User> users = new ArrayList<User>() ; 
	
	private Timestamp lastModificationDate ; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageUsersServlet() {
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
	private void writeUsers(HttpServletResponse response) throws IOException 
	{
		LoggerUtility.getInstance().info("Sending updated users list");
		
		Gson uJson = new Gson();
		
		String usersJsonString = uJson.toJson(users) ; 
		
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
			
			Gson uJson = new Gson();
			
			User u = null ; 
			
			try {
				u = uJson.fromJson(userJsonString, User.class) ; 
			} catch(JsonSyntaxException e) {
				e.printStackTrace();
				return ; 
			}
			
			
			if(!users.contains(u))
			{
				users.add(u) ; 
				LoggerUtility.getInstance().info("User added : " + u) ; 
				lastModificationDate.setTime(new Date().getTime()); 
			}
				
			doGet(request, response);
		}
		else if(type.equals("DC"))  // disconnection notify 
		{
			LoggerUtility.getInstance().info("DC");
			
			String userJsonString = request.getReader().readLine() ; 
			
			if(userJsonString == null) 
				return ; 
			
			Gson uJson = new Gson();
			
			User u = null ; 
			
			try {
				u = uJson.fromJson(userJsonString, User.class) ; 
			} catch(JsonSyntaxException e) {
				e.printStackTrace();
				return ; 
			}
			
			
			if(users.contains(u))
			{

				users.remove(u) ; 
				lastModificationDate.setTime(new Date().getTime()); 
			}
		}
		else if(type.equals("CU")) // checkusername request 
		{
			LoggerUtility.getInstance().info("CU");
			
			String userJsonString = request.getReader().readLine() ; 
			
			if(userJsonString == null )
				return ; 
			
			Gson uJson = new Gson();
			
			User user = null ; 
			
			try {
				user = uJson.fromJson(userJsonString, User.class) ; 
			} catch(JsonSyntaxException e) {
				e.printStackTrace();
				return ; 
			}
			
			if(users.contains(user))
			{
				users.remove(user) ; 
				users.add(user) ;
			}
		
		}
		

	}

}
