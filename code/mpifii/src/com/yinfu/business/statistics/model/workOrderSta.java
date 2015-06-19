
package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

public class workOrderSta extends Model<workOrderSta> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final workOrderSta dao = new workOrderSta();
	
	private static final Log log = LogFactory.getLog(workOrderSta.class);
	
	public SplitPage wordOrderStatis(SplitPage splitPage) {
		int totalRow = (int) getTotalRow(splitPage.getQueryParam());
		List pagelist = new ArrayList();
		List<Record> wolist = getWordOrderDatas(splitPage.getQueryParam());
		splitPage = splitPageBase(splitPage, "select u.id,u.name,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname");
		List userList = splitPage.getPage().getList();
		for (int i = 0; i < userList.size(); i++) {
			long shopSum = 0;
			long woSum = 0;
			long apSum = 0;
			long routerSum = 0;
			Record staRe = new Record();
			Record ure = (Record) userList.get(i);
			int uid = ure.get("id");
			String name = ure.getStr("name");
			String orgname = ure.getStr("orgname");
			if (uid != 0) {
				for (Record wo : wolist) {
					int userId = wo.getInt("user_id");
					int woType = wo.getInt("wo_type");
					int apNum = wo.get("ap_num") == null ? 0 : wo.getInt("ap_num");
					int routerNum = wo.get("router_num") == null ? 0 : wo.getInt("router_num");
					if (uid == userId) {
						woSum += 1;
						if (woType == 1) {
							shopSum += 1;
						}
						apSum += apNum;
						routerSum += routerNum;
					}
				}
			}
			staRe.set("orgname", orgname);
			staRe.set("name", name);
			staRe.set("shopSum", shopSum);
			staRe.set("woSum", woSum);
			staRe.set("apSum", apSum);
			staRe.set("routerSum", routerSum);
			pagelist.add(staRe);
		}
		int totalPage = splitPage.getPage().getTotalPage();
		splitPage.setPage(new Page(pagelist, splitPage.getPageNumber(), splitPage.getPageSize(), totalPage, totalRow));
		// splitPage.getPage().getList().addAll(pagelist);
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String userName = queryParam.get("userName");
		String shopId = ContextUtil.getShopByUser();
		String orgId = queryParam.get("org_id");
		formSqlSb.append(" from system_user u");
		formSqlSb.append(" LEFT JOIN sys_org_temp  org ON u.`org_id`=org.id ");
		formSqlSb.append(" inner join bp_work_order w on u.id=w.user_id");
		if (shopId != null && !shopId.equals("")) {
			formSqlSb.append(" and w.shop_id in(" + shopId + ")");
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
			formSqlSb.append(" and org.id in(" + ordIds + ")");
		}
		if (userName != null && !userName.equals("")) {
			formSqlSb.append(" and u.name='" + userName + "'");
		}
		if (startDate != null && !startDate.equals("")) {
			formSqlSb.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')>='" + startDate + "'");
		}
		if (endDate != null && !endDate.equals("")) {
			formSqlSb.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')<='" + endDate + "'");
		}
		formSqlSb.append(" group by u.id,u.name");
	}
	
	public List<Record> getWordOrderDatas(Map<String, String> queryParam) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String userName = queryParam.get("userName");
		String shopId = ContextUtil.getShopByUser();
		String orgId = queryParam.get("org_id");
		StringBuffer sql = new StringBuffer("");
		sql.append(" select w.wo_id,w.wo_type,w.user_id,w.ap_num,w.router_num,DATE_FORMAT(w.created_date,'%Y-%m-%d') as created_date");
		sql.append(" from bp_work_order w");
		sql.append(" left join system_user u on u.id=w.user_id ");
		sql.append(" left join  sys_org_temp  org ON u.`org_id`=org.id ");
		sql.append(" where 1=1");
		if (shopId != null && !shopId.equals("")) {
			sql.append(" and w.shop_id in(" + shopId + ")");
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
			sql.append(" and org.id in(" + ordIds + ")");
		}
		if (userName != null && !userName.equals("")) {
			
			sql.append("  and u.name like '%" + userName + "%'");
		}
		if (startDate != null && !startDate.equals("")) {
			sql.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')>='" + startDate + "'");
		}
		if (endDate != null && !endDate.equals("")) {
			sql.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')<='" + endDate + "'");
		}
		return Db.find(sql.toString());
	}
	
	public long getTotalRow(Map<String, String> queryParam) {
		StringBuffer sql = new StringBuffer("select count(distinct u.id) num");
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String userName = queryParam.get("userName");
		String orgId = queryParam.get("org_id");
		String shopId = ContextUtil.getShopByUser();
		sql.append(" from system_user u");
		sql.append(" LEFT JOIN sys_org_temp  org ON u.`org_id`=org.id ");
		sql.append(" inner join bp_work_order w on u.id=w.user_id");
		if (shopId != null && !shopId.equals("")) {
			sql.append(" and w.shop_id in(" + shopId + ")");
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
			sql.append(" and org.id in(" + ordIds + ")");
		}
		if (userName != null && !userName.equals("")) {
			sql.append(" and u.name='" + userName + "'");
		}
		if (startDate != null && !startDate.equals("")) {
			sql.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')>='" + startDate + "'");
		}
		if (endDate != null && !endDate.equals("")) {
			sql.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')<='" + endDate + "'");
		}
		long count = Db.findFirst(sql.toString()).get("num");
		return count;
	}
	
	
	//@formatter:off 
	/**
	 * Title: downWorkOrderFile
	 * Description:工单分析下载
	 * Created On: 2015年6月12日 下午4:26:18
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public Render downWorkOrderFile(Map<String, String> queryMap) {
		StringBuffer formSqlSb = new StringBuffer("select u.id,u.name,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname ");
		String startDate = queryMap.get("startDate");// 开始时间
		String endDate = queryMap.get("endDate");// 结束时间
		String userName = queryMap.get("userName");
		String orgId = queryMap.get("orgId");
		Map<String, String> newMap = new HashMap<String, String>();
		newMap.put("startDate", startDate);
		newMap.put("endDate", endDate);
		newMap.put("userName", userName);
		newMap.put("org_id", orgId);
		formSqlSb.append(" from system_user u");
		formSqlSb.append(" LEFT JOIN sys_org_temp  org ON u.`org_id`=org.id ");
		formSqlSb.append(" inner join bp_work_order w on u.id=w.user_id");
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
			formSqlSb.append(" and org.id in(" + ordIds + ")");
		}
		if (userName != null && !userName.equals("")) {
			formSqlSb.append(" and u.name like '%" + userName + "%'");
		}
		if (startDate != null && !startDate.equals("")) {
			formSqlSb.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')>='" + startDate + "'");
		}
		if (endDate != null && !endDate.equals("")) {
			formSqlSb.append(" and DATE_FORMAT(w.created_date,'%Y-%m-%d')<='" + endDate + "'");
		}
		formSqlSb.append(" group by u.id,u.name");
		List pagelist = new ArrayList();
		List<Record> wolist = getWordOrderDatas(newMap);
		List userList = Db.find(formSqlSb.toString());
		for (int i = 0; i < userList.size(); i++) {
			long shopSum = 0;
			long woSum = 0;
			long apSum = 0;
			long routerSum = 0;
			Record staRe = new Record();
			Record ure = (Record) userList.get(i);
			int uid = ure.get("id");
			String name = ure.getStr("name");
			String orgname = ure.getStr("orgname");
			if (uid != 0) {
				for (Record wo : wolist) {
					int userId = wo.getInt("user_id");
					int woType = wo.getInt("wo_type");
					int apNum = wo.get("ap_num") == null ? 0 : wo.getInt("ap_num");
					int routerNum = wo.get("router_num") == null ? 0 : wo.getInt("router_num");
					if (uid == userId) {
						woSum += 1;
						if (woType == 1) {
							shopSum += 1;
						}
						apSum += apNum;
						routerSum += routerNum;
					}
				}
			}
			staRe.set("orgname", orgname);
			staRe.set("name", name);
			staRe.set("shopSum", shopSum);
			staRe.set("woSum", woSum);
			staRe.set("apSum", apSum);
			staRe.set("routerSum", routerSum);
			pagelist.add(staRe);
		}
		PoiRender excel = new PoiRender(pagelist); 
		String[] columns = {"orgname","name","shopSum","woSum","apSum","routerSum"}; 
		String[] heades = {"组织名称","客户经理名称","创建商铺数量","创建工单数量","吸顶数量","智能路由数量"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("workInfo.xls");
		return excel;
	}
}
