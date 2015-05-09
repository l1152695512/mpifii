
package com.yinfu.system.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName = "bp_warehouse")
public class Page extends Model<Page> {
	private static final long serialVersionUID = -1288010102111787215L;
	
	public static Page dao = new Page();
	
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_warehouse  ");
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
		
		public List<Page> findNoUserDeviceInfo() {
			String sql = "  select * from bp_warehouse t where 1=1 and  t.palt_id is null ";
			return dao.find(sql);
		}
}
