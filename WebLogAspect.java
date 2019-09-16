package com.ygyw.ding.configration;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.RequestFacade;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.google.gson.Gson;
import com.ygyw.ding.base.BaseController;

/**
 * 日志log类，通过spring AOP 实现记录包下面所有public 方法的参数和返回值
 * @author Administrator
 *
 */
@Aspect
@Component
public class WebLogAspect extends BaseController {

	Gson json = new Gson();
	
	
	@Pointcut("(execution( * com.ygyw.ding.controller..*.*(..))) || (execution( * com.ygyw.ding.service..*.*(..)))  || (execution( * com.ygyw.ding.alibaba..*.*(..)))") 
	public void webLog() {
	}
	
	/**
	 * 通过spring AOP 切面，在所有controller的方法前置增加这个方法，达到记录所有请求发送的参数的需求
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		
		// 记录下请求内容
		Gson json = new Gson();
		String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
		
		Object[] args = joinPoint.getArgs();
		
		if(args == null || args.length == 0){
			return ;
		}
		
		StringBuilder param = new StringBuilder(256);
		
		for(Object o : args){
			if(o instanceof StandardMultipartHttpServletRequest){
				StandardMultipartHttpServletRequest request = (StandardMultipartHttpServletRequest)o;
				param.append(" 正常参数：" + json.toJson(request.getQueryString()));
				
			}else if(o instanceof RequestFacade){
				RequestFacade r = (RequestFacade)o;
				Map<String, String[]> parameterMap = r.getParameterMap();
				param.append(" 正常参数：" + json.toJson(parameterMap));
			}else{
				param.append("json参数：" +json.toJson(o));
			}
		}
		
		System.err.println(declaringTypeName + "."
				+ joinPoint.getSignature().getName() + "() 参数:" + param.toString());
		logger.info(declaringTypeName + "."
					+ joinPoint.getSignature().getName() + "() 参数:" + param.toString());
		
	}
	
	
	/**
	 * 后置通知，通过这个方法记录所有controller返回的返回值
	 * @param joinPoint
	 * @param o
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@AfterReturning(returning="o",pointcut="webLog()")
	public void doAfterReturning(JoinPoint joinPoint,Object o) {
		
		if(o != null){
			System.err.println(joinPoint.getSignature().getDeclaringTypeName() + "."
					+ joinPoint.getSignature().getName() + "():返回值：" + json.toJson(o));
			
			logger.info(joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName() + "():返回值：" + json.toJson(o));
		}
		
		
	}
	
	
	
}
