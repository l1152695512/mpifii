package com.yinfu.jbase.util;


import org.apache.commons.lang.StringUtils;

public class ValueUtils {

	public static String getStringValue(Object from) {
		if(from!=null&&StringUtils.isNotEmpty(from.toString())) {
			return from.toString();
		}
		return null;
	}
	
	public static Long getLongValue(Object from) {
		try {
			if (from != null && Long.valueOf(from.toString())!=0) {
				return Long.valueOf(from.toString());
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	public static Long getLongValue2(Object from) {
		try {
			if (from != null) {
				return Long.valueOf(from.toString());
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	public static long getlongValue(Object from) {
		try {
			if (from != null && Long.valueOf(from.toString())!=0) {
				return Long.valueOf(from.toString());
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
	
	public static boolean getBooleanValue(Object from) {
		try {
			if (from != null) {
				return Boolean.valueOf(from.toString());
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public static String[] getStringArrayValue(Object from) {
		if(from!=null) {
			return (String[])from;
		}
		return new String[]{};
	}
	
	public static int getIntValue(Object from) {
		if(from!=null) {
			return Integer.valueOf(from.toString());
		}
		return 0;
	}
}
