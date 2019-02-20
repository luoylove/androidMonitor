package com.ly.event;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartPanel;

import com.ly.biz.MeterCollection;
import com.ly.biz.SaveLog;
import com.ly.gui.MonitorChart;
import com.ly.gui.MonitorFrame;
import com.ly.utils.Util;

public class MemMonitorMouseEvent extends BaseMousePressedEvent{
	private MonitorFrame frmae;
	
	private Boolean isWriteLog = false;
	
	private SaveLog saveLog = null;
	
	public MemMonitorMouseEvent(MonitorFrame frmae) {
		this.frmae = frmae;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		String packageName = frmae.packageTextField.getText();
		
		String deviceId = frmae.deviceIdTextField.getText();
		
		String interval = frmae.intervalTextField.getText();
		
		if (StringUtils.isBlank(packageName) || StringUtils.isBlank(deviceId) || StringUtils.isBlank(interval)) {
			JOptionPane.showMessageDialog(null, "填写不完整，请重新填写", "alert", JOptionPane.ERROR_MESSAGE); 
		} else {
			MeterCollection meterCollection = new MeterCollection(deviceId, packageName); 
			
			JFrame frame = new JFrame("MEM监控");
			frame.setLayout(new GridLayout(2, 1));
			
			MonitorChart memChart1 = new MonitorChart();
			ChartPanel memPanel1 = memChart1.createChart("应用占用内存", "mem", "占用内存 m");
			
			MonitorChart memChart2 = new MonitorChart();
			ChartPanel memPanel2 = memChart2.createChart("应用占用内存比", "mem", "占用内存比 %");
			
			if (frmae.logCheckBox.isSelected()) {
				String logPath = frmae.textFieldLog.getText().trim();
				
				if (StringUtils.isBlank(logPath)) {
					JOptionPane.showMessageDialog(null, "数据写入路径为空", "alert",  JOptionPane.ERROR_MESSAGE); 
					
				} else {
					isWriteLog = true;
					saveLog = new SaveLog(logPath + "\\" +  Util.getToday_forPic() +"memData.csv");
					saveLog.saveHead(new String[]{"占用内存M", "占用内存比%"});
				}
			}
			
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					while (true) {
						Double[] memDatas = meterCollection.getMemData();
						
						if (isWriteLog) {
							saveLog.save(memDatas);
						}
						
						memChart1.setData(memDatas[0]);
						memChart2.setData(memDatas[1]);
						
						Util.sleep(Integer.parseInt(interval) * 1000);
					}
				}
			});
			
			thread.start();
			
			frame.add(memPanel1);
			frame.add(memPanel2);
			
			frame.setSize(500, 700);
			frame.addWindowListener(new CloseFrameEvent(frame, thread, saveLog));
			
			frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
		}
	}
}
