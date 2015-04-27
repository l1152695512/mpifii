package com.yinfu.business.application.model;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.yinfu.jbase.jfinal.ext.Model;

@TableBind(tableName = "bp_menu")
public class Menu extends Model<Menu>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1572663380671369664L;
	public static Menu dao = new Menu();
	

	/**
	 *  根据shopId获取所有菜单
	 */
	public Page<Menu> getListByShopId(int pageNum, int pageSize, String shopId)
	{
		String sql = "select a.id,a.type,b.name typeName,a.theme,a.unit,a.taste,a.name,a.icon,a.old_price,a.new_price,a.times,a.create_date ";
		
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append(" from bp_menu a ");
		sqlExceptSelect.append(" left join bp_menu_type b ");
		sqlExceptSelect.append(" on a.type = b.id ");
		sqlExceptSelect.append(" where b.shop_id= "+shopId);
		sqlExceptSelect.append(" order by  a.type ");
		
		Page<Menu> page = dao.paginate(pageNum, pageSize, sql, sqlExceptSelect.toString());
		return page;
	}
	
	/**
	 * 获取所有应用类型
	 */
	public List getTypeByShopId(String shopId)
	{
		 List list = dao.find("select id,name from bp_menu_type where shop_id = ?",new Object[]{shopId});
		 return list;
	}
	
	/**
	 * 添加菜单类型
	 */
	public JSONObject addType(String name,String shop_id){
		String sql = "insert into bp_menu_type(name,shop_id,create_date)  values('"+name+"','"+shop_id+"',now())";
		List<String> list = new ArrayList<String>();
		list.add(sql);
		boolean result = Db.batch(list, 1).length > 0;
		JSONObject json = new JSONObject();
		json.put("success", result);
		return json;
	}
	
	/**
	 * 修改菜单类型
	 */
	public JSONObject editType(String name,String id){
		boolean result = false;
		String sql = "update bp_menu_type set name = ? where id = ?";
		if(Db.update(sql,new Object[]{name,id})>0){
			result = true;
		};
		JSONObject json = new JSONObject();
		json.put("success", result);
		return json;
	}
	
}
