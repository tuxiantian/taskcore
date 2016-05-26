package com.ai.taskcore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 */
public final class StringUtil {
	/** Private Constructor **/
	private StringUtil() {
	}
	
	/**
	 * 判断字符串是否非null && 非空字符 
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNotEmpty(String param) {
		return param != null && !"".equals(param.trim());
	}

	/**
	 * 判断字符串是否为null || 空字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String param) {
		return param == null || "".equals(param.trim());
	}
	
	/**
	 * 判断是否为数字
	 * @param str
	 * @return True为数字
	 */
	public static boolean isNum(String str) {
		String regex = "^(-?\\d+)(\\.\\d+)?$";
		return matchRegex(str, regex);
	}
				
	private static boolean matchRegex(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	public static String trim2Empty(String str) {
		return isEmpty(str) ? "" : str.trim();
	}
	public static String replace(String old){
		
		return old.indexOf("html")>0?old.replace("../", "/module/"):old.replace("../", "/front/");
	}
}
