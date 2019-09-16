package com.zg.util;

import java.util.regex.Pattern;

public class RegexUtil {

		/**
		 * 通用判断正则表达式计算方法
		 * 
		 * @param inData
		 * @param reg
		 * @return
		 */
		public static boolean regexAll(String inData, String reg) {

			if (inData == null) {
				return false;
			}

			return Pattern.compile(reg).matcher(inData).matches() ? true : false;
		}


}
