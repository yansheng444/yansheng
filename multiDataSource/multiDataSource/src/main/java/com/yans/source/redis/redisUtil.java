package com.yans.source.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.Set;

import com.google.gson.Gson;
import com.yans.source.domain.DataSourceConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class redisUtil {
	
	public static JedisPool getJedisPool() throws Exception {
			// 生成连接池配置信息
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(200);
			config.setMaxIdle(5);
			config.setMaxWaitMillis(1000);//最大等待时间
			config.setTestOnBorrow(true);
			// 在应用初始化的时候生成连接池
			JedisPool pool = new JedisPool(config, RedisConfig.host, RedisConfig.port,10000,RedisConfig.auth,13);
			return pool;
		}

	
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		JedisPool jedisPool = getJedisPool();
		Jedis resource = jedisPool.getResource();
		
		Set<String> keys = resource.keys("87*");
		
		for(String s : keys ){
			resource.del(s);
		}
		
		
	}
	
	
	
	
	
}


