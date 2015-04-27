package com.yinfu.system.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.shiro.ShiroCache;
import com.yinfu.system.model.Org;
import com.yinfu.system.model.Role;

@ControllerBind(controllerKey = "/system/org")
public class OrgController extends Controller<Org>
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
		render("/page/system/org/treeIndex.jsp");
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
		String jsonText = Org.dao.childNodeData(id,getRequest());
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
		Org org = getModel();
		String res_ids = getPara("res_ids");
		renderJsonResult(Org.dao.batchGrant(org.getId(), res_ids));
		ShiroCache.clearAuthorizationInfoAll();
	}

	//@formatter:off 
	/**
	 * Title: add
	 * Description:增加组织
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
		String id = Org.dao.save(pid, name, orderid);
		
		Db.execute(new ICallback() {
			@Override
			public Object run(Connection conn) throws SQLException {
				try {
					 conn.prepareCall("{ call pro_show_childLst("+1+") }").execute(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return conn;
			}
		});

		renderText(id);
	}

	
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:修改组织
	 * Created On: 2014年10月28日 上午9:59:20
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit()
	{
		String id = getPara("id");
		String name = getPara("name");
		Org org = Org.dao.findById(id);
		org.set("name", name);
		org.update();
		
		Db.execute(new ICallback() {
			@Override
			public Object run(Connection conn) throws SQLException {
				try {
					 conn.prepareCall("{ call pro_show_childLst("+1+") }").execute(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return conn;
			}
		});
		renderText(org.get("id").toString());
	}

	//@formatter:off 
	/**
	 * Title: delete
	 * Description:删除组织
	 * Created On: 2014年10月28日 上午10:00:06
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void delete()
	{
		String id = getPara("id");
		
		Record orgRd = Db.findFirst("select 1 from sys_org where pid=?",new Object[]{id});
		if(orgRd != null){//有子节点不能删除　
			renderText("org");
			return;
		}
		
		Record shopRd = Db.findFirst("select 1 from bp_shop where org_id=?",new Object[]{id});
		if(shopRd != null){//有商铺不能删除　
			renderText("shop");
			return;
		}
		
		Record userRd = Db.findFirst("select 1 from system_user where org_id=?",new Object[]{id});
		if(userRd != null){//有用户不能删除　
			renderText("user");
			return;
		}
		
		Org.dao.deletes(id);
		
		Db.execute(new ICallback() {
			@Override
			public Object run(Connection conn) throws SQLException {
				try {
					 conn.prepareCall("{ call pro_show_childLst("+1+") }").execute(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return conn;
			}
		});
		renderText(id);
	}
	//@formatter:off 
	/**
	 * Title: orgChoice
	 * Description:组织选择页面
	 * Created On: 2014年11月13日 上午11:47:23
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void orgChoice(){
		setAttr("id", getPara("id"));
		setAttr("orgId", getPara("orgId"));
		setAttr("orgName", getPara("orgName"));
		render("/page/system/org/orgChoice.jsp");	
	}
	
	//@formatter:off 
	/**
	 * Title: shopManage
	 * Description:商铺管理
	 * Created On: 2014年11月17日 下午4:51:04
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void shopManage(){
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
	
	public void update(){
		String ids = getPara("ids");
		String pIds = getPara("pIds");
		
		String sql = "update sys_org set pid="+pIds+" where id in("+ids+")";
		List<String> sqlList = new ArrayList<String>();
		sqlList.add(sql);
		try{
			Db.batch(sqlList, sqlList.size());
			Db.execute(new ICallback() {
				@Override
				public Object run(Connection conn) throws SQLException {
					try {
						 conn.prepareCall("{ call pro_show_childLst("+1+") }").execute(); 
					} catch (Exception e) {
						e.printStackTrace();
					}
					return conn;
				}
			});
			renderJsonResult(true);
		}catch(Exception e){
			renderJsonResult(false);
		}
	}
	//@formatter:off 
	/**
	 * Title: orgMultipleChoice
	 * Description:组织多选
	 * Created On: 2015年1月9日 下午3:25:10
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void orgMultipleChoice(){
		render("/page/common/OrgTreeSelect.jsp");
	}
}
