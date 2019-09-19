package com.yans.source.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * 多数据源配置
 * @author yansheng
 *
 */
public class DataSourceConfig {
	
	private String url ; 
	private String driver ; 
	private String username  ; 
	private String password;
	private String cmd;
	private Integer type; //1 sql 2 redis 3 interface 4 file
	private String param;
	private String port ;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	public static void main(String[] args) {
		
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://123.56.165.120:3308/echain?useUnicode=true&characterEncoding=UTF-8");
		dsc.setDriver("com.mysql.jdbc.Driver");
		dsc.setType(1);
		dsc.setUsername("eppay1o");
		dsc.setPassword("pay5899!");
		dsc.setCmd("select * from channel ");
		
		List<DataSourceConfig> arr = new ArrayList<>();
		arr.add(dsc);
		
		Gson josn = new Gson();
		
		System.out.println(josn.toJson(arr));
		
		
		
	}
	
	
}
