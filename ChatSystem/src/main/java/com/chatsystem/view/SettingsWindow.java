package com.chatsystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

public class SettingsWindow extends JFrame implements ActionEmitter, ActionListener{

	private JPanel contentPane;
	private JButton changePathButton ; 
	private JLabel pathLabel ; 
	private String downloadPath ; 
	
	public static final String CHANGE_DOWNLOADPATH_ACTIONCOMMAND = "ChangeDownloadPath" ; 
	private ArrayList<ActionListener> actionListeners ; 
	
	public JButton getChangeDownloadPathButton() {
		return changePathButton;
	}
	
	public String getDownloadPath() 
	{
		return this.downloadPath ; 
	}
	
	protected void setDownloadPath(String newPath)
	{
		this.downloadPath = newPath ; 
		this.pathLabel.setText(newPath);
		this.pathLabel.repaint();
		this.pathLabel.validate();
		
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CHANGE_DOWNLOADPATH_ACTIONCOMMAND)));
	}

	public SettingsWindow(String username, String currentDownloadPath) {
		
		setBounds(100, 100, 700, 300);
		setMinimumSize(new Dimension(400,350));
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
		
		JLabel indicationLabel = new JLabel("Settings");
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
		
		JLabel usernameIndicationLabel = new JLabel("Username");
		usernameIndicationLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameIndicationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		UsernamePanel.add(usernameIndicationLabel);
		
		Component horizontalStrut2 = Box.createHorizontalStrut(30);
		UsernamePanel.add(horizontalStrut2);
		
		JLabel usernameLabel = new JLabel(username);
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		UsernamePanel.add(usernameLabel);
		
		Component horizontalStrut3 = Box.createHorizontalStrut(30);
		UsernamePanel.add(horizontalStrut3);
		
		JPanel DownloadPathPanel = new JPanel();
		GridBagConstraints gbc_DownloadPathPanel = new GridBagConstraints();
		gbc_DownloadPathPanel.insets = new Insets(0, 0, 5, 0);
		gbc_DownloadPathPanel.weighty = 1.0;
		gbc_DownloadPathPanel.fill = GridBagConstraints.BOTH;
		gbc_DownloadPathPanel.gridx = 0;
		gbc_DownloadPathPanel.gridy = 2;
		contentPane.add(DownloadPathPanel, gbc_DownloadPathPanel);
		DownloadPathPanel.setLayout(new BoxLayout(DownloadPathPanel, BoxLayout.X_AXIS));
		
		Component horizontalStrut4 = Box.createHorizontalStrut(20);
		DownloadPathPanel.add(horizontalStrut4);
		
		JLabel pathIndicationLabel = new JLabel("Download Path");
		pathIndicationLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pathIndicationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		DownloadPathPanel.add(pathIndicationLabel);
		
		Component horizontalStrut5 = Box.createHorizontalStrut(20);
		DownloadPathPanel.add(horizontalStrut5);
		
		pathLabel = new JLabel(currentDownloadPath);
		pathLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pathLabel.setHorizontalAlignment(SwingConstants.LEFT);
		DownloadPathPanel.add(pathLabel);
		
		Component horizontalStrut6 = Box.createHorizontalStrut(20);
		DownloadPathPanel.add(horizontalStrut6);
		
		changePathButton = new JButton("Edit");
		changePathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int r = j.showDialog(null, "Select") ; 
				
	            if (r == JFileChooser.APPROVE_OPTION) { 
	            	
	            	setDownloadPath(j.getSelectedFile().getAbsolutePath()); 
	            	
	            } 
	            // if the user cancelled the operation 
	            else
	                return ; 
			}
			
		});
		DownloadPathPanel.add(changePathButton) ; 

		
		
		actionListeners = new ArrayList<ActionListener>() ; 
		
		this.downloadPath = currentDownloadPath ; 
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
		
		actionListeners.forEach(l -> l.actionPerformed(new ActionEvent(this,0,CHANGE_DOWNLOADPATH_ACTIONCOMMAND)));
	}

}
