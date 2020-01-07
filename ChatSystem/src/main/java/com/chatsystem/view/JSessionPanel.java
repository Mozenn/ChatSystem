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
	
	public enum SessionPanelState
	{
		UNREAD("UNREAD"),
		READ("READ");
		
	    private String type;

	    SessionPanelState(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}
	
	private SessionModel session ; 
	
	private JButton closeButton ; 
	private JButton displayButton ; 
	private ArrayList<ActionListener> actionListeners ; 
	
	public static final String CLOSE_ACTIONCOMMAND = "Close" ; 
	public static final String DISPLAY_ACTIONCOMMAND = "Display" ; 
	public static final String MESSAGERECEIVED_ACTIONCOMMAND = "Received" ; 
	
	private final Color UNREAD_COLOR = new Color(255, 165, 0) ; 
	private final Color READ_COLOR = new Color(211,211,211); 

	/**
	 * Create the panel.
	 */
	public JSessionPanel(SessionModel s) {
		setBackground(new Color(211,211,211));
		setBorder(new LineBorder(new Color(105, 105, 105), 1, true));
		
		JLabel usernameLabel = new JLabel(s.getReceiver().getUsername());
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

	}
	
	public SessionModel getSessionModel()
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
		{
			actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,DISPLAY_ACTIONCOMMAND)));
			setState(SessionPanelState.READ); 
		}
			
	}

	@Override
	public void messageAdded(UserMessage m) {
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,MESSAGERECEIVED_ACTIONCOMMAND)));
		setState(SessionPanelState.UNREAD); 
	}
	
	private void setState(SessionPanelState state)
	{
		switch(state)
		{
			case UNREAD:
			{
				setBackground(UNREAD_COLOR);
				break ; 
			}
			case READ :
			{
				setBackground(READ_COLOR);
				break ; 
			}
		}
	}

}
