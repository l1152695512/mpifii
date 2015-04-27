
package com.yinfu.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.Consts;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.system.model.Org;
import com.yinfu.system.model.User;

public class ContextUtil {
	//@formatter:off 
		/**
		 * Title: getCurrentUser
		 * Description:获得当前登录用户
		 * Created On: 2014年9月18日 下午3:17:13
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public static User getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			String key = Consts.SESSION_USER;
			return (User) SecurityUtils.getSubject().getSession().getAttribute(key);
			/* return (User) currentUser; */
		}
		return null;
	}
	
	//@formatter:off 
		/**
		 * Title: getCurrentUserId
		 * Description:获取当前用户ID
		 * Created On: 2014年9月18日 下午3:51:35
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public static String getCurrentUserId() {
		User curUser = getCurrentUser();
		if (curUser != null) {
			return curUser.getInt("id").toString();
		}
		return null;
	}
	
	//@formatter:off 
		/**
		 * Title: isAdmin
		 * Description:判断当前用户是不是管理员
		 * Created On: 2014年9月19日 下午3:09:23
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public static boolean isAdmin() {
		User curUser = getCurrentUser();
		if (curUser != null) {
			String userid = curUser.get("id").toString();
			StringBuffer sql = new StringBuffer("SELECT u.`id` AS userid,r.`id` AS roleid,u.`name` AS username ,r.`isadmin`  FROM system_user u  ");
			sql.append("  LEFT JOIN system_user_role ur ON u.`id` = ur.`user_id` ");
			sql.append(" LEFT JOIN system_role  r ON r.`id` = ur.`role_id` ");
			sql.append(" where u.id=?");
			Record r = Db.findFirst(sql.toString(), userid);
			if (r.getInt("isadmin") == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	//@formatter:off 
	/**
	 * Title: getOrgByUser
	 * Description:获得当前用户的组织信息
	 * Created On: 2014年12月26日 下午3:46:52
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public static List<Record> getOrgListByUser() {
		User curUser = getCurrentUser();
		List<Record> list = new ArrayList<Record>();
		if (curUser != null) {
			String userid = String.valueOf(curUser.get("id"));
			list = Org.dao.getListByUserId(userid);
			return list;
		}
		return list;
	}
	
	//@formatter:off 
	/**
	 * Title: getOrgListByUser
	 * Description:
	 * Created On: 2015年1月4日 下午9:14:21
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public static List<Shop> getShopListByOrg(String orgids) {
		List<Shop> list = new ArrayList<Shop>();
		if (orgids != null) {
			list = Shop.dao.getShopListByOrg(orgids);
			return list;
		}
		return list;
	}
	
	//@formatter:off 
	/**
	 * Title: getShopByUser
	 * Description:获得当前登录用户的商铺列表
	 * Created On: 2014年12月30日 上午11:22:50
	 * @author JiaYongChao
	 * <p>
	 * @return 返回的数据为1,2,3
	 */
	//@formatter:on
	public static String getShopByUser() {
		User curUser = getCurrentUser();
		String shops = " ";
		if (curUser != null) {
			String userid = String.valueOf(curUser.get("id"));
			shops = Shop.dao.findByUserId(userid);
			return shops;
		}
		return shops;
	}
	
	//@formatter:off 
	/**
	 * Title: getShopByUser
	 * Description:获得当前登录用户的商铺列表
	 * Created On: 2014年12月30日 下午8:02:46
	 * @author JiaYongChao
	 * <p>
	 * @return 返回的数据为List<Shop>
	 */
	//@formatter:on
	public static List<Record> getShopListByUser() {
		User curUser = getCurrentUser();
		List<Record> list = new ArrayList<Record>();
		if (curUser != null) {
			String userid = String.valueOf(curUser.get("id"));
			list = Shop.dao.getListByUserId(userid);
			return list;
		}
		return list;
	}
	
	//@formatter:off 
	/**
	 * Title: getNextLevleByUser
	 * Description:获得当前用户所在组织的下一级
	 * Created On: 2015年1月11日 上午11:21:13
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public static String getNextLevleByUser() {
		
		User curUser = getCurrentUser();
		String orgIds = " ";
		if (curUser != null) {
			String orgId = String.valueOf(curUser.get("org_id"));
			orgIds = Org.dao.getNextLevleByOrgId(orgId);
			return orgIds;
		}
		return orgIds;
	}
	
		//@formatter:off 
		/**
		 * Title: getNextListByUser
		 * Description:
		 * Created On: 2015年1月11日 下午1:51:17
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public static List<Record> getNextListByUser(String orgId) {
		User curUser = getCurrentUser();
		List<Record> list = new ArrayList<Record>();
		if (curUser != null) {
			if(orgId==null ||orgId.equals("")){
				 orgId = String.valueOf(curUser.get("org_id"));
			}
			list =  Org.dao.getNextListByOrgId(orgId);
			return list;
		}
		return list;
	}
}
