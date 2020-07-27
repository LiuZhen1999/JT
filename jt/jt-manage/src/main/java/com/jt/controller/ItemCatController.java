package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 业务:查询商品分类名称 根据id
	 * url地址: http://localhost:8091/item/cat/queryItemName?itemCatId=3
	 * 参数:    itemCatId=3
	 * 返回值:   商品分类名称
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		
		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
	}
	/**
	 * url:http://localhost:8091/item/cat/list 
	 *   参数: 当展现二三级信息时,会传递父级的Id信息,如果展现1级菜单,则应该设定默认值
	 *   返回值: List<EasyUITree>
	 */
	@RequestMapping("list")
	public List<EasyUITree> findItemCatByParentId
	(@RequestParam(name="id",defaultValue="0")Long parentId){
		
		//1.查询一级商品分类信息,所以
		return itemCatService.findItemCatByParentId(parentId); //数据库操作
		//return itemCatService.findItemCatByCache(parentId);//缓存操作
	}
}
