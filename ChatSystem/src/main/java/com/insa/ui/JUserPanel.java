package com.insa.ui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;

public class JUserPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public JUserPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JButton userButton = new JButton("Hey");
		userButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(userButton);

	}

}
