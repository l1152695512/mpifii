
package com.yinfu.business.home.nav.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.home.nav.freemarker.NavMarker;
import com.yinfu.business.home.nav.model.Nav;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Org;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/business/home/nav")
public class NavController extends Controller<Nav> {
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "nav" + File.separator;
	
	public void index() {
		SplitPage splitPages = Nav.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/home/nav/index.jsp");
	}
	
	public void addOrModify() {
		if (StringUtils.isNotBlank(getPara("id"))) {
			Nav nav = Nav.dao.findById(getPara("id"));
			setAttr("navInfo", nav);
		}
		render("/page/business/home/nav/addOrModify.jsp");
	}
	
	public void save() {
		int max = 1024 * 1204 * 1024;
		String logo = "";
		UploadFile file = getFile("navImage", PathKit.getWebRootPath() + File.separator + LOGO_PATH, max);
		if (file != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File src = ImageKit.renameFile(file, name);
			logo = "upload/image/nav/" + src.getName();
		}
		User curUser = ContextUtil.getCurrentUser();
		String userid = String.valueOf(curUser.get("id"));
		Record r = DataOrgUtil.getUserSetting(userid,"flag");
		Nav nav = getModel();
		if (!"".equals(logo)) {
			nav.set("logo", logo);
		}
		if(r!=null){
			nav.set("org_root_id", r.get("id"));
		}
		if (nav.get("id") != null && StringUtils.isNotBlank(nav.get("id").toString())) {
			renderJsonResult(nav.update());
		} else {
			renderJsonResult(nav.saveAndCreateDate());
		}
	}
	
	public void delete() {
		boolean flag = false;
		String id = getPara("id");
		if(Nav.dao.deleteById(getPara("id"))){
			Record rd = DataOrgUtil.getUserSetting(ContextUtil.getCurrentUserId(),"flag");
			if(rd.getInt("id") == 1){
				NavMarker navMarker = NavMarker.getInstance();
				flag = navMarker.createHtml();
			}else{
				flag = true;
			}
		}
		renderJsonResult(flag);
	}
	
	public void changeStatus() {
		boolean flag = false;
		String id = getPara("id");
		Nav nav = Nav.dao.findById(id);
		nav.set("status", nav.getInt("status") == 0 ? 1:0);
		if(nav.update()){
			Record rd = DataOrgUtil.getUserSetting(ContextUtil.getCurrentUserId(),"flag");
			if(rd.getInt("id") == 1){
				NavMarker navMarker = NavMarker.getInstance();
				flag = navMarker.createHtml();
			}else{
				flag = true;
			}
		}
		
		renderJsonResult(flag);
	}
	
}
