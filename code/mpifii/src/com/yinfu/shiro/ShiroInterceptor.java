
package com.yinfu.shiro;

import java.util.List;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.system.model.Log;
import com.yinfu.system.model.Res;

/***
 * 让 shiro 基于 url 拦截 主要 数据库中也用url 保存权限
 * 
 * @author 12
 */
public class ShiroInterceptor implements Interceptor {
	private static ShiroExt ext = new ShiroExt();
	
	/**
	 * 获取全部 需要控制的权限
	 */
	private static List<String> urls;
	
	public static void updateUrls() {
		urls = Res.dao.getUrls();
	}
	
	public void intercept(ActionInvocation ai) {
		if (urls == null)
			urls = Res.dao.getUrls();
		
		String url = ai.getActionKey();
		try {
			//得到对应的action类的方法名称
			String method=url.substring(url.lastIndexOf("/"));
			if (method.contains("delete")|| method.contains("del"))
				Log.dao.insert(ai.getController(), Log.EVENT_DELETE);
			else if (method.contains("add") || method.contains("save"))
				Log.dao.insert(ai.getController(), Log.EVENT_ADD);
			else if (method.contains("edit") || method.contains("update"))
				Log.dao.insert(ai.getController(), Log.EVENT_UPDATE);
			else if (method.contains("grant"))
				Log.dao.insert(ai.getController(), Log.EVENT_GRANT);
			else if (method.contains("down")||method.contains("export"))//导出，或者下载
				Log.dao.insert(ai.getController(), Log.EVENT_DOWN);
			else if (method.contains("upload")||method.contains("import"))//上传或者导入
				Log.dao.insert(ai.getController(), Log.EVENT_UPLOAD);
			
			if (urls.contains(url) && !ext.hasPermission(url))
				ai.getController().renderError(401);
			
		} catch (Exception e) {
			ai.getController().renderError(401);
		}
		
		ai.invoke();
		
	}
	
}
