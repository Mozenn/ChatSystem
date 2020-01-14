package com.chatsystem.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.chatsystem.controller.ControllerContract;
import com.chatsystem.session.local.LocalSession;
import com.chatsystem.user.User;

import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.Frame;
import javax.swing.ScrollPaneConstants;

public class MainWindow extends JFrame implements ActionEmitter {



	private JPanel contentPane;
	private JPanel ongoingSessionPannel ; 
	private JPanel connectedUserPannel ; 
	private JMenuBar mainMenuBar ; 
	private JChatPanel chatPanel ; 
	
	public JPanel getOngoingSessionPannel() {
		return ongoingSessionPannel;
	}

	public JPanel getConnectedUserPannel() {
		return connectedUserPannel;
	}

	public JChatPanel getChatPanel() {
		return chatPanel;
	}

	public JMenuBar getmainMenuBar() {
		
		return this.mainMenuBar;
	}
	
	private ArrayList<ActionListener> actionListeners ; 
	
	public static final String CLOSEMAINWINDOW_ACTIONCOMMAND = "CloseMainWindow" ; 
	public static final String CHANGEUNAME_ACTIONCOMMAND = "ChangeUname" ; 
	
	public void addActionListener(ActionListener l)
	{
		if(!actionListeners.contains(l))
			actionListeners.add(l); 
	}
	
	public void removeActionListener(ActionListener l) 
	{
		actionListeners.remove(l) ; 
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		setMinimumSize(new Dimension(500, 350));
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CLOSEMAINWINDOW_ACTIONCOMMAND)));
		    }
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {2, 570, 2};
		gbl_contentPane.rowHeights = new int[] {350};
		gbl_contentPane.columnWeights = new double[]{0.5, 1.0, 0.5};
		gbl_contentPane.rowWeights = new double[]{1.0};
		contentPane.setLayout(gbl_contentPane);
		
		// Add mainMenuBar 
	
		mainMenuBar = new JMenuBar(); 
		
		var fileMenu = new JMenu("File") ; 
		fileMenu.setMnemonic(KeyEvent.VK_F);
		var exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		mainMenuBar.add(fileMenu); 
		
		var editMenu = new JMenu("Edit") ; 
		editMenu.setMnemonic(KeyEvent.VK_E);
		var ChangeUsernameItem = new JMenuItem("Change Username");
		editMenu.add(ChangeUsernameItem);
		mainMenuBar.add(editMenu); 
		
		setJMenuBar(mainMenuBar);
		
		// Session panel 
		
		ongoingSessionPannel = new JPanel();
				
		JScrollPane sessionScrollPane = new JScrollPane(ongoingSessionPannel);
		sessionScrollPane.setMinimumSize(new Dimension(100, 500));
		sessionScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		GridBagConstraints gbc_sessionScrollPane = new GridBagConstraints();
		gbc_sessionScrollPane.gridheight = 1;
		gbc_sessionScrollPane.weightx = 0.7;
		gbc_sessionScrollPane.weighty = 1.0;
		gbc_sessionScrollPane.fill = GridBagConstraints.BOTH;
		gbc_sessionScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_sessionScrollPane.gridx = 0;
		gbc_sessionScrollPane.gridy = 0;
		contentPane.add(sessionScrollPane, gbc_sessionScrollPane);
		ongoingSessionPannel.setLayout(new BoxLayout(ongoingSessionPannel, BoxLayout.Y_AXIS));
		

		// Connected User panel 
		
		connectedUserPannel = new JPanel();
		
		JScrollPane connectedUserScrollPane = new JScrollPane(connectedUserPannel);
		connectedUserScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		connectedUserScrollPane.setMinimumSize(new Dimension(100, 500));
		GridBagConstraints gbc_connectedUserScrollPane = new GridBagConstraints();
		gbc_connectedUserScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_connectedUserScrollPane.gridheight = 1;
		gbc_connectedUserScrollPane.weighty = 1.0;
		gbc_connectedUserScrollPane.weightx = 0.7;
		gbc_connectedUserScrollPane.fill = GridBagConstraints.BOTH;
		gbc_connectedUserScrollPane.gridx = 2;
		gbc_connectedUserScrollPane.gridy = 0;
		contentPane.add(connectedUserScrollPane, gbc_connectedUserScrollPane);
		connectedUserPannel.setLayout(new BoxLayout(connectedUserPannel, BoxLayout.Y_AXIS));
		
		// Chat Panel 		

		chatPanel = new JChatPanel();
		
		connectedUserScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		chatPanel.setMinimumSize(new Dimension(200, 500));
		GridBagConstraints gbc_chatPanel = new GridBagConstraints();
		gbc_chatPanel.insets = new Insets(0, 0, 5, 0);
		gbc_chatPanel.gridheight = 1;
		gbc_chatPanel.weighty = 1.0;
		gbc_chatPanel.weightx = 1.0;
		gbc_chatPanel.fill = GridBagConstraints.BOTH;
		gbc_chatPanel.gridx = 1;
		gbc_chatPanel.gridy = 0;
		contentPane.add(chatPanel, gbc_chatPanel);
		
		
		actionListeners = new ArrayList<ActionListener>();
		

	}

}
