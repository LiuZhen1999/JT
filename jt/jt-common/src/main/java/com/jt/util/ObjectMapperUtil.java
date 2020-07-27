package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static String toJSON(Object target) {
		
		if(target == null) {
			throw new NullPointerException("taget数据为null");
		}
		
		try {
			return MAPPER.writeValueAsString(target);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e); //如果转化过程中有问题则直接抛出异常
		}
		
	}
	
	//2.将json串按照指定的类型转化为对象
	//实现:传递什么类型,就返回什么对象
	// <T> 定义泛型
	public static <T> T  toObject(String json,Class<T> targetClass) {
		T t = null;
		try {
			t = MAPPER.readValue(json, targetClass);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return t;
	}
}
