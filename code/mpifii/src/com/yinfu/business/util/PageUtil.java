package com.yinfu.business.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class PageUtil {
	
	public static boolean SynRouterAllLog(Object router_sn){
		int changeRows = Db.update("insert into bp_page_operate(router_sn,operate_type,status,operate_date,type,key_id) values(?,?,?,now(),?,?)", 
				new Object[]{router_sn,"2","0","synAll",router_sn});
		if(changeRows > 0){
			return true;
		}
		return false;
	}
	
	 /**
	  * 同步数据
	  * @param shopId
	  * @param type（index_app：应用；index_adv：广告；index_shop:商店；index_temp：模板；index_page：发布）
	  * @param keyId
	  * @param operateType（1：增加；2：修改；3：删除；4：发布）
	  * @return
	  */
	public static boolean changPageLog(String shopId,String type,String keyId,String operateType){
//		if(isPagePublished(shopId)){
			List<String> devices = getDevice(shopId);
			final Object[][] params = new Object[devices.size()][5];
			for(int i=0;i<devices.size();i++){
				params[i] = new Object[]{devices.get(i),operateType,"0",type,keyId};
			}
			return Db.tx(new IAtom(){public boolean run() throws SQLException {
				int[] changeRows = Db.batch("insert into bp_page_operate(router_sn,operate_type,status,operate_date,type,key_id) values(?,?,?,now(),?,?)", params, params.length);
				for(int i=0;i<changeRows.length;i++){
					if(changeRows[i] < 1){
						return false;
					}
				}
				return true;
			}});
//		}else{
//			return true;
//		}
	}
	private static boolean isPagePublished(String shopId){
		Record record = Db.findFirst("select is_publish from bp_shop_page where shop_id=? ", new Object[]{shopId});
		try{
			if("1".equals(record.getStr("is_publish"))){
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}
	private static List<String> getDevice(String shopId){
		List<String> devices = new ArrayList<String>();
		String sql ="select router_sn from bp_device ";
		if(shopId!=null){
			if(shopId=="0"){
				sql+=" where shop_id <> ?";
			}else{
				sql+=" where shop_id=?";
			}
		}
		List<Record> Records = Db.find(sql, new Object[]{shopId});
		Iterator<Record> ite = Records.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			devices.add(rec.getStr("router_sn"));
		}
		return devices;
	}
	
	public static String getShopId(String pageId){
		Record rec = Db.findFirst("select shop_id from bp_shop_page where id=? ", new Object[]{pageId});
		if(null != rec){
			return rec.getInt("shop_id")+"";
		}else{
			return "";
		}
	}
	
}
