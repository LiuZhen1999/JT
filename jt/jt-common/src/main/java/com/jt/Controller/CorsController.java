package com.jt.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.User;

@RestController
public class CorsController {
	
	
	@RequestMapping("/web/testCors")
	public User testUser() {
		System.out.println("我执行了业务操作!!!");
		return new User().setId(100L).setPassword("我是cors的返回值!!!!");
	}
	
}