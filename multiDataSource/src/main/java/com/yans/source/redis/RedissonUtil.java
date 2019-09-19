package com.yans.source.redis;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

public class RedissonUtil {

	public static void getRedission() throws InterruptedException{
				
		Config config = new Config();
		
		SingleServerConfig useSingleServer = config.useSingleServer();
		useSingleServer.setAddress( RedisConfig.host + ":" + RedisConfig.port);
		useSingleServer.setPassword(RedisConfig.auth);
		useSingleServer.setDatabase(13);
		
		RedissonClient redisson = Redisson.create(config);
		
//		redisson.getBucket("userlist").set("1111");;
		RBucket<String> bucket = redisson.getBucket("userlist");
	
		
		String s = bucket.get();
		System.out.println(s);
		
		redisson.shutdown();
		
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		getRedission();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
