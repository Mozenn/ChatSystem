package com.chatsystem.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.chatsystem.controller.Controller;
import com.chatsystem.controller.ControllerContract;
import com.chatsystem.model.SessionModel;
import com.chatsystem.model.SystemListener;
import com.chatsystem.model.SystemModel;
import com.chatsystem.user.User;

public class View implements ActionListener, SystemListener{
	
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
		mainWindow.getChatPanel().getSendButton().addActionListener(this);
		mainWindow.getChatPanel().getSendButton().addActionListener(controller);

	}
	
    
    private void addSessionPanel(User u, SessionModel sm)
    {
    	JSessionPanel newSession = new JSessionPanel(u,sm); 
    	newSession.addActionListener(this);
    	newSession.addActionListener(controller);
    	mainWindow.getOngoingSessionPannel().add(newSession);
    }
    
    private void addConnectedUserPanel(User u)
    {
    	// TODO 
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(JSessionPanel.CLOSE_ACTIONCOMMAND)) 
		{
			JSessionPanel js = (JSessionPanel) e.getSource(); 
			
			
			for (Component c : mainWindow.getConnectedUserPannel().getComponents()) {
			    if (c instanceof JUserPanel ) { 
			       JUserPanel up = (JUserPanel)c ; 
			       if(up.getUser().equals(js.getReceiver()))
			       {
			    	   up.makeActive();
			       }
			    }
			}
			
			mainWindow.getOngoingSessionPannel().remove(js);
		}
		else if(e.getActionCommand().equals(JSessionPanel.DISPLAY_ACTIONCOMMAND)) 
		{
			JSessionPanel js = (JSessionPanel) e.getSource(); 
			
			mainWindow.getChatPanel().ChangeDisplayedConversation(js.getReceiver(), js.getSession().getMessages()); 
		}
		else if(e.getActionCommand().equals(JUserPanel.STARTSESSION_ACTIONCOMMAND)) // TODO this code should be called by listening to system Model 
		{
			JUserPanel up = (JUserPanel) e.getSource(); 
			
			addSession(up.getUser());
			up.makeInactive();
			mainWindow.getChatPanel().ChangeDisplayedConversation(up.getUser(), messagesToDisplay); // TODO query model to get history 
		}
		else if(e.getActionCommand().equals(JChatPanel.SENDMESSAGE_ACTIONCOMMAND)) 
		{
			// clear text area 
			mainWindow.getChatPanel().getTextArea().setText("");
		}
			
		
	}

	@Override
	public void sessionCreated(SessionModel sm) {
		// TODO create new session pannel 
		
	}

	@Override
	public void sessionClosed(SessionModel sm) {
		// TODO remove sessionpanel 
		
	}
    
}
