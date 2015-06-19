
package com.yinfu.business.operation.adv.model;

import java.util.List;
import java.util.Map;

import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

public class PutIn extends Model<PutIn> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static PutIn dao = new PutIn();
	
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sqlSelect = new StringBuffer();
		sqlSelect.append("select * ");
		splitPage = splitPageBase(splitPage,sqlSelect.toString());
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from (");
		formSqlSb.append("select adp.id,bac.name adv_name,bas.name spaces_name,if(su.id=bac.create_user,'1','0') edit_adv,");
		formSqlSb.append("GROUP_CONCAT(distinct so.name order by so.name) orgs,GROUP_CONCAT(distinct d1.value order by d1.create_date) industrys,");
		formSqlSb.append("adp.end_date<date(now()) expire,adp.enable and adp.end_date>=date(now()) enable,");
		formSqlSb.append("date_format(adp.start_date,'%Y-%m-%d') start_date,date_format(adp.end_date,'%Y-%m-%d') end_date,");
		formSqlSb.append("date_format(adp.create_date,'%Y-%m-%d %H:%i:%s') create_date,");
		formSqlSb.append("IF(adp.shelf_date is null or adp.enable,'',date_format(adp.shelf_date,'%Y-%m-%d %H:%i:%s')) shelf_date ");
		formSqlSb.append("from system_user su join bp_adv_putin adp on (su.id=? and adp.org_id=su.org_id) ");
		formSqlSb.append("join bp_adv_content bac on (adp.adv_content_id=bac.id) ");
		formSqlSb.append("join bp_adv_spaces bas on (adp.adv_space=bas.id) ");
		formSqlSb.append("join bp_adv_putin_org bapo on (adp.id=bapo.adv_putin_id) ");
		formSqlSb.append("join sys_org so on (so.id=bapo.org_id) ");
		formSqlSb.append("join bp_adv_putin_industry bapi on (adp.id=bapi.adv_putin_id) ");
		formSqlSb.append("join bp_dictionary d1 on (d1.id=bapi.industry_id) ");
		formSqlSb.append("group by adp.id ");
		formSqlSb.append("order by adp.enable desc,adp.create_date desc ) a");
		paramValue.add(ContextUtil.getCurrentUserId());
	}
	
//	public SplitPage findList(SplitPage splitPage) {
//		StringBuffer sqlSelect = new StringBuffer();
//		sqlSelect.append("select adp.id,bac.name adv_name,bas.name spaces_name,if(su.id=bac.create_user,'1','0') edit_adv,");
//		sqlSelect.append("GROUP_CONCAT(distinct so.name order by so.name) orgs,GROUP_CONCAT(distinct d1.value order by d1.create_date) industrys,");
//		sqlSelect.append("GROUP_CONCAT(distinct CONCAT(d2.value,' ',d3.value) order by d2.create_date,d3.create_date) weeks_times,adp.end_date<date(now()) expire,");
//		sqlSelect.append("adp.enable and adp.end_date>=date(now()) enable, date_format(adp.start_date,'%Y-%m-%d') start_date,date_format(adp.end_date,'%Y-%m-%d') end_date ");
//		splitPage = splitPageBase(splitPage,sqlSelect.toString());
//		return splitPage;
//	}
//	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
//		formSqlSb.append("from system_user su join bp_adv_putin adp on (su.id=? and adp.org_id=su.org_id) ");
//		formSqlSb.append("join bp_adv_content bac on (adp.adv_content_id=bac.id) ");
//		formSqlSb.append("join bp_adv_spaces bas on (adp.adv_space=bas.id) ");
//		formSqlSb.append("join bp_adv_putin_org bapo on (adp.id=bapo.adv_putin_id) ");
//		formSqlSb.append("join sys_org so on (so.id=bapo.org_id) ");
//		formSqlSb.append("join bp_adv_putin_industry bapi on (adp.id=bapi.adv_putin_id) ");
//		formSqlSb.append("join bp_dictionary d1 on (d1.id=bapi.industry_id) ");
//		formSqlSb.append("join bp_adv_putin_daytime bapd on (adp.id=bapd.adv_putin_id) ");
//		formSqlSb.append("join bp_dictionary d2 on (d2.id=bapd.week_id) ");
//		formSqlSb.append("join bp_dictionary d3 on (d3.id=bapd.time_id) ");
//		formSqlSb.append("group by adp.id ");
//		formSqlSb.append("order by adp.enable desc,adp.create_date desc ");
//		paramValue.add(ContextUtil.getCurrentUserId());
//	}
}
