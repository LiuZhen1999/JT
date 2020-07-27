package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestRedisShards {
	
	@Test
	public void test1() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.126.129",6379));
		shards.add(new JedisShardInfo("192.168.126.129",6380));
		shards.add(new JedisShardInfo("192.168.126.129",6381));
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("shards", "装备分片操作");
		System.out.println(shardedJedis.get("shards"));
	}
}
