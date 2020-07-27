package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service	//dubbo service注解
public class DubboUserServiceImpl implements DubboUserService {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	//将数据进行加密处理   email暂时用phone代替
	@Override
	@Transactional	//事务控制
	public void saveUser(User user) {
		
		String password = user.getPassword();  //明码
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		user.setPassword(password).setEmail(user.getPhone())
			.setCreated(new Date()).setUpdated(user.getCreated());
		userMapper.insert(user);
	}
	
	/**
	 * 步骤:
	 * 	1.校验用户信息是否正确
	 *  2.如果正确则利用redis保存到   key/value 
	 *  3.返回用户秘钥
	 */
	@Override
	public String doLogin(User user) {
		
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		//查询数据库检查是否正确   根据对象中不为null的属性充当where条件
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		
		if(userDB == null) {
			return null;
		}
		
		//用户名和密码正确.  开始单点登录
		String ticket = UUID.randomUUID().toString().replace("-", "");
		//防止涉密信息泄露,则需要进行脱敏处理
		userDB.setPassword("你猜猜,看看能不能猜对!!!!");
		String value = ObjectMapperUtil.toJSON(userDB);
		jedisCluster.setex(ticket, 7*24*3600, value);
		return ticket;
	}
	
}