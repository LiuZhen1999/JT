package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

//代表早期的配置文件
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

	@Value("${redis.nodes}")
	private String redisNodes;
	
	//bean注解  将生成的jedis对象交给Spring容器管理
	@Bean
//	public ShardedJedis shardedJedis() {
//		String[] nodes = redisNodes.split(",");
//		
//		//动态获取节点信息
//		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
//		for(String node:nodes) {
//			String host = node.split(":")[0];
//			int port = Integer.parseInt(node.split(":")[1]);
//			list.add(new JedisShardInfo(host,port));
//		}
//		return new ShardedJedis(list);
//	}
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodeSet = new HashSet<HostAndPort>();
		String[] clusters = redisNodes.split(",");
		for (String cluster : clusters) {	//host:port
			String host = cluster.split(":")[0];
			int port = Integer.parseInt(cluster.split(":")[1]);
			nodeSet.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodeSet);
	}
}