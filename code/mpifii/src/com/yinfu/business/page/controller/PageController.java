package com.yinfu.business.page.controller;


import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.yinfu.business.page.model.Page;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/business/page")
public class PageController extends Controller<Page>{
	public void index() {
		SplitPage splitPages = Page.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		/*render(UrlConfig.BUSINESS + "/index.jsp");*/
		render("/page/business/device/index.jsp");
	}
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			User user = User.dao.findById(getPara("id"), "id,name,email,icon,status,des");
			setAttr("userInfo", user);
		}
		render("/page/system/user/addOrModify.jsp");
	}
	
	public void delete(){
		renderJsonResult(Page.dao.deleteById(getPara("id")));
	}
	
}
