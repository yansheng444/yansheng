package com.yans.source.worker;

import java.util.concurrent.Callable;

import com.yans.source.domain.DataSourceConfig;

/**
 * 多线程读取文件
 * @author yansheng
 *
 */
public class FileCall implements Callable<String> {

	private DataSourceConfig dsc;
	
	public DataSourceConfig getDsc() {
		return dsc;
	}

	public void setDsc(DataSourceConfig dsc) {
		this.dsc = dsc;
	}
	

	@Override
	public String call() throws Exception {
		
		
		
		
		
		return null;
	}

}
