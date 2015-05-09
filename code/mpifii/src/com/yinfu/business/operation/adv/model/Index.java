
package com.yinfu.business.operation.adv.model;

import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_shop_page")
public class Index extends Model<Index> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Index dao = new Index();
	
	/**
	 * 	select a.*,COUNT(sgr.id) settingNum 
		from (select sg.id,u.name userName,sg.name groupName,COUNT(d.id) deviceNum 
				from bp_shop_group sg 
						left join system_user u on (sg.user_id=u.id) 
						-- join system_user_role ur on (u.id=ur.user_id) 
						left join bp_shop s on (s.owner=u.id or s.create_user=u.id) 
						left join bp_device d on (s.id=d.shop_id)
				group by u.id) a left join bp_shop_group_role sgr on (sgr.shop_group_id=a.id) 
		group by a.id
	 * @param splitPage
	 * @return
	 */
	
	public SplitPage findList(SplitPage splitPage) {
		splitPage = splitPageBase(splitPage,"select sg.id,u.name userName,sg.name groupName,sg.access_delete,COUNT(d.id) deviceNum ");
		return splitPage;
	}
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_shop_group sg ");
		formSqlSb.append("left join system_user u on (sg.user_id=u.id) ");
		formSqlSb.append("left join bp_shop s on (s.group_id=sg.id) ");
		formSqlSb.append("left join bp_device d on (s.id=d.shop_id) ");
		if(!ContextUtil.isAdmin()){
			formSqlSb.append("where sg.user_id='"+ContextUtil.getCurrentUserId()+"' ");
		}
		formSqlSb.append("group by sg.id ");
	}
}
