package com.yinfu.system.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD

import javax.servlet.http.HttpServletResponse;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
=======
import javax.servlet.http.HttpServletResponse;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
import com.yinfu.jbase.jfinal.ext.ListUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.shiro.ShiroCache;

@TableBind(tableName = "system_user")
public class User extends Model<User>
{
	private static final long serialVersionUID = -7615377924993713398L;

	public static User dao = new User();
	
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

	public User encrypt()
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
		sql.append("select su.id,su.name,su.des,su.status,su.email,date_format(su.date,'%Y-%m-%d %H:%i:%s') date,");
<<<<<<< HEAD
		sql.append("sr.name as roleName,org.name orgName ");
=======
		sql.append("sr.name as roleName,count(IFNULL(d1.id,d2.id)) device_num ");
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
<<<<<<< HEAD
		User user = ContextUtil.getCurrentUser();
		
		List<Record> list = DataOrgUtil.getChildrens(user.getInt("org_id"), true);
		
		formSqlSb.append("from system_user su ");
		formSqlSb.append("left join sys_org org on su.org_id =org.id ");
		formSqlSb.append("left join system_user_role sur on sur.user_id =su.id ");
		formSqlSb.append("left join system_role sr on sr.id = sur.role_id ");
		formSqlSb.append("where 1=1 ");
		formSqlSb.append("and su.org_id in("+DataOrgUtil.recordListToSqlIn(list,"id")+") ");
=======
		formSqlSb.append("from system_user su ");
		formSqlSb.append("left join system_user_role sur on sur.user_id =su.id ");
		formSqlSb.append("left join system_role sr on sr.id = sur.role_id ");
		formSqlSb.append("left join bp_device d1 on (d1.user_id=su.id) ");
		formSqlSb.append("left join bp_shop s on (s.owner=su.id) ");
		formSqlSb.append("left join bp_device d2 on (d2.shop_id=s.id) ");
		formSqlSb.append("where 1=1  ");
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
	}

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
	public List<User> getUserList() {
<<<<<<< HEAD
		String sql ="SELECT u.id,u.name,SUM(IF(d.`id` IS NULL,0,1)) AS sbs FROM SYSTEM_USER u LEFT JOIN bp_device d ON d.`create_user` = u.`id` where u.delete_date is null  GROUP BY u.`id`  ";
=======
		String sql ="SELECT u.id,u.name,SUM(IF(d.`id` IS NULL,0,1)) AS sbs FROM SYSTEM_USER u LEFT JOIN bp_device d ON d.`user_id` = u.`id` where u.delete_date is null  GROUP BY u.`id`  ";
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
	public User findInfoById(String id) {
<<<<<<< HEAD
		String sql=" SELECT t.*,s.`id` AS roleId,s.`name` AS roleName,t.org_id,org.name as orgName FROM SYSTEM_USER t ";
		sql+=" LEFT JOIN sys_org org ON t.org_id = org.id ";
=======
		String sql=" SELECT t.*,s.`id` AS roleid,s.`name` AS rolename FROM SYSTEM_USER t ";
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		sql+=" LEFT JOIN system_user_role r ON t.`id` = r.`user_id` ";
		sql+=" LEFT JOIN system_role s ON s.`id` = r.`role_id` ";
		sql+=" WHERE t.`delete_date` IS NULL AND t.`id`="+id;
		return dao.findFirst(sql);
	}

	//@formatter:off 
	/**
	 * Title: updateUserRole
	 * Description:
	 * Created On: 2014年11月13日 下午3:33:23
	 * @author JiaYongChao
	 * <p>
	 * @param roleId 
	 * @param userId 
	 * @param i 
	 */
	//@formatter:on
	
	public void updateUserRole(String roleId, int userId, int i) {
		/*String sql=" select * from system_user_role t where  t.user_id="+userId+" and t.role_id ";*/
		if(i==0){
			String sql = " insert into system_user_role set role_id="+roleId+" ,user_id="+userId;
			Db.update(sql);
		}else{
			String sql =" select * from system_user_role where user_id="+userId;
			List list = Db.find(sql);
			if(list.size()==0){
				String sql1= " insert into system_user_role set role_id="+roleId+" ,user_id="+userId;
				Db.update(sql1);
			}else{
				String sql2=" update system_user_role set role_id="+roleId+" where user_id="+userId;
				Db.update(sql2);
			}
			
		}
		
	}
<<<<<<< HEAD
	//@formatter:off 
	/**
	 * Title: findByUserName
	 * Description: 根据用户名查询用户信息
	 * Created On: 2015年1月20日 上午10:08:39
	 * @author JiaYongChao
	 * <p>
	 * @param userName 
	 */
	//@formatter:on
	public User findByUserName(String userName){
		String sql ="select * from system_user where name=?";
		User user = dao.findFirst(sql,userName);
		return user;
	}
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
}
