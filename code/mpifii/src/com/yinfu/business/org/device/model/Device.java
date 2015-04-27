
package com.yinfu.business.org.device.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.remote.SynAllUtil;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * 设备实体
 * 
 * @author JiaYongChao 2014年7月23日
 */
@TableBind(tableName = "bp_device")
public class Device extends Model<Device> {
	private static final long serialVersionUID = 1L;
	public static Device dao = new Device();
	
	public SplitPage findList(SplitPage splitPage) {
		String sql = "select s.name shop_name,d.id,d.name,d.type,d.time_out,d.router_sn,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_device d ");
		formSqlSb.append("left join bp_shop s on (d.shop_id=s.id) ");
		formSqlSb.append("where d.delete_date is null  ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 2 && "like".equalsIgnoreCase(keyInfo[2])){
					formSqlSb.append("and d."+keyInfo[0]+"_"+keyInfo[1]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and d."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
	}
	
	//@formatter:off 
		/**
		 * Title: findNoShopDeviceInfo
		 * Description:获得没有关联商铺的设备信息
		 * Created On: 2014年9月24日 下午2:38:07
		 * @author JiaYongChao
		 * <p>
		 * @param userid 
		 * @param 
		 * @return 
		 */
		//@formatter:on
	public List<Device> findNoShopDeviceInfo(String userid) {
		String sql = " select * from bp_device t where 1=1 and t.delete_date is null  and t.shop_id is null and t.create_user=" + userid;
		return dao.find(sql);
	}
	
	//@formatter:off 
		/**
		 * Title: findNoUserDeviceInfo
		 * Description:获得没有关联商户的设备信息
		 * Created On: 2014年9月24日 下午6:03:34
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public List<Device> findNoUserDeviceInfo() {
		String sql = "  select * from bp_device t where 1=1 and t.delete_date is null and t.shop_id is null and t.create_user is null  ";
		return dao.find(sql);
	}
	
	public List<Record> findDeviceWidthShopOrNoAssign(String shopId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct if(d.shop_id=?,1,0) isMe,d.* ");
		sql.append("from bp_device d left join system_user u on (d.create_user=u.id) left join bp_shop s on (s.id=? and s.`owner`=u.id) ");
		sql.append("where d.delete_date is null and ");// 未删除的盒子
		sql.append("(");
		sql.append("(d.shop_id is null and d.create_user is null) ");// 未分配的盒子
		sql.append("or d.shop_id=? ");// 当前商铺已分配的盒子（必须放在下面的条件前面）
		sql.append("or (d.shop_id is null and s.id is not null) ");// 当前商铺对应的用户的所有盒子
		sql.append(")");
		return Db.find(sql.toString(), new Object[] { shopId, shopId, shopId });
	}
	
	//@formatter:off 
		/**
		 * Title: svaeConfigDevice
		 * Description:报存配置信息
		 * Created On: 2014年9月24日 下午7:20:55
		 * @author JiaYongChao
		 * <p>
		 * @param ids设备id集合
		 * @param type shop或者user
		 * @param id shopid或者userid
		 * @return 
		 */
		//@formatter:on
	public boolean svaeConfigDevice(String ids, String type, String id) {
		if (ids != null && type != null) {
			List<String> sqlList = new ArrayList<String>();
			if (type.equals("shop")) {// 更新商铺设备关联
				String sql = " update bp_device t set t.shop_id=" + id + " where t.id in(" + ids + ")";
				sqlList.add(sql);
				Db.batch(sqlList, sqlList.size());
				
				List<Record> list = Db.find("select router_sn from bp_device where id in(" + ids + ")");
				for (Record rd : list) {
					SynAllUtil synAll = SynAllUtil.getInstance();
					synAll.synAllData(rd.getStr("router_sn"));
				}
			} else {
				String sql = " update bp_device t set t.create_user=" + id + "  where t.id in(" + ids + ")";
				sqlList.add(sql);
				Db.batch(sqlList, sqlList.size());
			}
			return true;
		}
		return false;
	}
	
	//@formatter:off 
		/**
		 * Title: findListByShopId
		 * Description:通过商铺ID获得设备列表
		 * Created On: 2014年9月29日 下午3:00:26
		 * @author JiaYongChao
		 * <p>
		 * @param shopid
		 * @param userid
		 * @return 
		 */
		//@formatter:on
	public List<Device> findListByShopId(String shopid, String userid) {
		if (shopid != null) {
			String sql = "SELECT * FROM bp_device d WHERE d.`delete_date` IS NULL and d.shop_id=" + shopid;
			return dao.find(sql);
		}
		return new ArrayList<Device>();
	}
	
	//@formatter:off 
		/**
		 * Title: findListByUserId
		 * Description:通过userid获得设备列表
		 * Created On: 2014年9月29日 下午3:56:22
		 * @author JiaYongChao
		 * <p>
		 * @param userid
		 * @return 
		 */
		//@formatter:on
	public List<Device> findListByUserId(String userid) {
		if (userid != null) {
			StringBuffer sql = new StringBuffer(" SELECT d.name,d.router_sn,d.mac,IF(s.`name` IS NULL,'暂未绑定商铺',s.`name`) AS shopname  ");
			sql.append(" FROM bp_device d ");
			sql.append(" LEFT JOIN bp_shop s ON d.`shop_id` =s.`id` ");
			sql.append(" WHERE d.`delete_date` IS NULL  ");
			sql.append(" and d.create_user=" + userid);
			return dao.find(sql.toString());
		}
		return new ArrayList<Device>();
	}
	
	public List<Device> findInfoByName(String name,String deviceType) {
		String sql = "  SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.type="+deviceType+" and t.`delete_date` IS NULL ";
		if (name != null || !name.equals("")) {
			sql += " AND t.router_sn LIKE '%" + name + "%' ";
		}
		return dao.find(sql);
	}

	//@formatter:off 
	/**
	 * Title: findAll
	 * Description:查询全部盒子
	 * Created On: 2014年12月2日 上午10:45:24
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Device> findAll() {
		String sql  = "  SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.`delete_date` IS NULL ";
		if(!ContextUtil.isAdmin()){
			sql+=" and t.create_user="+ContextUtil.getCurrentUserId();
		}
		return dao.find(sql);
	}

	//@formatter:off 
	/**
	 * Title: findByShop
	 * Description:通过shopId查询设备
	 * Created On: 2014年12月2日 上午11:31:00
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public List<Device> findByShop(String shopId) {
		String sql  = "  SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.`delete_date` IS NULL ";
		if(!ContextUtil.isAdmin()){
			sql+=" and t.create_user="+ContextUtil.getCurrentUserId();
		}
		if(shopId!=null && !shopId.equals("")){
			sql+=" and t.shop_id="+shopId;
		}
		return dao.find(sql);
	}
}
