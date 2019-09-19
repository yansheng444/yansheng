package com.yans.source.dto;

import java.util.List;

import com.yans.source.domain.CheckPoint;

public class CheckGroupDto {
	
	private String group_id;
	private String group_name ; 
	
	private List<CheckPoint> checkPointList;

	
	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public List<CheckPoint> getCheckPointList() {
		return checkPointList;
	}

	public void setCheckPointList(List<CheckPoint> checkPointList) {
		this.checkPointList = checkPointList;
	}

	
	
	
}
