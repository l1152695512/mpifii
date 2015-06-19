package com.yinfu.system.controller;


import com.jfinal.ext.route.ControllerBind;
import com.yinfu.UrlConfig;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Log;

@ControllerBind(controllerKey = "/system/syslog", viewPath = UrlConfig.SYSTEM)
public class SystemLogController extends Controller<com.yinfu.system.model.Log>{

	public void index(){
		SplitPage logpage = Log.dao.getSysLogList(splitPage);
		setAttr("splitPage", logpage);
		render("/page/system/syslog.jsp");
	}
}
