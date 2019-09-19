package com.yans.source.domain;

import java.io.Serializable;



public class UserDomain  implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = -4662242264596709560L;
	private String userid ;
	private String name ;
	private String mobile ;
	private String position ;
	private String email ;
	//在当前isv全局范围内唯一标识一个用户的身份，用户无法修改
	private String unionid;
	private String tel;
	private String workPlace;
	private String remark;
	private Boolean isAdmin;
	private Boolean isBoss;
	private Boolean isHide;
	private Boolean isLeader;
	//是否激活钉钉
	private Boolean active;
	private Long[] department;
	//员工的企业邮箱，如果员工的企业邮箱没有开通，返回信息中不包含
	private String orgEmail;
	private String avatar;
	private String jobnumber;
	private String hiredDate;
	private String extattr;
	
	
	//入库使用
	private String deptStr;
	
	private String topDepartment;
	private String url ;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Boolean getIsBoss() {
		return isBoss;
	}
	public void setIsBoss(Boolean isBoss) {
		this.isBoss = isBoss;
	}
	public Boolean getIsHide() {
		return isHide;
	}
	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}
	public Boolean getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(Boolean isLeader) {
		this.isLeader = isLeader;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Long[] getDepartment() {
		return department;
	}
	public void setDepartment(Long[] department) {
		this.department = department;
	}
	public String getOrgEmail() {
		return orgEmail;
	}
	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getJobnumber() {
		return jobnumber;
	}
	public void setJobnumber(String jobnumber) {
		this.jobnumber = jobnumber;
	}
	public String getHiredDate() {
		return hiredDate;
	}
	public void setHiredDate(String hiredDate) {
		this.hiredDate = hiredDate;
	}
	public String getExtattr() {
		return extattr;
	}
	public void setExtattr(String extattr) {
		this.extattr = extattr;
	}
	public String getDeptStr() {
		return deptStr;
	}
	public void setDeptStr(String deptStr) {
		this.deptStr = deptStr;
	}
	public String getTopDepartment() {
		return topDepartment;
	}
	public void setTopDepartment(String topDepartment) {
		this.topDepartment = topDepartment;
	}
	
	
}
