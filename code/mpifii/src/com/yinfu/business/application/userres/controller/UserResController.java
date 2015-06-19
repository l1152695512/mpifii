package com.yinfu.business.application.userres.controller;

import com.jfinal.ext.route.ControllerBind;
import com.yinfu.business.application.userres.model.UserRes;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/business/app/userRes",viewPath="")
public class UserResController extends Controller<UserRes>{
	
	public void index(){
		SplitPage splitPages=User.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/userres/userRelationRel.jsp");
	}
	
    /**
     * 转发到多选框树视图页面
     */
    public void resCheckBoxTree(){
    	String userId=getPara("id");
    	setAttr("id", userId);
    	render("/page/business/userres/resCheckBoxTree.jsp");
    }
    
    /**
     *加载菜单树
     */
    public void loadResTree(){
    	String loginUserId=ContextUtil.getCurrentUserId();
    	String userId=getPara("id");
    	String relJson=UserRes.dao.getSysResByUser(userId, loginUserId);
    	renderJson(relJson);
    }
    
    public void saveUserRes(){
    	String userId=getPara("id");
		String treeIds=getPara("treeIds");
		boolean rel=UserRes.dao.saveUserRes(treeIds,userId);
		renderJson(rel);
    }
}
