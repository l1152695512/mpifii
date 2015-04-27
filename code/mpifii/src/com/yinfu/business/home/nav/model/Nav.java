
package com.yinfu.business.home.nav.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

/**
 * nav实体
 * 
 * @author JiaYongChao 2014年7月23日
 */
@TableBind(tableName = "bp_nav")
public class Nav extends Model<Nav> {
	private static final long serialVersionUID = 1L;
	public static Nav dao = new Nav();
	
	public SplitPage findList(SplitPage splitPage) {
		String sql = "select id,title,type,logo,url,status,des,date_format(create_date,'%Y-%m-%d %H:%i:%s') create_date ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_nav where 1=1 ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and "+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and "+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		User curUser = ContextUtil.getCurrentUser();
		String userid = String.valueOf(curUser.get("id"));
		Record r = DataOrgUtil.getUserSetting(userid,"flag");
		if(r!=null){
			formSqlSb.append("and org_root_id="+r.get("id"));
		}
		formSqlSb.append(" order by status desc,create_date desc  ");
	}

}
