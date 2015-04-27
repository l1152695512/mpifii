package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.model.SplitPage.SplitPage;

public class ShopSta extends Model<ShopSta> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ShopSta dao = new ShopSta();
	//@formatter:off 
	/**
	 * Title: getDeviceList
	 * Description:
	 * Created On: 2014年12月4日 下午5:28:02
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getShopList(SplitPage splitPage) {
		String sql = "SELECT sd.search_date AS days, v.shopid,v.shopname,v.orgname,v.username,v.sbs,v.router,v.ap,";
		sql+=" CAST(IFNULL(ps.counts,0) AS SIGNED)  AS rs, CAST(IFNULL(sd.zss,0) AS SIGNED) AS zss, CAST( IFNULL(sd.djs,0) AS SIGNED) AS djs  ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}	
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		formSqlSb.append(" FROM view_shop_statistics v  ");
		formSqlSb.append(" JOIN bp_search_day sd ON (sd.search_date>='"+startDate+"' AND sd.search_date<='"+endDate+"') ");
		formSqlSb.append(" LEFT JOIN( SELECT s.`id`,s.`name`,SUM(pf.`counts`) AS counts ,pf.`date` AS dates FROM bp_statistics_pf pf  LEFT JOIN bp_device d ON d.`router_sn` = pf.`router_sn` ");
		formSqlSb.append(" LEFT JOIN bp_shop s ON s.`id` = d.`shop_id`  WHERE   pf.`date`>='"+startDate+"' AND pf.`date`<='"+endDate+"' GROUP BY s.`id`,pf.`date` ) ps ON ps.id= v.`shopid`  AND ps.dates = sd.search_date  ");
		formSqlSb.append(" LEFT JOIN ( SELECT adv.`shopId`,adv.`zss`,adv.`djs`,adv.`dates`  FROM bp_statistics_adv adv    ");
		formSqlSb.append(" WHERE 1=1 and  adv.`dates`>='"+startDate+"' AND adv.`dates`<='"+endDate+"'  ");
		formSqlSb.append("  GROUP BY adv.`shopId`,adv.`dates`) AS sd ON sd.shopId = v.`shopid` AND sd.dates= sd.search_date where 1=1 ");
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" and v.shopid IN ("+shopId+")  ");
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
			formSqlSb.append(" and  v.orgid IN ("+ordIds+")  ");
		}
		formSqlSb.append(" GROUP BY v.shopid,sd.search_date ");
		formSqlSb.append(" ORDER BY sd.search_date DESC,v.orgname DESC,ps.counts DESC ");
	}
}
