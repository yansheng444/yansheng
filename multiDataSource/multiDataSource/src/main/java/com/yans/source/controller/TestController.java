package com.yans.source.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yans.source.base.BaseController;
import com.yans.source.util.Config;

@RestController
@RequestMapping("test")
public class TestController extends BaseController {
	
	@Value("${filepath}")
	private String filepath;
	
	@RequestMapping("test")
	public Object test(){
	
		
		return filepath + ":" + Config.getString("filepath");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
