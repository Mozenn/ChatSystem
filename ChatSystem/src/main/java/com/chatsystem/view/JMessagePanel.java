package com.chatsystem.view;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

public class JMessagePanel extends JPanel {
	
	private JTextPane messagePane ; 

	public JTextPane getMessagePane() {
		return messagePane;
	}
	
	public void setToEmitterColor()
	{
		messagePane.setBackground(new Color(240, 248, 255));
	}
	
	public void setToReceiverColor()
	{
		messagePane.setBackground(new Color(255, 239, 213));
	}

	/**
	 * Create the panel.
	 */
	public JMessagePanel(String text) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(new Color(248, 248, 255));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		JLabel usernameLabel = new JLabel("Username");
		add(usernameLabel);
		
		messagePane = new JTextPane();
		messagePane.setText(text);
		messagePane.setBackground(new Color(255, 239, 213));
		add(messagePane);


	}

}
