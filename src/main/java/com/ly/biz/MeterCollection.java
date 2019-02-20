package com.ly.biz;

import com.ly.utils.BizException;
import com.ly.utils.DosExec;
import com.ly.utils.LogUtil;
import com.ly.utils.Util;

import static com.ly.constant.Constant.*;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class MeterCollection {
	
	static Logger log = LogUtil.getLogger(MeterCollection.class);
	
	private String packageName;
	
	private String ADB_START_CMD;
	
	private Double memSun;
	
	public MeterCollection(String deviceId, String packageName) {
		super();
		this.packageName = packageName;
		
		ADB_START_CMD = ADB_CMD_START + deviceId + " shell ";
		
		this.memSun = getMemTotal();
	}

	/**
	 * 内存统计，返回一个数组，占用内存，占用内存比
	 * @param deviceId
	 * @param packageName
	 * @return
	 */
	public Double[] getMemData() {
		
		Double memData[] = new Double[2];
		
		String cmd = ADB_START_CMD + MEN_ADB_CMD + packageName + MEN_ADB_CMD_FINDSTR;
		
		String result;
		try {
			result = isConnectAdbAndStartApp(DosExec.executeCmd(cmd));
			log.info("[mem]查询命令：" + cmd);
			log.info("[mem]查询结果：" + result);
			
			String results[] = result.trim().split("    ");
			
			if (results.length < 2) { 
				log.error("内存数据收集失败");
				throw new BizException("内存数据收集失败");
			} 
			Double nowMem = Double.valueOf(results[1]);

			memData[0] = Util.dFormat(nowMem / 1024, 2);
			memData[1] = Util.dFormat((nowMem / memSun) * 100, 2);

			log.info("[mem]采集数据结果:" + Arrays.toString(memData));
			return memData;
		} catch (Exception e) {
			memData[0] = 0d;
			memData[1] = 0d;
			
			log.error("数据统计失败", e);
			
			return memData;
		}
	}
	
	/**
	 * cpu统计，程序占用cpu百分比， 手机cpu总占用百分比
	 * @param packageName
	 */
//	public String [] getCpuData() {
//		String cpuData[] = new String[2];
//		String cmdAppCpu = ADB_START_CMD +  CPU_ADB_CMD + packageName;
//		
//		String cmdTotalCpu = ADB_START_CMD + CPU_ADB_CMD_TOTAL;
//		
//		String resultAppCpu = isConnectAdbAndStartApp(DosExec.executeCmd(cmdAppCpu));
//		String resultTotalCpu = isConnectAdbAndStartApp(DosExec.executeCmd(cmdTotalCpu));
//		
//		log.info("查询app占用cpu命令：" + cmdAppCpu);
//		log.info("查询结果：" + resultAppCpu);
//		
//		log.info("查询cpu总占用比命令：" + cmdTotalCpu);
//		log.info("查询结果：" + resultTotalCpu);
//		
//		if (!resultAppCpu.contains("%") || !resultTotalCpu.contains("%")) {
//			log.error("cpu查询失败");
//			throw new BizException("cpu查询失败");
//		} else {
//			cpuData[0] = resultAppCpu.trim().substring(0, resultAppCpu.trim().indexOf("%"));
//			cpuData[1] = resultTotalCpu.trim().substring(0, resultTotalCpu.trim().indexOf("%"));
//			return cpuData;
//		}
//	}
	
	private static Double getMemTotal() {
		String result = null;
		try {
			result = isConnectAdbAndStartApp(DosExec.executeCmd(MEN_SUN_CMD));
		} catch (BizException e) {
			e.printStackTrace();
		}
		String menSum = "";
		
		menSum = result.trim().substring("MemTotal:".length(), result.trim().length() - 2);
		return Double.valueOf(menSum);
	}
		
	public static String isConnectAdbAndStartApp(String result) throws BizException {
		if (StringUtils.isBlank(result)) {
			throw new BizException("请检查电脑是否有adb环境，手机是否连接电脑成功，应用是否已打开");
		} 
		
		if (!StringUtils.isBlank(result) && result.contains("/system/bin/sh:")){
			throw new BizException("文件不存在");
		} 
		
		return result;
	}
}
