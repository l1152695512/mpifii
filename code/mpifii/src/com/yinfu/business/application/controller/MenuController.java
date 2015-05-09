package com.yinfu.business.application.controller;

import java.io.File;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.yinfu.Consts;
import com.yinfu.UrlConfig;
import com.yinfu.business.application.model.Menu;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.L;

@ControllerBind(controllerKey = "/business/app/menu", viewPath = "/page/business/application/menu")
public class MenuController extends Controller<Menu>
{
	private static final String PREVIEW_PATH = "images"+File.separator+"menu"+File.separator;
	
	public void index()
	{
		render("/page/business/application/menu/add.jsp");
		
	}

	/**
	 * 根据shopId获取所有菜单
	 */
	public void getListByShopId()
	{	
		// 分页基本变量（jqGrid自带）
		int pageNum = getParaToInt("pageNum");
		int pageSize = getParaToInt("pageSize");
		String shopId = getPara("shopId");
		Page<Menu> page = Menu.dao.getListByShopId(pageNum, pageSize, shopId);
		renderJson(page);
	}
	
	/**
	 * 根据shopId获取所有菜单类型
	 */
	public void getTypeByShopId()
	{
		renderJson(Menu.dao.getTypeByShopId(getPara("shopId")));
	}
	
	public void add()
	{
		UploadFile file = getFile("upload", PathKit.getWebRootPath()+"/"+PREVIEW_PATH);
		if(null != file){
			String icon =  PREVIEW_PATH+file.getFile().getName();
			getModel().set("icon", icon);
			getModel().saveAndDate();
		}else{
			renderJson("");
		}
	}

	public void edit()
	{
		renderJsonResult(getModel().updateAndModifyDate());
	}
	
	/**
	 * 新增菜单类型
	 */
	public void addType(){
		String name = getPara("name");
		String shop_id = getPara("shop_id");
		
		renderJson(Menu.dao.addType(name, shop_id));
	}
	
	/**
	 * 修改菜单类型
	 */
	public void editMenuType(){
		String name = getPara("name");
		String id = getPara("id");
		
		renderJson(Menu.dao.editType(name, id));
	}
	
	/**
	 * 保存图片
	 */
	public void saveImg(){
		UploadFile file = getFile("upload", PathKit.getWebRootPath()+"/"+PREVIEW_PATH);
		if(null != file){
			renderJson( PREVIEW_PATH+file.getFile().getName());
		}else{
			renderJson("");
		}
	}
}
