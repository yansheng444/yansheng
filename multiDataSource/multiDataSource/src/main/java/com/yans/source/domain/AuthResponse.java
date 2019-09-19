package com.yans.source.domain;

public class AuthResponse {

	private String message;
	private UserDomain user;
	private String status;
	private String url ;
	
	
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UserDomain getUser() {
		return user;
	}
	public void setUser(UserDomain user) {
		this.user = user;
	}
	
	
}
