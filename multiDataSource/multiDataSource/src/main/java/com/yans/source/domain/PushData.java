package com.yans.source.domain;

/**
 * 记录推送次数 数据结构
 * @author yansheng
 *
 */
public class PushData {
	
	private String key ; //要重复推送的redis key
	private String lasttime ; //上次推送时间
	private int count ;//已经推送次数
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	} 
	
	
	
	
}		
