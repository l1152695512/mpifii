package com.yinfu.system.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sun.xml.internal.ws.message.StringHeader;
import com.yinfu.jbase.jfinal.ext.ListUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.shiro.ShiroCache;

@TableBind(tableName = "bp_platform")
public class Platform extends Model<Platform>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static Platform dao = new Platform();
	
	public List<String> getRolesName(String loginName)
	{
		return getAttr(sql("system.role.getRolesName"), "name", loginName);
	}
	
	public boolean grant(Integer[] role_ids, Integer userId)
	{
		boolean result = Db.deleteById("system_user_role", "user_id", userId);

		if (role_ids == null) return result;

		Object[][] params = ListUtil.ArrayToArray(userId, role_ids);
		result = Db.batch("insert into system_user_role(user_id,role_id)  values(?,?)", params, role_ids.length).length > 0;

		ShiroCache.clearAuthorizationInfoAll();

		return result;
	}

	public Platform encrypt()
	{
		String pwd = this.getPwd();
		if(Validate.isEmpty(pwd))pwd="123456";
		
		this.set("pwd", Sec.md5(pwd));
		return this;
	}

	public boolean batchGrant(Integer[] role_ids, String uids)
	{
		boolean result = Db.update("delete from system_user_role where user_id in (" + uids + ")") > 0;

		if (role_ids == null) return result;

		Object[][] params = ListUtil.ArrayToArray(uids, role_ids);

		result = Db.batch("insert into system_user_role(user_id,role_id)  values(?,?)", params, params.length).length > 0;

		ShiroCache.clearAuthorizationInfoAll();

		return result;
	}

	public boolean changeStaus(Integer id, Integer status)
	{
		if(status==null)return false;
		if(status.equals(1)) status=2;
		else status=1;
		return dao.update("status",status,id) ;

	}

	//@formatter:off 
	/**
	 * Title: getUserPic
	 * Description:获得用户图片
	 * Created On: 2014年7月29日 下午3:38:09
	 * @author JiaYongChao
	 * <p>
	 * @param valueOf 
	 */
	//@formatter:on
	public void getUserPic(Long id,HttpServletResponse response) {
		
	}
	
	

	//@formatter:off 
	/**
	 * Title: findList
	 * Description:分页显示数据
	 * Created On: 2014年8月4日 上午9:56:22
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage 
	 */
	//@formatter:on
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * FROM bp_platform ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
/*	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from system_user su ");
		formSqlSb.append("left join system_user_role sur on sur.user_id =su.id ");
		formSqlSb.append("left join system_role sr on sr.id = sur.role_id ");
		formSqlSb.append("left join bp_device d1 on (d1.user_id=su.id) ");
		formSqlSb.append("left join bp_shop s on (s.owner=su.id) ");
		formSqlSb.append("left join bp_device d2 on (d2.shop_id=s.id) ");
		formSqlSb.append("where 1=1  ");
		if(null != queryParam){
			Iterator<String> ite = queryParam.keySet().iterator();
			while(ite.hasNext()){
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if(keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])){
					formSqlSb.append("and su."+keyInfo[0]+" like '"+DbUtil.queryLike(queryParam.get(key))+"' ");
				}else{
					formSqlSb.append("and su."+key+"='"+queryParam.get(key)+"' ");
				}
			}
		}
		formSqlSb.append("group by su.id ");
	}*/

	//@formatter:off 
	/**
	 * Title: getUserList
	 * Description:获得用户list
	 * Created On: 2014年9月29日 上午10:11:59
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Platform> getUserList() {
		String sql ="SELECT id,name,down_url,auth_url,log_ip,log_port,home_url,create_date FROM bp_platform ";
		return dao.find(sql);
	}

	//@formatter:off 
	/**
	 * Title: findInfoById
	 * Description:
	 * Created On: 2014年11月12日 下午3:47:46
	 * @author JiaYongChao
	 * <p>
	 * @param para
	 * @return 
	 */
	//@formatter:on
	public Platform findInfoById(String id) {
		String sql=" SELECT t.*  FROM bp_platform t ";
		sql+=" WHERE 1=1 and  t.id='"+id+"'";
		return dao.findFirst(sql);
	}
	/**
	 * 
	 * updateUserRole(这里用一句话描述这个方法的作用)    
	 * TODO(这里描述这个方法适用条件 – 可选)    
	 * TODO(这里描述这个方法的执行流程 – 可选)    
	 * TODO(这里描述这个方法的使用方法 – 可选)    
	 * TODO(这里描述这个方法的注意事项 – 可选)    
	 * @param   object    
	 * @param string 
	 * @param  @return    设定文件    
	 * @return String    DOM对象    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	//@formatter:on
	public void updateUserRole(Object object,Object object2, Object object3,Object object4,Object object5, Object object6,String userId,int a) {
			if(a==1){
				String sql=" insert into bp_platform set name='"+object+"' , down_url='"+object2+"' ,auth_url='"+object3+"'   ,log_ip='"+object4+"' ,log_port='"+object5+"' ,home_url='"+object6+"',id='"+UUID.randomUUID().toString()+"' ";
				Db.update(sql);
			}else{
				String sql2=" update bp_platform set name='"+object+"' , down_url='"+object2+"' ,auth_url='"+object3+"'   ,log_ip='"+object4+"' ,log_port='"+object5+"' ,home_url='"+object6+"'  where id='"+userId+"'";
				Db.update(sql2);
			}
	}
	/**
	   	根据表中的 
	 * checkOwner(这里用一句话描述这个方法的作用)    
	   
	 * TODO(这里描述这个方法适用条件 – 可选)    
	   
	 * TODO(这里描述这个方法的执行流程 – 可选)    
	   
	 * TODO(这里描述这个方法的使用方法 – 可选)    
	   
	 * TODO(这里描述这个方法的注意事项 – 可选)    
	   
	 * @param   name    
	   
	 * @param  @return    设定文件    
	   
	 * @return String    DOM对象    
	   
	 * @Exception 异常对象    
	   
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	//@formatter:on
	public boolean checkOwner(String shopid) {
		if(shopid!=null){
			String sql = "SELECT  p.id as userid from bp_warehouse p  left join bp_platform b on b.id=p.palt_id  where p.palt_id is not null and  p.palt_id  ='"+shopid+"'";//palt_id
			Platform shop = dao.findFirst(sql);
			if(StringUtils.isEmpty((String) shop.get("userid"))){//userid
				return true;
			}
			return false;
		}
		return false;
	}
}
