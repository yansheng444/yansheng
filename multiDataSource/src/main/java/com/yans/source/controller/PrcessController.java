package com.yans.source.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.yans.source.base.BaseController;
import com.yans.source.base.DataRes;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.domain.SubscrUser;
import com.yans.source.service.SubUserService;
/**
 * 审批相关
 * @author yansheng
 *
 */

@RestController
@RequestMapping("process")
public class PrcessController extends BaseController {

	@Autowired
	private SubscrUserDao subscrUserDao;
	
	@Autowired
	private SubUserService subUserService;
	
	@RequestMapping("callback")
	public Object callback(HttpServletRequest request){
		
		String pid = request.getParameter("processInstanceId");
		String status = request.getParameter("status");
		String userid = request.getParameter("userid");
		
		if(StringUtils.isEmpty(status) || StringUtils.isEmpty(pid) || StringUtils.isEmpty(userid)){
			return new DataRes(400,"参数缺失");
		}
		
		DataRes dr ;
		
		try{
			
			Integer callback = subUserService.callback(userid, pid, status);
			
			switch (callback) {
			
			case 1:
				dr = new DataRes(1000,"success");
				break;
			case -1:
				dr = new DataRes(500,"error");
				break;
			default:
				dr = new DataRes(1001,"未知错误");
				break;
			}
			
		}catch(Exception e){
			logger.error("审批回调失败",e);
			return new DataRes(500,"error");
		}
		
		return dr;
	}
	
	
	@RequestMapping("delete")
	public Object delete(HttpServletRequest request){
		
		String pid = request.getParameter("point_id");
		String status = request.getParameter("userid");
		
		if(StringUtils.isEmpty(status) || StringUtils.isEmpty(pid)){
			return new DataRes(400,"参数缺失");
		}
		
		try{
			
			SubscrUser su = new SubscrUser();
			su.setTrigger_id(Long.valueOf(pid));
			su.setUser_id(status);
			
			int delete = subscrUserDao.delete(su);
			if(delete < 0 ){
				return new DataRes(1001,"fail");
			}
			
		}catch(Exception e){
			logger.error("审批回调失败",e);
			return new DataRes(500,"error");
		}
		
		return new DataRes(1000,"success");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
