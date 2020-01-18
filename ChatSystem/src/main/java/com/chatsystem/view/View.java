package com.chatsystem.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.chatsystem.controller.Controller;
import com.chatsystem.controller.ControllerContract;
import com.chatsystem.message.UserMessage;
import com.chatsystem.model.SessionListener;
import com.chatsystem.model.SessionModel;
import com.chatsystem.model.SystemContract;
import com.chatsystem.model.SystemListener;
import com.chatsystem.model.SystemModel;
import com.chatsystem.user.User;

public class View implements ActionListener, SystemListener, SessionListener{
	
	private MainWindow mainWindow ; 
	private CreateUserWindow createUserWindow ;
	private ChangeUsernameWindow changeUsernameWindow ; 
	private ControllerContract controller ; 
	private SystemModel systemModel ; 
	
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public View(ControllerContract c, SystemModel sm)
	{
		this.controller = c ; 
		this.systemModel = sm ; 
		sm.addSystemListener(this);
	
	}
	
	public void openMainWindow()
	{
		mainWindow = new MainWindow();
		mainWindow.addActionListener(controller) ; 
		mainWindow.addActionListener(this);
		mainWindow.setVisible(true);
		mainWindow.getChatPanel().addActionListener(this);
		mainWindow.getChatPanel().addChatListener(controller);

	}
	
	public void openCreateUserWindow()
	{
		createUserWindow = new CreateUserWindow();
		createUserWindow.addActionListener(controller) ; 
		createUserWindow.setVisible(true);

	}
	
	public void closeCreateUserWindow()
	{
		createUserWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE) ; 
		createUserWindow.setVisible(false);
		createUserWindow.dispose();
		createUserWindow = null ; 
	}
	
	public void openChangeUsernameWindow()
	{
		changeUsernameWindow = new ChangeUsernameWindow();
		changeUsernameWindow.addActionListener(controller) ; 
		changeUsernameWindow.setVisible(true);

	}
	
	public void closeChangeUsernameWindow()
	{
		changeUsernameWindow.setVisible(false);
		changeUsernameWindow.dispose();
		changeUsernameWindow = null ; 
	}
	
	public void openSettingsWindow()
	{
		if(systemModel.getUser().isPresent())
		{
			SettingsWindow settingsWindow = new SettingsWindow(systemModel.getUser().get().getUsername(),systemModel.getDownloadPath());
			settingsWindow.addActionListener(controller) ; 
			settingsWindow.setVisible(true);
		}


	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(JSessionPanel.CLOSE_ACTIONCOMMAND)) 
		{
			JSessionPanel js = (JSessionPanel) e.getSource(); 
			
			
			for (Component c : mainWindow.getConnectedUserPannel().getComponents()) {
			    if (c instanceof JUserPanel ) { 
			       JUserPanel up = (JUserPanel)c ; 
			       if(up.getUser().equals(js.getSessionModel().getReceiver()))
			       {
			    	   up.makeActive();
			       }
			    }
			}
			
			mainWindow.getOngoingSessionPannel().remove(js);
	    	mainWindow.getOngoingSessionPannel().validate();
	    	mainWindow.getOngoingSessionPannel().repaint();
	    	mainWindow.getChatPanel().clear(); 
		}
		else if(e.getActionCommand().equals(JSessionPanel.DISPLAY_ACTIONCOMMAND)) 
		{
			JSessionPanel js = (JSessionPanel) e.getSource(); 
			
			if(!mainWindow.getChatPanel().getCurrentReceiver().equals(js.getSessionModel().getReceiver()))
			{
				mainWindow.getChatPanel().ChangeConversation(js.getSessionModel().getEmitter(),js.getSessionModel().getReceiver(), js.getSessionModel().getMessages()); 
			}
			
			
		}
		else if(e.getActionCommand().equals(JUserPanel.STARTSESSION_ACTIONCOMMAND)) 
		{
			JUserPanel up = (JUserPanel) e.getSource(); 
			
			up.makeInactive();
			
		}
		else if(e.getActionCommand().equals(MainWindow.OPENSETTINGS_ACTIONCOMMAND)) 
		{
			openSettingsWindow() ; 
			
		}
			
		
	}
	
    private void addSessionPanel(SessionModel sm)
    {
    	sm.addSessionListener(this);
    	JSessionPanel newSession = new JSessionPanel(sm); 
    	newSession.addActionListener(this);
    	newSession.addActionListener(controller);
    	mainWindow.getOngoingSessionPannel().add(newSession);
    	mainWindow.getOngoingSessionPannel().validate();
    	mainWindow.getOngoingSessionPannel().repaint();
    	
    }
    
    private void addConnectedUserPanel(User u)
    {
    	JUserPanel newUser = new JUserPanel(u); 
    	newUser.addActionListener(this);
    	newUser.addActionListener(controller);
    	mainWindow.getConnectedUserPannel().add(newUser);
    	mainWindow.getConnectedUserPannel().validate();
    	mainWindow.getConnectedUserPannel().repaint();
    }

	@Override
	public void sessionStarted(SessionModel sm) {
		addSessionPanel(sm);
		mainWindow.getChatPanel().ChangeConversation(sm.getEmitter(),sm.getReceiver(), sm.getMessages()); 
		
		for (Component c : mainWindow.getConnectedUserPannel().getComponents()) {
		    if (c instanceof JUserPanel ) { 
		       JUserPanel up = (JUserPanel)c ; 
		       if(up.getUser().equals(sm.getReceiver()))
		       {
		    	   up.makeInactive();
		       }
		    }
		}
	}

	@Override
	public void sessionClosed(SessionModel sm) {
		
		for (Component c : mainWindow.getOngoingSessionPannel().getComponents()) {
		    if (c instanceof JSessionPanel ) { 
		    	JSessionPanel sp = (JSessionPanel)c ; 
		       if(sp.getSessionModel().getReceiver().equals(sm.getReceiver()))
		       {
		    	   sp.getSessionModel().clearSessionListeners();
		    	   mainWindow.getOngoingSessionPannel().remove(sp) ; 
		    	   mainWindow.getOngoingSessionPannel().validate();
		    	   mainWindow.getOngoingSessionPannel().repaint();
				
		       }
		    }
		}
		
		for (Component c2 : mainWindow.getConnectedUserPannel().getComponents()) {
		    if (c2 instanceof JUserPanel ) { 
		       JUserPanel up = (JUserPanel)c2 ; 
		       if(up.getUser().equals(sm.getReceiver()))
		       {
		    	   up.makeActive();
		       }
		    }
		}
		
		mainWindow.getChatPanel().clear() ; 
	}

	@Override
	public void userConnection(User u) {
		addConnectedUserPanel(u);
		
	}

	@Override
	public void userDisconnection(User u) {
		
		for (Component c : mainWindow.getConnectedUserPannel().getComponents()) {
		    if (c instanceof JUserPanel ) { 
		       JUserPanel up = (JUserPanel)c ; 
		       if(up.getUser().equals(u))
		       {
		    	   mainWindow.getConnectedUserPannel().remove(up);
			       mainWindow.getConnectedUserPannel().validate();
			       mainWindow.getConnectedUserPannel().repaint();
		       }
		    }
		}
		
	}

	@Override
	public void messageAdded(UserMessage m) {
		if(mainWindow.getChatPanel().getCurrentReceiver().getId().equals(m.getSenderId()) || mainWindow.getChatPanel().getCurrentReceiver().getId().equals(m.getReceiverId()))
		{
			System.out.println("View Message Received");
			mainWindow.getChatPanel().UpdateConversation(m);
		}
	}
    
}
