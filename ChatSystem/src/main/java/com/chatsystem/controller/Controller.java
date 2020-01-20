package com.chatsystem.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import com.chatsystem.model.SystemContract;
import com.chatsystem.system.CommunicationSystem;
import com.chatsystem.user.User;
import com.chatsystem.user.UserId;
import com.chatsystem.utility.ConfigurationUtility;
import com.chatsystem.view.ChangeUsernameWindow;
import com.chatsystem.view.CreateUserWindow;
import com.chatsystem.view.JChatPanel;
import com.chatsystem.view.JSessionPanel;
import com.chatsystem.view.JUserPanel;
import com.chatsystem.view.MainWindow;
import com.chatsystem.view.SettingsWindow;
import com.chatsystem.view.View;

/*
 * Transmit event triggered in view to model 
 */
public class Controller implements ControllerContract{
	
	private SystemContract model ; 
	private View view ; 
	
	public Controller() throws IOException
	{	
		ConfigurationUtility.initializeApplicationFolder() ; 
		
		this.model = new CommunicationSystem(); 
		
		this.view = new View(this,model);
		
		var user = model.getUser(); 
		
		if(user.isEmpty())
		{
			openCreateProfileWindow() ; 
		}
		else 
		{
			openMainWindow();
			/*
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	
	            }
	        }); */ 
		}
			

		

        
		
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	createAndShowMainWindow();
            }
        });
        
        
        model.start();
    }
	
    private boolean changeUsername(String newUsername)
	{
		return model.changeUname(newUsername);
	}
	
	private void changeDownloadPath(String newPath)
	{
		model.changeDownloadPath(newPath);
	}
	
	private void startSession(User receiver)
	{
		model.startSession(receiver); 
	}
	
	private void closeSession(User receiver)
	{
		model.closeSessionNotified(receiver);
	}
	
	private void sendMessage(User receiver, String text)
	{
		model.sendMessage(receiver, text);
	}
	
	private void sendFileMessage(User receiver, String filePath)
	{
		model.sendFileMessage(receiver, filePath);
	}
	
	private void downloadFile(UserId senderId, Timestamp date)
	{
		model.downloadFile(senderId, date) ; 
	}
	
	private void close()
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
		else if(e.getActionCommand().equals(MainWindow.CLOSEMAINWINDOW_ACTIONCOMMAND) || e.getActionCommand().equals(CreateUserWindow.CLOSE_CREATEUSERWINDOW_ACTIONCOMMAND)) 
		{
			close();
			
		}
		else if(e.getActionCommand().equals(CreateUserWindow.CHECK_CREATEUSERWINDOW_ACTIONCOMMAND)) 
		{
			CreateUserWindow cuw = (CreateUserWindow) e.getSource() ;
			
			String username = cuw.getTextField().getText() ; 
			
			if(username.length() <= User.MAX_NAME_SIZE && username.length() > 0 ) 
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
		else if(e.getActionCommand().equals(MainWindow.CHANGEUNAME_ACTIONCOMMAND)) 
		{
			view.openChangeUsernameWindow() ; 
			
		}
		else if(e.getActionCommand().equals(ChangeUsernameWindow.CHECK_CHANGEUSERNAME_ACTIONCOMMAND)) 
		{
			ChangeUsernameWindow cuw = (ChangeUsernameWindow) e.getSource() ;
			
			String username = cuw.getTextField().getText() ; 
			
			if(username.length() <= User.MAX_NAME_SIZE && username.length() > 0 ) 
			{
				if(changeUsername(username))
				{
					view.closeChangeUsernameWindow() ; 
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
		else if(e.getActionCommand().equals(SettingsWindow.CHANGE_DOWNLOADPATH_ACTIONCOMMAND)) 
		{
			SettingsWindow sw = (SettingsWindow) e.getSource() ;
			
			changeDownloadPath(sw.getDownloadPath());
			
		}
		
	}

	@Override
	public void fileDownloaded(UserId sender, Timestamp receiptDate) {
		
		downloadFile(sender, receiptDate);
	}


	@Override
	public void messageSent(User currentReceiver, String text) {
		sendMessage(currentReceiver, text) ; 
		
	}

	
	@Override
	public void fileMessageSent(User currentReceiver, DefaultListModel<String> pathList) {
		
        for(int i = 0; i< pathList.getSize();i++){
            String path = pathList.getElementAt(i) ; 
            sendFileMessage(currentReceiver,path) ;  
        }
		
	}

}
