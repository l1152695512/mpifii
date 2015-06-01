package com.yinfu.system.controller;


import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Page;

@ControllerBind(controllerKey = "/system/page")
public class PageController extends Controller<Page>{
	public void index() {
		SplitPage splitPages = Page.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		/*render(UrlConfig.BUSINESS + "/index.jsp");*/
		render("/page/system/device/index.jsp");
	}
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			Page user = Page.dao.findById(getPara("id"), "id,sn");
			setAttr("userInfo", user);
		}
		render("/page/system/device/addOrModify.jsp");
	}
	
//	public void delete(){
//		renderJsonResult(Page.dao.deleteById(getPara("id")));
//	}
	
	public void save() {
//		final Platform user = getModel();
		if(StringUtils.isNotBlank(getPara("id"))){
			Page.dao.updateUserRole(getPara("sn"),0,getPara("id"));
			renderJsonResult(true);
		}else{
			Page.dao.updateUserRole(getPara("sn"),1,null);
			renderJsonResult(true);
		}
	}
	
	public void delete() {
		renderJsonResult(Db.deleteById("bp_warehouse", getPara("id")));
	}
}
