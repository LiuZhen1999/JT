package com.jt.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(check=false)
	private DubboCartService cartService;
	//应该准备一个常量池
	private static final String JT_USER = "JT_USER";
	
	/**
	 * 业务功能:展现购物车列表记录
	 * url:http://www.jt.com/cart/show.html
	 *   返回值: cart.jsp页面
	 *   页面取值问题:  ${cartList}
	 */
	@RequestMapping("/show")
	public String findCartList(Model model,HttpServletRequest request) {
		User user = (User) request.getAttribute(JT_USER);
		//.查询购物车信息
		Long userId = user.getId();
		List<Cart> cartList = cartService.findCartList(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 完成购物车数量的更新
	 * 1.url:http://www.jt.com/cart/update/num/1474392189/3
	   2.参数: 通过url地址,拼接参数.
	   3.返回值: void
	   
	   规则: //如果restFul中的参数名称与对象的属性一致.则可以使用对象接收
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public void updateCartNum(Cart cart) { 
		
		Long userId = 7L;	//暂时写死,后期维护
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
	}
	
	/**
	 * 完成购物车新增操作
	 * 1.url地址:http://www.jt.com/cart/add/1474392220.html
	 * 2.参数:form表单提交
	 * 3.返回值结果:  重定向到购物车展现页面
	 */
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart) {
		
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}
	
	/**
	 * 思考问题: 用户添加购物车时,商品库存量数量信息是否变化????
	 *   	思路1:用户操作购物车时不考虑库存量的问题. 如果用户下订单时更新库存量
	 *   	思路2: 用户只要进行加够操作,会与当前库存量进行对比.如果购买的数量超过库存量,则提示用户库存不足.
	 * @param cart
	 * @return
	 */
	@RequestMapping("/delete/{itemId}")
	public String  deleteCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		//重定向到购物车首页
		return "redirect:/cart/show.html";
	}
}
