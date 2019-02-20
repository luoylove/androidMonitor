package com.ly.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class DosExec {
	
	static Logger log = LogUtil.getLogger(DosExec.class);
	
	public static String executeCmd(String cmd) {
		String result = "";
		Runtime rt = Runtime.getRuntime();
		
		BufferedReader br = null;
		
		InputStreamReader isr = null;
		
		InputStream stderr = null;
		
		try {
			Process proc = rt.exec("cmd /c " + cmd);
			
			try {
				if (proc.waitFor() != 0)
				    {
				      proc.destroy();
				    }
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
			stderr = proc.getInputStream();
			isr = new InputStreamReader(stderr);
			br = new BufferedReader(isr);
			
			StringBuffer stringBuffer = new StringBuffer();
			
			for (String line = null; (line = br.readLine()) != null;) {
			      stringBuffer.append(line + " ");
			}
			
			result = stringBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if (isr != null)
					isr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if (stderr != null)
					stderr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
