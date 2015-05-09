package com.yinfu;

public class Consts
{

	public static final String SESSION_USER = "user";

	/***
	 * shiro 权限 管理 开关
	 */
	public static boolean OPEN_SHIRO = true;

	/***
	 * 分布式session开关 请在redis.properties 配置ip和端口
	 */
	public static boolean OPEN_REDIS = true;

	public static boolean ALIYUN_DB = false;

	/**
	 * sae db 开关
	 */
	public static boolean SAE_DB = true;

}
