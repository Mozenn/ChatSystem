package com.chatsystem.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.chatsystem.controller.Controller;
import com.chatsystem.user.User;

public class View implements ActionListener{
	
	private MainWindow mainWindow ; 
	private ActionListener controller ; 
	
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public View(ActionListener c)
	{
		this.controller = c ; 
	}
	
	public void init() 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
    private void createAndShowGUI() {
		try {
			mainWindow = new MainWindow();
			mainWindow.setVisible(true);
			mainWindow.getSendButton().setActionCommand("SendMessage"); // TODO replace hardcoded string 
			mainWindow.getSendButton().addActionListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    
    public void addSession(User u)
    {
    	JSessionPanel newSession = new JSessionPanel(u); 
    	newSession.addActionListener(this);
    	newSession.addActionListener(controller);
    	mainWindow.getOngoingSessionPannel().add(newSession);
    }
    
    public void addConnectedUser(User u)
    {
    	JSessionPanel newSession = new JSessionPanel(u); 
    	newSession.addActionListener(this);
    	newSession.addActionListener(controller);
    	mainWindow.getOngoingSessionPannel().add(newSession);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("CloseSession")) // TODO replace hardcoded string 
		{
			// remove component from session panel, modify component on userpanel(make active) 
		}
		else if(e.getActionCommand().equals("StartSession")) // TODO replace hardcoded string 
		{
			// add call addSession, modify component on userpanel (make passive) 
		}
		else if(e.getActionCommand().equals("SendMessage")) // TODO replace hardcoded string 
		{
			controller.actionPerformed(new ActionEvent(mainWindow, 0, "SendMessage"));
			// clear text area 
		}
			
		
	}
    
}
