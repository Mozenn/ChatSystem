package com.chatsystem.view;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileSystemView;

import com.chatsystem.message.UserMessage;
import com.chatsystem.model.FileWrapper;
import com.chatsystem.model.SessionListener;
import com.chatsystem.user.User;
import com.chatsystem.utility.SerializationUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.awt.CardLayout;

public class JChatPanel extends JPanel implements ActionListener, ActionEmitter, ChatEmitter {
	
	private enum DisplayedCard
	{
		TEXT("TEXT"),
		FILE("REFILEAD");
		
	    private String type;

	    DisplayedCard(String type) {
	        this.type = type;
	    }

	    String getType() {
	        return type;
	    }
	}
	
	private static final String SENDMESSAGE_ACTIONCOMMAND = "Send" ; 
	private static final String SENDFILEMESSAGE_ACTIONCOMMAND = "SendFile" ; 
	private static final String SWITCH_ACTIONCOMMAND = "Switch" ; 
	
	private JPanel cardPanel;
	private CardLayout cardLayout ; 
	private JScrollPane messageScrollPane ; 
	
	// Text Panel card widgets 
	
	private JPanel messagePanel ;
	private JPanel textPannel ; 
	private JPanel buttonInTextPanel;
	private JButton sendTextButton ; 
	private JButton switchInTextButton;
	private JTextArea textArea ; 
	
	// File Panel card widgets 
	
	private JPanel filePannel ; 
	private JPanel buttonInFilePanel;
	private JPanel fileListButtonPanel;
	private JButton sendFileButton ; 
	private JButton joinFileButton ; 
	private JButton removeFileButton ;
	private JButton switchInFileButton;
	private JList<String> fileList ; 
	
	private ArrayList<ActionListener> actionListeners ; 
	private User currentReceiver;
	private User currentEmitter;
	private final EventListenerList listeners = new EventListenerList();




	public User getCurrentReceiver() {
		return currentReceiver;
	}
	
	public User getCurrentEmitter() {
		return currentEmitter;
	}

	public JPanel getMessagePanel() {
		return messagePanel;
	}

	public JButton getSendButton() {
		return sendTextButton;
	}
	
	public JButton getSendFileButton() {
		return sendFileButton;
	}

	public JTextArea getTextArea() {
		return textArea;
	}
	
