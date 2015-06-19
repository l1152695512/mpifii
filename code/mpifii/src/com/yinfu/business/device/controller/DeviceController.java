
package com.yinfu.business.device.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.page.model.Page;
import com.yinfu.business.util.DeleteUtils;
import com.yinfu.common.Result;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/device")
public class DeviceController extends Controller<Device> {
	private List<File> daoru = new ArrayList<File>();
	
	private List<String> daoruFileName = new ArrayList<String>();
	
	public List<File> getDaoru() {
		return daoru;
	}

	public void setDaoru(List<File> daoru) {
		this.daoru = daoru;
	}

	public List<String> getDaoruFileName() {
		return daoruFileName;
	}

	public void setDaoruFileName(List<String> daoruFileName) {
		this.daoruFileName = daoruFileName;
	}

	public void index() {
		SplitPage splitPages = Page.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/device/index.jsp");
	}
	
	public void addOrModify() {
		if (StringUtils.isNotBlank(getPara("id"))) {
			Device device = Device.dao.findById(getPara("id"), "id,name,type,time_out,des,router_sn");
			setAttr("deviceInfo", device);
		}
		render("/page/business/device/addOrModify.jsp");
	}
	
	public void save() {
		Device device = getModel();
		if (null == device.get("time_out") || StringUtils.isBlank(device.get("time_out").toString())) {
			device.set("time_out", 120);
		}
		if (device.get("id") != null && StringUtils.isNotBlank(device.get("id").toString())) {
			renderJsonResult(device.update());
		} else {
			if (checkSN(device.get("router_sn").toString())) {
				renderJson("error", "snRepeat");
			} else {
				device.set("name", "");
				device.set("remote_account", device.get("router_sn").toString() + "@pifii.com");
				device.set("remote_pass", PropertyUtils.getProperty("router.password.default", "2014@pifii.com-yinfu"));
				renderJsonResult(device.saveAndCreateDate());
			}
		}
	}
	
	private boolean checkSN(String routersn) {
		Record rec = Db.findFirst("select id from bp_device where router_sn=? ", new Object[] { routersn });
		return rec != null;
	}
	
	public void delete() {
		boolean success = Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				DeleteUtils utils = new DeleteUtils();
				Record device = Db.findFirst("select router_sn from bp_device where id=?", new Object[] { getPara("id") });
				if (null != device) {
					List<Object> routerSNs = new ArrayList<Object>();
					routerSNs.add(device.get("router_sn"));
					List<String> sqls = new ArrayList<String>();
					utils.deleteDevice(routerSNs, sqls, true);
					if (sqls.size() > 0) {
						DbExt.batch(sqls);
					}
				}
				return true;
			}
		});
		renderJsonResult(success);
	}
	
	//@formatter:off 
	/**
	 * Title: svaeConfigDevice
	 * Description:
	 * Created On: 2014年9月24日 下午7:03:08
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void svaeConfigDevice() {
		String ids = getPara("ids");
		String type = getPara("type");// 配置设备的类型(商铺配置设备，商户配置设备)
		String id = getPara("id");// shopid或者userid
		renderJsonResult(Device.dao.svaeConfigDevice(ids, type, id));
	}
	
	public void findByName() {
		String name = getPara("name");
		String deviceType = getPara("deviceType");
		List<Device> list = Device.dao.findInfoByName(name, deviceType);
		renderJson(list);
	}
	
	//@formatter:off 
	/**
	 * Title: findByShop
	 * Description:
	 * Created On: 2014年12月2日 上午11:26:46
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void findByShop() {
		String shopId = getPara("shopId");
		List<Device> list = Device.dao.findByShop(shopId);
		renderJson(list);
	}
	
	//@formatter:off 
	/**
	 * Title: deviceUpdate
	 * Description:盒子升级
	 * Created On: 2015年1月6日 下午2:54:34
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void deviceUpdate() {
		String deviceId = getPara("id");// 设备ID
		String state = Device.dao.deviceUpdate(deviceId);
		renderJson(state);
	}
	
	//@formatter:off 
	/**
	 * Title: checkUpdate
	 * Description:检查此盒子是否可以升级
	 * Created On: 2015年1月6日 下午2:57:58
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void checkUpdate() {
		String deviceId = getPara("id");// 设备ID
		JSONObject state = Device.dao.checkUpdate(deviceId);
		renderJson(state);
	}
	
	//@formatter:off 
    /**
     * Title: importFile
     * Description:盒子导入
     * Created On: 2015年1月14日 下午2:13:46
     * @author JiaYongChao
     * <p>
     * @return 
     */
    //@formatter:on
	public void importFile() {
		render("/page/business/device/importDevice.jsp");
	}
	
	//@formatter:off 
    /**
     * Title: save
     * Description:保存导入数据
     * Created On: 2015年1月14日 下午2:14:16
     * @author JiaYongChao
     * <p> 
     */
    //@formatter:on
	public void saveImport() {
		UploadFile ufile = getFile("daoru");
		if(ufile!=null){
			File file = ufile.getFile();
			daoru.add(file);
			String fileName = file.getName();
			daoruFileName.add(fileName);
		}
		Result result = Device.dao.importAD(daoru, daoruFileName, getRequest());
		if (!result.getState().equals(Result.SUCCESS)) {
			setAttr("total", 1);
			setAttr("progress", 1);
		}
		renderJson(result);
	}
	
}
