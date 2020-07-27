package com.jt.aop;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

//标识改类是全局异常处理机制的配置类
@RestControllerAdvice //advice通知   返回的数据都是json串
@Slf4j	//添加日志
public class SystemExceptionAOP {

/*
 * 添加通用异常返回的方法.
 * 底层原理:AOP的异常通知.
 * 
 * 常规手段:SysResult.fail();
 * 跨域访问: jsonp  必须满足跨域访问要求 callback(SysResult.fail())
 * 问题:如何判断是常规访问还是跨域
 * 分析:jsonp请求 get方式携带?callback参数
 * 判断依据用户参数是否携带callback 特定参数,一般条件下不会使用
 * */
@ExceptionHandler({RuntimeException.class}) //拦截运行时异常
public Object systemResultException(HttpServletRequest request,Exception exception) {
	
	String callback =request.getParameter("callback");
	if(StringUtils.isEmpty(callback)) { //为空不是跨域访问
		log.error("{~~~~~~"+exception.getMessage()+"}", exception); //输出日志
		return SysResult.fail();	 //返回统一的失败数据
	}
	
	//说明:有可能跨域jsonp只能提交get请求
	String method = request.getMethod();
	if(!method.equalsIgnoreCase("get")) {
		//不是get请求,亦然不是jsonp的请求方式
		log.error("{~~~~~~"+exception.getMessage()+"}", exception); //输出日志
		return SysResult.fail();	 //返回统一的失败数据
	}
	System.out.println(method+":"+callback);
	//如果程序执行到这里说明进行了jsonp跨域请求,按照jsonp方式返回数据
	//exception.printStackTrace(); //如果有问题,则直接在控制台打印
	log.error("{~~~~~~"+exception.getMessage()+"}", exception); //输出日志
	return new JSONPObject(callback,SysResult.fail());
	
	
}
}