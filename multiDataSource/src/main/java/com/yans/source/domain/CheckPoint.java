package com.yans.source.domain;


/**
 * 
 * @author yansheng
 *
 */
public class CheckPoint {
	
	private Long id ; 
	private String name ; //名称
	private String condition ;//时间表达式
	private String cp_etl_batch; //批处理数据处理js代码
	private String cp_collector;//批处理结合
	private Long group_id ;
	private String group_name;
	private Integer expire_time ;
	
	
	public Integer getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(Integer expire_time) {
		this.expire_time = expire_time;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public Long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getCp_etl_batch() {
		return cp_etl_batch;
	}
	public void setCp_etl_batch(String cp_etl_batch) {
		this.cp_etl_batch = cp_etl_batch;
	}
	public String getCp_collector() {
		return cp_collector;
	}
	public void setCp_collector(String cp_collector) {
		this.cp_collector = cp_collector;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}	
