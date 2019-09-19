package com.yans.source.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 读取文件工具类
 * @author yansheng
 *
 */
public class FileUtil {
	
	
	
	public static String readFile(String path) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		
		String s = ""; 
		StringBuffer sb = new StringBuffer();
		while((s = reader.readLine()) != null){
			sb.append(s).append("\n");
			sb.append(reader.readLine()).append("\n");
		}
		reader.close();
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		String readFile = readFile("D:/data/logs/multiDataSource/business-2018-11-29.log");
		System.out.println(readFile);
		
		
	}
 	
	
	
	
	
	
	
	
	
	
	
	
}
