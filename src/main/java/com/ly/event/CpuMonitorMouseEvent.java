package com.ly.event;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.ly.biz.CpuCollection;
import com.ly.biz.SaveLog;
import com.ly.gui.MonitorFrame;
import com.ly.utils.Util;

public class CpuMonitorMouseEvent extends MouseAdapter{
	private MonitorFrame frmae;
	
	private SaveLog saveLog = null ;
	
	private Boolean isWriteLog = false;
	
	public CpuMonitorMouseEvent(MonitorFrame frmae) {
		this.frmae = frmae;
	}
	
	public void mousePressed(MouseEvent e) {
		String packageName = frmae.packageTextField.getText();
		
		String deviceId = frmae.deviceIdTextField.getText();
		
		String interval = frmae.intervalTextField.getText();
		
		if (StringUtils.isBlank(packageName) || StringUtils.isBlank(deviceId) || StringUtils.isBlank(interval)) {
			JOptionPane.showMessageDialog(null, "填写不完整，请重新填写", "alert", JOptionPane.ERROR_MESSAGE); 
		} else {
			CpuCollection cpuCollection = new CpuCollection(deviceId, packageName); 
			
			JFrame frame = new JFrame("CPU监控");
			
			StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
	        standardChartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 20));
	        standardChartTheme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 15));
	        standardChartTheme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 15));
	        ChartFactory.setChartTheme(standardChartTheme);
	        
	        TimeSeries timeSeries1 = new TimeSeries("app占用cpu百分比");
			
	        TimeSeries timeSeries2 = new TimeSeries("手机cpu总占用百分比");
	        
	        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
	        timeseriescollection.addSeries(timeSeries1);
	        timeseriescollection.addSeries(timeSeries2);
	        
	        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("cpu", "时间(秒)", "百分比 %", timeseriescollection, true, true, false);
			
			
			if (frmae.logCheckBox.isSelected()) {
				String logPath = frmae.textFieldLog.getText().trim();
				
				if (StringUtils.isBlank(logPath)) {
					JOptionPane.showMessageDialog(null, "数据写入路径为空", "alert", JOptionPane.ERROR_MESSAGE); 
					
				} else {
					isWriteLog = true;
					saveLog = new SaveLog(logPath + "\\" + Util.getToday_forPic() + "cpuData.csv");
					saveLog.saveHead(new String[]{"总使用率", "app使用率", "app平均占用率", "监听次数"});
				}
			}
	        
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						Double[] cpuDatas = cpuCollection.getCpuData();
						
						if (isWriteLog) {
							saveLog.save(cpuDatas);
						}
						
						RegularTimePeriod r = new Millisecond();
						timeSeries1.add(r, cpuDatas[0]);
						timeSeries2.add(r, cpuDatas[1]);
						
						Util.sleep(Integer.parseInt(interval) * 1000);
					}
				}
			});
			
			thread.start();
			
			frame.add(new ChartPanel(jfreechart));
			
			frame.setSize(500, 350);
			frame.addWindowListener(new CloseFrameEvent(frame, thread, saveLog));
			
			frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
		}
	}
}
