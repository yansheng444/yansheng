package com.yans.source.domain;

/**
 * 消息项，用户id对应关系表
 * 
 * @author yansheng
 *
 */
public class SubscrUser {
	
	private Long id ; 
	private Long trigger_id;
	private String user_id;
	private String process_id;
	private String b_user_id;
	private Integer status;
	private String last_push_data;//上次推送数据，重复推送使用
	private String today_push_data ; //本日推送数据
	
	
	public String getToday_push_data() {
		return today_push_data;
	}

	public void setToday_push_data(String today_push_data) {
		this.today_push_data = today_push_data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLast_push_data() {
		return last_push_data;
	}

	public void setLast_push_data(String last_push_data) {
		this.last_push_data = last_push_data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getB_user_id() {
		return b_user_id;
	}

	public void setB_user_id(String b_user_id) {
		this.b_user_id = b_user_id;
	}

	public String getProcess_id() {
		return process_id;
	}

	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}

	public Long getTrigger_id() {
		return trigger_id;
	}

	public void setTrigger_id(Long trigger_id) {
		this.trigger_id = trigger_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
