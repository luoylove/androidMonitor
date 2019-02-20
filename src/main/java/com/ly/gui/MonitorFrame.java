package com.ly.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ly.biz.DeviceAndPackage;
import com.ly.event.BaseMousePressedEvent;
import com.ly.event.CpuMonitorMouseEvent;
import com.ly.event.LogPathMouseEvent;
import com.ly.event.MemMonitorMouseEvent;
import com.ly.event.NetMonitorMouseEvent;
import com.ly.utils.BizException;

public class MonitorFrame extends JFrame{
	
	public JLabel packageLabel, deviceIdLabel, logLabel, intervalLabel;
	
	public JButton memButton, netButton, cpuButton, buttonLog, packageButton, deviceButton;
	
	public JTextField packageTextField, textFieldLog, intervalTextField, deviceIdTextField;
	
	public JCheckBox logCheckBox;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MonitorFrame(String title) {
		super(title);
	}
	
	
	public void masterFrame() {
		
		logLabel = new JLabel("数据目录：");
		
		textFieldLog = new JTextField(20);
		
		logCheckBox = new JCheckBox("保存数据");
		
		packageLabel = new JLabel("应用包名：");
		
		packageTextField = new JTextField(20);
		
		deviceIdLabel = new JLabel("设备号：");
		
		deviceIdTextField = new JTextField(20);
		
		intervalLabel = new JLabel("监控间隔：");
		
		intervalTextField = new JTextField(20);
		
		packageButton = new JButton("获取");
		
		deviceButton = new JButton("获取");
		
		packageButton.addMouseListener(new BaseMousePressedEvent() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					packageTextField.setText(DeviceAndPackage.getPackageName());
				} catch (BizException e1) {
					e1.printStackTrace();
					String errMsg = e1.getMessage();
					JOptionPane.showMessageDialog(null, errMsg, "alert", JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
		
		deviceButton.addMouseListener(new BaseMousePressedEvent() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					deviceIdTextField.setText(DeviceAndPackage.getDeviceId());
				} catch (BizException e1) {
					e1.printStackTrace();
					String errMsg = e1.getMessage();
					JOptionPane.showMessageDialog(null, errMsg, "alert", JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
		
		memButton = new JButton("MEM性能监控");
		
		memButton.addMouseListener(new MemMonitorMouseEvent(this));
		
		cpuButton = new JButton("CPU性能监控");
		
		cpuButton.addMouseListener(new CpuMonitorMouseEvent(this));
		
		netButton = new JButton("NET性能监控");
		
		netButton.addMouseListener(new NetMonitorMouseEvent(this));
		
		buttonLog = new JButton("选择");
		
		buttonLog.addMouseListener(new LogPathMouseEvent(textFieldLog));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints packageLabelCons = new GridBagConstraints();
		packageLabelCons.insets = new Insets(5,5,5,5);
		packageLabelCons.gridx = 0;
		packageLabelCons.gridy = 0;
		packageLabelCons.gridwidth = 1;
		packageLabelCons.gridheight = 1;
		add(packageLabel, packageLabelCons);
		
		GridBagConstraints packageTextCons = new GridBagConstraints();
		packageTextCons.insets = new Insets(5,5,5,5);
		packageTextCons.gridx = 1;
		packageTextCons.gridy = 0;
		packageTextCons.gridwidth = 3;
		packageTextCons.gridheight = 1;
		add(packageTextField, packageTextCons);
		
		GridBagConstraints packageButtonCons = new GridBagConstraints();
		packageButtonCons.insets = new Insets(5,5,5,5);
		packageButtonCons.gridx = 4;
		packageButtonCons.gridy = 0;
		packageButtonCons.gridwidth = 1;
		packageButtonCons.gridheight = 1;
		add(packageButton, packageButtonCons);
		
		GridBagConstraints driversLabelCons = new GridBagConstraints();
		driversLabelCons.insets = new Insets(5,5,5,5);
		driversLabelCons.gridx = 0;
		driversLabelCons.gridy = 1;
		driversLabelCons.gridwidth = 1;
		driversLabelCons.gridheight = 1;
		add(deviceIdLabel, driversLabelCons);
		
		GridBagConstraints driversTextCons = new GridBagConstraints();
		driversTextCons.insets = new Insets(5,5,5,5);
		driversTextCons.gridx = 1;
		driversTextCons.gridy = 1;
		driversTextCons.gridwidth = 3;
		driversTextCons.gridheight = 1;
		add(deviceIdTextField, driversTextCons);
		
		GridBagConstraints driversButtonCons = new GridBagConstraints();
		driversButtonCons.insets = new Insets(5,5,5,5);
		driversButtonCons.gridx = 4;
		driversButtonCons.gridy = 1;
		driversButtonCons.gridwidth = 1;
		driversButtonCons.gridheight = 1;
		add(deviceButton, driversButtonCons);
		
		GridBagConstraints intervalCons = new GridBagConstraints();
		intervalCons.insets = new Insets(5,5,5,5);
		intervalCons.gridx = 0;
		intervalCons.gridy = 2;
		intervalCons.gridwidth = 1;
		intervalCons.gridheight = 1;
		add(intervalLabel, intervalCons);
		
		GridBagConstraints intervalTextCons = new GridBagConstraints();
		intervalTextCons.insets = new Insets(5,5,5,5);
		intervalTextCons.gridx = 1;
		intervalTextCons.gridy = 2;
		intervalTextCons.gridwidth = 3;
		intervalTextCons.gridheight = 1;
		add(intervalTextField, intervalTextCons);
		
		GridBagConstraints LogCons = new GridBagConstraints();
		LogCons.insets = new Insets(5,5,5,5);
		LogCons.gridx = 0;
		LogCons.gridy = 3;
		LogCons.gridwidth = 1;
		LogCons.gridheight = 1;
		add(logLabel, LogCons);
		
		GridBagConstraints LogTextCons = new GridBagConstraints();
		LogTextCons.insets = new Insets(5,5,5,5);
		LogTextCons.gridx = 1;
		LogTextCons.gridy = 3;
		LogTextCons.gridwidth = 3;
		LogTextCons.gridheight = 1;
		add(textFieldLog, LogTextCons);
		
		GridBagConstraints LogButtonCons = new GridBagConstraints();
//		LogButtonCons.insets = new Insets(5,5,5,5);
		LogButtonCons.gridx = 4;
		LogButtonCons.gridy = 3;
		LogButtonCons.gridwidth = 1;
		LogButtonCons.gridheight = 1;
		add(buttonLog, LogButtonCons);
		
		GridBagConstraints logBoxCons = new GridBagConstraints();
		logBoxCons.insets = new Insets(5,5,5,5);
		logBoxCons.gridx = 5;
		logBoxCons.gridy = 3;
		logBoxCons.gridwidth = 1;
		logBoxCons.gridheight = 1;
		add(logCheckBox, logBoxCons);
		
		GridBagConstraints memCons = new GridBagConstraints();
		memCons.insets = new Insets(40,5,5,5);
		memCons.gridx = 0;
		memCons.gridy = 4;
		memCons.gridwidth = 2;
		memCons.gridheight = 1;
		add(memButton, memCons);
		
		GridBagConstraints cpuCons = new GridBagConstraints();
		cpuCons.insets = new Insets(40,5,5,5);
		cpuCons.gridx = 2;
		cpuCons.gridy = 4;
		cpuCons.gridwidth = 2;
		cpuCons.gridheight = 1;
		add(cpuButton, cpuCons);
		
		GridBagConstraints netCons = new GridBagConstraints();
		netCons.insets = new Insets(40,5,5,5);
		netCons.gridx = 4;
		netCons.gridy = 4;
		netCons.gridwidth = 2;
		netCons.gridheight = 1;
		add(netButton, netCons);
		
		setBounds(400, 200, 500, 300);
		setBackground(new Color(199, 237, 204));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new MonitorFrame("android monitor").masterFrame();
	}
}
