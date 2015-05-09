package com.yinfu.system.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.shiro.ShiroCache;
import com.yinfu.system.model.Role;

@ControllerBind(controllerKey = "/system/role")
public class RoleController extends Controller<Role>
{

	//@formatter:off 
	/**
	 * Title: index
	 * Description:进入角色管理首页
	 * Created On: 2014年9月10日 上午10:24:10
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:onR
	public void index(){
		/*SplitPage splitPages = Role.dao.findList(splitPage);
		setAttr("splitPage", splitPages);*/
		render("/page/system/role/treeIndex.jsp");
	}
	
	
	//@formatter:off 
	/**
	 * Title: treeData
	 * Description:角色ztree模型
	 * Created On: 2014年8月4日 下午5:34:08
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void treeData(){
		String id = getPara("id");
		String jsonText = Role.dao.childNodeData(id,getRequest());
		renderJson(jsonText);
	}
	//@formatter:off 
	/**
	 * Title: grant
	 * Description:授权
	 * Created On: 2014年9月10日 下午3:37:52
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void grant()
	{
		Role role = getModel();
		String res_ids = getPara("res_ids");
		renderJsonResult(Role.dao.batchGrant(role.getId(), res_ids));
		ShiroCache.clearAuthorizationInfoAll();
	}

	//@formatter:off 
	/**
	 * Title: add
	 * Description:增加角色
	 * Created On: 2014年10月22日 下午6:17:54
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add()
	{
		String pid = getPara("pid");
		String name = getPara("name");
		int orderid = getParaToInt("orderId");
		String id = Role.dao.save(pid, name, orderid);
		renderText(id);
	}

	
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:修改角色
	 * Created On: 2014年10月28日 上午9:59:20
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit()
	{
		String id = getPara("id");
		String name = getPara("name");
		Role role = Role.dao.findById(id);
		role.set("name", name);
		role.update();
		renderText(role.get("id").toString());
	}

	//@formatter:off 
	/**
	 * Title: delete
	 * Description:删除角色
	 * Created On: 2014年10月28日 上午10:00:06
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void delete()
	{
		String id = getPara("id");
		Role.dao.deletes(id);
		renderText(id);
	}
	//@formatter:off 
	/**
	 * Title: roleChoice
	 * Description:角色选择页面
	 * Created On: 2014年11月13日 上午11:47:23
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void roleChoice(){
		setAttr("id", getPara("id"));
		setAttr("roleId", getPara("roleId"));
		setAttr("roleName", getPara("roleName"));
		render("/page/system/role/roleChoice.jsp");	
	}
	
	//@formatter:off 
	/**
	 * Title: roleGrant
	 * Description:角色授权
	 * Created On: 2014年11月17日 下午4:51:04
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void roleGrant(){
		String id = getPara("id");
		setAttr("id", id);
		render("/page/system/role/authConfig.jsp");
	}
	public void getRoleAuth(){
		String id = getPara("id");
	/*	Role role = Role.dao.findById(id);*/
		Record record = Role.dao.findRoleAuth(id);
		renderJson(record);
	}

	public void setRoleAuth(){
		String id = getPara("id");
		String moduleIds = getPara("moduleIds");
		String resIds = getPara("resIds");
		Role.dao.setRoleAuth(id, moduleIds, resIds);
		renderJson(id);
	}
}
