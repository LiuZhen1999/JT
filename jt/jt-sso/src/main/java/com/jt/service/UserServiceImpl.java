package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	private static Map<Integer,String> paramMap; //如果项目中固定写死的可以通过static方式维护
	
	static {
		//1.将type类型转化为具体的字段信息.
		Map<Integer,String> map = new HashMap<>();
		map.put(1, "username");
		map.put(2, "phone");
		map.put(3, "email");
		paramMap = map;
	}

	//校验数据库中是否有数据!!!  有true   没有false
	//type 1 username,  2 phone ,3 eamil 
	@Override
	public boolean checkUser(String param, Integer type) {
		//String column = type==1?"username":((type==2)?"phone":"email");
		String column = paramMap.get(type);
		//1.通过获取数据库中的记录总数,判断是否存在数据.
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		int count = userMapper.selectCount(queryWrapper);
		return count>0?true:false;
	}

}
