package com.chatsystem.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.border.LineBorder;

import com.chatsystem.user.User;

import java.awt.Color;

public class JUserPanel extends JPanel implements ActionListener{
	
	private User user ; 
	private JButton startSessionButton ; 
	private ArrayList<ActionListener> actionListeners ; 
	
	public static final String STARTSESSION_ACTIONCOMMAND = "StartSession" ; 

	/**
	 * Create the panel.
	 */
	public JUserPanel(User u) {
		
		this.user = u ; 
		
		setBorder(new LineBorder(new Color(105, 105, 105), 1, true));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {100};
		gridBagLayout.rowHeights = new int[] {50, 50};
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		setLayout(gridBagLayout);
		
		startSessionButton = new JButton("Start Session");
		startSessionButton.addActionListener(this);
		GridBagConstraints gbc_userButton = new GridBagConstraints();
		gbc_userButton.weighty = 1.0;
		gbc_userButton.weightx = 1.0;
		gbc_userButton.anchor = GridBagConstraints.NORTH;
		gbc_userButton.fill = GridBagConstraints.BOTH;
		gbc_userButton.insets = new Insets(0, 0, 5, 0);
		gbc_userButton.gridx = 0;
		gbc_userButton.gridy = 1;
		add(startSessionButton, gbc_userButton);
		
		JLabel lblNewLabel = new JLabel(user.getUsername());
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weighty = 1.0;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		actionListeners = new ArrayList<ActionListener>();

	}
	
	public User getUser() {
		return user;
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
		
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,STARTSESSION_ACTIONCOMMAND))); 
	}
	
	public void makeInactive()
	{
		startSessionButton.setEnabled(false);
	}
	
	public void makeActive()
	{
		startSessionButton.setEnabled(true);
	}

}
