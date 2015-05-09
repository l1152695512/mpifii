
package com.yinfu.business.operation.adv.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.operation.adv.model.Index;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/operation/adv", viewPath = "/page/business/operation/adv/")
public class IndexController extends Controller<Device> {
	public void index() {
		SplitPage splitPages = Index.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		if(ContextUtil.isAdmin()){
			setAttr("isAdmin", "true");
		}
		render("index.jsp");
	}
	
	public void addOrModify(){
		if(StringUtils.isNotBlank(getPara("id"))){
			Record group = Db.findFirst("select id,name from bp_shop_group where id=? ", new Object[]{getPara("id")});
			if(null != group){
				setAttr("id", group.get("id"));
				setAttr("name", group.get("name"));
			}
		}
		render("addOrModify.jsp");
	}
	
	public void delete(){
		boolean success = true;
		Record group = Db.findFirst("select access_delete from bp_shop_group where id=? ", new Object[]{getPara("id")});
		if(null != group){
			if(group.getInt("access_delete") == 1){
//				final List<Record> shops = Db.find("select id from bp_shop where group_id=? ", new Object[]{getPara("id")});
//				if(shops.size() > 0){
//					Record defaultGroup = Db.findFirst("select id from bp_shop_group where user_id=? and !access_delete", new Object[]{ContextUtil.getCurrentUserId()});
//					if(null == defaultGroup){
//						renderError(401);
//						return;
//					}
//				}
				success = Db.tx(new IAtom(){public boolean run() throws SQLException {
					List<Record> shops = Db.find("select id from bp_shop where group_id=? ", new Object[]{getPara("id")});
					if(shops.size() > 0){
						//更新商铺的分组为删除的分组的商铺为默认分组
						int changeRows = Db.update("update bp_shop s inner join bp_shop_group sg on (!sg.access_delete and s.group_id=? and "
								+ "(s.`owner`=sg.user_id or s.create_user=sg.user_id)) set s.group_id=sg.id", 
								new Object[]{getPara("id")});
						if(shops.size() != changeRows){
							return false;
						}
					}
					//删除该分组下的广告配置
					Db.update("delete from bp_shop_group_role where shop_group_id=? ", new Object[]{getPara("id")});
					//删除该分组
					Db.update("delete from bp_shop_group where id=? ", new Object[]{getPara("id")});
					return true;
				}});
			}else{
				renderError(401);
				return;
			}
		}
		renderJsonResult(success);
	}

	public void save(){
		if(StringUtils.isNotBlank(getPara("id"))){
			Db.update("update bp_shop_group set name=? where id=? ", new Object[]{getPara("name"),getPara("id")});
		}else{
			Db.update("insert into bp_shop_group(id,user_id,name,create_date) values(?,?,?,now())", 
					new Object[]{UUID.randomUUID().toString(),ContextUtil.getCurrentUserId(),getPara("name")});
		}
		renderJsonResult(true);
	}
}
