
package com.yinfu.business.operation.adv.model;

import java.util.List;
import java.util.Map;

import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

public class PutInMsg extends Model<PutInMsg> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static PutInMsg dao = new PutInMsg();
	
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sqlSelect = new StringBuffer();
		sqlSelect.append("select bapm.id,bac.name adv_name,bas.name spaces_name,if(su.id=bac.create_user,'1','0') edit_adv,");
		sqlSelect.append("GROUP_CONCAT(distinct d1.value order by d1.create_date) industrys,bapm.`status`,");
		sqlSelect.append("GROUP_CONCAT(distinct CONCAT(d2.value,' ',d3.value) order by d2.create_date,d3.create_date) weeks_times,");
		sqlSelect.append("date_format(adp.start_date,'%Y-%m-%d') start_date,date_format(adp.end_date,'%Y-%m-%d') end_date ");
		splitPage = splitPageBase(splitPage,sqlSelect.toString());
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from system_user su join bp_adv_putin_msg bapm on (su.id=? and su.org_id=bapm.org_id) ");
		formSqlSb.append("join bp_adv_putin adp on (adp.id=bapm.adv_putin_id) ");
		formSqlSb.append("join bp_adv_content bac on (adp.adv_content_id=bac.id) ");
		formSqlSb.append("join bp_adv_spaces bas on (adp.adv_space=bas.id) ");
		formSqlSb.append("join bp_adv_putin_industry bapi on (adp.id=bapi.adv_putin_id) ");
		formSqlSb.append("join bp_dictionary d1 on (d1.id=bapi.industry_id) ");
		formSqlSb.append("join bp_adv_putin_daytime bapd on (adp.id=bapd.adv_putin_id) ");
		formSqlSb.append("join bp_dictionary d2 on (d2.id=bapd.week_id) ");
		formSqlSb.append("join bp_dictionary d3 on (d3.id=bapd.time_id) ");
		formSqlSb.append("group by bapm.id ");
		formSqlSb.append("order by if(bapm.`status`=0,0,1),bapm.create_date desc ");
		paramValue.add(ContextUtil.getCurrentUserId());
	}
}
