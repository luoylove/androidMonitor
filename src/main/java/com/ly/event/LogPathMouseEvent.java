package com.ly.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class LogPathMouseEvent extends MouseAdapter{
	private JTextField textField;
	
	public LogPathMouseEvent(JTextField textField) {
		this.textField = textField;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		JFileChooser chooser = new JFileChooser("d:");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			textField.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}
}
