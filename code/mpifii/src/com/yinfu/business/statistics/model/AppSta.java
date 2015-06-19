
package com.yinfu.business.statistics.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

public class AppSta extends Model<AppSta> {
	private static final long serialVersionUID = 1L;
	public static AppSta dao = new AppSta();
	
	//@formatter:off 
		/**
		 * Title: getCustomerList
		 * Description:应用信息统计
		 * Created On: 2014年12月8日 下午5:27:48
		 * @author JiaYongChao
		 * <p>
		 * @param splitPage
		 * @return 
		 */
		//@formatter:on
	public SplitPage getAppStaList(SplitPage splitPage) {
		String sql = "SELECT sd.search_date AS days,s.`name` AS shopname,IFNULL(org.`name`,'暂未绑定') AS orgname,if(sa.app_type='app',a.name,b.title) AS apname,IFNULL(sa.access_num,0) num   ";
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
			startDate = DateUtil.getSevenDayBefore(endDate);
		}
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		formSqlSb.append(" FROM bp_shop s ");
		formSqlSb.append(" LEFT JOIN sys_org org ON s.`org_id` = org.`id`");
		formSqlSb.append(" JOIN bp_search_day sd ON (sd.search_date>='"+startDate+"' AND sd.search_date<='"+endDate+"')");
		formSqlSb.append(" LEFT JOIN bp_statistics_app sa ON ( sa.access_date>='"+startDate+"' AND sa.access_date<='"+endDate+"' AND sa.`shop_id` = s.`id` AND sa.access_date=sd.search_date)");
		formSqlSb.append(" LEFT  JOIN bp_app a ON (sa.app_id = a.id)");
		formSqlSb.append(" LEFT  JOIN bp_nav b ON (sa.app_id = b.id)");
		formSqlSb.append("  where 1=1 ");
		if(shopId!=null){
			formSqlSb.append(" and  S.id IN ("+shopId+")  ");
		}
		if(orgId!=null){
			formSqlSb.append(" and org.id IN ("+orgId+")  ");
		}
		formSqlSb.append(" GROUP BY sd.search_date,s.id,s.`name`,org.`name`,sa.app_type desc ");
	}
	
	//@formatter:off 
		/**
		 * Title: getAppStaInfo
		 * Description:应用统计信息
		 * Created On: 2014年12月22日 上午11:15:35
		 * @author JiaYongChao
		 * <p>	
		 * @param queryMap
		 * @return 
		 */
		//@formatter:on
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getAppStaInfo(Map<String, String> queryMap) {
		String startDate = queryMap.get("_query.startDate");// 开始时间
		String endDate = queryMap.get("_query.endDate");// 结束时间
		String shopId = queryMap.get("_query.shop_id");
		String orgId = queryMap.get("_query.org_id");
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getNow();
		}	
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getSevenDayBefore(endDate);
		}
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		String[] days = DateUtil.scopeTimes(startDate, endDate);// 获得天数
		StringBuffer zsSql = new StringBuffer("SELECT ");
		zsSql.append(" DATE_FORMAT(t.access_date,'%Y-%m-%d') AS day_num,COUNT(*) AS zs");
		zsSql.append(" FROM bp_statistics_app  t   ");
		zsSql.append(" LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` AND s.`delete_date` IS NULL  ");
		zsSql.append(" LEFT JOIN sys_org org ON s.`org_id` = org.`id` ");
		zsSql.append(" WHERE  DATE_FORMAT(t.access_date,'%Y-%m-%d') BETWEEN '"+startDate+"' AND '"+endDate+"'");
		if(shopId !=null && !shopId.equals("")){
			zsSql.append(" and s.id in ("+shopId+") ");
		}
		if(orgId !=null && !orgId.equals("")){
			zsSql.append(" and org.id in ("+orgId+") ");
		}
		zsSql.append(" GROUP BY  DATE_FORMAT(t.access_date, '%Y-%m-%d') ");
		List<Record> zslist = Db.find(zsSql.toString());
		Map zsMap = new HashMap();
		for (int i = 0; i < days.length; i++) {
			boolean flag = true;
			for (int f = 0; f < zslist.size(); f++) {
				Record zsRd = zslist.get(f);
				String nva = zsRd.getStr("day_num");
				if (nva.equals(days[i])) {
					zsMap.put(days[i], zsRd.get("zs"));
					flag = false;
					break;
				}
			}
			if (flag) {
				zsMap.put(days[i], 0);
			}
		}
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart yaxisname='点击数' theme='fint'>");
		xmlData.append("<categories>");
		for (int i = 0; i < days.length; i++) {
			xmlData.append("<category label='" + days[i] + "' />");
		}
		xmlData.append(" </categories>");
		xmlData.append(" <dataset seriesname='应用'>");
		for (int i = 0; i < days.length; i++) {
			xmlData.append("<set value='" + zsMap.get(days[i]) + "' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" </chart>");
		return xmlData.toString();
	}
}
