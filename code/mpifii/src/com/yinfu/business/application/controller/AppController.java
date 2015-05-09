package com.yinfu.business.application.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sun.org.apache.xpath.internal.operations.Gte;
import com.yinfu.business.application.model.App;
import com.yinfu.business.application.model.Coupon;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/app")
public class AppController extends Controller<App>
{
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "app" + File.separator;
	//@formatter:off 
	/**
	 * Title: index
	 * Description:进入应用中心首页
	 * Created On: 2014年9月25日 上午10:36:26
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index(){
		SplitPage splitPages = App.dao.getAppList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/appcenter/appCenterIndex.jsp");
	}
	//@formatter:off 
	/**
	 * Title: add
	 * Description:应用添加
	 * Created On: 2014年9月25日 上午11:49:16
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add(){
		List<Record> classifyList = App.dao.getAppClass();
		setAttr("classifyList", classifyList);
		render("/page/business/appcenter/appCenterAdd.jsp");
	}
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:应用修改
	 * Created On: 2014年9月25日 上午11:51:18
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit(){
		List<Record> classifyList = App.dao.getAppClass();
		setAttr("classifyList", classifyList);
		App app = App.dao.findById(getPara("id"));
		setAttr("app",app);
		render("/page/business/appcenter/appCenterAdd.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:应用信息保存
	 * Created On: 2014年9月25日 上午11:51:16
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		UploadFile file = getFile("appImage",PathKit.getWebRootPath() + "/" +LOGO_PATH);
		String icon = "";
		if(file!=null){
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file, name);
			icon = "upload/image/app/" + src.getName();
			Fs.copyFileToHome(src, "image", "app");
		}
		App app = getModel();
		if (app.getId() == null) {//新增
			renderJsonResult(app.set("icon", icon).set("create_date",new Date()).save());
		}else{//修改
			if(!icon.equals("")){
				renderJsonResult(app.set("icon", icon).update());
			}else{
				renderJsonResult(app.update());
			}
		}
	}
	
	//@formatter:off 
	/**
	 * Title: configInfo
	 * Description:配置信息
	 * Created On: 2014年9月25日 下午4:15:10
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void configInfo(){
		String id = getPara("id");//主键ID
		App app = App.dao.findById(id);
		String url=  app.get("configurl");
		JSONObject returnData = new JSONObject();
		if(url==null){
			returnData.put("state","error");
			renderJson(returnData);
		}else{
			returnData.put("state",url);
			renderJson(returnData);
		}
	}
	
	//@formatter:off 
	/**
	 * Title: delete
	 * Description:应用信息删除
	 * Created On: 2014年9月28日 上午11:03:49
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void delete(){
		String id = getPara("id");//主键ID
		App app = App.dao.findById(id);
		JSONObject returnData = new JSONObject();
		if(app.set("delete_date", new Date()).update()){
			returnData.put("state","success");
			renderJson(returnData);
		}else{
			returnData.put("state","error");
			renderJson(returnData);
		}
	}
}
