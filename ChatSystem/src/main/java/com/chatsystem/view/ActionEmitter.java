package com.chatsystem.view;

import java.awt.event.ActionListener;

public interface ActionEmitter {
	
	public void removeActionListener(ActionListener l) ; 
	
	public void addActionListener(ActionListener l)  ; 

}
