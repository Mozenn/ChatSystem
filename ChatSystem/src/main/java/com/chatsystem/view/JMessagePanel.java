package com.chatsystem.view;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import java.awt.Component;

public class JMessagePanel extends JPanel {
	
	private JLabel messageLabel ; 

	public JLabel getMessagePane() {
		return messageLabel;
	}
	
	public void setToEmitterColor()
	{
		setBackground(new Color(240, 248, 255));
		repaint();
	}
	
	public void setToReceiverColor()
	{
		setBackground(new Color(255, 239, 213));
		repaint();
	}

	/**
	 * Create the panel.
	 */
	public JMessagePanel(String username, String text,Timestamp date) {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(new Color(248, 248, 255));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel usernameLabel = new JLabel(username);
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(usernameLabel);
		
		Component horizontalStrut2 = Box.createHorizontalStrut(30);
		horizontalStrut2.setMaximumSize(getPreferredSize());
		panel.add(horizontalStrut2);
		
		JLabel timestampLabel = new JLabel(date.toString());
		timestampLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(timestampLabel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		messageLabel = new JLabel(text);
		messageLabel.setOpaque(true);
		messageLabel.setBackground(new Color(211, 211, 211));
		messageLabel.repaint();
		add(messageLabel);
		

		
		
		



	}

}
