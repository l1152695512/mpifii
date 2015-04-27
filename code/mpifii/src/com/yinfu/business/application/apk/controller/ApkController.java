
package com.yinfu.business.application.apk.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.apk.model.Apk;
import com.yinfu.business.application.book.model.Book;
import com.yinfu.business.application.video.model.Video;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/app/apk")
public class ApkController extends Controller<Apk> {
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "apk" + File.separator;
	private static final String APK_PATH = "upload" + File.separator + "apk" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: index
//	 * Description:进入apk配置页面
	 * Created On: 2014年9月25日 下午5:17:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index() {
		SplitPage splitPages = Apk.dao.getApkList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/appcenter/apk/apkIndex.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: add
	 * Description:进入apk增加页面
	 * Created On: 2014年9月25日 下午5:59:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add() {
		List<Record> themeList = Apk.dao.getApkThemes();
		setAttr("themeList", themeList);
		render("/page/business/appcenter/apk/apkAdd.jsp");
	}
	
	//@formatter:off 
		/**
		 * Title: edit
		 * Description:进入apk修改页面
		 * Created On: 2014年9月28日 下午4:58:54
		 * @author JiaYongChao
		 * <p> 
		 */
		//@formatter:on
	public void edit() {
		List<Record> themeList = Apk.dao.getApkThemes();
		setAttr("themeList", themeList);
		Apk apk = Apk.dao.findById(getPara("id"));
		setAttr("apk", apk);
		render("/page/business/appcenter/book/bookAdd.jsp");
	}
	
	//@formatter:off 
		/**
		 * Title: delete
		 * Description:删除apk
		 * Created On: 2014年9月28日 下午5:00:58
		 * @author JiaYongChao
		 * <p> 
		 */
		//@formatter:on
	public void delete() {
		String id = getPara("id");// 主键ID
		Apk apk = Apk.dao.findById(id);
		JSONObject returnData = new JSONObject();
		if (apk.set("delete_date", new Date()).update()) {
			returnData.put("state", "success");
			renderJson(returnData);
		} else {
			returnData.put("state", "error");
			renderJson(returnData);
		}
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存小说信息
	 * Created On: 2014年9月25日 下午6:36:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
		int max = 1024 * 1204 * 1024;
		UploadFile file1 = getFile("apkImage", PathKit.getWebRootPath() + "/" + LOGO_PATH, max);
		UploadFile file2 = getFile("apkInfo", PathKit.getWebRootPath() + "/" + APK_PATH, max);
		String icon = "";
		String link = "";
		if (file1 != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file1, name);
			icon = "upload/image/apk/" +src.getName();
			Fs.copyFileToHome(src, "image", "apk");
		}
		if (file2 != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file2, name);
			link = "upload/file/apk/" +src.getName();
			Fs.copyFileToHome(src, "file", "apk");
		}
		Apk Apk = getModel();
		if (Apk.getId() == null) {// 新增
			renderJsonResult(Apk.set("icon", icon).set("link", link).set("create_date", new Date()).save());
		} else {// 修改
			if (!icon.equals("")) {
				renderJsonResult(Apk.set("icon", icon).set("link", link).update());
			} else {
				renderJsonResult(Apk.update());
			}
		}
	}
	//@formatter:off 
	/**
	 * Title: changeStatus
	 * Description:改变状态
	 * Created On: 2014年10月8日 下午2:23:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void changeStatus() {
		String id = getPara("id");// id
		JSONObject returnData = new JSONObject();
		Apk apk = Apk.dao.findById(id);
		if (id != null) {
			if (apk.getInt("status") == 0) {//未发布(发布)
				if (apk.set("status", 1).update()) {
					if(DataSynUtil.addTask("0", "apk", id, "1")){
						PageUtil.changPageLog("0", "apk", id, "1");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			} else {//已发布(取消发布)
				if (apk.set("status", 0).update()) {
					if(DataSynUtil.addTask("0", "apk", id, "2")){
						PageUtil.changPageLog("0", "apk", id, "3");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			}
			
		} else {
			returnData.put("state", "error");
			renderJson(returnData);
		}
	}
}
