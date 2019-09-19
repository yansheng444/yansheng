package com.yans.source.configruation;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yans.source.base.BaseController;



/**
 * 自定义filter ,处理一些基础的拦截
 * @author yansheng
 *
 */
@Configuration
public class ConfigurationFilter extends BaseController {

	
	@Bean
	public MyFilter getMyFilter(){
		
		return new MyFilter();
	}
	
	
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
            
//            打印请求Url
//           String requestURI = request.getRequestURI();
//            System.out.println(requestURI);
//          
//            if(!checkUri(requestURI, "html,images")){
//            	System.out.println(1111);
//            	PrintWriter writer = response.getWriter();
//            	
//            	writer.write("request uri is not visible");
//            	writer.flush();
//            	writer.close();
//            }else {
//            	chain.doFilter(srequest, sresponse);
//            }
            chain.doFilter(srequest, sresponse);
        }

        public void init(FilterConfig arg0) throws ServletException {
        }
        
        
        public boolean checkUri(String uri,String errpath){
        	String[] split = errpath.split(",");
        	String substring = "";
        	if(uri.contains(".")){
        		substring = uri.substring(0,uri.lastIndexOf("."));
        	}else {
        		substring = uri;
        	}
        	
        	for(String s : split){
        		if(substring.contains(s)){
        			return false;
        		}
        	}
        	return true;
        }
        
        
       
        
        
        
        
    }
    
    
}