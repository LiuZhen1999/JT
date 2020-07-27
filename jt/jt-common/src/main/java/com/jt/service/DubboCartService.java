package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartList(Long userid);

	void updateCartNum(Cart cart);

	void deleteCart(Cart cart);

	void saveCart(Cart cart);

}
