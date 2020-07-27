package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_cat") //关联商品分类表
public class ItemCat  extends BasePojo{
	@TableId(type = IdType.AUTO) //主键自增
	private Long id;
	private Long parentId;
	private String name;
	private Integer status;	//1正常，2删除
	private Integer sortOrder;	//排序号
	private Boolean	isParent;   //0 false   !0 真	
	
}