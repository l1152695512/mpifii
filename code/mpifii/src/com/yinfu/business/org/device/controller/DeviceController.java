
package com.yinfu.business.org.device.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.org.device.model.Device;
import com.yinfu.business.util.DeleteUtils;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.remote.SynAllUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.routersyn.task.SynAllTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/org/device")
public class DeviceController extends Controller<Device> {
	public void index() {
		if(getPara("shopId") != null){
			splitPage.queryParam.put("shop_id", getPara("shopId"));
		}
		SplitPage splitPages = Device.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/system/org/device/index.jsp");
	}
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			Device device = Device.dao.findById(getPara("id"),"id,shop_id,name,type,time_out,des,router_sn");
			setAttr("device", device);
		}else{
			Map m = new HashMap();
			m.put("shop_id", getPara("shopId"));
			setAttr("device", m);
		}
		render("/page/system/org/device/addOrModify.jsp");
	}
	
	public void save() {
		final Device device = getModel();
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
				
				
				final String router_sn = device.getStr("router_sn");
				final String shop_id = device.get("shop_id").toString();
				final String type = device.get("type").toString();
				final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
				
				boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
					if("2".equals(type)){
						return device.saveAndCreateDate();
					}else{
						device.saveAndCreateDate();
						
						List<Record> synList = new ArrayList<Record>();
						synList.add(new Record().set("router_sn", router_sn));
						List<String> sqls = new ArrayList<String>();
						Record taskInfo = new Record().set("task_desc", "发布Portal页面");
						Map<String,List<File>> res = SynAllTask.synRes(shop_id, sqls, taskInfo, null, synList);
						if(null != res){
							SynUtils.putAllFiles(deleteRes, res);
							if(sqls.size()>0){
								DbExt.batch(sqls);
							}
							
							try{
								SynAllUtil synAll = SynAllUtil.getInstance();
								synAll.synAllData(router_sn);
							}catch(Exception e){
								e.printStackTrace();
								return false;
							}
							
							return true;
						}else{
							return false;
						}
					}
				}});
				if(isSuccess){
					SynUtils.deleteRes(deleteRes.get("success"));
				}else{
					SynUtils.deleteRes(deleteRes.get("fail"));
				}
				
				renderJsonResult(isSuccess);
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
	//@formatter:off 
	/**
	 * Title: findByShop
	 * Description:
	 * Created On: 2014年12月2日 上午11:26:46
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void findByShop(){
		String  shopId = getPara("shopId");
		List<Device> list = Device.dao.findByShop(shopId);
		renderJson(list);
	}
	
	
	
}
