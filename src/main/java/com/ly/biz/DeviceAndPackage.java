package com.ly.biz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ly.utils.BizException;
import com.ly.utils.DosExec;

public class DeviceAndPackage {
	public static String getDeviceId() throws BizException {

		final String defStr = "List of devices attached";

		String cmd = "adb devices";

		String device = "";

		String result = null;
		result = MeterCollection.isConnectAdbAndStartApp(DosExec.executeCmd(cmd));
		String deviceId = result.substring(defStr.length());
		
		if (!deviceId.contains("device")) {
			throw new BizException("请检查手机是否连接");
		}
		
		device = deviceId.substring(0, deviceId.indexOf("device")).trim();
		
		return device;
	}

	public static String getPackageName() throws BizException {
		String packageName = "";

		Pattern pattern = Pattern.compile("(?<=\\{)[^\\}]+");

		final String cmd = "adb shell dumpsys window |findstr mCurrentFocus";

		String result = MeterCollection.isConnectAdbAndStartApp(DosExec.executeCmd(cmd));
		Matcher m = pattern.matcher(result);
		
		if (!m.find()) {
			throw new BizException("未知原因，获取packageName失败");
		}

		String results[] = m.group(0).split(" ");

		String pName = results[results.length - 1];

		if (!pName.contains("/")) {
			throw new BizException("手机是锁屏，获取packageName失败");
		}

		packageName = pName.substring(0, pName.indexOf("/"));
			
		return packageName;
	}
}
