package com.insa.ui;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;

public class JMessagePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public JMessagePanel() {
		setBackground(new Color(248, 248, 255));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		JLabel usernameLabel = new JLabel("Username");
		add(usernameLabel);
		
		JTextPane messagePane = new JTextPane();
		messagePane.setText("Hey there, just filling some space");
		messagePane.setBackground(new Color(211, 211, 211));
		add(messagePane);


	}

}
