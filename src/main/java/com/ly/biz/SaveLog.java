package com.ly.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ly.utils.LogUtil;
import com.ly.utils.Util;

public class SaveLog {
	static Logger log = LogUtil.getLogger(SaveLog.class);
	
	private String path;
	List<Double[]> datas = new ArrayList<>();
	
	public SaveLog(String path) {
		this.path = path;
	}
	
	public void saveHead(String[] heads) {
		List<String[]> list = new ArrayList<String[]>();
		list.add(heads);
		Util.wirteCSVFile(path, list, true);
		log.info("首行写入成功：" + Arrays.toString(heads));
	}
	
	public void save(Double[] data) {
		datas.add(data);
		
		if (datas.size() >= 500) {
			Util.wirteCSVFileForDouble(path, datas, true);
			log.info("缓存数据写入成功：" + path);
			datas.clear();
		}
	}
	
	public void saveEnd() {
		if (datas.size() > 0) {
			Util.wirteCSVFileForDouble(path, datas, true);
			log.info("剩余数据写入成功：" + path);
			datas.clear();
		}
	}
}
