package com.insa.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Frame;
import javax.swing.ScrollPaneConstants;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setMinimumSize(new Dimension(500, 350));
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {2, 570, 2};
		gbl_contentPane.rowHeights = new int[] {306, 23};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 0.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		
		// Add menuBar 
	
		var menuBar = new JMenuBar(); 
		
		var fileMenu = new JMenu("File") ; 
		fileMenu.setMnemonic(KeyEvent.VK_F);
		var exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu); 
		
		var editMenu = new JMenu("Edit") ; 
		editMenu.setMnemonic(KeyEvent.VK_E);
		var ChangeUsernameItem = new JMenuItem("Change Username");
		editMenu.add(ChangeUsernameItem);
		menuBar.add(editMenu); 
		
		setJMenuBar(menuBar);
		
		// Session panel 
		
		JPanel ongoingSessionPannel = new JPanel();
		
				
		JScrollPane sessionScrollPane = new JScrollPane(ongoingSessionPannel);
		sessionScrollPane.setMinimumSize(new Dimension(200, 500));
		GridBagConstraints gbc_sessionScrollPane = new GridBagConstraints();
		gbc_sessionScrollPane.gridheight = 2;
		gbc_sessionScrollPane.weightx = 1.0;
		gbc_sessionScrollPane.weighty = 1.0;
		gbc_sessionScrollPane.fill = GridBagConstraints.BOTH;
		gbc_sessionScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_sessionScrollPane.gridx = 0;
		gbc_sessionScrollPane.gridy = 0;
		contentPane.add(sessionScrollPane, gbc_sessionScrollPane);
		ongoingSessionPannel.setLayout(new BoxLayout(ongoingSessionPannel, BoxLayout.Y_AXIS));
		

		// Connected User panel 
		
		JPanel connectedUserPannel = new JPanel();
		JScrollPane connectedUserScrollPane = new JScrollPane(connectedUserPannel);
		connectedUserScrollPane.setMinimumSize(new Dimension(200, 500));
		GridBagConstraints gbc_connectedUserScrollPane = new GridBagConstraints();
		gbc_connectedUserScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_connectedUserScrollPane.gridheight = 2;
		gbc_connectedUserScrollPane.weighty = 1.0;
		gbc_connectedUserScrollPane.weightx = 1.0;
		gbc_connectedUserScrollPane.fill = GridBagConstraints.BOTH;
		gbc_connectedUserScrollPane.gridx = 2;
		gbc_connectedUserScrollPane.gridy = 0;
		contentPane.add(connectedUserScrollPane, gbc_connectedUserScrollPane);
		connectedUserPannel.setLayout(new BoxLayout(connectedUserPannel, BoxLayout.Y_AXIS));
		
		// Messages Panel 		
		JPanel messagePannel = new JPanel();
		
		JScrollPane messageScrollPane = new JScrollPane(messagePannel);
		messagePannel.setLayout(new BoxLayout(messagePannel, BoxLayout.Y_AXIS));
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
		
		JPanel textPannel = new JPanel();
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
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textPannel.add(textArea);
		
		JButton sendButton = new JButton("Send");
		textPannel.add(sendButton);
		
		for(int i = 0 ; i < 100 ; i++)
		{
			JButton btnNewButton = new JButton("New button");
			connectedUserPannel.add(btnNewButton);
		}
		
		for(int i = 0 ; i < 100 ; i++)
		{
			JButton User1 = new JButton("New button");
			ongoingSessionPannel.add(User1);
		}
		

	}

}
