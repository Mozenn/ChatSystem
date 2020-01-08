package com.chatsystem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import com.chatsystem.model.SystemContract;
import com.chatsystem.system.LocalSystem;
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
		this.model = new LocalSystem(); 
		
		this.view = new View(this,model);
		
		var user = model.getUser(); 
		
		if(user.isEmpty())
		{
			// TODO 
			// Open user creation window 
			// get username from user 
			// createUser in model 
			// open main window 
			model.createLocalUser("Name") ; 
		}
		else 
			openMainWindow();

		/*
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
            }
        });
        */
		
	}
	
    private void createAndShowMainWindow() {
    	
		this.view.openMainWindow();
    }
    
    private void openCreateProfileWindow()
    {
    	// TODO 
    	// create window 
    	// subscribe to ConfirmButton 
    	// implementation on button push in actionperformed 
    	
    }
    
    private void openMainWindow()
    {
        createAndShowMainWindow();
        
        model.start();
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
		model.closeSessionNotified(receiver);
	}
	
	public void sendMessage(User receiver, String text)
	{
		model.sendMessage(receiver, text);
	}
	
	public void sendMessage(User receiver, File file)
	{
		model.sendFileMessage(receiver, file);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(JSessionPanel.CLOSE_ACTIONCOMMAND)) 
		{
			JSessionPanel s = (JSessionPanel) e.getSource() ;
			closeSession(s.getSessionModel().getReceiver()) ; 
		}
		else if(e.getActionCommand().equals(JUserPanel.STARTSESSION_ACTIONCOMMAND)) 
		{
			JUserPanel u = (JUserPanel) e.getSource() ;
			startSession(u.getUser());
		}
		else if(e.getActionCommand().equals(JChatPanel.SENDMESSAGE_ACTIONCOMMAND)) 
		{
			JChatPanel mp = (JChatPanel) e.getSource() ;
			
			if(mp.getCurrentUser() != null )
				sendMessage(mp.getCurrentUser(),mp.getTextArea().getText()) ; 
			
			mp.getTextArea().setText("");
			
		}
		// TODO CHANGEUNAME_ACTIONCOMMAND
		// TODO SENDFILEMESSAGE_ACTIONCOMMAND 
		// TODO CHECKPROFILE_ACTIONCOMMAND 
		
	}

}
