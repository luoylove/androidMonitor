package com.ly.event;

import java.awt.Font;
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

import com.ly.biz.NetCollection;
import com.ly.biz.SaveLog;
import com.ly.gui.MonitorFrame;
import com.ly.utils.Util;

public class NetMonitorMouseEvent extends BaseMousePressedEvent{
	private MonitorFrame frmae;

	private SaveLog saveLog = null ;
	
	private Boolean isWriteLog = false;
	
	public NetMonitorMouseEvent(MonitorFrame frmae) {
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
			NetCollection netCollection = new NetCollection(deviceId, packageName); 
			
			JFrame frame = new JFrame("NET监控");
			
			StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
	        standardChartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 20));
	        standardChartTheme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 15));
	        standardChartTheme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 15));
	        ChartFactory.setChartTheme(standardChartTheme);
	        
	        TimeSeries timeSeries1 = new TimeSeries("接收数据");
			
	        TimeSeries timeSeries2 = new TimeSeries("传输数据");
	        
	        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
	        timeseriescollection.addSeries(timeSeries1);
	        timeseriescollection.addSeries(timeSeries2);
	        
	        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("net", "时间(秒)", "kb", timeseriescollection, true, true, false);
			
			if (frmae.logCheckBox.isSelected()) {
				String logPath = frmae.textFieldLog.getText().trim();
				
				if (StringUtils.isBlank(logPath)) {
					JOptionPane.showMessageDialog(null, "数据写入路径为空", "alert",  JOptionPane.ERROR_MESSAGE); 
					
				} else {
					isWriteLog = true;
					saveLog = new SaveLog(logPath + "\\" +  Util.getToday_forPic() +"netData.csv");
					//接收数据，传输数据, 接收数据平均值，传输数据平均值， 总监控次数
					saveLog.saveHead(new String[] {"接收数据kb","传输数据kb", "接收数据平均值kb","传输数据平均值kb", "总监控次数"});
				}
			}
	        
	        
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					
					while (true) {
						Double[] netDatas = netCollection.getNetData();
						
						if (isWriteLog) {
							saveLog.save(netDatas);
						}
						
						RegularTimePeriod r = new Millisecond();
						timeSeries1.add(r, netDatas[0]);
						timeSeries2.add(r, netDatas[1]);
						
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
