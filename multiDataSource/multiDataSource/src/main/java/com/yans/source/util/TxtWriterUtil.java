package com.yans.source.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.imageio.stream.FileImageInputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yans.source.dto.OssDto;
import com.yans.source.dto.OssResDto;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 写txt工具类
 * @author yansheng
 *
 */
public class TxtWriterUtil {
	
	private static Gson json = new Gson();
	
	public static String writeTxt(String path,String json) throws Exception{
		List<List<String>> arr = new ArrayList<List<String>>();
		
		String date = Function.DateFomartString(new Date());
		path = path + date ;
		Long now = System.currentTimeMillis();
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
//		String path = "D://2.csv";
		CsvWriter cw = new CsvWriter(path + "/" + now + ".csv" ,',',Charset.forName("UTF-8"));
		
		JSONArray fromObject = JSONArray.fromObject(json);
		
		for(int i = 0 ; i < fromObject.size() ; i ++ ){
			Object object = fromObject.get(i);
			List<String> data = new ArrayList<String>();
			 JSONObject jsonObj = JSONObject.fromObject(object.toString());  
	         Iterator it = jsonObj.keys();  
	         
	         List<String> keyListstr = new ArrayList<String>();  
	         
	         while(it.hasNext()){  
	            String key = (String) it.next();
	            if(i == 0){
	            	cw.write(key);
	            }
	            String value = jsonObj.getString(key);
	            data.add(value);
	         }
	         arr.add(data);
		}
		cw.endRecord();
		for(List<String> l : arr){
			
			for(String s : l){
				cw.write(s);
			}
			cw.endRecord();
		}
		
		cw.flush();
		cw.close();
		
		return getDomain(path, now + ".csv");
		
	}
	
	
	public static String writeList(String path,Set<String> jsons) throws Exception{
		
		List<List<String>> arr = new ArrayList<List<String>>();
		String date = Function.DateFomartString(new Date());
		path = path + date ;
		Long now = System.currentTimeMillis();
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		
		CsvWriter cw = new CsvWriter(path + "/" + now + ".csv" ,',',Charset.forName("UTF-8"));
		int a = 0 ;
		for(String json : jsons){
			JSONArray fromObject = JSONArray.fromObject(json);
			for(int i = 0 ; i < fromObject.size() ; i ++ ){
				Object object = fromObject.get(i);
				List<String> data = new ArrayList<String>();
				 JSONObject jsonObj = JSONObject.fromObject(object.toString());  
		         Iterator it = jsonObj.keys();  
		         
		         List<String> keyListstr = new ArrayList<String>();  
		         
		         while(it.hasNext()){  
		            String key = (String) it.next();
		            if(a == 0){
		            	cw.write(key);
		            }
		            String value = jsonObj.getString(key);
		            data.add(value);
		         }
		         arr.add(data);
		         a ++;
			}
			cw.endRecord();
			for(List<String> l : arr){
				
				for(String s : l){
					cw.write(s);
				}
				cw.endRecord();
			}
			cw.write("");
			cw.endRecord();
		}
		cw.flush();
		cw.close();
		
		return getDomain(path, now + ".csv");
	}
	
	
	
	public static String getDomain(String prefix,String path) throws Exception{
		
		String url = "http://oss.98ep.com/backstage_sap/attachment/uploadfilestr.do";
		List<OssDto> arr = new ArrayList<OssDto>();
		OssDto od = new OssDto();
		od.setFile(byte2hex(image2byte(prefix + "/" + path)));
		od.setFilename(path);
		od.setGuid(UUID.randomUUID().toString());
		od.setObjid("0");
		od.setObjtype("91");
		arr.add(od);
		String sendHttpPost = HttpUtil.sendHttpPost(url, json.toJson(arr));
		
		if(!sendHttpPost.contains("true")){
			return null;
		}
		
		String substring = sendHttpPost.substring(sendHttpPost.indexOf("path=")+5);
		String substring2 = substring.substring(0,substring.indexOf(","));
		
		return substring2;
		
	}
	
	
	/**
	    * 图片转字节
	    * @param path
	    * @return
	    */
	   public static  byte[] image2byte(String path) {
	    byte[] data = null;
	    FileImageInputStream input = null;
	    try {
	     input = new FileImageInputStream(new File(path));
	     ByteArrayOutputStream output = new ByteArrayOutputStream();
	     byte[] buf = new byte[1024];
	     int numBytesRead = 0;
	     while ((numBytesRead = input.read(buf)) != -1) {
	      output.write(buf, 0, numBytesRead);
	     }
	     data = output.toByteArray();
	     output.close();
	     input.close();
	    } catch (FileNotFoundException ex1) {
	     ex1.printStackTrace();
	    } catch (IOException ex1) {
	     ex1.printStackTrace();
	    }
	    return data;
	   }

	   
	   
	   public static  String byte2hex(byte[] b){  // 二进制转字符串  
	          StringBuffer sb = new StringBuffer();  
	          String stmp = "";  
	          for (int n = 0; n < b.length; n++) {  
	              stmp = Integer.toHexString(b[n] & 0XFF);  
	              if (stmp.length() == 1) {  
	                  sb.append("0" + stmp);  
	              } else {  
	                  sb.append(stmp);  
	              }  
	          }  
	          return sb.toString();  
	     } 
	
	
	public static void main(String[] args) throws Exception {
		
		String domain = getDomain("D://", "huikuan.png");
		String substring = domain.substring(domain.indexOf("path=")+1);
		System.out.println(substring );
	}
	
	
	
}
