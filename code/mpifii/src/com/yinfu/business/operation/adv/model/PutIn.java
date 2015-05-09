
package com.yinfu.business.operation.adv.model;

import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_shop_page")
public class PutIn extends Model<PutIn> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static PutIn dao = new PutIn();
	
	public SplitPage findList(SplitPage splitPage) {
		splitPage = splitPageBase(splitPage,"select ifnull(sgr.id,'') groupRoleId,ifnull(sgr.image,'') image,ifnull(sgr.link,'') link,ifnull(sgr.des,'') des,bt.name templateName,bat.id advId,bat.name advName,bat.img_size_tips ");
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_adv_type bat ");
		formSqlSb.append("join system_role sr on (bat.default_access_role=sr.unique_mark) ");
		formSqlSb.append("left join bp_temp bt on (bat.template_id=bt.id) ");
		formSqlSb.append("left join bp_shop_group_role sgr on (sgr.shop_group_id=? and sgr.adv_type_id=bat.id) ");
		formSqlSb.append("join system_user_role sur on (sur.user_id=? and (sgr.role_id=sur.role_id or sr.id=sur.role_id)) ");
		formSqlSb.append("order by bat.create_date ");
		
		paramValue.add(queryParam.get("groupId"));
		paramValue.add(ContextUtil.getCurrentUserId());
	}
}
