
package com.yinfu.business.statistics.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.model.SplitPage.SplitPage;

public class AdvSta extends Model<AdvSta> {
	private static final long serialVersionUID = 1L;
	public static AdvSta dao = new AdvSta();
	
	//@formatter:off 
		/**
		 * Title: getCustomerList
		 * Description:广告信息统计
		 * Created On: 2014年12月8日 下午5:27:48
		 * @author JiaYongChao
		 * <p>
		 * @param splitPage
		 * @return 
		 */
		//@formatter:on
	public SplitPage getAdvStaList(SplitPage splitPage) {
		String sql = " SELECT adv.`dates` AS dates,s.id AS shopId,s.`name` AS shopNmae,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,adv.`zss`,adv.`djs`";
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
		formSqlSb.append(" FROM bp_statistics_adv adv  ");
		formSqlSb.append(" LEFT JOIN bp_shop s ON  adv.`shopId` = s.`id` ");
		formSqlSb.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id`  ");
		formSqlSb.append(" WHERE 1=1   ");
		formSqlSb.append(" and adv.`dates`>='" + startDate + "' AND adv.`dates`<='" + endDate + " '");
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
		formSqlSb.append("  GROUP BY adv.shopId,adv.dates ");
		formSqlSb.append("  ORDER BY adv.zss desc,adv.dates,org.id,s.id,adv.advName ");
	}
	
	//@formatter:off 
		/**
		 * Title: getAdvStaInfo
		 * Description:广告信息统计
		 * Created On: 2014年12月29日 下午2:12:27
		 * @author JiaYongChao
		 * <p>
		 * @param queryMap
		 * @return 
		 */
		//@formatter:on
	public String getAdvStaInfo(Map<String, String> queryMap) {
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
		String[] days = DateUtil.scopeTimes(startDate, endDate);// 获得天数
		StringBuilder sql = new StringBuilder(" ");
		sql.append("  SELECT adv.dates,SUM(adv.`zss`) AS zss,SUM(adv.`djs`) AS djs ");
		sql.append("  FROM bp_statistics_adv adv   ");
		sql.append("  LEFT JOIN bp_shop s ON  adv.`shopId` = s.`id`    ");
		sql.append("  LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id`   ");
		sql.append("  WHERE 1=1    AND adv.`dates`>='" + startDate + "' AND adv.`dates`<='" + endDate + " '");
		if (shopId != null && !shopId.equals("")) {
			sql.append(" and s.id in (" + shopId + ") ");
		}
		if (orgId != null && !orgId.equals("")) {
			List<Record> orgList = DataOrgUtil.getChildrens(orgId, true);
			String ordIds = DataOrgUtil.recordListToSqlIn(orgList, "id");
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
		sql.append(" GROUP BY adv.dates ");
		List<Record> zslist = Db.find(sql.toString());
		String caption = startDate + "至" + endDate + "广告分组统计表";
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart palette='1' caption='"
				+ caption
				+ "' xAxisName='组织' showlabels='1' showvalues='0' numbersuffix='次' snumbersuffix='%' syaxisvaluesdecimals='2' connectnulldata='0' pyaxisname='展示/点击次数' syaxisname='点击率' numdivlines='4' formatnumberscale='0' hoverCapSepChar='/'>");
		xmlData.append("<categories>");
		for (int i = 0; i < days.length; i++) {
			xmlData.append("<category label='" + days[i] + "' />");
		}
		xmlData.append(" </categories>");
		xmlData.append(" <dataset seriesname='展示次数' color='AFD8F8' showvalues='0'>");
		for (int i = 0; i < days.length; i++) {
			String day = days[i];
			int zss = 0;
			for (int j = 0; j < zslist.size(); j++) {
				String dates = String.valueOf(zslist.get(j).get("dates"));
				if (dates.equals(day)) {
					int rZss = Integer.parseInt(zslist.get(j).get("zss").toString());
					zss += rZss;
				}
			}
			xmlData.append("<set value='" + zss + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" <dataset seriesname='点击次数' color='F6BD0F' showvalues='0'>");
		for (int i = 0; i < days.length; i++) {
			String day = days[i];
			int djs = 0;
			for (int j = 0; j < zslist.size(); j++) {
				String dates = String.valueOf(zslist.get(j).get("dates"));
				if (dates.equals(day)) {
					int rDjs = Integer.parseInt(zslist.get(j).get("djs").toString());
					djs += rDjs;
				}
			}
			xmlData.append("<set value='" + djs + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" <dataset seriesname='点击率' color='8BBA00' showvalues='0' parentyaxis='S' renderas='Line'>");
		for (int i = 0; i < days.length; i++) {
			String day = days[i];
			int djs = 0;
			int zss = 0;
			for (int j = 0; j < zslist.size(); j++) {
				String dates = String.valueOf(zslist.get(j).get("dates"));
				if (dates.equals(day)) {
					int rDjs = Integer.parseInt(zslist.get(j).get("djs").toString());
					djs += rDjs;
					int rZss = Integer.parseInt(zslist.get(j).get("zss").toString());
					zss += rZss;
				}
			}
			String rate = "0";
			if (zss > 0) {
				float num = (float) djs / zss;
				DecimalFormat df = new DecimalFormat("0.00");
				rate = df.format(num * 100);
			}
			xmlData.append("<set value='" + rate + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" </chart>");
		return xmlData.toString();
	}
	
	//@formatter:off 
	/**
	 * Title: getAdvOrgStaInfo
	 * Description:广告分组织统计表
	 * Created On: 2015年1月11日 下午10:09:12
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public String getAdvOrgStaInfo(Map<String, String> queryMap) {
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
		sql.append(" SELECT org.id as id,org.pid AS pid,org.`NAME`,SUM(adv.`zss`) AS zss,SUM(adv.`djs`) AS djs ");
		sql.append("  FROM bp_statistics_adv adv   ");
		sql.append("  LEFT JOIN bp_shop s ON  adv.`shopId` = s.`id`    ");
		sql.append("  LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id`   ");
		sql.append("  WHERE 1=1    AND adv.`dates`>='" + startDate + "' AND adv.`dates`<='" + endDate + " ' AND ORG.`id` IS NOT NULL ");
		if (shopId != null && !shopId.equals("")) {
			sql.append(" and s.id IN (" + shopId + ")  ");
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
		sql.append(" GROUP BY org.`id` ");
		List<Record> zslist = Db.find(sql.toString());
		List<Record> orgList = ContextUtil.getNextListByUser(orgId);
		Map<Object, List<Record>> hashMap = DataOrgUtil.getChildrensForOrgs(orgList, false);
		String caption = startDate + "至" + endDate + "广告分组统计表";
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart palette='1' caption='"
				+ caption
				+ "' xAxisName='组织' showlabels='1' showvalues='0' numbersuffix='次' snumbersuffix='%' syaxisvaluesdecimals='2' connectnulldata='0' pyaxisname='展示/点击次数' syaxisname='点击率' numdivlines='4' formatnumberscale='0' hoverCapSepChar='/'>");
		xmlData.append("<categories>");
		for (int i = 0; i < orgList.size(); i++) {
			xmlData.append("<category label='" + orgList.get(i).get("name") + "' />");
		}
		xmlData.append(" </categories>");
		xmlData.append(" <dataset seriesname='展示次数' color='AFD8F8' showvalues='0'>");
		for (int i = 0; i < orgList.size(); i++) {
			int zss = 0;
			Integer id = orgList.get(i).get("id");
			List<Record> info = hashMap.get(id);
			for (int j = 0; j < zslist.size(); j++) {
				if (zslist.get(j).get("id").equals(id)) {
					zss += Integer.parseInt(zslist.get(j).get("zss").toString());
				} else {
					for (int s = 0; s < info.size(); s++) {
						Integer ids = info.get(s).get("id");
						if (zslist.get(j).get("id").equals(ids)) {
							zss += Integer.parseInt(zslist.get(j).get("zss").toString());
						}
						
					}
				}
			}
			xmlData.append("<set value='" + zss + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" <dataset seriesname='点击次数' color='F6BD0F' showvalues='0'>");
		for (int i = 0; i < orgList.size(); i++) {
			int djs = 0;
			Integer id = orgList.get(i).get("id");
			List<Record> info = hashMap.get(id);
			for (int j = 0; j < zslist.size(); j++) {
				if (zslist.get(j).get("id").equals(id)) {
					djs += Integer.parseInt(zslist.get(j).get("djs").toString());
				} else {
					for (int s = 0; s < info.size(); s++) {
						Integer ids = info.get(s).get("id");
						if (zslist.get(j).get("id").equals(ids)) {
							djs += Integer.parseInt(zslist.get(j).get("djs").toString());
						}
						
					}
				}
			}
			xmlData.append("<set value='" + djs + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" <dataset seriesname='点击率' color='8BBA00' showvalues='0' parentyaxis='S' renderas='Line'>");
		for (int i = 0; i < orgList.size(); i++) {
			Integer id = orgList.get(i).get("id");
			List<Record> info = hashMap.get(id);
			int zss = 0;
			int djs = 0;
			for (int j = 0; j < zslist.size(); j++) {
				if (zslist.get(j).get("id").equals(id)) {
					int rDjs = Integer.parseInt(zslist.get(j).get("djs").toString());
					djs += rDjs;
					int rZss = Integer.parseInt(zslist.get(j).get("zss").toString());
					zss += rZss;
				} else {
					for (int s = 0; s < info.size(); s++) {
						Integer ids = info.get(s).get("id");
						if (zslist.get(j).get("id").equals(ids)) {
							int rDjs = Integer.parseInt(zslist.get(j).get("djs").toString());
							djs += rDjs;
							int rZss = Integer.parseInt(zslist.get(j).get("zss").toString());
							zss += rZss;
						}
					}
				}
			}
			String rate = "0";
			if (zss > 0) {
				float num = (float) djs / zss;
				DecimalFormat df = new DecimalFormat("0.00");
				rate = df.format(num * 100);
			}
			xmlData.append("<set value='" + rate + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" </chart>");
		return xmlData.toString();
	}
}
