package com.chatsystem.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.Font;
import javax.swing.DropMode;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;

public class CreateUserWindow extends JFrame implements ActionEmitter, ActionListener{

	private JPanel contentPane;
	private JFormattedTextField textField;
	private JButton confirmButton ; 
	private JLabel errorLabel ; 
	
	public static final String CLOSE_CREATEUSERWINDOW_ACTIONCOMMAND = "CloseCreateUserWindow" ; 
	public static final String CHECK_CREATEUSERWINDOW_ACTIONCOMMAND = "CheckCreateUserWindow" ; 
	private ArrayList<ActionListener> actionListeners ; 
	
	public JButton getConfirmButton() {
		return confirmButton;
	}

	public JFormattedTextField getTextField() {
		return textField;
	}
	
	

	public CreateUserWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CLOSE_CREATEUSERWINDOW_ACTIONCOMMAND)));
		    }
		}); 
		
		setBounds(100, 100, 450, 300);
		setMinimumSize(new Dimension(400,300));
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {434};
		gbl_contentPane.rowHeights = new int[] {50, 50, 50,50};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0,0.0};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel indicationLabel = new JLabel("Create Profile");
		indicationLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		indicationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_indicationLabel = new GridBagConstraints();
		gbc_indicationLabel.weighty = 1.0;
		gbc_indicationLabel.fill = GridBagConstraints.BOTH;
		gbc_indicationLabel.insets = new Insets(0, 0, 5, 0);
		gbc_indicationLabel.gridx = 0;
		gbc_indicationLabel.gridy = 0;
		contentPane.add(indicationLabel, gbc_indicationLabel);
		
		JPanel UsernamePanel = new JPanel();
		GridBagConstraints gbc_UsernamePanel = new GridBagConstraints();
		gbc_UsernamePanel.insets = new Insets(0, 0, 5, 0);
		gbc_UsernamePanel.weighty = 1.0;
		gbc_UsernamePanel.fill = GridBagConstraints.BOTH;
		gbc_UsernamePanel.gridx = 0;
		gbc_UsernamePanel.gridy = 1;
		contentPane.add(UsernamePanel, gbc_UsernamePanel);
		UsernamePanel.setLayout(new BoxLayout(UsernamePanel, BoxLayout.X_AXIS));
		
		Component horizontalStrut1 = Box.createHorizontalStrut(30);
		UsernamePanel.add(horizontalStrut1);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		UsernamePanel.add(usernameLabel);
		
		Component horizontalStrut2 = Box.createHorizontalStrut(30);
		UsernamePanel.add(horizontalStrut2);
		
		textField = new JFormattedTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		UsernamePanel.add(textField);
		textField.setColumns(10);
		textField.setMaximumSize( textField.getPreferredSize() );
		
		Component horizontalStrut3 = Box.createHorizontalStrut(30);
		UsernamePanel.add(horizontalStrut3);
		
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		errorLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_ErrorLabel = new GridBagConstraints();
		gbc_ErrorLabel.fill = GridBagConstraints.BOTH;
		gbc_ErrorLabel.weighty = 1.0;
		gbc_ErrorLabel.gridx = 0;
		gbc_ErrorLabel.gridy = 2;
		contentPane.add(errorLabel,gbc_ErrorLabel);
		
		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		GridBagConstraints gbc_ConfirmButton = new GridBagConstraints();
		gbc_ConfirmButton.weighty = 1.0;
		gbc_ConfirmButton.gridx = 0;
		gbc_ConfirmButton.gridy = 3;
		contentPane.add(confirmButton, gbc_ConfirmButton);
		
		
		actionListeners = new ArrayList<ActionListener>() ; 
	}
	
	public void showEmptyUsernameError()
	{
		errorLabel.setText("Username cannot be empty");
	}
	
	public void showTooLongUsernameError()
	{
		errorLabel.setText("Username can't have more than 20 characters");
	}
	
	public void showUnavailableUsernameError()
	{
		errorLabel.setText("Username unavailable");
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
		
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CHECK_CREATEUSERWINDOW_ACTIONCOMMAND)));
	}

}
