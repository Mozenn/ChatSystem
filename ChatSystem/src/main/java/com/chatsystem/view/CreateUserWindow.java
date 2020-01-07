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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.Font;
import javax.swing.DropMode;
import java.awt.Component;
import javax.swing.Box;

public class CreateUserWindow extends JFrame {

	private JPanel contentPane;
	private JFormattedTextField textField;
	private JButton confirmButton ; 

	public CreateUserWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setMinimumSize(new Dimension(400,300));
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {434};
		gbl_contentPane.rowHeights = new int[] {50, 50, 50};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0};
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
		

		
		JButton confirmButton = new JButton("Confirm");
		GridBagConstraints gbc_ConfirmButton = new GridBagConstraints();
		gbc_ConfirmButton.weighty = 1.0;
		gbc_ConfirmButton.gridx = 0;
		gbc_ConfirmButton.gridy = 2;
		contentPane.add(confirmButton, gbc_ConfirmButton);
	}

}
