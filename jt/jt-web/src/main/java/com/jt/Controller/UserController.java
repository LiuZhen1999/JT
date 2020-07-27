package com.jt.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//消费者启动时,不需要检查是否有服务的提供者
	@Reference(check = false)
	private DubboUserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 实现页面跳转
	 * 1.http://www.jt.com/user/login.html
	 * 2.要求跳转的页面是 login.jsp
	 * 
	 * 业务说明:一般利用springMVC进行请求拦截时,不会拦截后缀.
	 * 要求: 用户通过www.jt.com/user/login的方式同样可以实现页面跳转.
	 *核心: 告知springMVC框架,要求拦截后缀.
	 */
	
	@RequestMapping("/{moduleName}")
	public String login(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
	/**
	 * 用户数据注册
	 * url:/user/doRegister
	 * 参数:{password:_password,username:_username,phone:_phone}
	 * 返回值:SysResult对象 的json数据
	 */
	@RequestMapping("/doRegister")
	@ResponseBody	//手动转化为json
	public SysResult saveUser(User user) {
		System.out.println(user.toString());
		userService.saveUser(user);
		return SysResult.success();
	}
	
	/**
	 * 15分钟联系    
	 * A. 我清楚的知道我要干什么 ,代码我会写  时间问题    少
	 * B. 我大概了解干什么,思路清楚,代码会写一部分   API不熟
	 * C. 图能看懂 ,只会写controller(部分-全部) service没有思路
	 * D. 图能看懂.但是代码不会写  没思路  不会API.  
	 * 业务需求:实现单点登录
	 * url地址:  /user/doLogin
	 * 参数:     {username:_username,password:_password}
	 * 返回值:    SysResult对象
	 * 
	 * 关于Cookie参数说明:
	 * Cookie特点: Cookie默认条件下,只能在自己的网址下进行展现, 京东的网站,看不到百度的cookie.
	 * Domain: 指定域名可以实现Cookie数据的共享.
	 * Path:(权限设定) 只有在特定的路径下,才能获取Cookie.
	 * 		  网址: www.jd.com/abc/findAll
	 * 		 cookie.setPath("/aa"); 只允许/aa的路径获取该Cookie信息 
	 * 		 cookie.setPath("/");  任意网址,都可以获取Cookie信息.
	 */
	@RequestMapping("/doLogin")
	@ResponseBody	//返回JSON
	public SysResult doLogin(User user,HttpServletResponse response) {
		
		//1.通过user传递用户名和密码,交给业务层service进行校验,获取ticket信息(校验之后的回执)
		String ticket = userService.doLogin(user);
		
		if(StringUtils.isEmpty(ticket)) {
			//证明用户名或密码错误.
			return SysResult.fail();
		}
		
		
		//2.准备Cookie实现数据存储.
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setDomain("jt.com");
		cookie.setPath("/");
		cookie.setMaxAge(7*24*60*60); //7天超时
		//将cookie保存到客户端中.
		response.addCookie(cookie);
		
		return SysResult.success();
	}
	
	/**
	 * 实现用户的退出操作
	 * url地址:/user/logout.html
	 * 返回值结果:  重定向到系统首页
	 * 业务功能: 
	 * 	 1.删除cookie  设定最大的超时时间0
	 * 	 2.删除redis	      根据ticket信息删除redis
	 * 
	 * 知识补充: 通过request对象获取的cookie信息只有name/value 其他的信息
	 * request对象无权获取.
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		//1.首先获取JT_TICKET的cookie信息
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equalsIgnoreCase(cookie.getName())) {
				//删除redis
				String ticket = cookie.getValue();
				jedisCluster.del(ticket);
				
				//删除cookie
				cookie.setDomain("jt.com");
				cookie.setPath("/");
				cookie.setMaxAge(0);	//控制cookie有效期.
				response.addCookie(cookie);
				break;	//跳出循环
			}
		}
		
		
		return "redirect:/";  //重定向到系统的根目录
	}
}