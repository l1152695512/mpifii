package com.yinfu.jbase.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

public class PropertyUtils {
	private static Properties properties = new Properties();
	
	static{
		loadPropertyFile("classes/setting.properties");
	}
	
	/**
	 * Load property file
	 * Example: loadPropertyFile("db_username_pass.txt");
	 * @param file the file in WEB-INF directory
	 */
	private static void loadPropertyFile(String file) {
//		Properties p = new Properties();
		if (StrKit.isBlank(file))
			throw new IllegalArgumentException("Parameter of file can not be blank");
		if (file.contains(".."))
			throw new IllegalArgumentException("Parameter of file can not contains \"..\"");
		
		InputStream inputStream = null;
		String fullFile;	// String fullFile = PathUtil.getWebRootPath() + file;
		if (file.startsWith(File.separator))
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + file;
		else
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + file;
		
		try {
			inputStream = new FileInputStream(new File(fullFile));
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: " + fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
		}finally {
			try {if (inputStream != null) inputStream.close();} catch (IOException e) {e.printStackTrace();}
		}
//		return p;
	}
	
	public static String getProperty(String key) {
		checkPropertyLoading();
		return properties.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		checkPropertyLoading();
		return properties.getProperty(key, defaultValue);
	}
	
	public static Integer getPropertyToInt(String key) {
		checkPropertyLoading();
		Integer resultInt = null;
		String resultStr = properties.getProperty(key);
		if (resultStr != null)
			resultInt =  Integer.parseInt(resultStr);
		return resultInt;
	}
	
	public static Integer getPropertyToInt(String key, Integer defaultValue) {
		Integer result = getPropertyToInt(key);
		return result != null ? result : defaultValue;
	}
	
	public static Boolean getPropertyToBoolean(String key) {
		checkPropertyLoading();
		String resultStr = properties.getProperty(key);
		Boolean resultBool = null;
		if (resultStr != null) {
			if (resultStr.trim().equalsIgnoreCase("true"))
				resultBool = true;
			else if (resultStr.trim().equalsIgnoreCase("false"))
				resultBool = false;
		}
		return resultBool;
	}
	
	public static Boolean getPropertyToBoolean(String key, boolean defaultValue) {
		Boolean result = getPropertyToBoolean(key);
		return result != null ? result : defaultValue;
	}
	
	private static void checkPropertyLoading() {
		if (properties == null)
			throw new RuntimeException("You must load properties file by invoking loadPropertyFile(String) method in configConstant(Constants) method before.");
	}
}
