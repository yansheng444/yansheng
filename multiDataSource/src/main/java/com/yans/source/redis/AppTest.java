package com.yans.source.redis;

import java.util.LinkedHashSet;
import java.util.Set;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class AppTest {
	
	
	public static JedisCluster getCluster() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 最大连接数
		poolConfig.setMaxTotal(1);
		// 最大空闲数
		poolConfig.setMaxIdle(1);
		// 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
		// Could not get a resource from the pool
		poolConfig.setMaxWaitMillis(1000);
		Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
		nodes.add(new HostAndPort(RedisClusterConfig.HOST1, RedisClusterConfig.PORT1));
		nodes.add(new HostAndPort(RedisClusterConfig.HOST2, RedisClusterConfig.PORT2));
		nodes.add(new HostAndPort(RedisClusterConfig.HOST3, RedisClusterConfig.PORT3));
		nodes.add(new HostAndPort(RedisClusterConfig.HOST4, RedisClusterConfig.PORT4));
		nodes.add(new HostAndPort(RedisClusterConfig.HOST5, RedisClusterConfig.PORT5));
		nodes.add(new HostAndPort(RedisClusterConfig.HOST6, RedisClusterConfig.PORT6));
		JedisCluster cluster = new JedisCluster(nodes, poolConfig);
		return cluster;
	}
	
	
	
}