package com.yans.source.util;

import java.io.File;
import java.io.FileWriter;

/**
 * 文件工具类
 * 
 * @author yansheng
 *
 */
public class FileUitl {

	public void writeWord(String word, String filename) throws Exception {
		
		File f = new File(filename);
		
		if (f.exists()) {
			FileWriter fw = new FileWriter(f);
			fw.write(word);
			fw.flush();
			fw.close();
		}
		
	}

}
