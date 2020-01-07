package com.chatsystem.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.chatsystem.message.UserMessage;
import com.chatsystem.user.User;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JChatPanel extends JPanel implements ActionListener {
	
	public static final String SENDMESSAGE_ACTIONCOMMAND = "Send" ; 
	
	private JPanel messagePanel ;
	private JPanel textPannel ; 
	private JButton sendButton ; 
	private JTextArea textArea ; 
	
	private ArrayList<ActionListener> actionListeners ; 
	private User currentUser; 

	public User getCurrentUser() {
		return currentUser;
	}

	public JPanel getMessagePanel() {
		return messagePanel;
	}

	public JButton getSendButton() {
		return sendButton;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * Create the panel.
	 */
	public JChatPanel() {
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {570};
		gbl_contentPane.rowHeights = new int[] {306, 23};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0};
		setLayout(gbl_contentPane);
		
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
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.weightx = 1.0;
		add(messageScrollPane, gbc_scrollPane);
		
		
		// Text Panel 
		
		textPannel = new JPanel();
		
		GridBagConstraints gbc_textPannel = new GridBagConstraints();
		gbc_textPannel.weighty = 1.0;
		gbc_textPannel.insets = new Insets(0, 0, 5, 5);
		gbc_textPannel.anchor = GridBagConstraints.SOUTH;
		gbc_textPannel.fill = GridBagConstraints.BOTH;
		gbc_textPannel.gridx = 0;
		gbc_textPannel.gridy = 1;
		gbc_textPannel.weightx = 1.0;
		add(textPannel, gbc_textPannel);
		textPannel.setLayout(new BoxLayout(textPannel, BoxLayout.X_AXIS));
		

		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setRows(2);
		
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.getVerticalScrollBar().setUnitIncrement(15);

		textPannel.add(textScrollPane);
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);
		textPannel.add(sendButton);
		
		actionListeners = new ArrayList<ActionListener>();

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
		
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,SENDMESSAGE_ACTIONCOMMAND)));
	}
	
	public void ChangeConversation(User newUser,List<UserMessage> messages)
	{
		clear();

		sendButton.setEnabled(true);
		
		currentUser = newUser ; 
		
		for(UserMessage m : messages)
		{
			switch(m.getSubtype())
			{
				case TX:
				{
					JMessagePanel mp = new JMessagePanel(new String(m.getContent()));
					System.out.println("Message Added " + new String(m.getContent()));
					if(m.getSenderId().equals(currentUser.getId()))
					{
						mp.setToEmitterColor();
					}
					else
					{
						mp.setToReceiverColor();
					}
					
					messagePanel.add(mp);
					break ; 
				}
				case FL:
				{
					// TODO 
					break ; 
				}
			}
		}
		
		messagePanel.validate();
		messagePanel.repaint();
	}
	
	public void UpdateConversation(UserMessage newMessage)
	{
		JMessagePanel mp = new JMessagePanel(new String(newMessage.getContent()));
		if(newMessage.getSenderId().equals(currentUser.getId()))
		{
			mp.setToEmitterColor();
		}
		else
		{
			mp.setToReceiverColor();
		}
		
		messagePanel.add(mp);
		messagePanel.validate();
		messagePanel.repaint();
		
	}
	
	public void clear()
	{
		messagePanel.removeAll();
		messagePanel.validate();
		messagePanel.repaint();
		sendButton.setEnabled(false);
	}

}
