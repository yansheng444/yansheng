package com.yans.source.base;

/**
 * 返回结果数据结果
 * 
 * @author yansheng
 *
 */
public class DataRes {

	private int status;
	private String msg;
	private Object data;

	public DataRes() {
	}

	public DataRes(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public DataRes(int status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
