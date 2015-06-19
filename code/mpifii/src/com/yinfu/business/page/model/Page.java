
package com.yinfu.business.page.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_shop_page")
public class Page extends Model<Page> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Page dao = new Page();
	
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ifnull(u.name,'未分配') as  user_name,");
		sql.append("IF(s.id is null,'未指定商铺',s.name) shop_name,");
		sql.append("d.id,d.name,d.type,d.time_out,d.router_sn,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date,d.router_version,d.script_version,d.data_version ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String shopId="";
		if(!ContextUtil.isAdmin()){
			shopId = ContextUtil.getShopByUser();
		}
		formSqlSb.append("from bp_device d ");
		formSqlSb.append("left join bp_shop s on (d.shop_id=s.id) ");
		formSqlSb.append("left join system_user u on u.id=s.owner ");
		formSqlSb.append("where d.delete_date is null ");
		if (shopId != null && !shopId.equals("")) {
			formSqlSb.append("and s.id IN (" + shopId + ")  ");
		}
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 2 && "like".equalsIgnoreCase(keyInfo[2])){
					formSqlSb.append("and d."+keyInfo[0]+"_"+keyInfo[1]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and s."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}
			}
		}
		
		formSqlSb.append(" order by d.create_date desc ");
	}
}
