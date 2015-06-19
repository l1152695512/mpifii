
package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.model.SplitPage.SplitPage;

public class Statistics extends Model<Statistics> {
	private static final long serialVersionUID = 1L;
	public static Statistics dao = new Statistics();
	
	public List<Record> getShopList(String userId, String type, String ids) {
		String sql = "select id,name from bp_shop where 1=1 ";
		if (!ContextUtil.isAdmin()) {
			sql += " and user_id=" + ContextUtil.getCurrentUserId();
		}
		
		if ("".equals(ids) || ids == null) {
			if ("in".equals(type)) {
				sql += " and id is null";
			}
		} else {
			sql += " and id " + type + " (" + ids + ")";
		}
		List<Record> list = Db.find(sql);
		return list;
	}
	
	public SplitPage getShopList(SplitPage splitPage) {
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔,要考虑网络延迟
		String sql = "select d.*,s.name shopname,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,if(date_add(report_date, interval "
				+ interval + " second) > now(),online_num,-1) online_num,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔,要考虑网络延迟
		String type = queryParam.get("type");// 开始时间
		String orgId = queryParam.get("org_id");
		String shopId = queryParam.get("shop_id");
		String router_sn = queryParam.get("router_sn");
		String dtype = queryParam.get("d_type");
		String shop_type = queryParam.get("shop_type");
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser();
		}
		formSqlSb.append(" from bp_device d ");
		formSqlSb.append(" left join bp_shop s on d.shop_id = s.id ");
		formSqlSb.append(" left join sys_org_temp org ON s.`org_id` = org.`id` ");
		formSqlSb.append(" where 1=1 and d.delete_date is null and s.delete_date is null  ");
		if (shopId != null) {
			formSqlSb.append("and s.id IN (" + shopId + ")  ");
		}
		if (orgId != null && !orgId.equals("")) {
			List<Record> orgList = new ArrayList<Record>();
			for (String oid : orgId.split(",")) {
				List<Record> resultList = DataOrgUtil.getChildrens(oid, true);
				orgList.addAll(resultList);
			}
			HashSet h = new HashSet(orgList);
			orgList.clear();
			orgList.addAll(h);
			String ordIds = DataOrgUtil.recordListToSqlIn(orgList, "id");
			formSqlSb.append("and org.id IN (" + ordIds + ")  ");
		}
		if (type != null) {
			if (type.equals("0")) {// 在线
				formSqlSb.append(" and if(date_add(report_date, interval " + interval + " second) > now(),online_num,-1)>=0 ");
			} else if (type.equals("1")) {// 离线
				formSqlSb.append(" and if(date_add(report_date, interval " + interval + " second) > now(),online_num,-1)<0 ");
			}
		}
		if(shop_type!=null){
			if(shop_type.equals("0")){
				//未绑定商铺的
				formSqlSb.append(" and d.shop_id is null ");
			}else if(shop_type.equals("1")){
				//绑定商铺的
				formSqlSb.append(" and d.shop_id is not null  ");
			}
		}
		if (router_sn != null ) {
			formSqlSb.append(" and d.router_sn like '%"+router_sn+"%' ");
		}
		if (dtype != null ) {
			if (dtype.equals("1")) {// 盒子
				formSqlSb.append(" and d.type=1 ");
			} else if (dtype.equals("2")) {// AP
				formSqlSb.append(" and d.type=2 ");
			}
		}
		//formSqlSb.append(" order by org.id desc,if(date_add(report_date, interval " + interval + " second) > now(),online_num,-1) desc");
	}
	
	//@formatter:off 
	/**
	 * Title: getDeviceStaInfo
	 * Description:获得设备统计信息
	 * Created On: 2014年12月4日 下午3:44:07
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public String getDeviceStaInfo(Map<String, String> queryMap) {
		String orgId = queryMap.get("_query.org_id");
		String shopId = queryMap.get("_query.shop_id");
		String shop_type = queryMap.get("_query.shop_type");
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser();
		}
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔,要考虑网络延迟
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart palette='4' decimals='0' showValues='1' labelSepChar=':' enableSmartLabels='1' enableRotation='0' formatNumberScale='0'  bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='0' baseFontSize='20' startingAngle='80' >");
		String sql = "SELECT IFNULL(SUM(CASE WHEN s.online_num=-1 THEN 1 ELSE 0 END),0) AS noOnline,IFNULL(SUM(CASE WHEN s.online_num=-1 THEN 0 ELSE 1 END),0) AS isOnline  from ";
		sql += " (SELECT IF(DATE_ADD(d.report_date, INTERVAL "
				+ interval
				+ " SECOND) > NOW(),d.online_num,-1) online_num FROM bp_device d left JOIN bp_shop s ON (d.shop_id = s.id) LEFT JOIN sys_org org ON s.`org_id` = org.`id` where 1=1 and d.delete_date is null   ";
		if(shop_type!=null){
			if(shop_type.equals("0")){
				//未绑定商铺的
				sql +=" and d.shop_id is null ";
			}else if(shop_type.equals("1")){
				//绑定商铺的
				sql +=" and d.shop_id is not null  ";
			}
		}
		if (shopId != null && !shopId.equals("")) {
			sql += " and s.id in (" + shopId + ") ";
		}
		if (orgId != null && !orgId.equals("")) {
			List<Record> orgList = new ArrayList<Record>();
			for (String oid : orgId.split(",")) {
				List<Record> resultList = DataOrgUtil.getChildrens(oid, true);
				orgList.addAll(resultList);
			}
			HashSet h = new HashSet(orgList);
			orgList.clear();
			orgList.addAll(h);
			String ordIds = DataOrgUtil.recordListToSqlIn(orgList, "id");
			sql += " and org.id in (" + ordIds + ") ";
		}
		sql += " ) s";
		Record r = Db.findFirst(sql);
		xmlData.append("<set label='在线' color='504F05' value='" + r.get("isOnline") + "' />");
		xmlData.append("<set label='离线' color='CF9292'  value='" + r.get("noOnline") + "' />");
		xmlData.append("</chart>");
		return xmlData.toString();
	}
}
