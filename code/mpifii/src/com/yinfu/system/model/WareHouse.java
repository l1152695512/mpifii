
package com.yinfu.system.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_warehouse")
public class WareHouse extends Model<WareHouse> {
	private static final long serialVersionUID = -1288010102111787215L;
	
	public static WareHouse dao = new WareHouse();
	
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.sn,a.plat_no,b.name,date_format(a.create_date,'%Y-%m-%d %H:%i:%s') create_date ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_warehouse a left join bp_platform b on a.plat_no=b.plat_no ");
		formSqlSb.append("where 1=1 ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and a."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and a."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		formSqlSb.append("group by a.id ");
	}
	
	public void updateUserRole(Object object,int v,Object o) {
		if(v==1){
			String sql1=" insert into bp_warehouse set sn='"+object+"' ,id='"+UUID.randomUUID().toString()+"',create_date='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"' ";
			Db.update(sql1);	
		}else{
			String sql="update bp_warehouse set sn='"+object+"' ,create_date='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"' where id='"+o+"'";
			Db.update(sql);
		}
	}
	
	public List<WareHouse> findNoUserDeviceInfo() {
		String sql = "  select * from bp_warehouse t where 1=1 and  t.plat_no is null ";
		return dao.find(sql);
	}
}
