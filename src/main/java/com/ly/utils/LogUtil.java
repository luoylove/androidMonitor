package com.ly.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtil {

	private static void logConfig(){
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
//		PropertyConfigurator.configure("log4j.properties");
	}
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class c){
		logConfig();
		return  Logger.getLogger(c);
	}
}
