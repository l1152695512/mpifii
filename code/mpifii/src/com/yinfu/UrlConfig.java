
package com.yinfu;

public class UrlConfig {
	
	public static final String SAE_BASE = "/Framework";
	public static String LOGIN = "/loginView";
	
	public static final String BASE = "/page";
	
	// 子模块
	public static final String COMMON = BASE + "/common";
	public static final String SYSTEM = BASE + "/system";
	public static final String APPLICATION = BASE + "/application";
	public static final String ERROR = BASE + "/error";
	public static final String INDEX = BASE + "/index";
	public static final String BUSINESS = BASE + "/business";// 业务模块
	
	public static final String VIEW_COMMON_LOGIN = COMMON + "/login.jsp";
	public static final String VIEW_COMMON_INIT = COMMON + "/init.html";
	public static final String VIEW_COMMON_JUMP = COMMON + "/jump.html";
	
	public static final String VIEW_INDEX = INDEX + "/index.jsp";
	public static final String VIEW_USERINDEX = BUSINESS + "/index.jsp";
	public static final String VIEW_INDEX_MSG = INDEX + "/msg.jsp";
	
	public static final String VIEW_ERROR_401 = ERROR + "/401.html";
	
}
