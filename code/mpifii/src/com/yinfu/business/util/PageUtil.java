package com.yinfu.business.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
<<<<<<< HEAD
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
=======

>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
<<<<<<< HEAD
	public static Object getPageIdByShopId(Object shopId){
		Record rec = Db.findFirst("select id from bp_shop_page where shop_id=? ", new Object[]{shopId});
		if(null != rec){
			return rec.get("id");
		}else{
			return "";
		}
	}
	public static Object getTemplateId(Object shopId){
		Record rec = Db.findFirst("select template_id from bp_shop_page where shop_id=? ", new Object[]{shopId});
		if(null != rec){
			return rec.get("template_id");
		}else{
			return "";
		}
	}
	public static Object getTemplateIdByPageId(Object pageId){
		Record rec = Db.findFirst("select template_id from bp_shop_page where id=? ", new Object[]{pageId});
		if(null != rec){
			return rec.get("template_id");
		}else{
			return "";
		}
	}
	public static Object getAdvTemplateId(Object advId){
		Record rec = Db.findFirst("select template_id from bp_adv_type where id=? ", new Object[]{advId});
		if(null != rec){
			return rec.get("template_id");
		}else{
			return "";
		}
	}
	/**
	 * 1.插入商铺认证方式
	 * 2.插入针对该商铺组织设置的广告（未实现，需要考虑怎么实现）
	 * @param shopId
	 * @param orgId
	 * @return
	 */
	public static boolean initShopData(Object shopId,Object orgId){
		if(null != shopId){//插入该商铺的认证方式，如果这里不插入，则该商铺的上网应用就不包含任何认证方式
			Db.update("delete from bp_auth_setting where shop_id=?", new Object[]{shopId});
			List<Record> authTypes = Db.find("select id from bp_auth_type where is_used");
			if(authTypes.size() > 0){
				Object[][] parames = new Object[authTypes.size()][3];
				for(int i=0;i<authTypes.size();i++){
					parames[i][0] = UUID.randomUUID().toString();
					parames[i][1] = shopId;
					parames[i][2] = authTypes.get(i).get("id");
				}
				DbExt.batch("insert into bp_auth_setting(id,shop_id,auth_type_id,create_date) values(?,?,?,now()) ",parames);
			}
			if(null != orgId){
				insertShopOrgAdv(shopId,orgId);
			}
		}
		return true;
	}
	
	public static String queryLike(String srcStr) {
		//适用于sqlserver
//		result = StringUtils.replace(result, "[", "[[]");
//		result = StringUtils.replace(result, "_", "[_]");
//		result = StringUtils.replace(result, "%", "[%]");
//		result = StringUtils.replace(result, "^", "[^]");
		//适用于mysql
		srcStr = StringUtils.replace(srcStr, "\\", "\\\\");
		srcStr = StringUtils.replace(srcStr, "'", "\\'");
		srcStr = StringUtils.replace(srcStr, "_", "\\_");
		srcStr = StringUtils.replace(srcStr, "%", "\\%");
		
		return "%" + srcStr + "%";
	}
	
	/**
	 * 插入该商铺所在组织架构或者父组织架构对广告的编辑数据，
	 * 目前是使用最新编辑的数据，如：两个组织架构上的管理用户都对同一个广告编辑了数据，则使用最近一次设置的数据（数据来自bp_adv_org），
	 * 		以后可能会更改方式，改为以最靠近该商铺的组织设置的数据生效，
	 * 这里需要对每个设置了的广告（每个模板的广告，即bp_adv_type表上的数据）数据都要插入（插入到bp_adv_shop表）
	 * 
	 * 
	 * @return
	 */
	public static void insertShopOrgAdv(Object shopId,Object orgId){
		List<Record> parentOrgs = DataOrgUtil.getParents(orgId, true);
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select bao.adv_spaces,bao.content_id,bac.update_date,0 update_by_shop ");
		sql.append("from bp_adv_org bao join bp_adv_content bac on (bao.content_id=bac.id) ");
		sql.append("where bao.content_id is not null and bao.org_id in ("+DataOrgUtil.recordListToSqlIn(parentOrgs, "id")+") ");
		sql.append("union ");
		sql.append("select bas.adv_spaces,bas.content_id,bac.update_date,bas.update_by_shop ");
		sql.append("from bp_adv_shop bas join bp_adv_content bac on (bas.content_id=bac.id) ");
		sql.append("where bas.shop_id=? and bas.update_by_shop ");
		sql.append("order by update_date desc ");
		sql.append(") a group by adv_spaces desc ");
		List<Record> advs = Db.find(sql.toString(),shopId);
		if(advs.size() > 0){
			Object[][] parames = new Object[advs.size()][5];
			for(int i=0;i<advs.size();i++){
				parames[i][0] = UUID.randomUUID().toString();
				parames[i][1] = shopId;
				parames[i][2] = advs.get(i).get("adv_spaces");
				parames[i][3] = advs.get(i).get("content_id");
				parames[i][4] = advs.get(i).get("update_by_shop");
			}
			Db.update("delete from bp_adv_shop where shop_id=? ", new Object[]{shopId});
			DbExt.batch("insert into bp_adv_shop(id,shop_id,adv_spaces,content_id,update_by_shop,create_date) values(?,?,?,?,?,now()) ",parames);
		}
	}
=======
	
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
}
