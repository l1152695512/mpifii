package com.yinfu.jbase.util;

import org.apache.commons.lang.StringUtils;

public class DbUtil {
	public static String queryLike(String srcStr) {
		return "%" + replaceSqlStr(srcStr) + "%";
	}
	public static String replaceSqlStr(String srcStr){
		String result = srcStr;
		//适用于sqlserver
//		result = StringUtils.replace(result, "[", "[[]");
//		result = StringUtils.replace(result, "_", "[_]");
//		result = StringUtils.replace(result, "%", "[%]");
//		result = StringUtils.replace(result, "^", "[^]");
		//适用于mysql
		result = StringUtils.replace(result, "\\", "\\\\");
		result = StringUtils.replace(result, "'", "\\'");
		result = StringUtils.replace(result, "_", "\\_");
		result = StringUtils.replace(result, "%", "\\%");
		return result;
	}
}
