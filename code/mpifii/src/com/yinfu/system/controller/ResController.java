package com.yinfu.system.controller;

import java.util.List;
import com.jfinal.ext.route.ControllerBind;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.model.tree.Tree;
import com.yinfu.system.model.Res;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/system/res")
public class ResController extends Controller<Res>
{
	//@formatter:off 
	/**
	 * Title: index
	 * Description:菜单管理首页
	 * Created On: 2014年10月14日 下午3:07:48
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index(){
		SplitPage splitPages = Res.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/system/menu/index.jsp");
	}
	
	
	
	public void tree()
	{
		Integer pid = getParaToInt("id");
		Integer passId = getParaToInt("passId");
		int type = getParaToInt("type", Res.TYPE_MEUE);
		renderJson(Res.dao.getTree(pid, type, passId));

	}
	
	public void menu(){
		String userId = getAttr("userid");
		User user = User.dao.findById(userId);
		List<Tree> menuList = Res.dao.getTree(null,  Res.TYPE_MEUE, null);
		setAttr("menuList", menuList);
		render("/page/common/menu.jsp");
	}
	public void list()
	{
		renderJson(Res.dao.listOrderBySeq());
	}

	public void delete()
	{
		renderJsonResult(Res.dao.deleteByIdAndPid(getParaToInt("id")));

	}

	//@formatter:off 
	/**
	 * Title: add
	 * Description:增加菜单
	 * Created On: 2014年11月11日 下午9:18:44
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add()
	{
		render("/page/system/menu/menuAdd.jsp");
	}

	//@formatter:off 
	/**
	 * Title: edit
	 * Description:修改菜单
	 * Created On: 2014年11月11日 下午9:21:51
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit()
	{
		String id = getPara("id");
		Res res =  Res.dao.findById(id);
		setAttr("res", res);
		render("/page/system/menu/menuAdd.jsp");
	}
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存菜单
	 * Created On: 2014年11月12日 下午2:20:20
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		Res res = getModel();
		boolean result=false;
		try {
			if (res.get("id") == null) {// 新增
				result = res.save();
				renderJsonResult(result);
			} else {// 修改
				result = res.update();
				renderJsonResult(result);
			}
		} catch (Exception e) {
			renderJsonResult(result);
			e.printStackTrace();
		}
	}
	//@formatter:off 
	/**
	 * Title: treeData
	 * Description:菜单功能树
	 * Created On: 2014年11月17日 下午5:59:04
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void treeData(){
		String moduleIds = getPara("moduleIds");
		String json = Res.dao.childNodeData(moduleIds);
		renderJson(json);
	}
	
	
}
