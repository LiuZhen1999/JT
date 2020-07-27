package com.jt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

@SpringBootTest
public class TestRedis {

//	@Autowired
//	DataSource ds;
//	
//	@Test
//	void testi() {
//		System.out.println(ds);
//	}
	
	/**
	 * 如果测试有误,则检查上午修改的三处配置文件. 重启redis.
	 * @throws InterruptedException 
	 */
	@Test
	public void testString() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.126.129",6379);
		jedis.set("aaaa","123456");
		System.out.println(jedis.get("aaaa"));
		
		//判断redis中的key是否存在
		if(!jedis.exists("abc")) {
			jedis.set("abc","123456");
		}
		
		//为数据添加超时时间
		jedis.set("h", "123");
		jedis.expire("h", 10);
		Thread.sleep(2000);
		System.out.println("剩余的存活时间:"+jedis.ttl("h")+"秒");
		
	}
	
	
	/**
	 * 要求:如果key已经存在,则不允许修改!!!
	 * @throws InterruptedException
	 */
	@Test
	public void testStringSetNX() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.126.129",6379);
		jedis.flushAll();	//清空redis
		jedis.setnx("abc", "123");	//只有当数据不存在时才会赋值.
		jedis.setnx("abc", "456");
		System.out.println(jedis.get("abc"));
	}
	
	/**
	 * 虽然expire可以为数据添加超时时间,但是从宏观角度分析,该方法不具备原子性的操作
	 * 使用该方法可能存在风险.
	 * @throws InterruptedException
	 */
	@Test
	public void testStringSetEx() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.126.129",6379);
		jedis.flushAll();	//清空redis
		jedis.setex("abc", 100, "123"); //保证了数据的原子性操作
	}
	
	/**
	 * 1.set操作时,如果该数据存在则不允许赋值
	 * 2.set操作的同时要求添加超时时间
	 * 3.上述的操作,必须同时成功或者同时失败 保证原子性操作.
	 * 
	 * XX = "xx";  当key存在时    才会赋值
  	   NX = "nx";  当key不存在时才会赋值.  
       PX = "px";  添加超时时间的单位  毫秒
  	   EX = "ex";  秒
	 */
	@Test
	public void testStringSet() throws InterruptedException {
		Jedis jedis = new Jedis("192.168.126.129",6379);
		jedis.flushAll();	//清空redis
		SetParams params = new SetParams();
		params.nx().ex(60);
		jedis.set("abc", "123456", params);
	}
}
