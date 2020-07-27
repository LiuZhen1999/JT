package com.jt.inter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
//处理器的拦截器

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;
@Component
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	private static final String TICKET = "JT_TICKET";
	private static final String JT_USER = "JT_USER";
	//实现某一个接口,就应该实现该接口的方法.

	/**
	 * 返回值说明
	 * 	boolean  true  表示放行
	 * 			 false 表示拦截
	 * 
	 * 	拦截器策略说明:
	 * 	 要求用户访问服务器时首先应该通过拦截器判断用户是否登陆.如果用户登陆则放行.
	 *       如果用户没有登陆则拦截,重定向到用户的登陆页面.
	 *  
	 *       业务分析:
	 *       1.如何判断用户是否登陆
	 *       	1.cookie是否有数据     2.根据JT_TICKET中的值 校验redis.
	 *       2.控制哪些请求需要使用拦截器 
	 *       3.如果没有登陆则重定向到登陆页面
	 * @param request
	 * @return
	 */

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		
		//1.判断用户是否有指定的cookie
		Cookie[] cookies = request.getCookies();
		
		//2.判断cookie是否有记录
		String ticket = null;
		if(cookies !=null &&  cookies.length >0) {
			for (Cookie cookie : cookies) {
				if(TICKET.equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		//3.判断ticket数据是否有效
		if(!StringUtils.isEmpty(ticket)) {
			
			//4.根据ticket信息判断redis集群中时候有该记录.
			if(jedisCluster.exists(ticket)) {
				
				//5.根据ticket信息获取redis中的数据
				String userJSON = jedisCluster.get(ticket);
				//6.利用request对象传递用户信息
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				request.setAttribute(JT_USER, user);
				return true;	//表示放行
			}
		}
		
		response.sendRedirect("/user/login.html");
		return false;	//表示拦截
	}


	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}


	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
					throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}



}