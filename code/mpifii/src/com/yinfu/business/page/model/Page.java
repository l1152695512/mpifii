
package com.yinfu.business.page.model;

import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_shop_page")
public class Page extends Model<Page> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Page dao = new Page();
	
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select IF(u1.id is null,IF(u2.id is null,'未分配',u2.name),u1.name) user_name,");
		sql.append("IF(s.id is null,'未指定商铺',s.name) shop_name,");
		sql.append("d.id,d.name,d.type,d.time_out,d.router_sn,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_device d ");
		formSqlSb.append("left join bp_shop s on (d.shop_id=s.id) ");
		formSqlSb.append("left join system_user u1 on (u1.id=s.owner) ");
//		if(!ContextUtil.isAdmin())
//			System.out.println(ContextUtil.isAdmin());
		formSqlSb.append("left join system_user u2 on (u2.id=d.user_id) ");
		if(!ContextUtil.isAdmin())
		formSqlSb.append("inner join system_user su on (su.id=d.user_id) ");
		formSqlSb.append("where d.delete_date is null  order by d.create_date desc ");
	}
}
