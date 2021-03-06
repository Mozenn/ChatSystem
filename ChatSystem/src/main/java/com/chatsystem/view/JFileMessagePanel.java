package com.chatsystem.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.chatsystem.user.User;

public class JFileMessagePanel extends JPanel implements ActionEmitter, ActionListener, MessagePanel {
	
	public static final String DOWNLOADFILE_ACTIONCOMMAND = "DownloadFile" ; 
	
	private JLabel fileNameLabel ; 
	private JButton fileIconButton ; 
	private JLabel usernameLabel ; 
	private Timestamp date ; 
	private User sender ; 
	
	private ArrayList<ActionListener> actionListeners ; 

	public JLabel getMessagePane() {
		return fileNameLabel;
	}
	
	public Timestamp getTimestamp()
	{
		return this.date ; 
	}
	
	public JButton getFileIconButton()
	{
		return this.fileIconButton ; 
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
	public JFileMessagePanel(User sender, String fileName,Timestamp date) {
		
		this.date = date ; 
		actionListeners = new ArrayList<ActionListener>() ; 
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel headerPanel = new JPanel();
		headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(headerPanel);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		
		usernameLabel = new JLabel(sender.getUsername());
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(usernameLabel);
		
		Component horizontalStrut2 = Box.createHorizontalStrut(30);
		horizontalStrut2.setMaximumSize(getPreferredSize());
		headerPanel.add(horizontalStrut2);
		
		JLabel timestampLabel = new JLabel(date.toString());
		timestampLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(timestampLabel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		
		fileIconButton = new JButton("Download"); 
		fileIconButton.addActionListener(this);
		contentPanel.add(fileIconButton);
		
		fileNameLabel = new JLabel(fileName);
		fileNameLabel.setOpaque(true);
		fileNameLabel.setBackground(new Color(211, 211, 211));
		fileNameLabel.repaint();
		contentPanel.add(fileNameLabel);
		
		this.sender = sender ; 

	}

	@Override
	public void removeActionListener(ActionListener l) {
		actionListeners.remove(l) ;
		
	}

	@Override
	public void addActionListener(ActionListener l) {
		if(!actionListeners.contains(l))
			actionListeners.add(l); 
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,DOWNLOADFILE_ACTIONCOMMAND)));
		
	}
	
	@Override
	public void updateUsername()
	{
		usernameLabel.setText(sender.getUsername());
		usernameLabel.repaint(); 
		usernameLabel.validate(); 
	}

}
