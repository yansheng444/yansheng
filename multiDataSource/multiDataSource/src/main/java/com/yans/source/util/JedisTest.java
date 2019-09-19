package com.yans.source.util;

import redis.clients.jedis.Jedis;

public class JedisTest {
	
	
	public static String testData(String url,String password,String cmd) throws Exception{
		 int no = 1;
		 Jedis jedis=new Jedis(url,6379);
		 
		 jedis.auth(password);
         //向redis中添加数据
         jedis.set("mytest","123");
         //从redis中读取数据
         String value=jedis.get("mytest");
         jedis.del("mytest");
         
         if(!value.equals("123")){
        	no = -1;
         }

        //关闭连接
        jedis.close();
        
        return "";
	}
	
	
	
	
	
	
	
	
	
}
