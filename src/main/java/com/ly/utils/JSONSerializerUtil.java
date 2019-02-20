package com.ly.utils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class JSONSerializerUtil {
	
	public static <T> String serialize(T t) {
		if (t == null) 
			return "";
		else {
			return JSON.toJSONString(t);
		}
	}
	
	public static <T> T unSerialize(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		} else {
			return (T)JSON.parseObject(json, clazz);
		}
	}
}
