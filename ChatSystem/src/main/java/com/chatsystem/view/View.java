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
import com.chatsystem.model.SystemListener;
import com.chatsystem.model.SystemModel;
import com.chatsystem.user.User;

public class View implements ActionListener, SystemListener, SessionListener{
	
	private MainWindow mainWindow ; 
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
		mainWindow.setVisible(true);
		mainWindow.getChatPanel().getSendButton().setActionCommand(JChatPanel.SENDMESSAGE_ACTIONCOMMAND); 
		mainWindow.getChatPanel().addActionListener(this);
		mainWindow.getChatPanel().addActionListener(controller);

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
			
			if(!mainWindow.getChatPanel().getCurrentUser().equals(js.getSessionModel().getReceiver()))
			{
				mainWindow.getChatPanel().ChangeConversation(js.getSessionModel().getReceiver(), js.getSessionModel().getMessages()); 
			}
			
			
		}
		else if(e.getActionCommand().equals(JUserPanel.STARTSESSION_ACTIONCOMMAND)) 
		{
			JUserPanel up = (JUserPanel) e.getSource(); 
			
			up.makeInactive();
			
		}
		else if(e.getActionCommand().equals(JSessionPanel.MESSAGERECEIVED_ACTIONCOMMAND)) 
		{
			//
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
		mainWindow.getChatPanel().ChangeConversation(sm.getReceiver(), sm.getMessages()); 
		
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
		
		if(mainWindow.getChatPanel().getCurrentUser().getId().equals(m.getReceiverId()) || mainWindow.getChatPanel().getCurrentUser().getId().equals(m.getSenderId()))
		{
			mainWindow.getChatPanel().UpdateConversation(m);
		}
	}
    
}
