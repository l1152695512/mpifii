
package com.yinfu.business.bigdata.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.business.util.StrUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * @author JiaYongChao 用户行为分析
 */
public class UserBehavior extends Model<UserBehavior> {
	private static final long serialVersionUID = 1L;
	public static UserBehavior dao = new UserBehavior();
	
	//@formatter:off 
	/**
	 * Title: getBehaviorList
	 * Description:用户行为分析列表
	 * Created On: 2015年6月4日 下午3:50:47
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getBehaviorList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.phone,ty.`typename`,s.`name` AS shopname,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,SUM(t.count) AS counts,sd.search_date AS dates  ");
		splitPage = splitPageBase(splitPage, sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String date = queryParam.get("date");// 时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		if (date == null || date.equals("")) {
			date = DateUtil.currentYearMonth();
		}
		formSqlSb.append(" FROM ( ");
		formSqlSb.append(" SELECT *, COUNT(DISTINCT phone) FROM  bg_tat_uesrtype_mon_all_" + date
				+ "  WHERE 1=1 AND phone NOT LIKE '' GROUP BY phone ");
		formSqlSb.append(" )  AS  t");
		formSqlSb.append(" LEFT JOIN bp_shop s ON  t.`businessid` = s.`id`  ");
		formSqlSb.append(" LEFT JOIN bp_search_day sd ON sd.`search_date`= DATE_FORMAT(t.startime,'%Y-%m-%d') ");
		formSqlSb.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id` ");
		formSqlSb.append(" LEFT JOIN bg_sys_user_type ty ON t.`typenum` = ty.`typenum`   ");
		formSqlSb.append(" WHERE 1=1   ");
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
		formSqlSb.append("  GROUP BY t.phone,t.typenum,sd.search_date ");
	}
	
	//@formatter:off 
	/**
	 * Title: getBehaviorXml
	 * Description:用户行为分析报表
	 * Created On: 2015年6月4日 下午3:52:02
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public String getBehaviorXml(Map<String, String> queryMap) {
		String date = queryMap.get("_query.date");// 时间
		String shopId = queryMap.get("_query.shop_id");
		String orgId = queryMap.get("_query.org_id");
		if (date == null || date.equals("")) {
			date = DateUtil.currentYearMonth();
		}
		StringBuilder xmlSql = new StringBuilder(" SELECT t.typenum,ty.`typename`,COUNT(t.phone) AS counts FROM (");
		xmlSql.append(" SELECT *, COUNT(DISTINCT phone) FROM  bg_tat_uesrtype_mon_all_" + date + "  WHERE 1=1 AND phone NOT LIKE '' GROUP BY phone ");
		xmlSql.append(" )  AS  t");
		xmlSql.append(" LEFT JOIN bg_sys_user_type ty ON t.`typenum` = ty.`typenum` ");
		xmlSql.append(" LEFT JOIN bp_shop s ON  t.`businessid` = s.`id`   ");
		xmlSql.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id` ");
		xmlSql.append(" where 1=1 ");
		if (shopId != null && !shopId.equals("")) {
			xmlSql.append(" and s.id IN (" + shopId + ")  ");
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
			xmlSql.append(" and org.id IN (" + ordIds + ")  ");
		}
		xmlSql.append(" GROUP BY ty.typenum");
		List<Record> dataList = Db.find(xmlSql.toString());// 数据List
		StringBuilder sb = new StringBuilder();
		sb.append("<chart palette='5' caption='用户行为分析' yAxisName='访问人数' numberprefix='' formatNumberScale='0' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		sb.append("<categories>");
		sb.append("<category label='" + date + "' />");
		sb.append("</categories>");
		List<Record> typeList = getPreferenceTypeList();
		for (int i = 0; i < typeList.size(); i++) {
			String lineTypeNum = typeList.get(i).get("typenum").toString();
			sb.append("<dataset seriesname='" + typeList.get(i).getStr("typename") + "'  showvalues='1'>");
			for (int t = 0; t < dataList.size(); t++) {
				String typeNum = dataList.get(t).get("typenum").toString();
				if (lineTypeNum.equals(typeNum)) {
					sb.append("<set value='" + dataList.get(t).get("counts") + "' />");
				}
			}
			sb.append("</dataset>");
		}
		sb.append("</chart>");
		return sb.toString();
	}
	
	//@formatter:off 
		/**
		 * Title: getPreferenceTypeList
		 * Description:获得用户偏好类型
		 * Created On: 2015年6月2日 下午2:43:27
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	private List<Record> getPreferenceTypeList() {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.* from bg_sys_user_type t  ");
		return Db.find(sb.toString());
	}
	
	//@formatter:off 
		/**
		 * Title: downBehaviorFile
		 * Description:用户偏好数据下载
		 * Created On: 2015年6月15日 下午6:05:13
		 * @author JiaYongChao
		 * <p>
		 * @param queryMap
		 * @return 
		 */
		//@formatter:on
	public Render downBehaviorFile(Map<String, String> queryMap) {
		String date = queryMap.get("date");// 时间
		String shopId = queryMap.get("shop_id");
		String orgId = queryMap.get("org_id");
		if (date == null || date.equals("")) {
			date = DateUtil.currentYearMonth();
		}
		StringBuffer formSqlSb = new StringBuffer();
		formSqlSb.append("SELECT t.phone,ty.`typename`,s.`name` AS shopname,SUBSTRING_INDEX(IFNULL(org.pathname,'暂未绑定'),'/',-3) AS orgname,SUM(t.count) AS counts,sd.search_date AS dates  ");
		formSqlSb.append(" FROM ( ");
		formSqlSb.append(" SELECT *, COUNT(DISTINCT phone) FROM  bg_tat_uesrtype_mon_all_" + date
				+ "  WHERE 1=1 AND phone NOT LIKE '' GROUP BY phone ");
		formSqlSb.append(" )  AS  t");
		formSqlSb.append(" LEFT JOIN bp_shop s ON  t.`businessid` = s.`id`  ");
		formSqlSb.append(" LEFT JOIN bp_search_day sd ON sd.`search_date`= DATE_FORMAT(t.startime,'%Y-%m-%d') ");
		formSqlSb.append(" LEFT JOIN sys_org_temp org ON s.`org_id` = org.`id` ");
		formSqlSb.append(" LEFT JOIN bg_sys_user_type ty ON t.`typenum` = ty.`typenum`   ");
		formSqlSb.append(" WHERE 1=1   ");
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
		formSqlSb.append("  GROUP BY t.phone,t.typenum,sd.search_date ");
		List list=Db.find(formSqlSb.toString());
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"phone","typename","shopname","orgname","counts","dates"}; 
		String[] heades = {"用户手机号码","用户类型","商铺","组织","点击次数","访问时间"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("bigDataBehavior.xls");
		return excel;
	}
}
