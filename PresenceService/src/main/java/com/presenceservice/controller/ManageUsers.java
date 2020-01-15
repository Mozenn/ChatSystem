package com.presenceservice.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.presenceservice.model.User;

/**
 * Servlet implementation class QueryUser
 */
@WebServlet(urlPatterns = "/manageuser", loadOnStartup = 1)
public class ManageUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static ArrayList<User> users = new ArrayList<User>() ; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageUsers() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	
    	// TODO load user from db 
    }
    
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// check if any change has been made since ... 
		
		response.setContentType("application/json");
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Connected User list to json
		// write it to printWriter 
		
		response.setContentType("application/json");
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userJsonString = request.getReader().readLine() ; 
		// check if sending user is already stored in db 
		// if not, add it 
		// change its statues to online 
		
		
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
