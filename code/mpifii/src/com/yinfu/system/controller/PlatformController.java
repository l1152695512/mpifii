
package com.yinfu.system.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.UrlConfig;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.WareHouse;
import com.yinfu.system.model.Platform;
import com.yinfu.system.model.User;

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
			setAttr("platInfo", user);
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
	 * Description:保存平台信息
	 * Created On: 2014年8月4日 下午6:49:52
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
		final Platform platfrom = getModel();
		boolean success = false;
		if (platfrom.get("id") != null && StringUtils.isNotBlank(platfrom.get("id").toString())) {
			success = platfrom.update();
		}else{
			if (checkPlatNo(platfrom.get("plat_no").toString())) {// 平台编号重复
				renderJson("error", "platNoRepeat");
				return;
			}
			
			platfrom.set("create_date", new Date());
			success = platfrom.save();
		}
		renderJsonResult(success);
	}
	//@formatter:on
	public void configDevice() {
		String platNo = getPara("platNo");// 用户id
		List<WareHouse> deviceList = WareHouse.dao.findNoUserDeviceInfo();
		setAttr("deviceList", deviceList);
		render("/system/platform/deviceList.jsp");
		renderJsonResult(true);
	}
	
	
	public boolean checkPlatNo(String platNo) {
		List<Platform> list = Platform.dao.list("where plat_no=?",new Object[]{platNo});
		return list.size()>0;
	}
	
	
	
}
