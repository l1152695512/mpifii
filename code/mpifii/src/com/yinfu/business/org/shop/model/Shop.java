
package com.yinfu.business.org.shop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.Consts;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

@TableBind(tableName = "bp_shop")
public class Shop extends Model<Shop> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Shop dao = new Shop();
	
	public List<Shop> list() {
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		return dao.find("select id,name from bp_shop where delete_date is null and owner = ? ", new Object[] { user.getId() });
	}
	
	public JSONObject delete(String id) {
		JSONObject json = new JSONObject();
		json.put("success", dao.deleteById(id));
		return json;
	}
	
	public JSONObject add(String name,String des) {
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		Shop shop = new Shop().set("owner", user.getId()).set("name", name).set("des", des);
		JSONObject json = new JSONObject();
		json.put("success", shop.save());
		return json;
	}
	
	public JSONObject edit(String id, String name, String des) {
		Shop shop = new Shop().set("id", id).set("name", name).set("des", des);
		JSONObject json = new JSONObject();
		json.put("success", shop.update());
		return json;
	}

	//@formatter:off 
	/**
	 * Title: getShopList
	 * Description:获得商铺列表
	 * Created On: 2014年9月23日 下午4:58:00
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage 
	 * @return 
	 */
	//@formatter:on
	public SplitPage getShopList(SplitPage splitPage) {
		String sql = "SELECT s.*,SUM(IF(d.`id` IS NULL ,0,1)) AS sbs ";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_shop s ");
		formSqlSb.append(" LEFT JOIN bp_device d on d.shop_id = s.id ");
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
		formSqlSb.append(" GROUP BY s.`id`  ");
		formSqlSb.append(" order by s.create_date desc ");
	}

	//@formatter:off 
	/**
	 * Title: checkOwner
	 * Description:检查此商铺是否绑定商户
	 * Created On: 2014年9月29日 上午11:50:53
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public boolean checkOwner(String shopid) {
		if(shopid!=null){
			String sql = "SELECT s.*,u.`id` AS userid FROM bp_shop s LEFT JOIN SYSTEM_USER u ON s.`owner` = u.`id` WHERE s.`delete_date` IS NULL and s.id="+shopid;
			Shop shop = dao.findFirst(sql);
			if(shop.get("userid")!=null){
				return true;
			}
			return false;
		}
		return false;
		
	}

	//@formatter:off 
	/**
	 * Title: getShopView
	 * Description:获得商铺详情
	 * Created On: 2014年9月29日 下午3:00:03
	 * @author JiaYongChao
	 * <p>
	 * @param shopid
	 * @param userid
	 * @return 
	 */
	//@formatter:on
	public Shop getShopView(String shopid, String userid) {
		if(shopid!=null){
			StringBuffer sql = new StringBuffer(" SELECT s.* ");
			sql.append(" FROM bp_shop s  ");
			sql.append(" WHERE s.`delete_date` IS NULL  ");
			sql.append(" and s.id="+shopid);
			Shop shop = dao.findFirst(sql.toString());
			return shop;
		}
		return new Shop();
	}

	//@formatter:off 
	/**
	 * Title: findListByUserId
	 * Description:通过用户id获得商铺列表
	 * Created On: 2014年9月29日 下午4:15:55
	 * @author JiaYongChao
	 * <p>
	 * @param userid
	 * @return 
	 */
	//@formatter:on
	public List<Shop> findListByUserId(String userid) {
		if(userid!=null){
			String sql = " select * from bp_shop s where 1=1 ";
			if(!userid.equals("1")){
				sql+=" and s.owner= "+userid;
			}
			sql+=" and s.delete_date is null ";
			return dao.find(sql);
		}
		return new ArrayList<Shop>();
	}

	public List<Shop> findInfoByName(String name) {
		String sql=" SELECT t.`id`,t.`name`,u.`name` AS username FROM bp_shop t LEFT JOIN SYSTEM_USER u ON t.`owner` = u.`id` WHERE t.`delete_date` IS NULL  ";
		if(name!=null || !name.equals("")){
			sql+=" AND t.name LIKE '%"+name+"%' ";
		}
		return dao.find(sql);
	}

	//@formatter:off 
	/**
	 * Title: finAll
	 * Description:查询全部商铺
	 * Created On: 2014年12月2日 上午10:52:24
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Shop> finAll() {
		String sql=" SELECT t.`id`,t.`name`,u.`name` AS username FROM bp_shop t LEFT JOIN SYSTEM_USER u ON t.`owner` = u.`id` WHERE t.`delete_date` IS NULL ";
		if(!ContextUtil.isAdmin()){
			sql+=" and t.owner = "+ContextUtil.getCurrentUserId();
		}
		return dao.find(sql);
	}

}
