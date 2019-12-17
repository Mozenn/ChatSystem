package UI;

import java.awt.BorderLayout;
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

public class MainWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Add menuBar 
		
		var menuBar = new JMenuBar(); 
		var fileMenu = new JMenu("File") ; 
		fileMenu.setMnemonic(KeyEvent.VK_F);
		var exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu); 
		
		setJMenuBar(menuBar);
		
		// Text Panel 
		
		JPanel textPannel = new JPanel();
		contentPane.add(textPannel, BorderLayout.SOUTH);
		textPannel.setLayout(new BoxLayout(textPannel, BoxLayout.X_AXIS));
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textPannel.add(textArea);
		
		JButton sendButton = new JButton("Send");
		textPannel.add(sendButton);
		
		// Session panel 
		
		JPanel ongoingSessionPannel = new JPanel();
		
		JButton User1 = new JButton("New button");
		ongoingSessionPannel.add(User1);
		
		JButton User2 = new JButton("New button");
		ongoingSessionPannel.add(User2);
		
		JScrollPane sessionScrollPane = new JScrollPane(ongoingSessionPannel);
		contentPane.add(sessionScrollPane, BorderLayout.WEST);
		ongoingSessionPannel.setLayout(new BoxLayout(ongoingSessionPannel, BoxLayout.Y_AXIS));
		
		// Connected User panel 
		
		JPanel connectedUserPannel = new JPanel();
		contentPane.add(connectedUserPannel, BorderLayout.EAST);
		connectedUserPannel.setLayout(new BoxLayout(connectedUserPannel, BoxLayout.Y_AXIS));
		
		JButton btnNewButton = new JButton("New button");
		connectedUserPannel.add(btnNewButton);
	}

}
