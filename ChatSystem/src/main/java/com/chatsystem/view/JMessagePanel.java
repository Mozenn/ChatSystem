package com.chatsystem.view;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

public class JMessagePanel extends JPanel {
	
	private JLabel messageLabel ; 

	public JLabel getMessagePane() {
		return messageLabel;
	}
	
	public void setToEmitterColor()
	{
		messageLabel.setBackground(new Color(240, 248, 255));
		messageLabel.repaint();
	}
	
	public void setToReceiverColor()
	{
		messageLabel.setBackground(new Color(255, 239, 213));
		messageLabel.repaint();
	}

	/**
	 * Create the panel.
	 */
	public JMessagePanel(String text,Timestamp date) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(new Color(248, 248, 255));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		JLabel usernameLabel = new JLabel("Username");
		add(usernameLabel);
		
		messageLabel = new JLabel(text);
		messageLabel.setOpaque(true);
		messageLabel.setBackground(new Color(211, 211, 211));
		messageLabel.repaint();
		add(messageLabel);


	}

}
