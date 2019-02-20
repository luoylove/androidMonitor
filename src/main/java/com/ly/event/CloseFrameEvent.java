package com.ly.event;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.ly.biz.SaveLog;

public class CloseFrameEvent extends WindowAdapter {
	
	private JFrame jFrame;
	
	private Thread thread;
	
	private SaveLog saveLog;

	public CloseFrameEvent(JFrame jFrame, Thread thread, SaveLog saveLog) {
		super();
		this.jFrame = jFrame;
		this.thread = thread;
		this.saveLog = saveLog;
	}


	@SuppressWarnings("deprecation")
	public void windowClosing(WindowEvent e){
		if (saveLog != null) {
			saveLog.saveEnd();
		}

		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		thread.stop();
	}
}
