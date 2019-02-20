package com.ly.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Util {
	/**
	 * 写入文件
	 * @param path
	 * @param result
	 * @param flag
	 * 		true 追加，false覆盖
	 */
	public static void write(String path, String result, Boolean flag) {
		File file = new File(path);
        FileWriter fw = null;
		try {
			fw = new FileWriter(file,flag);
			fw.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
     * 读取path位置文件内容
     * @param path
     * @return
     */
	public static String read(String path, String encode) {
		String result = null;
		
		try (FileInputStream file = new FileInputStream(path);
				BufferedReader reader = new BufferedReader(new InputStreamReader(file, encode));) {
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				strber.append(line + "\n");
			}

			result = strber.toString();
		} catch (IOException e) {
			e.getStackTrace();
		}
		
		return result;	
	}
	
	/**
	 * 读取path位置文件内容
     * @param path
     * @return
     */
	public static String read(String path) {
		return read(path, "UTF-8");
	}
	
	/**
	 * 写入
	 * @param path
	 * @param result
	 * @param encode
	 */
	public static void write(String path, String result, String encode) {
		File file = new File(path);  
	    file.delete();  
	      
	    try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encode));) {
			file.createNewFile(); 
			writer.write(result);  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成日期格式字符串 格式：yyyy_mm_dd_hh_mm
	 * 
	 * @return String
	 */
	public static String getTimeNow() {
		SimpleDateFormat timePattern = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		return timePattern.format(new Date());
	}
	
	/**
	 * 生成当天的日期字符串为截图 格式：yyyyMMddHHmmss
	 * 
	 * @return 日期格式的字符串
	 */
	public static String getToday_forPic() {
		SimpleDateFormat timePattern = new SimpleDateFormat("yyyyMMddHHmmss");
		return timePattern.format(new Date());
	}
	
	/**
	 * 修改文件名，加时间戳
	 * @param oldFileName
	 */
	public static void fixFileName(String oldFileName) {
		File oldFile = new File(oldFileName);
		
		if(oldFile.exists()) {
			String oldName = oldFile.getName();
			
			String filePath = oldFileName.substring(0, oldFileName.lastIndexOf("\\")) + "\\";
			
			String newName = getToday_forPic() + "_" + oldName;
			
			File newFile = new File(filePath + newName);
			
			oldFile.renameTo(newFile);
		}
	}
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入csv文件
	 * 
	 * @param filePath
	 * @param list
	 * @param append
	 */
	public static void wirteCSVFile(String filePath, List<String[]> list, Boolean append){
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(filePath, append);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				CSVWriter csvWriter = new CSVWriter(bufferedWriter, ',', CSVWriter.NO_QUOTE_CHARACTER);) {
			for (String[] strings : list) {
				csvWriter.writeNext(strings);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入csv文件
	 * 
	 * @param filePath
	 * @param list
	 * @param append
	 */
	public static void wirteCSVFileForDouble(String filePath, List<Double[]> list, Boolean append) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(filePath, append);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				CSVWriter csvWriter = new CSVWriter(bufferedWriter, ',', CSVWriter.NO_QUOTE_CHARACTER);) {
			for (Double[] d : list) {
				String strings[] = new String[d.length];
				
				for (int i = 0; i < d.length; i++) {
					strings[i] = String.valueOf(d[i]);
				}
				csvWriter.writeNext(strings);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取csv文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<String[]> readCSVFile(String filePath) {
		List<String[]> listCSV = new ArrayList<>();
		try (FileInputStream fileInputStream = new FileInputStream(filePath);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				CSVReader csvReader = new CSVReader(bufferedReader);) {
			listCSV = csvReader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listCSV;
	}
	
	/**
	 * double保留几位小数，四舍五入模式
	 * @param d
	 * @param digit
	 * @return
	 */
	public static Double dFormat(Double d, int digit) {
		BigDecimal bg = new BigDecimal(d);
		
		return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
