package com.yans.source.util;


import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


/**
 * httpclient 接口测试方法，包括get和post
 * 需要以下jar包 
 * httpclinet-3.x.jar
 * httpcore-x.x.jar
 * commons-logiing-x.jar
 * commons-codec-x.x.jar
 * 
 * @author Administrator
 *
 */
public class HttpUtil {
	
	public static String HttpGetRequest(String url, Map<String, String> maps) throws HttpException, IOException{
		HttpClient httpClient = new HttpClient();
		
		GetMethod getMethod = new GetMethod(url);
		if(maps != null && maps.size() > 0){
			NameValuePair[] params = new NameValuePair[maps.size()];
			
			int count = 0 ;
			 for (Entry<String, String> key : maps.entrySet()) {  
				 NameValuePair param = new NameValuePair();
				 param.setName(key.getKey());
				 param.setValue(key.getValue());
				 params[count] = param;
		        count ++;
	        }  
			 
			 getMethod.setQueryString(params);
		}
		String response = "";
		
		
			int statusCode = httpClient.executeMethod(getMethod);
			
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("返回状态: "+ getMethod.getStatusLine());
			}
			
			
			Header[] headers  = getMethod.getResponseHeaders();
			for (Header h : headers)
				System.out.println(h.getName() + "------------ " + h.getValue());
			
			byte[] responseBody = getMethod.getResponseBody();// 璇诲彇涓哄瓧鑺傛暟缁�
			response = new String(responseBody, "UTF-8");
			
			return response;
		
	}
	
	public static String HttpPostRequest(String url, Map<String, String> maps) throws HttpException, IOException{
		HttpClient httpClient = new HttpClient();
		
		PostMethod postMethod = new PostMethod(url);
		
		
		 for (Entry<String, String> key : maps.entrySet()) {  
           
			 postMethod.addParameter(key.getKey(), key.getValue());
        }  
		 
		 httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		String response = "";
		
		
			int statusCode = httpClient.executeMethod(postMethod);
			
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("返回状态: "+ postMethod.getStatusLine());
			}
			
			Header[] headers  = postMethod.getResponseHeaders();
			for (Header h : headers)
				System.out.println(h.getName() + "------------ " + h.getValue());
			
			byte[] responseBody = postMethod.getResponseBody();// 璇诲彇涓哄瓧鑺傛暟缁�
			response = new String(responseBody, "UTF-8");
			
			return response;
		
	}
	
	/**
	 * 短信验证码，参数通过拼接方式处理
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String validateCode(String url) throws HttpException, IOException{
		HttpClient httpClient = new HttpClient();
		
		PostMethod postMethod = new PostMethod(url);
		
		String response = "";
		
			int statusCode = httpClient.executeMethod(postMethod);
			
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("返回状态: "+ postMethod.getStatusLine());
			}
			
			byte[] responseBody = postMethod.getResponseBody();
			response = new String(responseBody, "UTF-8");
			
			return response;
		
	}
	
	 public static String sendHttpPost(String url, String body) throws Exception {
		 HttpClient httpClient = new HttpClient();
			
		PostMethod postMethod = new PostMethod(url);
		
		postMethod.addRequestHeader("Content-type","application/json; charset=utf-8");
		postMethod.setRequestHeader("Accept", "application/json");
		postMethod.setRequestBody(body);
	
		String response = "";
		
		
		int statusCode = httpClient.executeMethod(postMethod);
		
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("返回状态: "+ postMethod.getStatusLine());
//			return null;
		}
		
//		Header[] headers  = postMethod.getResponseHeaders();
		
//		for (Header h : headers)
//			System.out.println(h.getName() + "------------ " + h.getValue());
		
		byte[] responseBody = postMethod.getResponseBody();// 璇诲彇涓哄瓧鑺傛暟缁�
		response = new String(responseBody, "UTF-8");
		
		return response;
	}
	
	 
	 
	 
	 
	 
	 
	 
	
}
