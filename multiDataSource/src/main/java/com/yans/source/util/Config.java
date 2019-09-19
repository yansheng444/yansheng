package com.yans.source.util;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class Config {

	private static ResourceBundle bundle;
	static {
		
		try {
			bundle = ResourceBundle.getBundle("application");
		} catch (Exception e) {
		}
	}

	public static String getString(String key) {
		try {
			String result = new String(bundle.getString(key).getBytes("iso-8859-1"),"utf-8");
			return result;
		} catch (java.util.MissingResourceException mre) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}

	}
	
	
	
	
	
	
	public static void main(String[] args) {
		String string = getString("filepath");
		System.out.println(string);
	}
	
	
	
	
}
	