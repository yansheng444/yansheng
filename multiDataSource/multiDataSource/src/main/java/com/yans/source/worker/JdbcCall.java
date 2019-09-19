package com.yans.source.worker;

import java.sql.ResultSet;
import java.util.concurrent.Callable;

import com.yans.source.domain.DataSourceConfig;
import com.yans.source.handler.JsonHandler;
import com.yans.source.util.ConnectionUtil;

import net.sf.json.JSONArray;

/**
 * 多线程 请求数据库
 * 
 * @author yansheng
 *
 */
public class JdbcCall implements Callable<String> {

	private DataSourceConfig dsc;
	
	public DataSourceConfig getDsc() {
		return dsc;
	}

	public void setDsc(DataSourceConfig dsc) {
		this.dsc = dsc;
	}

	@Override
	public String call() throws Exception {

		ResultSet test = ConnectionUtil.test(dsc.getUrl(), dsc.getDriver(), dsc.getUsername()
				, dsc.getPassword(), dsc.getCmd());
		
		JsonHandler jh = new JsonHandler();
		JSONArray handle = jh.handle(test);
		return handle.toString();
		
	}

}
