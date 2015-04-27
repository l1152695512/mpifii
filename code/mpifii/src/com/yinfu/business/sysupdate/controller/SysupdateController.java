
package com.yinfu.business.sysupdate.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.sysupdate.model.Sysupdate;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Txt;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/business/sysupdate")
public class SysupdateController extends Controller<Sysupdate> {
	private static final String PATH = "upload" + File.separator +"file" + File.separator + "backpage" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: index
	 * Description:进入版本升级首页
	 * Created On: 2014年11月25日 下午4:29:55
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index() {
		Sysupdate info = Sysupdate.dao.findFrist("script");
		setAttr("info", info);
		render("/page/system/version/versionup.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存升级包信息
	 * Created On: 2014年11月25日 下午5:13:28
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
		int max = 1024 * 1204 * 1024;
		UploadFile file = getFile("versionPack", PathKit.getWebRootPath() + "/" + PATH, max);
		String url = "";
		if (file != null) {
			String name = String.valueOf(System.currentTimeMillis());
//			File src = ImageKit.renameFile(file, name);
			url = "upload/file/backpage/" + file.getFile().getName();
			Fs.copyFileToHome(file.getFile(), "file", "backpage");
		}
		Sysupdate sysupdate = getModel();
		String selectId = getPara("selectId");
		boolean success = false;
		success = Sysupdate.dao.saveInfo(sysupdate,selectId,url);
		renderJsonResult(success);
	}
	
	//@formatter:off 
	/**
	 * Title: checkVersion
	 * Description:检查升级版本号
	 * Created On: 2014年11月25日 下午5:13:50
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public void checkVersion() {
		String number = getPara("version");
		String fileType = getPara("fileType");
		JSONObject returnData = Sysupdate.dao.checkVersion(number,fileType);
		renderJson(returnData);
	}
	
	//@formatter:off 
	/**
	 * Title: shopUpgrade
	 * Description:商铺升级
	 * Created On: 2014年11月26日 上午11:12:46
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void shopUpgrade() {
		String name = getPara("name");
		List<Shop> shopList = Sysupdate.dao.getUpgradeShop(name);
		setAttr("shopList", shopList);
		render("/page/system/version/selectUpShop.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: shopUpgrade
	 * Description:盒子升级
	 * Created On: 2014年11月26日 上午11:12:46
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void deviceUpgrade() {
		String version = getPara("version");
		String deviceType = getPara("deviceType");
		List<Device> deviceList = Sysupdate.dao.getUpgradeDevice(version,deviceType);
		setAttr("deviceList", deviceList);
		setAttr("deviceType", deviceType);
		render("/page/system/version/selectUpDevice.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: showUpVserion
	 * Description:版本提示
	 * Created On: 2014年11月26日 上午11:12:46
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void showUpVserion() {
		String upType = getPara("upType");
		JSONObject returnData = Sysupdate.dao.shopUpgrade(upType);
		renderJson(returnData);
	}
}
