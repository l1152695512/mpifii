
package com.yinfu.business.org.user.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;
import com.yinfu.Consts;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.org.role.model.Role;
import com.yinfu.business.org.shop.model.Shop;
import com.yinfu.business.org.user.model.User;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Validate;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.service.EmailService;
import com.yinfu.shiro.ShiroCache;
import com.yinfu.system.validator.UserValidator;

@ControllerBind(controllerKey = "/business/org/user")
public class UserController extends Controller<User> {

	public void index() {
		if(getPara("orgId") != null){
			splitPage.queryParam.put("org_id", getPara("orgId"));
		}
		SplitPage splitPages = User.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/system/org/user/index.jsp");
	}

	public void addOrModify() {
		String sql = "";
		if (StringUtils.isNotBlank(getPara("id"))) {
			User user = User.dao.findInfoById(getPara("id"));
			setAttr("user", user);
			
			sql = "SELECT a.id,a.name,b.user_id FROM system_role a LEFT JOIN system_user_role b ON a.id=b.role_id AND b.user_id=?  WHERE a.org_id=?";
			List<Role> roleList = Role.dao.find(sql,new Object[]{user.get("id"),user.get("org_id")});
			setAttr("roleList", roleList);
		}else{
			Map m = new HashMap();
			m.put("org_id", getPara("orgId"));
			setAttr("user", m);
			
			sql = "SELECT id,name,null user_id FROM system_role WHERE org_id=?";
			List<Role> roleList = Role.dao.find(sql,new Object[]{getPara("orgId")});
			setAttr("roleList", roleList);
		}
		render("/page/system/org/user/addOrModify.jsp");
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 删除
	 * Created On: 2014年8月5日 上午9:22:19
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	public void delete() {
		renderJsonResult(User.dao.update("status", 2, getPara("id")));
	}
	
	//@formatter:off 
	/**
	 * Title: freeze
	 * Description:
	 * Created On: 2014年8月5日 上午9:22:25
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void freeze() {
		renderJsonResult(User.dao.changeStaus(getParaToInt("id"), getParaToInt
		
		("status")));
	}
	
	//@formatter:off 
	/**
	 * Title: batchDelete
	 * Description:批量删除
	 * Created On: 2014年8月5日 上午9:20:13
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void batchDelete() {
		renderJsonResult(User.dao.batchDelete(getPara("ids")));
	}
	
	//@formatter:off 
	/**
	 * Title: batchGrant
	 * Description:批量授权
	 * Created On: 2014年8月5日 上午9:20:24
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void batchGrant() {
		
		Integer[] role_ids = getParaValuesToInt("role_ids");
		String ids = getPara("ids");
		
		renderJsonResult(User.dao.batchGrant(role_ids, ids));
		
		ShiroCache.clearAuthorizationInfoAll();
		
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存用户信息
	 * Created On: 2014年8月4日 下午6:49:52
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
		String icon = "";
		try {
			UploadFile file = getFile("userImage", PathKit.getWebRootPath() +"/" + PREVIEW_PATH);
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			icon = "upload/image/user/" + name + src.getName().substring(src.getName().indexOf('.'));
		} catch (Exception e) {
		}
		final User user = getModel();
		if (StringUtils.isNotBlank(icon)) {
			user.set("icon", icon);
		}
		boolean success = false;
		String roleIds = getPara("roleIds");
		if (user.get("id") != null && StringUtils.isNotBlank(user.get("id").toString())) {
			success = user.update();
			user.dao.setRole(user.get("id").toString(), roleIds);
			renderJsonResult(success);
		} else {
			if (checkUserName(user.getName())) {// 用户名重复
				renderJson("error", "userNameRepeat");
				return;
			}else if(ContextUtil.isAdmin() && StringUtils.isBlank(roleIds)){
				renderJson("error", "choiceRole");
				return;
			}else {
				success = Db.tx(new IAtom() {
					public boolean run() throws SQLException {
						String password = getPara("password");
						boolean insertStatus = user.set("pwd", Sec.md5(password)).saveAndDate();
						return insertStatus;
					}
				});
				User us = user.dao.findByName(user.getName());
				user.dao.setRole(us.getId().toString(), roleIds);
			}
			renderJsonResult(success);
		}
	}
	
	public boolean checkUserName(String userName) {
		User rec = User.dao.findByName(userName);
		return rec != null;
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: edit
	 * Description: 修改用户信息
	 * Created On: 2014年8月5日 上午9:20:44
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#edit()
	 */
	//@formatter:on
	@Before(value = { UserValidator.class })
	public void edit() {
		
		renderJsonResult(getModel().update());
		
	}
	
	//@formatter:off 
	/**
	 * Title: pwd
	 * Description:忘记密码
	 * Created On: 2014年8月5日 上午9:21:28
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	@Before(value = { UserValidator.class })
	public void pwd() {
		renderJsonResult(getModel().encrypt().update());
		
		// send eamil
		User user = User.dao.findById(getModel().getId());
		if (!Validate.isEmpty(user.getStr("email")))
			;
		new EmailService().sendModifyPwdEmail(user.getStr("email"));
		
	}
	
	//@formatter:off 
	/**
	 * Title: grant
	 * Description:授权
	 * Created On: 2014年8月5日 上午9:21:36
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void grant() {
		Integer[] role_ids = getParaValuesToInt("role_ids");
		renderJsonResult(User.dao.grant(role_ids, getModel().getId()));
		ShiroCache.clearAuthorizationInfoAll();
		
	}
	
	//@formatter:off 
	/**
	 * Title: changePassword
	 * Description:修改密码
	 * Created On: 2014年8月5日 上午9:21:42
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void changePassword() {
		String msg = "";
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		String oldPassword = getPara("oldPass");
		String newPassword = getPara("newPass");
		String repeatNewPassword = getPara("repeatNewPass");
		if (newPassword.equals(repeatNewPassword)) {
			if (user.getPwd().equals(Sec.md5(oldPassword))) {
				if (!newPassword.equals(oldPassword)) {
					String newPasswordEncrypt = Sec.md5(newPassword);
					boolean success = new User().set("id", user.getId
					
					()).set("pwd", newPasswordEncrypt).update();
					if (success) {// 更新session中的用户信息
						user.set("pwd", newPasswordEncrypt);
						msg = "更新成功，下次登录请使用新密码！";
					} else {
						msg = "更新失败稍后请重试！";
					}
				}
			} else {
				msg = "原始密码输入有误！";
			}
		} else {
			msg = "新密码两次输入不同！";
		}
		renderJson("msg", msg);
	}
	
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" +
	
	File.separator + "user" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: savePic
	 * Description:保存图片信息
	 * Created On: 2014年7月29日 下午3:52:01
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	@SuppressWarnings("deprecation")
	public void savePic() {
		
		User user = ShiroExt.getSessionAttr("user");
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" +
		
		PREVIEW_PATH);
		String name = String.valueOf(System.currentTimeMillis());
		ImageKit.renameFile(file, name);
		File src = file.getFile();
		String icon = "/upload/image/user/" + name + src.getName().substring
		
		(src.getName().indexOf('.'));
		if (true == User.dao.update("icon", icon, user.getId())) {
			User users = user.dao.findById(user.getId());
			setSessionAttr("user", users);
			redirect("/userIndex");
		}
	}
	
	//@formatter:off 
	/**
	 * Title: configDevice
	 * Description:配置设备信息
	 * Created On: 2014年9月24日 下午6:00:09
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void configDevice() {
		String userid = getPara("id");// 用户id
		List<Device> deviceList = Device.dao.findNoUserDeviceInfo();
		setAttr("infoid", userid);
		setAttr("type", "user");
		setAttr("deviceList", deviceList);
		render("/page/business/device/deviceList.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: view
	 * Description:用户详情
	 * Created On: 2014年9月29日 下午3:41:27
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void view() {
		String userid = getPara("id");
		User user = User.dao.findById(userid);
		setAttr("user", user);
		List<Device> deviceList = Device.dao.findListByUserId(userid);
		setAttr("deviceList", deviceList);
		List<Shop> shopList = Shop.dao.findListByUserId(userid);
		setAttr("shopList", shopList);
		render("/page/system/user/view.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: getUserInfo
	 * Description:获得用户信息
	 * Created On: 2014年10月27日 下午2:35:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getUserInfo() {
		String userid = ContextUtil.getCurrentUserId();
		User user = User.dao.findById(userid);
		setAttr("user", user);
		List<Device> deviceList = Device.dao.findListByUserId(userid);
		setAttr("deviceList", deviceList);
		List<Shop> shopList = Shop.dao.findListByUserId(userid);
		setAttr("shopList", shopList);
		render("/page/system/user/view.jsp");
	}
}
