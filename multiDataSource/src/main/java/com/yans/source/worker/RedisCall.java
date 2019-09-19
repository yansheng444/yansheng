package com.yans.source.worker;

import java.util.concurrent.Callable;

import com.yans.source.domain.DataSourceConfig;
import com.yans.source.redis.redisUtil;

/**
 * 多线程下 调用redis
 * @author yansheng
 *
 */
public class RedisCall implements Callable<String> {

	
	private DataSourceConfig dsc;
	
	public DataSourceConfig getDsc() {
		return dsc;
	}

	public void setDsc(DataSourceConfig dsc) {
		this.dsc = dsc;
	}
	
	
	@Override
	public String call()  {
		
		return "redis end ! ";
	}


}
