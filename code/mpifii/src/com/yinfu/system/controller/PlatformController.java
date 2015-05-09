
package com.yinfu.system.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;
import com.yinfu.Consts;
import com.yinfu.UrlConfig;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.Sec;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.shiro.ShiroCache;
import com.yinfu.system.model.Page;
import com.yinfu.system.model.Platform;
import com.yinfu.system.model.User;
import com.yinfu.system.validator.UserValidator;

@ControllerBind(controllerKey = "/system/platform", viewPath = UrlConfig.SYSTEM)
public class PlatformController extends Controller<Platform> {
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: index
	 * Description: 进入首页
	 * Created On: 2014年8月4日 上午9:47:57
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#index()
	 */
	//@formatter:on
	public void index() {
		SplitPage splitPages = Platform.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		setAttr("isAdmin", ContextUtil.isAdmin());
		render("/page/system/platform/index.jsp");
	}
	
	public void indexdev(){
		SplitPage splitPages = Shop.dao.getShopList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/shop/shopMgtIndex.jsp");
	}
	
	/**
	 * Title: addUser
	 * Description:增加用户
	 * Created On: 2014年8月4日 上午10:01:00
	 * @author JiaYongChao
	 * <p> 
	 * 
	 */
	//@formatter:on
	public void addOrModify() {
		if (StringUtils.isNotBlank(getPara("id"))) {
			Platform user = Platform.dao.findInfoById(getPara("id"));
			setAttr("userInfo", user);
		}
		render("/page/system/platform/addOrModify.jsp");
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 删除
	 * Created On: 2014年8月5日 上午9:22:19
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	public void delete() {
		renderJsonResult(Db.deleteById("bp_platform", getPara("id")));
	}
	
	//@formatter:off 
	/**
	 * Title: freeze
	 * Description:
	 * Created On: 2014年8月5日 上午9:22:25
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void freeze() {
		renderJsonResult(User.dao.changeStaus(getParaToInt("id"), getParaToInt("status")));
	}
	
	//@formatter:on
	public void checkOwner(){
//		String shopId = getPara("id");//商铺ID
//		boolean result = Platform.dao.checkOwner(shopId);
		JSONObject returnData = new JSONObject();
//		if(result== true){
//			returnData.put("state","success");
//			renderJson(returnData);
//		}else{
//			returnData.put("state","error");
//			renderJson(returnData);
//		}
		returnData.put("state","success");
		renderJson(returnData);
		
	}
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存用户信息
	 * Created On: 2014年8月4日 下午6:49:52
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
//		final Platform user = getModel();
		
		if (getPara("id") != null && StringUtils.isNotBlank(getPara("id"))) {
			//id取得
			Platform.dao.updateUserRole(getPara("name"),getPara("down_url"),getPara("auth_url"),getPara("log_ip"),getPara("log_port"),getPara("home_url"),getPara("id"),0);
			renderJsonResult(true);
		}else{
			Platform.dao.updateUserRole(getPara("name"),getPara("down_url"),getPara("auth_url"),getPara("log_ip"),getPara("log_port"),getPara("home_url"),getPara("id"),1);
			renderJsonResult(true);	
		}
	}
	//@formatter:on
	public void configDevice() {
//		String userid = getPara("id");// 用户id
		List<Page> deviceList = Page.dao.findNoUserDeviceInfo();
//		setAttr("infoid", userid);
//		setAttr("type", "user");
		setAttr("deviceList", deviceList);
		render("/system/platform/deviceList.jsp");
		renderJsonResult(true);
	}
	
	
	
}
