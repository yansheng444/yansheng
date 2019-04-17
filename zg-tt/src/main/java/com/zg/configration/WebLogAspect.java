package com.zg.configration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zg.base.GsonProvider;
import com.zg.base.LogProvider;

/**
 * 日志log类，通过spring AOP 实现记录包下面所有public 方法的参数和返回值
 * @author Administrator
 *
 */
@Aspect
@Component
public class WebLogAspect implements LogProvider,GsonProvider {
	
	@Pointcut("( execution(public * com.ygyy.alipay.controller..*.*(..)))  )") 
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
		String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
		String param = "";
		if(declaringTypeName.contains("controller")){
			param = getParam();
		}else {
			Object[] args = joinPoint.getArgs();
			param = json_util.toJson(args);
		}
		
		System.err.println(declaringTypeName + "."
				+ joinPoint.getSignature().getName() + "() 参数:" + param);
		logger.info(declaringTypeName + "."
				+ joinPoint.getSignature().getName() + "() 参数:" + param);
		
	}
	
	/**
	 * 获取所有的参数，通过servlet获取GET、POST请求的所有参数,controller包下面单独使用
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getParam() throws IOException{
		//只能获取get
		StringBuffer sb = new StringBuffer();
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			
			for(Entry<String, String[]> e : entrySet ){
//				System.out.println(e.getKey());
				sb.append(e.getKey()+"=");
				
				for(String s: e.getValue()){
					sb.append(s).append(";");
				}
//				System.err.println(sb.toString());
			}
			
			ServletInputStream inputStream = request.getInputStream();
			
			InputStreamReader isr = new InputStreamReader(inputStream);   
			BufferedReader br = new BufferedReader(isr); 
			String s = "" ; 
			while((s=br.readLine()) != null){ 
//				System.out.println(s);
				sb.append(s) ; 
			} 
//			System.out.println(sb.toString());
		
		return sb.toString();
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
					+ joinPoint.getSignature().getName() + "():返回值：" + json_util.toJson(o));
			
			logger.info(joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName() + "():返回值：" + json_util.toJson(o));
		}
		
		
	}
	
	
	
}