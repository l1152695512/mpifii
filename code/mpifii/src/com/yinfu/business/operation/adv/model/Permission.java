
package com.yinfu.business.operation.adv.model;

import java.util.List;
import java.util.Map;

import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

public class Permission extends Model<Permission> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Permission dao = new Permission();
	
	public SplitPage findList(SplitPage splitPage) {
		splitPage = splitPageBase(splitPage,"select so.id,so.name,sum(IF(ao.id is null,0,IF(ao.edit_able,1,0))) adv_num ");
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from system_user su ");
		formSqlSb.append("join sys_org so on (su.id=? and su.org_id=so.pid) ");
		formSqlSb.append("left join bp_adv_org ao on (so.id=ao.org_id) ");
		formSqlSb.append("group by so.id ");
		paramValue.add(ContextUtil.getCurrentUserId());
	}
}
