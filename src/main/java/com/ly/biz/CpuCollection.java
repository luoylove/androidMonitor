package com.ly.biz;

import static com.ly.constant.Constant.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ly.utils.BizException;
import com.ly.utils.DosExec;
import com.ly.utils.LogUtil;
import com.ly.utils.Util;

public class CpuCollection {
	static Logger log = LogUtil.getLogger(CpuCollection.class);
	
	private String packageName;
	
	private String ADB_START_CMD;
	
	private Long defTotalCpu = 0l;
	
	private Long defIdleTotalCpu = 0l;
	
	private Long defAppCpu = 0l;
	
	private Double cacheTotalCpuRate = 0d;
	
	private Double cacheAppCpuRate = 0d;
	
	private Double aveAppCpuRate = 0d;
	
	private Double runNum = 1d;
	
	public CpuCollection(String deviceId, String packageName) {
		super();
		this.packageName = packageName;
		
		ADB_START_CMD = ADB_CMD_START + deviceId + " shell ";
	}
	
	
	/**
	 * 总使用率， app使用率， app cpu平均占用率，监听次数
	 * @return
	 */
	public Double[] getCpuData() {
		Double [] cpuDatas = new Double[4];
		String pid = getAppPid(packageName);
		
		if (!StringUtils.isBlank(pid)) {
			String cmd_pid = ADB_START_CMD +  "cat /proc/" + pid + "/stat";
			String cmd = "adb shell cat /proc/stat |findstr cpu";
			String sp = "echo @@@";
			
			try {
				String result = MeterCollection.isConnectAdbAndStartApp(DosExec.executeCmd(cmd + " && " + sp + " && " + cmd_pid));
				log.info("[cpu]总使用率查询命令：" + cmd);
				log.info("[cpu]app使用率查询命令：" + cmd_pid);
				log.info("[cpu]查询结果：" + result);
				
				
				String masterResults[] = result.split("@@@");
				
				String totalCpuResult = masterResults[0].trim();
				
				totalCpuResult = totalCpuResult.substring(3).trim();

				totalCpuResult = totalCpuResult.substring(0, totalCpuResult.indexOf("cpu")).trim();

				String results[] = totalCpuResult.split(" ");
				
				Long[] totalCpus = new Long[2];
				totalCpus[0] = Long.parseLong(results[0]) + Long.parseLong(results[1]) + Long.parseLong(results[2])
								+ Long.parseLong(results[3]) + Long.parseLong(results[4]) + Long.parseLong(results[5])
								+ Long.parseLong(results[6]);
				
				totalCpus[1] = Long.parseLong(results[3]);
				
				String resultsAppCpu[] = masterResults[1].trim().split(" ");
				
				Long appCpu = Long.parseLong(resultsAppCpu[13]) + Long.parseLong(resultsAppCpu[14]) 
								+ Long.parseLong(resultsAppCpu[15]) + Long.parseLong(resultsAppCpu[16]);
				
				Long totalCpu = totalCpus[0];
				
				Long idleTotalCpu = totalCpus[1];
				
				if (defTotalCpu == 0 && defAppCpu == 0) {
					cpuDatas[0] = 0d;
					cpuDatas[1] = 0d;
					cpuDatas[2] = 0d;
					cpuDatas[3] = 1d;
					
				} else {
					runNum++;
					
					Long totalCpuDiff = totalCpu - defTotalCpu;
					
					Double totalIdleDiff = (double) (idleTotalCpu - defIdleTotalCpu);
					
					Double totalCpuRate = 0d;
					
					Double appCpuRate = 0d;
					
					//CPU可能在某个线程退出或者死掉时候，会影响整个cpu参数采集，当有这个问题时候，取上一次数据返回
					if (totalCpuDiff > 0 && totalIdleDiff > 0 && totalCpuDiff >= totalIdleDiff && appCpu >= defAppCpu) {
						totalCpuRate = Util.dFormat(100 * (totalCpuDiff -totalIdleDiff) / totalCpuDiff , 2);
						appCpuRate =  Util.dFormat(100 * (double) (appCpu - defAppCpu) / totalCpuDiff, 2);
					} else {
						totalCpuRate = cacheTotalCpuRate;
						appCpuRate = cacheAppCpuRate;
					}
					
					aveAppCpuRate += appCpuRate;
					
					cpuDatas[0] = totalCpuRate;
					
					cpuDatas[1] =  appCpuRate;
					
					cpuDatas[2] = Util.dFormat(aveAppCpuRate / runNum, 2);
					
					cpuDatas[3] = runNum;
					
					cacheTotalCpuRate = cpuDatas[0];
					cacheAppCpuRate = cpuDatas[1];
				}
				
				defTotalCpu = totalCpu;
				defAppCpu = appCpu;
				defIdleTotalCpu = idleTotalCpu;
				
				log.info("[cpu]采集数据结果：" + Arrays.toString(cpuDatas));
				
				return cpuDatas;
			} catch (BizException e) {
				cpuDatas[0] = 0d;
				cpuDatas[1] = 0d;
				cpuDatas[2] = 0d;
				cpuDatas[3] = runNum;
				runNum++;
				defTotalCpu = 0l;
				defAppCpu = 0l;
				defIdleTotalCpu = 0l;
				return cpuDatas;
			}
		} else {
			cpuDatas[0] = 0d;
			cpuDatas[1] = 0d;
			cpuDatas[2] = 0d;
			cpuDatas[3] = runNum;
			runNum++;
			defTotalCpu = 0l;
			defAppCpu = 0l;
			defIdleTotalCpu = 0l;
			return cpuDatas;
		}
	}

	public static String getAppPid(String packageName) {
		String cmd = GET_APP_PID_CMD + packageName;
		
		String result = DosExec.executeCmd(cmd);
		
		if (StringUtils.isBlank(result)) {
			cmd = GET_APP_PID_CMD_LOW + packageName;
			result = DosExec.executeCmd(cmd);
		}
		
		String pid = "";
		
		if (!StringUtils.isBlank(result)) {
			String results[] = result.trim().split(" ");
			
			List<String> strList = Arrays.asList(results);
			
			List<String> strListNew = new ArrayList<>();
			
			for(int i = 0; i<strList.size(); i++) { 
				if (strList.get(i) != null && !strList.get(i).equals("") && !strList.get(i).trim().equals(" ")){
					strListNew.add(strList.get(i));
				}
			}
			
			String[] resultsNew = strListNew.toArray(new String[strListNew.size()]);
			
			for (int i = 0; i < resultsNew.length; i++) {
				if (resultsNew[i].equals(packageName)) {
					pid = resultsNew[i - 7];
				}
			}
			
		} 
		return pid;
	}
	
	public static void main(String[] args) {
		
		CpuCollection collection = new CpuCollection("3f836acc", "cn.com.letzgo.hsj_zhuanche");
		while (true) {
			System.out.println(Arrays.toString(collection.getCpuData()));
			Util.sleep(1000);
		}
		
	}
}
