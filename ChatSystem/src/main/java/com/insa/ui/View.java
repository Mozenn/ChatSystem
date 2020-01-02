package com.insa.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class View {
	
	// JSplitPane 
	// JButton 
	// JScrollPane 
	
	// Textfield 
	
	/**
	 * Launch the application.
	 * MOVE THIS TO LOCAL SYSTEM MAIN 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
    private static void createAndShowGUI() {
		try {
			MainWindow frame = new MainWindow();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }

}
