
package com.yinfu;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.yinfu.interceptor.ParamPkgInterceptor;
import com.yinfu.jbase.jfinal.ext.xss.XssHandler;
import com.yinfu.shiro.SessionHandler;

/**
 * API引导式配置
 */
public class MyConfig extends JFinalConfig {
	private Routes routes;
	private boolean isDev = isDevMode();
	
	private boolean isDevMode() {
		String osName = System.getProperty("os.name");// 操作系统的名称
		return osName.indexOf("Windows") != -1;
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
//		me.setError404View("/page/error/404.html");
//		me.setError401View("/page/error/401.html");
//		me.setError403View("/page/error/403.html");
//		me.setError500View("/page/error/500.html");
//		
//		if (isDev)
//			loadPropertyFile("classes/db.properties");
//		me.setDevMode(true);
//		me.setViewType(ViewType.JSP); // 设置视图类型为Jsp，否则默认为FreeMarker
	
		
loadPropertyFile("classes/db.properties");
		
		me.setError404View("/page/error/404.html");
		me.setError401View("/page/error/401.html");
		me.setError403View("/page/error/403.html");
		me.setError500View("/page/error/500.html");
		
		me.setDevMode(getPropertyToBoolean("devMode"));
		me.setViewType(ViewType.JSP); // 设置视图类型为Jsp，否则默认为FreeMarker
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		this.routes = me;
		// 自动扫描 建议用注解
		me.add(new AutoBindRoutes(false));
		/* me.add("/hello",CommonController.class); */
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		DruidPlugin dp=new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		dp.set(50,50,100);
		dp.addFilter(new StatFilter());
		me.add(dp);
		WallFilter wf=new WallFilter();
		wf.setDbType("mysql");
		dp.addFilter(wf);
		me.add(new EhCachePlugin());
		// add sql xml plugin
		me.add(new SqlInXmlPlugin());
		// add shrio
		if (Consts.OPEN_SHIRO)
			me.add(new ShiroPlugin(this.routes));
		// sql记录
		SqlReporter.setLogger(true);
		// 配置AutoTableBindPlugin插件
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(dp);
		if (isDev)
			atbp.setShowSql(true);
		atbp.autoScan(false);
		me.add(atbp);
		
		
	}
	
	private void c3poPlugin(Plugins me){
		C3p0Plugin dbPlugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(dbPlugin);
		// add EhCache
		me.add(new EhCachePlugin());
		// add sql xml plugin
		me.add(new SqlInXmlPlugin());
		// add shrio
		if (Consts.OPEN_SHIRO)
			me.add(new ShiroPlugin(this.routes));
		
		// 配置AutoTableBindPlugin插件
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(dbPlugin);
		if (isDev)
			atbp.setShowSql(true);
		atbp.autoScan(false);
		me.add(atbp);
		// sql记录
		SqlReporter.setLogger(true);
		
	
		
		
		
	}
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		// shiro权限拦截器配置
		if (Consts.OPEN_SHIRO)
			me.add(new ShiroInterceptor());
		if (Consts.OPEN_SHIRO)
			me.add(new com.yinfu.shiro.ShiroInterceptor());
		// 让 模版 可以使用session
		me.add(new SessionInViewInterceptor());
		// 配置参数封装拦截器
		me.add(new ParamPkgInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		// 计算每个page 运行时间
		// me.add(new RenderingTimeHandler());
		
		// xss 过滤
		me.add(new XssHandler("s"));
		// 伪静态处理
		me.add(new FakeStaticHandler());
		// 去掉 jsessionid 防止找不到action
		me.add(new SessionHandler());
		me.add(new UrlSkipHandler(".*/servlet.*", false));
		// me.add(new DruidStatViewHandler("/druid"));
	  DruidStatViewHandler dvh =  new DruidStatViewHandler("/druid");
	  me.add(dvh);
		me.add(new ContextPathHandler());
	}
	
	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 4444, "/", 5);
	}
	
}
