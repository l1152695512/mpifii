
package com.yinfu.business.device.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
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
		String sql = "select IF(u.id is null,'未分配',u.name) user_name,IF(s.id is null,IF(u.id is null,'','未指定商铺'),s.name) shop_name,d.id,d.name,d.time_out,d.router_sn,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_device d ");
		formSqlSb.append("left join bp_shop s on (d.shop_id=s.id) ");
		formSqlSb.append("left join system_user u on (u.id=d.user_id) ");
		formSqlSb.append("where d.delete_date is null  ");
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
		String sql = " select * from bp_device t where 1=1 and t.delete_date is null  and t.shop_id is null   and t.user_id=" + userid;
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
		String sql = "  select * from bp_device t where 1=1 and t.delete_date is null and t.shop_id is null and t.user_id is null  ";
		return dao.find(sql);
	}
	
	public List<Record> findDeviceWidthShopOrNoAssign(String shopId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct if(d.shop_id=?,1,0) isMe,d.* ");
		sql.append("from bp_device d left join system_user u on (d.user_id=u.id) left join bp_shop s on (s.id=? and s.`owner`=u.id) ");
		sql.append("where d.delete_date is null and ");// 未删除的盒子
		sql.append("(");
		sql.append("(d.shop_id is null and d.user_id is null) ");// 未分配的盒子
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
				String sql = " update bp_device t set t.user_id=" + id + "  where t.id in(" + ids + ")";
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
			sql.append(" and d.user_id=" + userid);
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
}
