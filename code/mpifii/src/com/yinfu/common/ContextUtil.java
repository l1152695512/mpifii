package com.yinfu.common;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.Consts;
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
			if(curUser!=null){
				String userid = curUser.get("id").toString();
				StringBuffer sql = new StringBuffer("SELECT u.`id` AS userid,r.`id` AS roleid,u.`name` AS username ,r.`isadmin`  FROM system_user u  ");
				sql.append("  LEFT JOIN system_user_role ur ON u.`id` = ur.`user_id` ");
				sql.append(" LEFT JOIN system_role  r ON r.`id` = ur.`role_id` ");
				sql.append(" where u.id=?");
				Record r = Db.findFirst(sql.toString(), userid);
				if(r.getInt("isadmin")==1){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
}
