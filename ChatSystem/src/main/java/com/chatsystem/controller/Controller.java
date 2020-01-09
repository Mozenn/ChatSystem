package com.chatsystem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import com.chatsystem.model.SystemContract;
import com.chatsystem.system.CommunicationSystem;
import com.chatsystem.user.User;
import com.chatsystem.view.CreateUserWindow;
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
		this.model = new CommunicationSystem(); 
		
		this.view = new View(this,model);
		
		var user = model.getUser(); 
		
		if(user.isEmpty())
		{
			openCreateProfileWindow() ; 
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
    	this.view.openCreateUserWindow();
    	
    }
    
    private void closeCreateUserWindow()
    {
    	this.view.closeCreateUserWindow() ; 
    }
    
    private void openMainWindow()
    {
        createAndShowMainWindow();
        
        model.start();
    }
	
	public void changeUsername(String newUsername)
	{
		boolean res = model.changeUname(newUsername);
	}
	
	public void startSession(User receiver)
	{
		model.startSession(receiver); // TODO differentiate between local and distant 
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
	
	public void close()
	{
		if(model.hasStarted())
			model.close();
	}
	
	private boolean createLocalUser(String username)
	{
		return model.createLocalUser(username).isPresent() ; 
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
			
			if(mp.getCurrentReceiver() != null )
				sendMessage(mp.getCurrentReceiver(),mp.getTextArea().getText()) ; 
			
			mp.getTextArea().setText("");
			
		}
		else if(e.getActionCommand().equals(MainWindow.CLOSEMAINWINDOW_ACTIONCOMMAND) || e.getActionCommand().equals(CreateUserWindow.CLOSE_CREATEUSERWINDOW_ACTIONCOMMAND)) 
		{
			close();
			
		}
		else if(e.getActionCommand().equals(CreateUserWindow.CHECK_CREATEUSERWINDOW_ACTIONCOMMAND)) 
		{
			CreateUserWindow cuw = (CreateUserWindow) e.getSource() ;
			
			String username = cuw.getTextField().getText() ; 
			
			if(username.length() <= User.MAX_NAME_SIZE && username.length() > 0 ) // TODO add other validation constraints ? 
			{
				if(createLocalUser(username))
				{
					closeCreateUserWindow();
					openMainWindow() ; 
				}
				else
				{
					cuw.showUnavailableUsernameError();
				}
			}
			else
			{
				if(username.equals(""))
					cuw.showEmptyUsernameError();
				else
				{
					cuw.showTooLongUsernameError();
				}
			}
		}
		// TODO CHANGEUNAME_ACTIONCOMMAND
		// TODO SENDFILEMESSAGE_ACTIONCOMMAND 
		
	}

}
