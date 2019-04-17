package com.zg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zg.base.BaseController;
import com.zg.domain.Channel;
import com.zg.domain.ContentConfig;
import com.zg.dto.BaseRes;
import com.zg.dto.ChannelDto;
import com.zg.dto.DataRes;
import com.zg.service.ChannelService;

@RestController
@RequestMapping("channel")
public class ChannelController extends BaseController {

	@Autowired
	private ChannelService channelService;
	
	@RequestMapping("create")
	public BaseRes create(@RequestBody ChannelDto cd){
		
		if(cd == null  || cd.getChannel() == null
			|| cd.getContentConfig() ==  null || cd.getContentConfig().size() == 0
				){
			return new BaseRes(400,"参数缺失");
		}
		
		List<ContentConfig> cnfs = cd.getContentConfig();
		
		if(cd.getBackupConfig() != null){
			cnfs.add(cd.getBackupConfig());
		}
		
		for(ContentConfig f : cnfs){	
			if(StringUtils.isEmpty(f.getChannel_name()) || StringUtils.isEmpty(f.getSource_name())
				|| f.getPercent() == null || f.getType() == null || f.getPercent() > 100 || f.getPercent() <= 0
				|| f.getType() != 1 && f.getType() != 2
					){
				return new BaseRes(400,"参数缺失");
			}
		}
		
		BaseRes br = null;
		
		try{
			
			int create = channelService.create(cd);
			
			switch (create) {
			
				case -2:
					br = new BaseRes(203,"重名的频道，请更换频道名称");
					break;
				case -1:
					br = new BaseRes(500,"系统错误");
					break;
				case 0:
					br = new BaseRes(201,"新增失败");
					break;
				case 1:
					br = new BaseRes(200,"SUCCESS");
					break;
	
				default:
					br = new BaseRes(200,"SUCCESS");
					break;
			}
			
			
		}catch(Exception e){
			logger.error("频道创建异常：",e);
			return new BaseRes(500,"系统异常");
		}
		
		return br;
	}
	
	
	/**
	 * 下线
	 * @param channelId
	 * @return
	 */
	@RequestMapping("offline")
	public BaseRes updateStatus(@RequestBody Channel c){
		
		if(c == null || c.getId() == null || c.getStatus() ==null && c.getIs_delete() == null){
			return new BaseRes(400,"参数缺失");
		}
		
		try{
			int create = channelService.update(c);
			
			if(create <= 0){
				return new BaseRes(201,"创建失败");
			}
			
		}catch(Exception e){
			logger.error("频道创建异常：",e);
			return new BaseRes(500,"系统异常");
		}
		
		return new BaseRes(1000,"success");
	}
	
	/**
	 * 查询列表
	 * @param channelId
	 * @return
	 */
	@RequestMapping("list")
	public BaseRes list(){
		
		try{
			
			List<Channel> create = channelService.queryData();
			
			if(create.size() == 0){
				return new BaseRes(201,"无数据");
			}
			DataRes dr = new DataRes();
			dr.setCode(200);
			dr.setMsg("SUCCESS");
			dr.setData(create);
			
			return dr;
			
		}catch(Exception e){
			logger.error("查询列表异常：",e);
			return new BaseRes(500,"系统异常");
		}
		
	}
	
	/**
	 * 保存排序
	 * @param c
	 * @return
	 */
	@RequestMapping("saveSort")
	public Object updateSort(@RequestBody List<Channel> c){
		
		if(c == null || c.size() == 0){
			return new BaseRes(400,"参数缺失");
		}
		
		for(Channel ch : c){
			if(ch.getId() == null || ch.getSort() == null ){
				return new BaseRes(400,"参数缺失");
			}
		}
		
		int i = 0 ;
		for(Channel cc : c) {
			i ++;
			cc.setSort(i);
		}
		
		try{
			
			int updateSort = channelService.updateSort(c);
			
			if(updateSort <= 0){
				return new BaseRes(201,"更新失败");
			}
			
			return new BaseRes(200,"SUCCESS");
			
		}catch(Exception e){
			logger.error("更新排序异常：",e);
			return new BaseRes(500,"系统异常");
		}
		
	}
	
	
	
	
	
	@RequestMapping("check")
	public Object check(String name){
		
		if(StringUtils.isEmpty(name)){
			return new BaseRes(400,"参数缺失");
		}
		
		BaseRes br = new BaseRes();
		
		try{
			
			int queryByName = channelService.queryByName(name);
			
			if(queryByName == 0){
				br.setCode(200);
				br.setMsg("SUCCESS");
			}else {
				br.setCode(201);
				br.setMsg("重复");
			}
			
		}catch(Exception e){
			logger.error("查重失败：",e);
			br.setCode(500);
			br.setMsg("error,请重试");
		}
		
		return br;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
