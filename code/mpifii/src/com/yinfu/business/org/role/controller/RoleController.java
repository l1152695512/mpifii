package com.yinfu.business.org.role.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.org.role.model.Role;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/org/role")
public class RoleController extends Controller<Role>
{

	public void index(){
		if(getPara("orgId") != null){
			splitPage.queryParam.put("org_id", getPara("orgId"));
		}
		SplitPage splitPages = Role.dao.getRoleList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/system/org/role/index.jsp");
	}
	
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			Role role = Role.dao.findById(getPara("id"),"id,org_id,name,des,create_date");
			setAttr("role", role);
		}else{
			Map<String,String> m = new HashMap<String,String>();
			m.put("org_id", getPara("orgId"));
			setAttr("role", m);
		}
		render("/page/system/org/role/addOrModify.jsp");
	}
	
	public void save(){
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = true;
			Role role = getModel();
			if (role.getId() == null) {//新增
				success = role.set("create_date",new Date()).save();
			}else{//修改
				success = role.update();
			}
			return success;
		}});
		renderJsonResult(success);
	}

	public void delete()
	{
		String id = getPara("id");
		Role.dao.deletes(id);
		renderText(id);
	}
	
	public void shopGrant(){
		String id = getPara("id");
		String orgId = getPara("orgId");
		setAttr("id", id);
		setAttr("orgId", orgId);
		render("/page/system/org/role/shopConfig.jsp");
	}
	
	public void manageGrant(){
		String id = getPara("id");
		String rootId = getPara("rootId");
		setAttr("id", id);
		setAttr("rootId", rootId);
		render("/page/system/org/role/manageConfig.jsp");
	}
	
	public void treeData(){
		String moduleIds = getPara("id");
		String roleId = getPara("roleId");
		String rootId = getPara("rootId");
		String json = Role.dao.childNodeData(moduleIds,roleId,rootId,getRequest());
		renderJson(json);
	}
	
	public void treeShopData(){
		String moduleIds = getPara("id");
		String orgId = getPara("orgId");
		String roleId = getPara("roleId");
		String json = Role.dao.childNodeShopData(moduleIds,orgId,roleId,getRequest());
		renderJson(json);
	}
	//@formatter:off 
	/**
	 * Title: getRoleAuth
	 * Description:获得角色所拥有的权限
	 * Created On: 2014年12月26日 下午5:08:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getRoleAuth(){
		String id = getPara("id");
		Record r = Role.dao.findRoleAuth(id);
		renderJson(r);
	}
	public void setRoleAuth(){
		String roleId = getPara("roleId");
		String menuIds = getPara("menuIds");
		String type = getPara("type");
		if("1".equals(type)){
			Role.dao.setRoleShopAuth(roleId, menuIds);
		}else if("2".equals(type)){
			Role.dao.setRoleAuth(roleId, menuIds);
		}
		renderJson(roleId);
	}
}
