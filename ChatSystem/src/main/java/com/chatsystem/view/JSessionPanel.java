package com.chatsystem.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;

import com.chatsystem.user.User;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.border.LineBorder;

public class JSessionPanel extends JPanel implements ActionListener {
	
	private User receiver ; 
	private JButton closeButton ; 
	private JButton displayButton ; 
	private ArrayList<ActionListener> actionListeners ; 
	
	public static final String CLOSE_ACTIONCOMMAND = "Close" ; 
	public static final String DISPLAY_ACTIONCOMMAND = "Display" ; 

	/**
	 * Create the panel.
	 */
	public JSessionPanel(User u) {
		setBorder(new LineBorder(new Color(240, 230, 140), 1, true));
		
		JLabel usernameLabel = new JLabel(u.getUsername());
		add(usernameLabel);
		
		displayButton = new JButton("Display");
		displayButton.addActionListener(this);
		displayButton.setActionCommand("Display");
		add(displayButton);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		closeButton.setActionCommand("Close");
		add(closeButton);
		


	}
	
	public User getReceiver() {
		return receiver;
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
		
		if(e.getActionCommand().equals("Close"))
			actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CLOSE_ACTIONCOMMAND)));
		else if(e.getActionCommand().equals("Display"))
			actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,DISPLAY_ACTIONCOMMAND)));
	}

}
