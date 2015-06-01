
package com.yinfu.business.operation.adv.model;

import java.util.List;
import java.util.Map;

<<<<<<< HEAD
import com.yinfu.common.ContextUtil;
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

public class Permission extends Model<Permission> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Permission dao = new Permission();
	
	public SplitPage findList(SplitPage splitPage) {
<<<<<<< HEAD
		splitPage = splitPageBase(splitPage,"select so.id,so.name,sum(IF(ao.id is null,0,IF(ao.edit_able,1,0))) adv_num ");
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from system_user su ");
		formSqlSb.append("join sys_org so on (su.id=? and su.org_id=so.pid) ");
		formSqlSb.append("left join bp_adv_org ao on (so.id=ao.org_id) ");
		formSqlSb.append("group by so.id ");
		paramValue.add(ContextUtil.getCurrentUserId());
=======
		splitPage = splitPageBase(splitPage,"select bat.id,ifnull(sgr.id,'') groupRoleId,ifnull(bt.name,'') templateName,bat.name advName,ifnull(sr2.name,sr1.name) roleName,ifnull(sr2.id,sr1.id) roleId ");
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_adv_type bat ");
		formSqlSb.append("join system_role sr1 on (bat.default_access_role=sr1.unique_mark) ");
		formSqlSb.append("left join bp_temp bt on (bat.template_id = bt.id) ");
		formSqlSb.append("left join bp_shop_group_role sgr on (sgr.shop_group_id=? and sgr.adv_type_id=bat.id) ");
		formSqlSb.append("left join system_role sr2 on (sgr.role_id=sr2.id) ");
		formSqlSb.append("order by bat.create_date ");
		
		paramValue.add(queryParam.get("groupId"));
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	}
}
