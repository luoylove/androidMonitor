package com.ly.constant;

public class Constant {
	public static final String ADB_CMD_START = "adb -s ";
	
	public static final String  MEN_ADB_CMD = "dumpsys meminfo ";
	
	public static final String  MEN_ADB_CMD_FINDSTR = " | findstr \"TOTAL\"";
	
	public static final String MEN_SUN_CMD = "adb shell cat /proc/meminfo |findstr \"MemTotal\"";
	
	public static final String CPU_ADB_CMD = "dumpsys cpuinfo |findstr ";
	
	public static final String CPU_ADB_CMD_TOTAL = "dumpsys cpuinfo |findstr \"TOTAL\"";
	
	public static final String GET_APP_UID_CMD = "adb shell dumpsys package " ;
	
	public static final String GET_APP_UID_CMD_END = " | findstr userId=";
	
	public static final String ADB_NET_CMD = "cat /proc/net/xt_qtaguid/stats |findstr ";
	
	public static final String ADB_NET_CMD_END = " |findstr 0x0";
	
	public static final String GET_APP_PID_CMD = "adb shell ps -A |findstr " ;
	
	public static final String GET_APP_PID_CMD_LOW = "adb shell ps |findstr " ;
}
