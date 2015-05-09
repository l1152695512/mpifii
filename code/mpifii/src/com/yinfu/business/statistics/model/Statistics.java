
package com.yinfu.business.statistics.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.RouterHelper;
import com.yinfu.model.SplitPage.SplitPage;


public class Statistics extends Model<Statistics> {
	
	/**
	 * 
	 */
	public int getOnLineTotal(String shopId){
		int sum = 0;
		String sql = " select remote_account,remote_pass from bp_device where shop_id = ?";
		List<Record> list = Db.find(sql, new Object[]{shopId});
		for(int i = 0;i<list.size();i++){
			Record record = list.get(i);
			String email = record.get("remote_account").toString();
			String password = record.get("remote_pass").toString();
			try{
				String token = RouterHelper.routerToken(email, password);
				String backData = RouterHelper.wifiClientList(token);
				JSONArray jsonarry = JSONArray.parseArray(backData);
				sum += jsonarry.size();
			}catch(Exception e){
			}
		}
		
		return sum;
	}
	
	
	public int getClientTotal(String shopId){
		int sum = 0;
		StringBuffer sql = new StringBuffer(" select ");
		sql.append(" ifnull(sum(case when to_days(now()) - to_days( a.auth_date) = 1 then 1 else 0 end),0) as y_total ");
		sql.append(" from bp_auth a ");
		sql.append(" left join bp_device b ");
		sql.append(" on a.dev_mac = b.mac ");
		sql.append(" where b.shop_id  = ? ");
		
		List list = Db.find(sql.toString(), new Object[]{shopId});
		Map map = (Map)list.get(0);
		sum = Integer.parseInt(map.get("y_total").toString());
		
		return sum;
	}
	
	public int getAdvClick(String shopId){
		int sum = 0;
		StringBuffer sql = new StringBuffer(" select ");
		sql.append(" ifnull(sum(case when to_days(now()) - to_days( a.create_date) = 1 then 1 else 0 end),0) as y_total ");
		sql.append(" from bp_adv_log a ");
		sql.append(" left join bp_device b ");
		sql.append(" on a.dev_mac = b.mac ");
		sql.append(" where b.shop_id  = ? ");
		
		List list = Db.find(sql.toString(), new Object[]{shopId});
		Map map = (Map)list.get(0);
		sum = Integer.parseInt(map.get("y_total").toString());
		
		return sum;
	}
	
	public int getAdvShow(String shopId){
		int sum = 0;
		StringBuffer sql = new StringBuffer(" select ");
		sql.append(" ifnull(sum(case when to_days(now()) - to_days( a.create_date) = 1 then 1 else 0 end),0) as y_total ");
		sql.append(" from bp_adv_log a ");
		sql.append(" left join bp_device b ");
		sql.append(" on a.dev_mac = b.mac ");
		sql.append(" where b.shop_id  = ? ");
		
		List list = Db.find(sql.toString(), new Object[]{shopId});
		Map map = (Map)list.get(0);
		sum = Integer.parseInt(map.get("y_total").toString());
		
		return sum;
	}
	
	public int getSmsTotal(String shopId){
		int sum = 0;
		String sql = "select ifnull(sum(send_num),0)  as sms_total from bp_sms where status = 1 and shop_id = ?";
		List list = Db.find(sql, new Object[]{shopId});
		Map map = (Map)list.get(0);
		sum = Integer.parseInt(map.get("sms_total").toString());
		
		return sum;
	}
	
	public List<Record> getShopList(String userId,String type,String ids){
		String sql = "select id,name from bp_shop where 1=1 ";
		if(!ContextUtil.isAdmin()){
			sql += " and user_id="+ContextUtil.getCurrentUserId();
		}
		
		if("".equals(ids) || ids == null){
			if("in".equals(type)){
				sql += " and id is null";
			}
		}else{
			sql += " and id "+type+" ("+ids+")";
		}
		List<Record> list = Db.find(sql);
		return list;
	}
	
	public SplitPage getShopList(SplitPage splitPage) {
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
		String sql = "select d.*,s.name shopName,if(date_add(report_date, interval "+interval+" second) > now(),online_num,-1) online_num,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from bp_device d ");
		formSqlSb.append(" left join bp_shop s on d.shop_id = s.id ");
		formSqlSb.append(" where 1=1 and s.delete_date is null ");
		if(!ContextUtil.isAdmin()){
			formSqlSb.append(" and d.user_id="+ContextUtil.getCurrentUserId());
		}
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and s."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else if(keyInfo.length > 1 && "in".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and s."+keyInfo[0]+" in ("+queryParam.get(key)+") ");
				}else{
					formSqlSb.append("and s."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		
		
		
		formSqlSb.append(" order by s.create_date desc ");
		
		System.out.println(formSqlSb);
	}
	
}
