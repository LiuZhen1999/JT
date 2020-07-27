package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController	//返回数据时不需要经过视图解析器.
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 业务: 展现商品列表数据,以EasyUI表格数据展现
	 * url地址: http://localhost:8091/item/query?page=1&rows=20
	 * 参数:	page=1&rows=20
	 * 返回值: EasyUITable VO对象
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {

		return itemService.findItemByPage(page,rows);
	}

	/**
	 * 商品新增操作
	 * url: /item/save
	 * 参数: form表单数据
	 * 返回值结果: SysResult对象
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {

		//2张表同时入库
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 商品修改操作
	 * url: /item/update
	 * 参数: form表单数据
	 * 返回值结果: SysResult对象
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 商品删除操作
	 * url:/item/delete
	 * 参数: id
	 * 返回值结果: SysResult对象
	 */
	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {

		itemService.deleteItems(ids);
		return SysResult.success();
	}

	/**
	 * url地址:   /item/updateStatus/2   /item/updateStatus/1
	 * 参数:  1.url中  2.ids
	 * 返回值:  SysResult对象
	 */
	@RequestMapping("/updateStatus/{status}")
	public SysResult updateItemStatus(Long[] ids,@PathVariable Integer status) {

		//实现商品状态修改
		itemService.updateItemStatus(ids,status);
		return SysResult.success();
	}

	/**
	 * 业务需求:  根据itemId查询商品详情信息
	 * url地址:  http://localhost:8091/item/query/item/desc/1474391972
	 * 参数:		使用restFul方式使用传输传递
	 * 返回值:    SysResult对象,并且需要携带itemDesc数据信息.
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {

		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
}