package com.jt.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Reference(check = false)
	private DubboCartService cartService;
	
	
	/**
	 * 跳转到订单确认页面
	 * 1.url:http://www.jt.com/order/create.html
	 * 2.请求参数: 无
	 * 3.返回值结果: 订单页面逻辑名称
	 * 4.页面取值信息: ${carts}  获取购物车记录
	 */
	@RequestMapping("/create")
	public String create(HttpServletRequest request,Model model) {
		
		//1.获取用户id
		User user = (User)request.getAttribute("JT_USER");
		Long userId = user.getId();
		List<Cart> carts = cartService.findCartList(userId);
		//2.封装页面数据信息
		model.addAttribute("carts", carts);
		
		return "order-cart";	//跳转指定的页面
	}
}