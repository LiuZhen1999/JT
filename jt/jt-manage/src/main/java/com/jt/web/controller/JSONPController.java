package com.jt.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;

@RestController
public class JSONPController {
	
	/**
	 * url:http://manage.jt.com/web/testJSONP?callback=jQuery111107990405330439474_1595323762313&_=1595323762314
	 * @return JSONPObject 专门负责封装JSONP的返回值结果的.
	 * 注意事项:  返回值结果必须通过特殊的格式封装    callback(JSON数据)
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		
		//准备返回数据
		User user = new User();
		user.setId(100L).setPassword("我是密码");
		return new JSONPObject(callback, user);
	}
	
	
}