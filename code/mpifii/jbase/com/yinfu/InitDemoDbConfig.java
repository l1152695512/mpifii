package com.yinfu;

import java.util.List;

import com.jfinal.config.Plugins;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class InitDemoDbConfig{
	
	public static void initPlugin(String jdbcUrl,String userName,String pass) {
		Plugins plugins = new Plugins();
		//jdbc:mysql://127.0.0.1:3306/pifii?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
		//root
		//ifidc1120
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, userName, pass);
//		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://113.106.98.60:3306/ynsqt?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull", 
//				"root", "ifidc1120");
		plugins.add(c3p0Plugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		plugins.add(arp);
		List<IPlugin> pluginList = plugins.getPluginList();
		if (pluginList != null) {
			for (IPlugin plugin : pluginList) {
				try {
					boolean success = plugin.start();
					if (!success) {
						String message = "Plugin start error: " + plugin.getClass().getName();
						throw new RuntimeException(message);
					}
				}
				catch (Exception e) {
					String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
					throw new RuntimeException(message, e);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		initPlugin("jdbc:mysql://127.0.0.1:3306/pifii?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
				"root", "ifidc1120");
		
		List<Record> data = Db.find("select * from system_user ");
		System.err.println(data.size());
	}
}
