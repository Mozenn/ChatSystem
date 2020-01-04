package com.chatsystem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import com.chatsystem.localsystem.LocalSystem;
import com.chatsystem.user.User;
import com.chatsystem.view.JSessionPanel;
import com.chatsystem.view.JUserPanel;
import com.chatsystem.view.MainWindow;
import com.chatsystem.view.View;

public class Controller implements ActionListener{
	
	private LocalSystem localSystem ; 
	private View view ; 
	
	public Controller() throws IOException
	{
		this.localSystem = new LocalSystem(); 
		
		view = new View(this) ; 
		view.init();
	}
	
	public void changeUsername(User userToModify, String newUsername)
	{
		// TODO call localSystem method  
	}
	
	public void startSession(User receiver)
	{
		// TODO call localSystem method  
	}
	
	public void closeSession(User receiver)
	{
		// TODO call localSystem method  
	}
	
	public void sendMessage(User receiver, String text)
	{
		// TODO call localSystem method
	}
	
	public void sendMessage(User receiver, File file)
	{
		// TODO call localSystem method
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("CloseSession")) // TODO replace hardcoded string 
		{
			JSessionPanel s = (JSessionPanel) e.getSource() ;
			closeSession(s.getReceiver()) ; 
		}
		else if(e.getActionCommand().equals("StartSession")) // TODO replace hardcoded string 
		{
			JUserPanel u = (JUserPanel) e.getSource() ;
			startSession(u.getUser());
		}
		else if(e.getActionCommand().equals("SendMessage")) // TODO replace hardcoded string 
		{
			MainWindow m = (MainWindow) e.getSource() ;
			//sendMessage() ; 
			
		}
		
	}

}
