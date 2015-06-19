
package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

public class Customer extends Model<Customer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Customer dao = new Customer();
	
	//@formatter:off 
	/**
	 * Title: getCustomerList
	 * Description:客户信息统计
	 * Created On: 2014年12月8日 下午5:27:48
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getCustomerList(SplitPage splitPage) {
		String sql = "SELECT h.`name` AS shopname,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,";
		sql += " s.`tag` AS info,s.`client_mac` AS mac,s.`auth_date`,s.auth_type, ";
		sql += " s.`address` AS address,s.`cardtype` AS cardtype,s.`ftutype`  AS ftutype ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String cardType = queryParam.get("cardtype");
		String phone = queryParam.get("phone");
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getNow();
		}
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getNow();
		}
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser(); 
		}
		formSqlSb.append(" FROM  bp_auth s  ");
		formSqlSb.append("  JOIN bp_device d ON s.`router_sn` = d.`router_sn`  ");
		formSqlSb.append("  JOIN bp_shop h  ON h.`id` = d.`shop_id` ");
		formSqlSb.append(" LEFT JOIN sys_org_temp org ON h.`org_id` = org.`id` ");
		formSqlSb.append(" WHERE 1=1 AND S.`client_mac` !='' AND S.`client_mac` IS NOT NULL ");
		formSqlSb.append(" AND s.`client_mac` REGEXP '^([0-9a-fA-F]{2})(([/\\s:][0-9a-fA-F]{2}){5})$' AND s.`client_mac` !='00:00:00:00:00:00' ");
		formSqlSb.append(" AND  s.`auth_date`>='" + startDate +" 00:00:00"+ "' AND s.`auth_date`<='" + endDate + " 23:59:59'");
		if (shopId != null) {
			formSqlSb.append(" and h.id IN (" + shopId + ")  ");
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
		if (cardType != null) {
			formSqlSb.append(" and s.cardtype like '%" + cardType + "%' ");
		}
		if (phone != null && !phone.equals("")) {
			formSqlSb.append(" and s.tag like '%" + phone + "%' ");
		}
		formSqlSb.append("  ORDER BY s.auth_date DESC ");
	}
	
	//@formatter:off 
	/**
	 * Title: getCustomerStaInfo
	 * Description:客户信息统计报表
	 * Created On: 2015年1月10日 下午4:27:20
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCustomerStaInfo(Map<String, String> queryMap) {
		String startDate = queryMap.get("_query.startDate");// 开始时间
		String endDate = queryMap.get("_query.endDate");// 结束时间
		String shopId = queryMap.get("_query.shop_id");
		String orgId = queryMap.get("_query.org_id");
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getNow();
		}
		if (startDate == null || startDate.equals("")) {
			startDate =DateUtil.getNow();
		}
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser();
		}
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT  org.id,org.name,DATE_FORMAT(a.`auth_date`,'%Y-%m-%d') AS dates,COUNT(DISTINCT a.`client_mac`) AS zs  ");
		sql.append(" FROM bp_auth  a   ");
		sql.append(" LEFT JOIN bp_device d ON a.`router_sn` = d.`router_sn`    ");
		sql.append(" LEFT JOIN bp_shop s  ON s.`id` = d.`shop_id` ");
		sql.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id`  ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND  a.`auth_date`>='" + startDate +" 00:00:00"+ "' AND a.`auth_date`<='" + endDate + " 23:59:59'");
		sql.append(" AND a.`client_mac` !=\"\"  AND org.`id` IS NOT NULL   ");
		sql.append(" AND a.`client_mac` REGEXP '^([0-9a-fA-F]{2})(([/\\s:][0-9a-fA-F]{2}){5})$' AND a.`client_mac` !='00:00:00:00:00:00' ");
		if(shopId !=null && !shopId.equals("")){
			sql.append(" and s.id in ("+shopId+") ");
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
			sql.append(" and org.id in ("+ordIds+") ");
		}
		sql.append(" GROUP BY org.`id`  ");
		List<Record> zslist = Db.find(sql.toString());
		List<Record> orgList = ContextUtil.getNextListByUser(orgId);
		Map<Object, List<Record>> hashMap = DataOrgUtil.getChildrensForOrgs(orgList, false);
		String caption = startDate + "至" + endDate + "客户统计表(组织)";
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart caption='" + caption + "'  xaxisname='组织' yaxisname='客户数' decimalPrecision='10'  theme='fint'>");
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
	
	/**
	 * 下载所有客户数据
	 * @param queryMap
	 */
	public PoiRender downCustAllInfo(Map<String, String> queryMap){
		String cardType = queryMap.get("type");
		String phone = queryMap.get("phone");
		String startDate = queryMap.get("startDate");// 开始时间
		String endDate = queryMap.get("endDate");// 结束时间
		String shopId = queryMap.get("shopId");
		String orgId = queryMap.get("orgId");
		
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getNow();
		}
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getNow();
		}
		if ((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()) {
			shopId = ContextUtil.getShopByUser();
		}
		StringBuffer sql = new StringBuffer("SELECT h.`name` AS shopname,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,");
		sql.append("IF(s.`auth_type`='phone','手机','其他') AS auth_type,S.`tag` AS info,s.`client_mac` AS mac,s.address,s.cardtype,s.`auth_date`,");
		sql.append(" IFNULL(s.`address`,'未知') AS address,IFNULL(s.`cardtype`,'未知') AS cardtype,IFNULL(s.`ftutype`,'未知')  AS ftutype ");
		sql.append(" FROM  bp_auth s  ");
		sql.append("  JOIN bp_device d ON s.`router_sn` = d.`router_sn`  ");
		sql.append("  JOIN bp_shop h  ON h.`id` = d.`shop_id` ");
		sql.append(" LEFT JOIN sys_org_temp org ON h.`org_id` = org.`id` ");
		sql.append(" WHERE s.`auth_type`='phone' AND S.`client_mac` !='' AND S.`client_mac` IS NOT NULL ");
		sql.append(" and  DATE_FORMAT(s.`auth_date`,'%Y-%m-%d')>='" + startDate + "' AND DATE_FORMAT(s.`auth_date`,'%Y-%m-%d')<='" + endDate
				+ "'  ");
		if (shopId != null && !shopId.equals("")) {
			sql.append(" and h.id IN (" + shopId + ")");
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
			sql.append(" and org.id IN (" + ordIds + ")");
		}
		if (cardType != null && !cardType.equals("")) {
			sql.append(" and s.cardtype like '%" + cardType + "%'");
		}
		if (phone != null && !phone.equals("")) {
			sql.append(" and s.tag like '%" + phone + "%'");
		}
		sql.append(" ORDER BY s.auth_date DESC ");
		List list=Db.find(sql.toString());
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"orgname","shopname","auth_type","info","cardtype","address","mac","ftutype","auth_date"}; 
		String[] heades = {"组织","商户","认证类型","用户","卡类型","归属地","mac地址","终端","认证时间"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("custinfo.xls");
		return excel;
	}
}
