package com.jt;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestObjectMapper {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	/*
	 * 实现对象与json之间转化
	 */
	@Test
	public void test01() throws JsonProcessingException {
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(101l).setItemDesc("qqqq")
		.setCreated(new Date()).setUpdated(itemDesc.getCreated());
		
		//对象转json
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//json转对象
		ItemDesc itemDesc2 = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2.getItemDesc());
	}
}
