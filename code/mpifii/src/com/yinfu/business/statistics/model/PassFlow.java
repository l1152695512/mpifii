
package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.yinfu.model.SplitPage.SplitPage;

public class PassFlow extends Model<PassFlow> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static PassFlow dao = new PassFlow();
	
	//@formatter:off 
	/**
	 * Title: getPassFlowList
	 * Description:客流统计列表
	 * Created On: 2014年12月8日 下午9:20:22
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getPassFlowList(SplitPage splitPage) {
		String sql = "select * ";
		splitPage = splitPageBase(splitPage, sql);
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
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser();
		}
		formSqlSb.append(" from ( SELECT sd.`search_date` AS dates,s.`id`,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,s.`name` AS shopname,IFNULL(pf.`counts`,0) AS counts  	 FROM bp_shop s ");
		formSqlSb.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id` ");
		formSqlSb.append(" LEFT JOIN bp_search_day sd ON (sd.search_date>='" + startDate + "' AND sd.search_date<='" + endDate + "')   ");
		formSqlSb.append(" LEFT JOIN bp_shop_pf pf ON pf.`date`= sd.`search_date` AND pf.`shop_id` = s.`id`  AND  pf.`date`>='"+ startDate + "' AND pf.`date`<='" + endDate + "'");
		formSqlSb.append(" WHERE s.`delete_date` IS NULL  ");
		if (shopId != null) {
			formSqlSb.append(" and s.id IN (" + shopId + ")  ");
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
			formSqlSb.append(" and org.id IN (" + ordIds + ")  ");
		}
		formSqlSb.append(" GROUP BY s.`id`,sd.`search_date`  ");
		formSqlSb.append(" ORDER BY sd.search_date,org.id,pf.`counts` DESC ) a ");
	}
	
	//@formatter:off 
	/**
	 * Title: getPassFlowStaInfo
	 * Description:客流信息统计
	 * Created On: 2015年1月11日 下午12:27:01
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getPassFlowStaInfo(Map<String, String> queryMap) {
		String startDate = queryMap.get("_query.startDate");// 开始时间
		String endDate = queryMap.get("_query.endDate");// 结束时间
		String shopId = queryMap.get("_query.shop_id");
		String orgId = queryMap.get("_query.org_id");
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser();
		}
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT org.id AS id,org.`pid` AS pid,sum(IFNULL(pf.`counts`,0)) AS zs ");
		sql.append(" FROM bp_shop_pf pf  ");
		sql.append(" LEFT JOIN  bp_shop s  ON  pf.shop_id = s.`id`  ");
		sql.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id`   ");
		sql.append(" WHERE  1=1 ");
		sql.append(" AND  pf.`date`>='" + startDate + "' AND pf.`date`<='" + endDate + "' AND org.`id` IS NOT NULL  ");
		if (shopId != null && !shopId.equals("")) {
			sql.append(" and s.id in (" + shopId + ") ");
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
			sql.append(" and org.id in (" + ordIds + ") ");
		}
		sql.append(" GROUP BY org.`id`  ");
		List<Record> zslist = Db.find(sql.toString());
		List<Record> orgList = ContextUtil.getNextListByUser(orgId);
		Map<Object, List<Record>> hashMap = DataOrgUtil.getChildrensForOrgs(orgList, false);
		String caption = startDate + "至" + endDate + "客流统计表(组织)";
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart caption='" + caption + "'  xaxisname='日期' yaxisname='人数' decimalPrecision='10'  theme='fint'>");
		xmlData.append("<categories>");
		for (int i = 0; i < orgList.size(); i++) {
			xmlData.append("<category label='" + orgList.get(i).get("name") + "' />");
		}
		xmlData.append(" </categories>");
		xmlData.append(" <dataset >");
		for (int i = 0; i < orgList.size(); i++) {
			int zs = 0;
			Integer id = orgList.get(i).get("id");
			List<Record> info = hashMap.get(id);
			for (int j = 0; j < zslist.size(); j++) {
				if (zslist.get(j).get("id").equals(id)) {
					zs += Integer.parseInt(zslist.get(j).get("zs").toString());
				} else {
					for (int s = 0; s < info.size(); s++) {
						Integer ids = info.get(s).get("id");
						if (zslist.get(j).get("id").equals(ids)) {
							zs += Integer.parseInt(zslist.get(j).get("zs").toString());
						}
						
					}
				}
			}
			xmlData.append("<set value='" + zs + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" </chart>");
		return xmlData.toString();
	}

	//@formatter:off 
	/**
	 * Title: downPassFlowFile
	 * Description:
	 * Created On: 2015年6月11日 下午4:00:05
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public Render downPassFlowFile(Map<String, String> queryMap) {
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
		StringBuilder formSqlSb = new StringBuilder(" select * ");
		formSqlSb.append(" from ( SELECT sd.`search_date` AS dates,s.`id`,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,s.`name` AS shopname,IFNULL(pf.`counts`,0) AS counts  	 FROM bp_shop s ");
		formSqlSb.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id` ");
		formSqlSb.append(" LEFT JOIN bp_search_day sd ON (sd.search_date>='" + startDate + "' AND sd.search_date<='" + endDate + "')   ");
		formSqlSb.append(" LEFT JOIN bp_shop_pf pf ON pf.`date`= sd.`search_date` AND pf.`shop_id` = s.`id`  AND  pf.`date`>='"+ startDate + "' AND pf.`date`<='" + endDate + "'");
		formSqlSb.append(" WHERE s.`delete_date` IS NULL  ");
		if (shopId != null && !shopId.equals("")) {
			formSqlSb.append(" and s.id IN (" + shopId + ")  ");
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
			formSqlSb.append(" and org.id IN (" + ordIds + ")  ");
		}
		formSqlSb.append(" GROUP BY s.`id`,sd.`search_date`  ");
		formSqlSb.append(" ORDER BY sd.search_date,org.id,pf.`counts` DESC ) a ");
		List list=Db.find(formSqlSb.toString());
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"dates","orgname","shopname","counts"}; 
		String[] heades = {"日期","组织名称","商铺名称","人数"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("passFlowInfo.xls");
		return excel;
	}
}
