package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
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
		formSqlSb.append(" FROM bp_statistics_shop v  ");
		formSqlSb.append(" JOIN bp_search_day sd ON (sd.search_date>='"+startDate+"' AND sd.search_date<='"+endDate+"') ");
		formSqlSb.append(" LEFT JOIN( SELECT s.`id`,s.`name`,pf.`counts` AS counts ,pf.`date` AS dates  FROM bp_shop_pf pf   ");
		formSqlSb.append(" LEFT JOIN bp_shop s ON s.`id` = pf.`shop_id`   WHERE   pf.`date`>='"+startDate+"' AND pf.`date`<='"+endDate+"' GROUP BY s.`id`,pf.`date` ) ps ON ps.id= v.`shopid`  AND ps.dates = sd.search_date  ");
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
			formSqlSb.append(" and  v.orgid IN ("+ordIds+") ");
		}
		formSqlSb.append(" GROUP BY v.shopid,sd.search_date ");
		//formSqlSb.append(" ORDER BY sd.search_date DESC,v.orgname DESC,ps.counts DESC ");
	}
	//@formatter:off 
	/**
	 * Title: downShopStaFile
	 * Description:
	 * Created On: 2015年6月11日 下午3:12:27
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public Render downShopStaFile(Map<String, String> queryMap) {
		String startDate = queryMap.get("startDate");// 开始时间
		String endDate = queryMap.get("endDate");// 结束时间
		String shopId = queryMap.get("shopId");
		String orgId = queryMap.get("orgId");
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}	
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		StringBuilder formSqlSb = new StringBuilder(" SELECT sd.search_date AS days, v.shopid,v.shopname,v.orgname,v.username,v.sbs,v.router,v.ap,");
		formSqlSb.append("CAST(IFNULL(ps.counts,0) AS SIGNED)  AS rs, CAST(IFNULL(sd.zss,0) AS SIGNED) AS zss, CAST( IFNULL(sd.djs,0) AS SIGNED) AS djs, ");
		formSqlSb.append(" IFNULL(ROUND(sd.zss/ps.counts,2),0) AS rjzs,IFNULL(ROUND(sd.djs/sd.zss,2),0) AS djzs");
		formSqlSb.append(" FROM bp_statistics_shop v  ");
		formSqlSb.append(" JOIN bp_search_day sd ON (sd.search_date>='"+startDate+"' AND sd.search_date<='"+endDate+"') ");
		formSqlSb.append(" LEFT JOIN( SELECT s.`id`,s.`name`,pf.`counts` AS counts ,pf.`date` AS dates  FROM bp_shop_pf pf   ");
		formSqlSb.append(" LEFT JOIN bp_shop s ON s.`id` = pf.`shop_id`   WHERE   pf.`date`>='"+startDate+"' AND pf.`date`<='"+endDate+"' GROUP BY s.`id`,pf.`date` ) ps ON ps.id= v.`shopid`  AND ps.dates = sd.search_date  ");
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
			formSqlSb.append(" and  v.orgid IN ("+ordIds+") ");
		}
		formSqlSb.append(" GROUP BY v.shopid,sd.search_date ");
		formSqlSb.append(" ORDER BY sd.search_date DESC,v.orgname DESC,ps.counts DESC ");
		List list=Db.find(formSqlSb.toString());
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"days","orgname","shopname","rs","router","ap","zss","djs","rjzs","djzs"}; 
		String[] heades = {"日期","组织名称","商铺名称","客流","盒子式路由数量","吸顶式路由数量","广告展示次数","广告点击次数","人均广告展示次数","广告展示点击次数"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("shopInfo.xls");
		return excel;
	}
}
