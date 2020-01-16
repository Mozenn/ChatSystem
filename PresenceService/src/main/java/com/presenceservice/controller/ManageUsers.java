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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.presenceservice.dao.*;


/**
 * Servlet implementation class QueryUser
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
    	
    	/*
    	File f = new File(".") ; 
    	System.out.println(f.getAbsolutePath()) ;
    	*/
    	
    	UserDAO dao = new UserDAOSQLite() ; 
    	
    	List<User> usersResult = dao.getAllUsers();
    	
    	try {
			User u1 = new User(new UserId("id".getBytes()),InetAddress.getLocalHost(),"hey") ;
			User u2 = new User(new UserId("id2".getBytes()),InetAddress.getLocalHost(),"hey2") ;
			
			usersResult.add(u1) ; 
			usersResult.add(u2) ; 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}  ; 
    	
    	for(User u : usersResult)
    	{
    		users.put(u,false) ; 
    	}
    	
		Date d = new Date();
		this.lastModificationDate = new Timestamp(d.getTime());
		
		System.out.println("Server Started ! ") ;
    }
    
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		
		String tsString = request.getParameter("timestamp") ; 
		
		if(tsString != null)
		{
			Timestamp ts = Timestamp.valueOf(tsString) ; 
			
			if(lastModificationDate.after(ts))
			{
				doGet(request,response) ; 
			}
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("get") ;
		
		ObjectMapper uJson = new ObjectMapper();
		
		String usersJsonString = uJson.writeValueAsString(users) ; 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try(var out = response.getWriter() )
		{
			out.println(usersJsonString);
			out.flush();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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

}
