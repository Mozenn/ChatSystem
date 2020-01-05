package com.chatsystem.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;

import com.chatsystem.message.UserMessage;
import com.chatsystem.model.SessionListener;
import com.chatsystem.model.SessionModel;
import com.chatsystem.user.User;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.border.LineBorder;

public class JSessionPanel extends JPanel implements ActionListener, SessionListener {
	
	private User receiver ; 
	private SessionModel session ; 
	
	private JButton closeButton ; 
	private JButton displayButton ; 
	private ArrayList<ActionListener> actionListeners ; 
	
	public static final String CLOSE_ACTIONCOMMAND = "Close" ; 
	public static final String DISPLAY_ACTIONCOMMAND = "Display" ; 
	public static final String MESSAGERECEIVED_ACTIONCOMMAND = "Received" ; 

	/**
	 * Create the panel.
	 */
	public JSessionPanel(User u, SessionModel s) {
		setBorder(new LineBorder(new Color(105, 105, 105), 1, true));
		
		JLabel usernameLabel = new JLabel(u.getUsername());
		add(usernameLabel);
		
		displayButton = new JButton("Display");
		displayButton.addActionListener(this);
		displayButton.setActionCommand(DISPLAY_ACTIONCOMMAND);
		add(displayButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		closeButton.setActionCommand(CLOSE_ACTIONCOMMAND);
		add(closeButton);
		
		actionListeners = new ArrayList<ActionListener>();
		
		this.session = s ; 
		session.addSessionListener(this);
		this.receiver = u ; 

	}
	
	public User getReceiver() {
		return receiver;
	}
	
	public SessionModel getSession()
	{
		return session ; 
	}
	
	public void addActionListener(ActionListener l)
	{
		if(!actionListeners.contains(l))
			actionListeners.add(l); 
	}
	
	public void removeActionListener(ActionListener l) 
	{
		actionListeners.remove(l) ; 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(CLOSE_ACTIONCOMMAND) && !actionListeners.isEmpty())
			actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CLOSE_ACTIONCOMMAND)));
		else if(e.getActionCommand().equals(DISPLAY_ACTIONCOMMAND) && !actionListeners.isEmpty())
			actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,DISPLAY_ACTIONCOMMAND)));
	}

	@Override
	public void messageReceived(UserMessage m) {
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,MESSAGERECEIVED_ACTIONCOMMAND)));
		
	}

}
