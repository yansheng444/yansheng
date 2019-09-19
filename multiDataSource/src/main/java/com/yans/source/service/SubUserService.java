package com.yans.source.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.yans.source.base.BaseService;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.domain.AuthResponse;
import com.yans.source.domain.SubscrUser;
import com.yans.source.handler.JsonHandler;
import com.yans.source.redis.redisUtil;
import com.yans.source.util.ConnectionUtil;
import com.yans.source.util.HttpUtil;
import com.yans.source.util.RunJsOnJava;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class SubUserService extends BaseService {

	@Autowired
	private SubscrUserDao subscrUserDao;

	public Integer callback(final String userid, final String pid, final String status) {
		
		int res = 0 ;
		
		ResultSet resultset = null;
		
		Jedis jedis = null;
		
		try {
			
			JedisPool jedisPool = redisUtil.getJedisPool();
			jedis = jedisPool.getResource();

			String last_user = jedis.get("last_user");
			
			if(last_user == null){
				last_user = "30947";
			}else {
				String[] split = last_user.split(",");
				last_user = split[split.length - 1];
			}
			
			// 同意了
			if (status.equals("1") && userid.equals(last_user)) {

				SubscrUser queryOne = subscrUserDao.queryOne(pid);
				
				String url = "http://auth.easypnp.com/ding_employee/auth/userinfo.do";

				Map<String, String> map = new HashMap<String, String>();
				map.put("userid", queryOne.getUser_id());
				String httpPostRequest = "";
				try {
					httpPostRequest = HttpUtil.HttpPostRequest(url, map);
				} catch (Exception e) {
					logger.error("请求刘明峰接口错误：", e);
					subscrUserDao.update("2", pid,null);
					map.put("userid", "30947");
					map.put("msg", pid+ "自动匹配未匹配上" );
					HttpUtil.HttpGetRequest("http://auth.easypnp.com/dd_auth/message/sendMessage", map);
					return -1;
				}

				Gson json = new Gson();

				AuthResponse fromJson = json.fromJson(httpPostRequest, AuthResponse.class);

				if(!fromJson.getStatus().equals("200")){
					return -1;
				}
				
				resultset = ConnectionUtil.test("jdbc:sqlserver://182.92.176.247:1433;DatabaseName=new_ygyy_db",
						"com.microsoft.sqlserver.jdbc.SQLServerDriver", "backsa", "nihao123!", 
						"SELECT a.* FROM (select u.u_loginname b_user_id,u.u_cname name ,g.G_CName dep from framework.dbo.sys_User u left join framework.dbo.sys_Group g on g.groupId = new_ygyy_db.dbo.F_GetTopGroupID(u.u_groupId) left join framework.dbo.sys_Group sg on u.u_groupId =sg.groupId where u_groupId > 0  and g.g_delete = 0 and u.u_status = 0) a WHERE a.name  = '" + fromJson.getUser().getName() + "'");
				
				JsonHandler jh = new JsonHandler();
				JSONArray handle = jh.handle(resultset);
				
				String js = "function getUidOrDep(a,data) {var xqo = eval('(' + data+ ')');var box = '';for(var i in xqo){if(a == xqo[i].name ){box += xqo[i].b_user_id + ',' + xqo[i].dep + ',';}}return box;}";
				
				String invokeJsFun = (String)RunJsOnJava.invokeJsFun(js, "getUidOrDep", fromJson.getUser().getName(),handle.toString());
				
				if(!StringUtils.isEmpty(invokeJsFun.toString())){
					subscrUserDao.update("2", pid,invokeJsFun.toString());
					logger.info( userid + "自动匹配b_userid:" + invokeJsFun.toString());
					return 1;
				}else {
					subscrUserDao.update("2", pid,null);
					map.put("userid", "30947");
					map.put("msg", pid+ "自动匹配未匹配上" );
					HttpUtil.HttpGetRequest("http://auth.easypnp.com/dd_auth/message/sendMessage", map);
					return -1;
				}

			} else  if (status.equals("-1") && userid.equals(last_user)) {
				subscrUserDao.deleteFromPid(pid);
				res = 1;
			}else {
				res = 1;
			}

		} catch (Exception e) {
			logger.error("审批回调失败：" + e);
			res = -1;
		}finally {
			
			if(jedis != null){
				jedis.quit();
				jedis.disconnect();
			}
			
			try {
				if(resultset != null){
					resultset.close();
				}
			} catch (SQLException e) {
			}
		}
		
		return res;
		
	}
	
	
	

}
