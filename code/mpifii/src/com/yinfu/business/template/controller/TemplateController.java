package com.yinfu.business.template.controller;

import java.io.File;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.yinfu.UrlConfig;
import com.yinfu.business.template.model.Template;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/template", viewPath = "")
public class TemplateController extends Controller<Object>{
	private static final String PREVIEW_PATH = "images"+File.separator+"template"+File.separator;
	private static final String FILE_PATH = "file"+File.separator+"template"+File.separator;
	
	public void showTemplate() {
		StringBuffer strB = new StringBuffer();
		strB.append("select t.id,t.name,t.preview_img,IF(sp.id is null,'0','1') is_used ");
		strB.append("from bp_temp t left join bp_shop_page sp on (sp.shop_id=? and t.id = sp.template_id) ");
		strB.append("where t.delete_date is null and t.is_used ");
		
		setAttr("templates", JSONObject.toJSON(Db.find(strB.toString(), new Object[]{getPara("shopId")})));
		render(UrlConfig.BUSINESS + "/pageGuide/choiceTemplate.jsp");
	}
	
	public void list(){
		StringBuffer strB = new StringBuffer();
		strB.append("select t.id,t.name,t.preview_img,IF(sp.id is null,'0','1') is_used ");
		strB.append("from bp_temp t left join bp_shop_page sp on (sp.shop_id=? and t.id = sp.template_id) ");
		strB.append("where t.delete_date is null and t.is_used ");
		renderJson(Db.find(strB.toString(), new Object[]{getPara("shopId")}));
	}
	public void delete(){
		renderJson(Template.dao.delete(getPara("id")));
	}
	public void add(){
		String imgPath = saveImg();
		String filePath = saveFile();
		renderJson(Template.dao.add(getPara("name"),imgPath,filePath,getPara("isUsed")));
	}
	public void edit(){
		String imgPath = saveImg();
		if(null == imgPath){
			imgPath = getPara("imgPath");
		}
		String filePath = saveFile();
		if(null == filePath){
			filePath = getPara("filePath");
		}
		renderJson(Template.dao.edit(getPara("id"),getPara("name"),imgPath,filePath,getPara("isUsed")));
	}
	
	private String saveImg(){
		UploadFile file = getFile("previewImg", PathKit.getWebRootPath()+PREVIEW_PATH);
		if(null != file){
			return PREVIEW_PATH+file.getFile().getName();
		}else{
			return null;
		}
	}
	
	private String saveFile(){
		UploadFile file = getFile("file", PathKit.getWebRootPath()+FILE_PATH);
		if(null != file){
			return FILE_PATH+file.getFile().getName();
		}else{
			return null;
		}
	}
}
