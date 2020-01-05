package com.chatsystem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import com.chatsystem.localsystem.LocalSystem;
import com.chatsystem.model.SystemContract;
import com.chatsystem.user.User;
import com.chatsystem.view.JChatPanel;
import com.chatsystem.view.JSessionPanel;
import com.chatsystem.view.JUserPanel;
import com.chatsystem.view.MainWindow;
import com.chatsystem.view.View;

public class Controller implements ControllerContract{
	
	private SystemContract model ; 
	private View view ; 
	
	public Controller() throws IOException
	{
		// TODO 
		
		// this.localSystem = new LocalSystem(); 
		
		this.view = new View(this);
		
		var user = model.getUser(); 
		
		if(user.isEmpty())
		{
			// Open user creation window ( async and wait ) 
			// get username from user 
			// createUser in model 
			// open main window 
		}
		else
		{
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	createAndShowMainWindow();
	            }
	        });
		}
		

		
	}
	
    private void createAndShowMainWindow() {
		this.view.openMainWindow();
    }
	
	public void changeUsername(String newUsername)
	{
		model.changeUname(newUsername);
	}
	
	public void startSession(User receiver)
	{
		model.startLocalSession(receiver); // TODO differentiate between local and distant 
	}
	
	public void closeSession(User receiver)
	{
		model.closeSession(receiver);
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
		
		if(e.getActionCommand().equals(JSessionPanel.CLOSE_ACTIONCOMMAND)) 
		{
			JSessionPanel s = (JSessionPanel) e.getSource() ;
			closeSession(s.getReceiver()) ; 
		}
		else if(e.getActionCommand().equals(JUserPanel.STARTSESSION_ACTIONCOMMAND)) 
		{
			JUserPanel u = (JUserPanel) e.getSource() ;
			startSession(u.getUser());
		}
		else if(e.getActionCommand().equals(JChatPanel.SENDMESSAGE_ACTIONCOMMAND)) 
		{
			JChatPanel mp = (JChatPanel) e.getSource() ;
			sendMessage(mp.getCurrentUser(),mp.getTextArea().getText()) ; 
			
		}
		
	}

}
