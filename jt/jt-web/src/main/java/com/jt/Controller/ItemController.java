package com.jt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboCartService;
import com.jt.service.DubboItemService;

@Controller
public class ItemController {
	
	@Reference(check=false)
	private DubboItemService itemService;
	@Reference(check=false)
	private DubboCartService cartService;
	
	/**
	 * url:http://www.jt.com/items/{itemId}.html
	 * 参数: itemId 商品编号
	 * 返回值: 
	 * 		1.页面返回值     item.jsp    
	 * 		2.数据返回值     item数据对象   
	 * 									${item.title }
	 * 									${itemDesc.itemDesc }
	 */
	@RequestMapping("/items/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		
		//1.根据itemId 查询item信息
		Item item = itemService.findItemById(itemId);
		model.addAttribute("item", item);
		
		//2.根据itemId 查询ItemDesc信息
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
	
	
}