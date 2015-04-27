package com.yinfu.jbase.util.remote;

import java.net.URLEncoder;

import org.apache.commons.httpclient.NameValuePair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 常用接口的调用
 * 
 * @author Robert Lo
 * 
 */
public class RouterHelper {

	/**
	 * 访问登录后的其它的接口必须的参数，通过获得服务器信息接口获得
	 */
	private static String xsrf;
	public static String routerId;

	/**
	 * 中转服务器登录
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static String login(String email, String password) {
		YFHttpClient client = YFHttpClient.getInstance();

		xsrf = client.serverInfo();

		NameValuePair[] nameValuePairs = { new NameValuePair("email", email),
				new NameValuePair("password", password),
				new NameValuePair("return_router_states", "1"),
				new NameValuePair("_xsrf", xsrf),
				new NameValuePair("return_user_info", "1") };

		return client.httpPost(APIDefine.SERVICE_LOGIN, nameValuePairs);
	}

	/**
	 * 登录并且获得Router的Token,直接用这个可以解决第一步
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static String routerToken(String email, String password) {
		String loginResult = login(email, password);

		JSONObject obj = JSONObject.parseObject(loginResult);
		JSONArray states = (JSONArray) obj.get("router_states");
		JSONObject router = (JSONObject) states.get(0);
		routerId = router.getString("id");

		return router.getString("token");
	}

	/**
	 * 踢线功能
	 * 
	 * @param mac
	 * @param option
	 *            “+” :断开 “-” ：连接
	 * @return
	 */
	public static String kickSet(String mac, String token, String option) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("mac", mac),
				new NameValuePair("token", token),
				new NameValuePair("option", URLEncoder.encode(option)) };
		return client.httpRouterGet(APIDefine.LAN_KICK_SET, params);
	}

	/**
	 * 获得黑名单列表
	 * 
	 * @return
	 */
	public static String kickGet(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.LAN_KICK_GET, params);
	}

	/**
	 * 重启路由
	 * 
	 * @param mac
	 * @param option
	 * @return
	 */
	public static String reboot(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.SYS_REBOOT, params);
	}

	/**
	 * 在线升级固件
	 * 
	 * @param token
	 * @return
	 */
	public static String sysUpgrade(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterPost(APIDefine.SYS_UPGRADE, params);
	}

	/**
	 * 设置白名单
	 * 
	 * @param token
	 * @param macArray
	 * @return
	 */
	public static String passlistSet(String token, NameValuePair[] params) {
		YFHttpClient client = YFHttpClient.getInstance();
		return client.httpRouterGet(APIDefine.PASS_LIST_SET, params);
	}

	/**
	 * 获取白名单
	 * 
	 * @param token
	 * @return
	 */
	public static String passlistGet(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.PASS_LIST_GET, params);
	}

	/**
	 * 设置允许访问域名
	 * 
	 * @param token
	 * @param macArray
	 * @return
	 */
	public static String whitelistSet(String token, NameValuePair[] params) {
		YFHttpClient client = YFHttpClient.getInstance();
		// NameValuePair[] params = { new NameValuePair("token", token),
		// new NameValuePair("domain", macArray) };
		return client.httpRouterGet(APIDefine.WHITE_LIST_SET, params);
	}

	/**
	 * 获得允许访问域名
	 * 
	 * @param token
	 * @return
	 */
	public static String whitelistGet(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WHITE_LIST_GET, params);
	}

	/**
	 * 获得设备信息
	 * 
	 * @param token
	 * @return
	 */
	public static String deviceInfo(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.DEVICE_INFO, params);
	}

	/**
	 * 获得在线列表
	 * 
	 * @param token
	 * @return
	 */
	public static String wifiClientList(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WIFI_CLIENT_LIST, params);
	}

	/**
	 * 设置广告和认证页
	 * 
	 * @param authUrl
	 * @param adUrl
	 * @return
	 */
	public static String authurlSet(String authUrl, String adUrl, String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("ad", adUrl),
				new NameValuePair("token", token),
				new NameValuePair("_xsrf", xsrf),
				new NameValuePair("auth", authUrl) };
		return client.httpRouterGet(APIDefine.AUTHURL_SET, params);
	}
	
	public static String authurlGet(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { 
				new NameValuePair("token", token),
				new NameValuePair("_xsrf", xsrf)};
		return client.httpRouterGet(APIDefine.AUTHURL_GET, params);
	}

	/**
	 * 设置访问超时时间
	 * 
	 * @param timeout
	 * @return
	 */
	public static String timeoutSet(String timeout,String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("timeout", timeout),new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.TIMEOUT_SET, params);
	}

	/**
	 * 设置路由器的名称和密码
	 * 
	 * @param hostname
	 * @param pw
	 * @param quary
	 * @param token
	 * @return
	 */
	public static String wizardNamePWSet(String hostname, String pw,
			String token, String quary) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("hostname", hostname),
				new NameValuePair("pw", pw), new NameValuePair("quary", quary),
				new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WIZARD_NAME_PW_SET, params);
	}

	/**
	 * 获得路由的状态，后续用于实时检查是否在线
	 * 
	 * @param routerId
	 * @return
	 */
	public static String routerState(String routerId) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("router_id", routerId) };
		return client.httpGet(APIDefine.ROUTER_STATE, params);
	}

	public static String fileList(String fileType, String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("list", "true"),
				new NameValuePair("token", token),
				new NameValuePair("_xsrf", xsrf) };
		String function = String.format(APIDefine.FILE_LIST, fileType);
		return client.httpGet(function, params);
	}
	
	
	/**
	 * 描述：下载功能API（此API不作逻辑处理，只将任务转交给后台的下载器。） URL：
	 * http://192.168.1.1/api/0/extension/ext_download 输入参数：以下参数请参考下载API的文档 参数名
	 * 必选 描述 Json 是 下载的JSON命令（具体命令另见downloader api文档）。
	 * @param routerId
	 * @return
	 */
	public static String download(String json,String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("json", json),new NameValuePair("_xsrf", xsrf), new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.EXT_DOWNLOAD, params);
	}
	
	/**
	 * 设置wifi名称
	 * 
	 * @param wifiName
	 * @return
	 */
	public static String sys_hostname_set(String wifiName,String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("hostname", wifiName),new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.SYS_HOSTNAME_SET, params);
	}
	
	/**
	 * 新增黑名单
	 * 
	 * @param wifiName
	 * @return
	 */
	public static String wifi_macl_list_set(String mac,String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("action", "add"),new NameValuePair("mac", mac),new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WIFI_MACL_LIST_SET, params);
	}
	
	/**
	 * 删除黑名单
	 * 
	 * @param wifiName
	 * @return
	 */
	public static String wifi_macl_list_del(String id,String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("action", "del"),new NameValuePair("id", id),new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WIFI_MACL_LIST_SET, params);
	}
	
	/**
	 * 获取黑名单
	 * 
	 * @param wifiName
	 * @return
	 */
	public static String wifi_macl_get(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WIFI_MACL_GET, params);
	}
	
	public static String log(String token,String ip,String port) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token),new NameValuePair("ip", ip),new NameValuePair("port", port) };
		return client.httpRouterGet("ifidc/logserver_set", params);
	}
	
	public static String glog(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("token", token) };
		return client.httpRouterGet("ifidc/logserver_get", params);
	}
	
	/**
	 * 设置 wifi 的 mac 过滤的启停和黑白名单模式，同时让 mac 列表生效。
	 * @param wifiName
	 * @return
	 */
	public static String wifi_macl_basic_set(String enable,String white,String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { new NameValuePair("enable", enable),new NameValuePair("white", white),new NameValuePair("token", token) };
		return client.httpRouterGet(APIDefine.WIFI_MACL_BASIC_SET, params);
	}
	
	/**
	 * 数据同步方法
	 * @param userName
	 * @param password
	 * @param sourcePath
	 * @param savePath
	 * @return
	 */
	public static boolean sysData(String userName,String password,String sourcePath,String savePath){
		boolean flag = false;
		String token = RouterHelper.routerToken(userName, password);
		
		JSONObject setJson = new JSONObject();
		setJson.put("cmd", "setdownopt");

		JSONObject setObj = new JSONObject();
		setObj.put("down-speed", "0");
		setObj.put("errcode", "0");
		setObj.put("up-speed", "51200");
		setObj.put("seed-time", "60");
		setObj.put("up-each", "20480");
		setObj.put("max-down", "3");
		setObj.put("down-dir", savePath);
		setObj.put("down-each", "0");
		
		JSONObject option = new JSONObject();
		option.put("option", setObj);

		setJson.put("param", option);
		String backSetData = RouterHelper.download(setJson.toString(),token);
		JSONObject bkSetJson = JSONObject.parseObject(backSetData);
		
		if (bkSetJson.containsKey("errcode") && "0".equals(bkSetJson.getString("errcode"))) {
			JSONObject json = new JSONObject();
			json.put("cmd", "add");
			String[] array = new String[] { URLEncoder.encode(sourcePath) };
			JSONObject obj = new JSONObject();
			obj.put("link", array);
			obj.put("islocal", false);
			json.put("param", obj);
			String backData = RouterHelper.download(json.toString(), token);
			JSONObject bkJson = JSONObject.parseObject(backData);
			if (bkJson.containsKey("errcode") && "0".equals(bkJson.getString("errcode"))) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	public static String sys_upgrade_get(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { 
				new NameValuePair("token", token),
				new NameValuePair("_xsrf", xsrf)};
		return client.httpRouterGet("module/sys_upgrade_get", params);
	}
	
	public static String sys_upgrade_set(String token) {
		YFHttpClient client = YFHttpClient.getInstance();
		NameValuePair[] params = { 
				new NameValuePair("token", token),
				new NameValuePair("_xsrf", xsrf)};
		return client.httpRouterGet("module/sys_upgrade_set", params);
	}
	

	/**
	 * 测试范例，一般情况下，直接调用routerToken(调用一次)方法，得到token后就可以对路由器进行其它控制
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		YFHttpClient.getInstance().setShowLog(true);
		
		sysData("test2@pifii.com","123456","http://58.67.196.187:8083/ttopyd/apps/测试.txt","/storageroot/data/doc");
		

//		String token = routerToken("minglo@syncbox.cn", "aaaaabbbbb");
//
//		if (token.length() > 0) {
//			// routerState(routerId);
//			wifiClientList(token);
//			// String list = fileList("demo/Video",token);
//			// String kickList = kickGet(token);
//			// System.out.println(kickList);
//			// System.out.println(list);
//		}

	}
}
