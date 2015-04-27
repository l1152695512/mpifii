package com.yinfu.business.application.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.Consts;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

@TableBind(tableName = "bp_app")
public class App extends Model<App>
{
	private static final long serialVersionUID = 1813462134625829854L;
	public static App dao = new App();
	

	/**
	 *  获取所有可用的应用
	 */
	public List getAll()
	{
		StringBuffer sql = new StringBuffer("select ");
		sql.append("a.id,a.edit_url,a.classify,b.name as className,a.name,a.icon,a.link,a.des,a.create_date ");
		sql.append("from bp_app a ");
		sql.append("left join bp_app_class b ");
		sql.append("on a.classify = b.id ");
		sql.append("where a.status = 1 ");
		
		List list = dao.find(sql.toString());
		return list;
	}
	
	/**
	 * 获取所有应用类型
	 */
	public List getAppType()
	{
		 List list = dao.find("select id,name from bp_app_class");
		 return list;
	}
	
	public JSONObject add(String name, String logo, String des) {
		App app = new App();
		app.set("", "");
		JSONObject json = new JSONObject();
		json.put("success", app.save());
		return json;
	}

	//@formatter:off 
	/**
	 * Title: getAppList
	 * Description:获得应用列表
	 * Created On: 2014年9月25日 上午10:40:37
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage
	 * @return 
	 */
	//@formatter:on
	public SplitPage getAppList(SplitPage splitPage) {
		String sql = "SELECT s.*,c.`name` AS className ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_app s LEFT JOIN bp_app_class c  ON s.`classify` = c.`id` ");
		formSqlSb.append("where 1=1 and s.delete_date is null ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and s."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and s."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		formSqlSb.append(" ORDER BY s.create_date DESC");
	}

	//@formatter:off 
	/**
	 * Title: getAppClass
	 * Description:获得应用类型
	 * Created On: 2014年9月25日 下午2:31:31
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Record> getAppClass() {
		String sql = " select id,name from bp_app_class where delete_date is null ";
		return Db.find(sql);
	}
}
