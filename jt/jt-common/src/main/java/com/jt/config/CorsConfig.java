package com.jt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//类似于web项目中使用的web.xml配置文件
@Configuration
public class CorsConfig implements WebMvcConfigurer{
	
	/**
	 * 配置后端服务器可以跨域的清单
	 * 参数说明:  addMapping:什么样的请求可以进行跨域   /web/**    /aaa/b/c/e/d/d/d
	 *          /*   匹配一级目录
	 *          /**  匹配多级目录   使用最多
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")
				.allowedOrigins("*");/*   //配置源 通配
				.allowedMethods("GET","POST","PUT","DELETE","HEAD") //允许的请求方式
				.allowCredentials(true)		//是否允许携带cookie
				.maxAge(1800);				//允许跨域的持续时间*/
	}
}
