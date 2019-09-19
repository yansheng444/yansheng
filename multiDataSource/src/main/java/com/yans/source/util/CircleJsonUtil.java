package com.yans.source.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.security.krb5.internal.crypto.crc32;

/**
 * 遍历json未知key，获取值util
 * @author yansheng
 *
 */
public class CircleJsonUtil {
	
	
	
	public static List<String> circleJson(String json){
		
		JSONArray fromObject = JSONArray.fromObject(json);
		List<String> arr = new ArrayList<String>();
		
		for(int i = 0 ; i < fromObject.size() ; i ++ ){
			Object object = fromObject.get(i);
			JSONObject jsonObj = JSONObject.fromObject(object.toString());  
			arr.add(jsonObj.toString());
		}
		
		return arr;
	}
	
}
