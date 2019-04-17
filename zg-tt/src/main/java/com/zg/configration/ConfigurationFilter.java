package com.zg.configration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import com.zg.base.GsonProvider;
import com.zg.base.LogProvider;
import com.zg.dto.BaseRes;

/**
 * 自定义filter ,处理一些基础的拦截
 * @author yansheng
 *
 */
@Configuration
public class ConfigurationFilter {

	@Bean
	public MyFilter getMyFilter(){
		return new MyFilter();
	}
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
    public class MyFilter implements Filter {
    	
        public void destroy() {
        	
        }

        public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain
                chain)
                throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) srequest;
            HttpServletResponse response = (HttpServletResponse) sresponse;
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*"); 
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
            response.setHeader("Access-Control-Max-Age", "3600");  
            response.setHeader("Access-Control-Allow-Headers", "*");  
           String requestURI = request.getRequestURI();
           
           if(requestURI.contains("/login")){
        	   chain.doFilter(srequest, sresponse);
           }
            
           String token = request.getHeader("token");
           String userid = request.getHeader("userid");
           
           if(token == null || userid == null || StringUtils.isEmpty(token) || StringUtils.isEmpty(userid)){
        	   PrintWriter writer = response.getWriter();
        	   BaseRes br = new BaseRes(400,"missing token");
        	   writer.write(GsonProvider.json_util.toJson(br));
        	   writer.flush();
        	   return;
           }
           
           try{
        	   String string = redisTemplate.opsForValue().get(userid);
        	   
        	   if(string == null || !string.equals(token)){
        		   PrintWriter writer = response.getWriter();
            	   BaseRes br = new BaseRes(400,"invalid token");
            	   writer.write(GsonProvider.json_util.toJson(br));
            	   writer.flush();
            	   return;
        	   }
        	   
           }catch(Exception e){
        	   LogProvider.logger.error("获取token失败：",e);
           }
           
            chain.doFilter(srequest, sresponse);
            
            
        }

        public void init(FilterConfig arg0) throws ServletException {
        }
    }
}