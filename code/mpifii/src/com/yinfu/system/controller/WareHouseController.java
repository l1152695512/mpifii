package com.yinfu.system.controller;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Platform;
import com.yinfu.system.model.WareHouse;

@ControllerBind(controllerKey = "/system/warehouse")
public class WareHouseController extends Controller<WareHouse>{
	public void index() {
		SplitPage splitPages = WareHouse.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		setAttr("platList", platList());
		render("/page/system/device/index.jsp");
	}
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			WareHouse warehouse = WareHouse.dao.findById(getPara("id"), "id,sn,type,plat_no");
			setAttr("warehouseInfo", warehouse);
		}
		setAttr("platList", platList());
		
		render("/page/system/device/addOrModify.jsp");
	}
	
	public void save() {
		final WareHouse wareHouse = getModel();
		boolean success = false;
		if (wareHouse.get("id") != null && StringUtils.isNotBlank(wareHouse.get("id").toString())) {
			success = wareHouse.update();
		}else{
			if (checkSn(wareHouse.get("sn").toString())) {// 平台编号重复
				renderJson("error", "snRepeat");
				return;
			}
			
			wareHouse.set("create_date", new Date());
			success = wareHouse.save();
		}
		renderJsonResult(success);
	}
	
	public void delete() {
		renderJsonResult(Db.deleteById("bp_warehouse", getPara("id")));
	}
	
	public boolean checkSn(String sn) {
		List<WareHouse> list = WareHouse.dao.list("where sn=?",new Object[]{sn});
		return list.size()>0;
	}
	
	public List<Platform> platList(){
		Platform dao = new Platform();
		List<Platform> platList = dao.list();
		return platList;
	}
}
