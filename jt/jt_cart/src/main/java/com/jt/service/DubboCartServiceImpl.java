package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService{

	@Autowired
	private CartMapper cartMapper;

	//根据userId查询购物车记录
	@Override
	public List<Cart> findCartList(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	//修改购物车数量信息   set num,updated where item_id,user_id
	//entity: 修改数据的实体   
	@Override
	public void updateCartNum(Cart cart) {
		Cart entity = new Cart();
		entity.setNum(cart.getNum())
		.setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("item_id", cart.getItemId())
		.eq("user_id", cart.getUserId());
		cartMapper.update(entity, updateWrapper);
	}

	//1.itemId   2.userId
	@Override
	public void deleteCart(Cart cart) {
		//根据对象中不为null的属性充当where条件
		cartMapper.delete(new QueryWrapper<Cart>(cart));
	}

	/*
	 * 第一次加够 则新增记录
	 * 否则更新数量信息
	 * */
	@Override
	@Transactional
	public void saveCart(Cart cart) {

		//1.根据user_id 和item_id查询购物车信息
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);

		//2.判断数据库中是否有记录.
		if(cartDB == null) {
			//第一次新增购物车
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//不是第一次加够 ,数量的更新操作
			int num = cartDB.getNum() + cart.getNum();
			//根据主键,更新购物车记录
			//cartDB.setNum(num).setUpdated(new Date());
			//UPDATE tb_cart SET item_id=?, item_title=?, created=?, num=?, item_price=?, user_id=?, updated=?, item_image=? WHERE id=? 
			//cartMapper.updateById(cartDB);

			Cart entity = new Cart();	//该对象封装了要修改的记录
			entity.setNum(num).setUpdated(new Date());
			UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("item_id", cart.getItemId())
						 .eq("user_id", cart.getUserId());
			cartMapper.update(entity, updateWrapper);
		}
	}
}
