
package com.yinfu.business.sysupdate.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.Txt;

/**
 * @author JiaYongChao 版本升级实体
 */
@SuppressWarnings("rawtypes")
@TableBind(tableName = "router_script_version")
public class Sysupdate extends Model<Sysupdate> {
	private static final long serialVersionUID = 1L;
	public static Sysupdate dao = new Sysupdate();
	
	//@formatter:off 
		/**
		 * Title: checkVersion
		 * Description:
		 * Created On: 2014年11月25日 下午5:52:11
		 * @author JiaYongChao
		 * <p>
		 * @param number
		 * @return 
		 */
		//@formatter:on
	public JSONObject checkVersion(String number,String fileType) {
		JSONObject returnData = new JSONObject();
		String sql = " select * from router_script_version where version=" + number + " and file_type='"+fileType+"'";
		Sysupdate s = dao.findFirst(sql);
		if(s!=null ){
			returnData.put("state", "error");
			returnData.put("msg", "此版本号已存在");
			return returnData;
		}else{
			String sql2 ="SELECT * FROM router_script_version where file_type='"+fileType+"' ORDER BY VERSION DESC LIMIT 0,1";
			Sysupdate s2 = dao.findFirst(sql2);
			String oldversion = "0";
			if(s2!=null){
				oldversion = s2.get("version");
			}
			if(Float.parseFloat(number)<=Float.parseFloat(oldversion)){
				returnData.put("state", "error");
				returnData.put("msg", "新版本号应小于旧版本号");
				return returnData;
			}
		}
		returnData.put("state", "success");
		return returnData;
	}
	
	//@formatter:off 
		/**
		 * Title: getUpgradeShop
		 * Description:获得当前版本暂未升级的商铺
		 * Created On: 2014年11月26日 下午2:42:14
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public List<Shop> getUpgradeShop(String name) {
		List<Shop> list = new ArrayList<Shop>();
		String sql = " SELECT t.`id`,t.`name`,u.`name` AS username FROM bp_shop t LEFT JOIN SYSTEM_USER u ON t.`owner` = u.`id` WHERE t.`delete_date` IS NULL  ";
		list = Shop.dao.find(sql);
		return list;
	}
	
	//@formatter:off 
	/**
	 * Title: getUpgradeDevice
	 * Description:获得当前版本暂未升级的盒子
	 * Created On: 2014年11月26日 下午4:22:30
	 * @author JiaYongChao
	 * <p>
	 * @param version
	 * @return 
	 */
	//@formatter:on
	public List<Device> getUpgradeDevice(String version,String deviceType) {
		List<Device> list = new ArrayList<Device>();
		if (version == null) {
			String sql = " SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.`type`="+deviceType+" and t.`delete_date` IS NULL  ORDER BY s.`id` ";
			list = Device.dao.find(sql);
		} else {
			
		}
		return list;
	}

	public boolean saveInfo(final Sysupdate sysupdate, final String selectId, final String url) {
		boolean success = false;
		success=Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean status =sysupdate.set("id",Txt.getUuidByJdk(true)).set("res_url",url).set("create_date", new Date()).set("create_user",ContextUtil.getCurrentUserId()).save();
				String id="";
				int type=0;
				int level=0;
				if(status==true){
					id = sysupdate.get("id");
					type = sysupdate.get("type");
					if(type==1){
						String sql ="insert into router_script_update_role (id,version_id,level,related_id,create_date,create_user) values(?,?,?,?,?,?) ";
						int rRows = Db.update(sql,new Object[] {Txt.getUuidByJdk(true),id,null,null,new Date(),ContextUtil.getCurrentUserId()});
						if(rRows>0){
							return true;
						}
					}else{
						String[] reid = selectId.split(",");
						if(type==2){//shop
							level = 5;
						}else if(type==3){
							level = 10;
						}
						int count=0;
						for(int i=0;i<reid.length;i++){
							String sql =" insert into router_script_update_role (id,version_id,level,related_id,create_date,create_user) values(?,?,?,?,?,?) ";
							int rRows = Db.update(sql,new Object[] {Txt.getUuidByJdk(true),id,level,reid[i],new Date(),ContextUtil.getCurrentUserId()});
							if(rRows>0){
								count++;
							}
						}
						if(count==reid.length){
							return true;
						}
					}
				}
				return false;
			}
		});
		return success;
	}

	//@formatter:off 
	/**
	 * Title: findFrist
	 * Description:
	 * Created On: 2014年11月27日 下午2:45:39
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public Sysupdate findFrist(String type) {
		String sql =" SELECT * FROM router_script_version where file_type='"+type+"' ORDER BY VERSION DESC LIMIT 0,1 ";
		return dao.findFirst(sql);
	}
	
	
	//@formatter:off 
	/**
	 * Title: shopUpgrade
	 * Description:
	 * Created On: 2014年11月25日 下午5:52:11
	 * @author JiaYongChao
	 * <p>
	 * @param upType
	 * @return 
	 */
	//@formatter:on
public JSONObject shopUpgrade(String upType) {
	JSONObject returnData = new JSONObject();
	String sql =" SELECT version FROM router_script_version where file_type='"+upType+"' ORDER BY VERSION DESC LIMIT 0,1 ";
	Sysupdate s = dao.findFirst(sql);
	if(s!=null){
		returnData.put("msg", s.getStr("version"));
	}else{
		returnData.put("msg", "初始版本");
	}
	returnData.put("state", "success");
	return returnData;
}
	
}
