package com.yans.source.redis;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class redisUtil {
	
	public static JedisPool getJedisPool() throws Exception {
		// 生成连接池配置信息
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(2000);
		config.setMaxIdle(50);
		config.setMaxWaitMillis(3000);//最大等待时间
		config.setTestOnBorrow(true);
		// 在应用初始化的时候生成连接池
		JedisPool pool = new JedisPool(config, RedisConfig.host, RedisConfig.port,2000,RedisConfig.auth,13);
		return pool;
	}

	
	
	public static void main(String[] args) throws Exception {
		
		JedisPool jedisPool = getJedisPool();
		Jedis resource = jedisPool.getResource();
		
		
		Set<String> zrevrange = resource.zrevrange("trigger_hot", 1, 4);
		
		for(String s : zrevrange){
			System.out.println(s);
		}
		
		resource.quit();
		resource.disconnect();
		
		System.exit(-1);
	}
	
	
	
	
	
	
	
}


