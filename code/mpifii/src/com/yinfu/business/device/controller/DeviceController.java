
package com.yinfu.business.device.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.page.model.Page;
import com.yinfu.business.util.DeleteUtils;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/device")
public class DeviceController extends Controller<Device> {
	public void index() {
		SplitPage splitPages = Page.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/device/index.jsp");
	}
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			Device device = Device.dao.findById(getPara("id"),"id,name,type,time_out,des,router_sn");
			setAttr("deviceInfo", device);
		}
		render("/page/business/device/addOrModify.jsp");
	}
	
	public void save() {
		Device device = getModel();
		if(null == device.get("time_out") || StringUtils.isBlank(device.get("time_out").toString())){
			device.set("time_out", 120);
		}
		if(device.get("id") != null && StringUtils.isNotBlank(device.get("id").toString())){
			renderJsonResult(device.update());
		}else{
			if(checkSN(device.get("router_sn").toString())){
				renderJson("error", "snRepeat");
			}else{
				device.set("name", "");
				renderJsonResult(device.saveAndCreateDate());
			}
		}
	}
	
	private boolean checkSN(String routersn){
		Record rec = Db.findFirst("select id from bp_device where router_sn=? ", new Object[]{routersn});
		return rec != null;
	}
	
	public void delete(){
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			DeleteUtils utils = new DeleteUtils();
			Record device = Db.findFirst("select router_sn from bp_device where id=?", new Object[]{getPara("id")});
			if(null != device){
				List<Object> routerSNs = new ArrayList<Object>();
				routerSNs.add(device.get("router_sn"));
				List<String> sqls = new ArrayList<String>();
				utils.deleteDevice(routerSNs, sqls, true);
				if(sqls.size() > 0){
					DbExt.batch(sqls);
				}
			}
			return true;
		}});
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
	public void svaeConfigDevice(){
		String ids = getPara("ids");
		String type =  getPara("type");//配置设备的类型(商铺配置设备，商户配置设备)
		String id = getPara("id");//shopid或者userid
		renderJsonResult(Device.dao.svaeConfigDevice(ids,type,id));
	}
	public void findByName(){
		String name = getPara("name");
		String deviceType = getPara("deviceType");
		List<Device> list = Device.dao.findInfoByName(name,deviceType);
		renderJson(list);
	}
}
