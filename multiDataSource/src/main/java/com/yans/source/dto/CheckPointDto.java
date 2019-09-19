package com.yans.source.dto;

/**
 * 
 * @author yanshng
 *
 */
public class CheckPointDto {
	
	private Long id ; 
	private String name ; 
	private Long group_id;
	private String condition ; 
	private String cp_etl_batch; //批处理结合
	private String cp_collector;//批处理数据处理js代码
	
	
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
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
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