	public JList<String> getFileList()
	{
		return fileList ; 
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
		
		messageScrollPane = new JScrollPane(messagePanel);
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messageScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 5.0;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.anchor = GridBagConstraints.SOUTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.weightx = 1.0;
		add(messageScrollPane, gbc_scrollPane);
		
		
		cardPanel = new JPanel();
		GridBagConstraints gbc_cardPanel = new GridBagConstraints();
		gbc_cardPanel.weighty = 1.0;
		gbc_cardPanel.insets = new Insets(0, 0, 5, 0);
		gbc_cardPanel.anchor = GridBagConstraints.SOUTH;
		gbc_cardPanel.fill = GridBagConstraints.BOTH;
		gbc_cardPanel.gridx = 0;
		gbc_cardPanel.gridy = 1;
		gbc_cardPanel.weightx = 1.0;
		add(cardPanel, gbc_cardPanel);
		cardLayout = new CardLayout(0, 0) ; 
		cardPanel.setLayout(cardLayout);
		
		// =============  Text Panel =============== 
		
		textPannel = new JPanel();
		textPannel.setLayout(new BoxLayout(textPannel, BoxLayout.X_AXIS));
		cardPanel.add(textPannel, DisplayedCard.TEXT.getType());

		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setRows(2);

		
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		textPannel.add(textScrollPane);
		
		buttonInTextPanel = new JPanel();
		textPannel.add(buttonInTextPanel);
		buttonInTextPanel.setLayout(new BoxLayout(buttonInTextPanel, BoxLayout.Y_AXIS));
		
		sendTextButton = new JButton("Send");
		sendTextButton.addActionListener(this);
		sendTextButton.setActionCommand(SENDMESSAGE_ACTIONCOMMAND);
		sendTextButton.setEnabled(false);
		buttonInTextPanel.add(sendTextButton) ; 
		
		switchInTextButton = new JButton("Switch");
		switchInTextButton.addActionListener(this);
		switchInTextButton.setActionCommand(SWITCH_ACTIONCOMMAND);
		buttonInTextPanel.add(switchInTextButton) ; 
		
		// =============  File Panel =============== 
		
		filePannel = new JPanel();
		filePannel.setLayout(new BoxLayout(filePannel, BoxLayout.X_AXIS));
		cardPanel.add(filePannel, DisplayedCard.FILE.getType());
		
		fileList = new JList<String>(new DefaultListModel<String>()) ; 
		
		JScrollPane fileListScrollPane = new JScrollPane(fileList);
		fileListScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		filePannel.add(fileListScrollPane) ; 
		
		fileListButtonPanel = new JPanel();
		filePannel.add(fileListButtonPanel);
		fileListButtonPanel.setLayout(new BoxLayout(fileListButtonPanel, BoxLayout.Y_AXIS));
		
		joinFileButton = new JButton("Join");
		joinFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				var model = (DefaultListModel<String>)fileList.getModel() ; 
				
				// TODO use jFileChooser 
				JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				
				int r = j.showDialog(null, "Select") ; 
				
	            if (r == JFileChooser.APPROVE_OPTION) { 
	                // get the selelcted files 
	                File file = j.getSelectedFile() ; 
	                
	                model.addElement(file.getAbsolutePath());

	            } 
	            // if the user cancelled the operation 
	            else
	                return ; 
				
				
				
			}});
		fileListButtonPanel.add(joinFileButton);
		
		removeFileButton = new JButton("Remove");
		removeFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				var model = (DefaultListModel<String>)fileList.getModel() ; 
				while(fileList.getSelectedIndex() != -1)
				{
					model.remove(fileList.getSelectedIndex());
				}
			}});
		fileListButtonPanel.add(removeFileButton);
		
		buttonInFilePanel = new JPanel();
		filePannel.add(buttonInFilePanel);
		buttonInFilePanel.setLayout(new BoxLayout(buttonInFilePanel, BoxLayout.Y_AXIS));
		
		sendFileButton = new JButton("Send");
		sendFileButton.addActionListener(this);
		sendFileButton.setActionCommand(SENDFILEMESSAGE_ACTIONCOMMAND);
		sendFileButton.setEnabled(false);
		buttonInFilePanel.add(sendFileButton);
		
		switchInFileButton = new JButton("Switch");
		switchInFileButton.addActionListener(this);
		switchInFileButton.setActionCommand(SWITCH_ACTIONCOMMAND);
		buttonInFilePanel.add(switchInFileButton) ; 
		
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
		if(e.getActionCommand().equals(SENDMESSAGE_ACTIONCOMMAND))
		{	
			fireMessageSent(textArea.getText());
			
			textArea.setText("");
			
		}
			
		else if(e.getActionCommand().equals(SENDFILEMESSAGE_ACTIONCOMMAND) )
		{
			var model = (DefaultListModel<String>)fileList.getModel();
			fireFileMessageSent(model);

			model.removeAllElements();
					
		}
		else if(e.getActionCommand().equals(SWITCH_ACTIONCOMMAND))
			cardLayout.next(cardPanel);
		else if(e.getActionCommand().equals(JFileMessagePanel.DOWNLOADFILE_ACTIONCOMMAND))
		{
			JFileMessagePanel fmp = (JFileMessagePanel)e.getSource() ; 
			
			fireFileDownloaded(fmp.getTimestamp()) ; // notify controller of file download 
		}
	}
	
	public void ChangeConversation(User newSender, User newReceiver,List<UserMessage> messages)
	{
		clear();

		sendTextButton.setEnabled(true);
		sendFileButton.setEnabled(true);
		
		currentReceiver = newReceiver ; 
		currentEmitter = newSender ; 
		
		for(UserMessage m : messages)
		{
			switch(m.getSubtype())
			{
				case TX:
				{

					System.out.println("Text Message Added " + new String(m.getContent()));
					JMessagePanel mp ; 
					if(m.getSenderId().equals(currentEmitter.getId()))
					{
						mp = new JMessagePanel(currentEmitter.getUsername(),new String(m.getContent()),m.getDate());
						mp.setToEmitterColor();
					}
					else
					{
						mp = new JMessagePanel(currentReceiver.getUsername(),new String(m.getContent()),m.getDate());
						mp.setToReceiverColor();
					}
					
					messagePanel.add(mp);
					break ; 
				}
				case FL:
				{
					System.out.println("Text Message Added to UI" + new String(m.getContent()));
					JFileMessagePanel mp ; 
					
					FileWrapper fw = null; // TODO use FileWrapperModel class to hide serialization from view 
					try {
						fw = SerializationUtility.deserializeFileWrapper(m.getContent()); 
					} catch (JsonParseException e) {
						e.printStackTrace();
						return ; 
					} catch (JsonMappingException e) {
						e.printStackTrace();
						return ; 
					} catch (IOException e) {
						e.printStackTrace();
						return ; 
					}  
					if(m.getSenderId().equals(currentEmitter.getId())) 
					{
						mp = new JFileMessagePanel(currentEmitter.getUsername(),fw.getFileName(),m.getDate());
						mp.addActionListener(this);
						mp.setToEmitterColor(); 
					}
					else
					{
						
						mp = new JFileMessagePanel(currentReceiver.getUsername(),fw.getFileName(),m.getDate());
						mp.addActionListener(this);
						mp.setToReceiverColor(); 
					}
					
					messagePanel.add(mp);
					break ; 
				}
			}
		}
		
		messagePanel.validate();
		messagePanel.repaint();
		messageScrollPane.validate();
		messageScrollPane.repaint();
	}
	
	public void UpdateConversation(UserMessage newMessage)
	{
		if(newMessage.getSubtype().equals(UserMessage.UserMessageType.TX))
		{
			JMessagePanel mp ; 
			if(newMessage.getSenderId().equals(currentEmitter.getId()))
			{
				mp = new JMessagePanel(currentEmitter.getUsername(),new String(newMessage.getContent()),newMessage.getDate());
				mp.setToEmitterColor();
			}
			else
			{
				mp = new JMessagePanel(currentReceiver.getUsername(),new String(newMessage.getContent()),newMessage.getDate());
				mp.setToReceiverColor();
			}
			
			messagePanel.add(mp);
		}
		else
		{
			JFileMessagePanel mp ;
			
			FileWrapper fw = null ; 
			
			try {
				fw = SerializationUtility.deserializeFileWrapper(newMessage.getContent()) ;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			if(newMessage.getSenderId().equals(currentEmitter.getId()))
			{
				mp = new JFileMessagePanel(currentEmitter.getUsername(),fw.getFileName(),newMessage.getDate());
				mp.addActionListener(this);
				mp.setToEmitterColor();
			}
			else
			{
				mp = new JFileMessagePanel(currentReceiver.getUsername(),fw.getFileName(),newMessage.getDate());
				mp.addActionListener(this);
				mp.setToReceiverColor();
			}
			
			messagePanel.add(mp);
		}

		
		messagePanel.validate();
		messagePanel.repaint();
		
	}
	
	public void clear()
	{
		messagePanel.removeAll();
		messagePanel.validate();
		messagePanel.repaint();

		sendTextButton.setEnabled(false);
		sendFileButton.setEnabled(false);
	}

	@Override
	public void addChatListener(ChatListener cl) {
		listeners.add(ChatListener.class, cl);
		
	}

	@Override
	public void removeChatListener(ChatListener cl) {
		
		listeners.remove(ChatListener.class, cl);
		
	}

	@Override
	public ChatListener[] getChatListeners() {

		return listeners.getListeners(ChatListener.class); 
	}

	@Override
	public void clearChatListeners() {

		for(var cl : getChatListeners())
		{
			listeners.remove(ChatListener.class, cl);
		}
	}
	
	protected void fireFileDownloaded(Timestamp date)
	{
		for(ChatListener cl : getChatListeners())
		{
			cl.fileDownloaded(currentReceiver.getId(), date);
		}
	}
	
	protected void fireMessageSent(String text)
	{
		for(ChatListener cl : getChatListeners())
		{
			cl.messageSent(currentReceiver, text);
		}
	}
	
	protected void fireFileMessageSent(DefaultListModel<String> model)
	{
		for(ChatListener cl : getChatListeners())
		{
			cl.fileMessageSent(currentReceiver,model) ; 
		}
	}

}
