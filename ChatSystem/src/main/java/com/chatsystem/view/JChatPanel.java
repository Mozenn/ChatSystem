package com.chatsystem.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class JChatPanel extends JPanel {
	
	private JPanel messagePanel ;
	private JPanel textPannel ; 
	private JButton sendButton ; 
	private JTextArea textArea ; 

	/**
	 * Create the panel.
	 */
	public JChatPanel() {
		
		// Messages Panel 		
		messagePanel = new JPanel();
		
		JScrollPane messageScrollPane = new JScrollPane(messagePanel);
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messageScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 4.0;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.anchor = GridBagConstraints.SOUTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.weightx = 1.0;
		contentPane.add(messageScrollPane, gbc_scrollPane);
		
		// Text Panel 
		
		textPannel = new JPanel();
		GridBagConstraints gbc_textPannel = new GridBagConstraints();
		gbc_textPannel.weighty = 1.0;
		gbc_textPannel.insets = new Insets(0, 0, 5, 5);
		gbc_textPannel.anchor = GridBagConstraints.PAGE_END;
		gbc_textPannel.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPannel.gridx = 1;
		gbc_textPannel.gridy = 1;
		gbc_textPannel.weightx = 1.0;
		contentPane.add(textPannel, gbc_textPannel);
		textPannel.setLayout(new BoxLayout(textPannel, BoxLayout.X_AXIS));
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textPannel.add(textArea);
		
		JButton sendButton = new JButton("Send");
		textPannel.add(sendButton);

	}

}
