package com.yinfu.common;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import com.jfinal.ext.route.ControllerBind;
import com.yinfu.Consts;
import com.yinfu.UrlConfig;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.RSA;
import com.yinfu.jbase.util.Sec;
import com.yinfu.model.tree.Tree;
import com.yinfu.servlet.CaptchaServlet;
import com.yinfu.shiro.CaptchaException;
import com.yinfu.system.model.Res;
import com.yinfu.system.model.User;

/***
 * 
 * 
 * 
 * @author 
 * 
 */
@ControllerBind(controllerKey = "/")
public class CommonController extends Controller {
	//@formatter:off 
	/**
	 * Title: index
	 * Description:系统管理员首页
	 * Created On: 2014年7月21日 下午5:23:47
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index()
	{
		setAttr("menuList", showMneu());
		setAttr("user",ShiroExt.getSessionAttr(Consts.SESSION_USER));
		render(UrlConfig.VIEW_INDEX);
	}
	
	public void loginView()
	{
		RSAPublicKey publicKey = RSA.getDefaultPublicKey();
		String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
		String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
		setAttr("modulus", modulus);
		setAttr("exponent", exponent);
		render(UrlConfig.VIEW_COMMON_LOGIN);

	}
	//@formatter:off 
	/**
	 * Title: loginOut
	 * Description:注销
	 * Created On: 2014年7月21日 下午5:26:03
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void loginOut()
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
			RSAPublicKey publicKey = RSA.getDefaultPublicKey();
			String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
			String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
			setAttr("modulus", modulus);
			setAttr("exponent", exponent);
			render(UrlConfig.VIEW_COMMON_LOGIN);
		} catch (AuthenticationException e)
		{
			e.printStackTrace();
			renderText("异常：" + e.getMessage());
		}
	}

	//@formatter:off 
	/**
	 * Title: login
	 * Description:登录
	 * Created On: 2014年7月21日 下午5:26:11
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void login()
	{
		String[] result = RSA.decryptUsernameAndPwd(getPara("key"));

		try
		{
			// 增加判断验证码逻辑
			String captcha = getPara("validateCode");
			String exitCode = (String) getSession().getAttribute(CaptchaServlet.KEY_CAPTCHA);
			if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
				setAttr("msg", "验证码错误");
				throw new CaptchaException("验证码错误");
			}
			UsernamePasswordToken token = new UsernamePasswordToken(result[0], Sec.md5(result[1]));
			Subject subject = SecurityUtils.getSubject();
			User user = User.dao.findByName(result[0]);
			if (!subject.isAuthenticated())
			{
				token.setRememberMe(true);
				subject.login(token);
				subject.getSession(true).setAttribute(Consts.SESSION_USER,user);

			}
			redirect("/");

		} catch (UnknownAccountException e)
		{

			forwardAction("用户名不存在", UrlConfig.LOGIN);

		} catch (IncorrectCredentialsException e)
		{
			forwardAction("密码错误", UrlConfig.LOGIN);

		} catch (LockedAccountException e)
		{
			forwardAction("对不起 帐号被封了", UrlConfig.LOGIN);
			e.printStackTrace();
		} catch (ExcessiveAttemptsException e)
		{
			forwardAction("尝试次数过多 请明天再试", UrlConfig.LOGIN);
		} catch (AuthenticationException e)
		{
			forwardAction("对不起 没有权限 访问", UrlConfig.LOGIN);
		} catch (CaptchaException e)
		{
			forwardAction("验证码错误", UrlConfig.LOGIN);
		} catch (Exception e)
		{
			e.printStackTrace();
			forwardAction("请重新登录", UrlConfig.LOGIN);
		}

	}

	public void unauthorized()
	{

		render(UrlConfig.VIEW_ERROR_401);
	}


	public void forwardAction(String msg, String url) {

		setAttr("msg", msg);
		forwardAction(url);
	}
	
	//@formatter:off 
	/**
	 * Title: showMneu
	 * Description:首页菜单显示
	 * Created On: 2014年7月24日 下午3:25:13
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public List<Tree> showMneu(){
		List<Tree> menuList = Res.dao.getTree(null,1,null);
		return menuList;
	}
	//@formatter:off 
	/**
	 * Title: content
	 * Description:首页内容
	 * Created On: 2014年10月9日 上午10:15:26
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void content(){
		render("/page/index/content.jsp");
	}
}
