package com.yinfu.jbase.util.remote;

public interface APIDefine {
	/**
	 * 登录中转服务器
	 */
	public static String SERVICE_LOGIN = "service/login";
	/**
	 * 描述：永久断开或重新允许设备连上路由器，通过mac标识。 URL：
	 * http://192.168.1.1/api/0/module/lan_kick_set 输入参数： 参数名 必选 描述 mac 是
	 * 要断开或恢复的mac地址。 option 否 取值 +|-（默认为+），+ 表示永久断开，- 表示允许连接。
	 */
	public static String LAN_KICK_SET = "module/lan_kick_set";
	/**
	 * 描述：返回当前被永久断开的设备的mac列表（黑名单）。
	 * URL：http://192.168.1.1/api/0/module/lan_kick_get 输入参数：无 返回：JSON
	 * 数组，每个成员就是被永久断开的（进入了黑名单）设备mac。
	 */
	public static String LAN_KICK_GET = "module/lan_kick_get";
	/**
	 * 描述：设置认证页面的url。 URL： http://192.168.1.1/api/0/ifidc/authurl_set 输入参数： 参数名
	 * 必选 描述 auth 是 认证页面的url ad 是 广告页的url
	 */
	public static String AUTHURL_SET = "ifidc/authurl_set";

	public static String AUTHURL_GET = "ifidc/authurl_get";
	/**
	 * 描述：设置可直接通过ip以（这些IP的上网时间不受限制）。 URL：
	 * http://192.168.1.1/api/0/ifidc/passlist_set 输入参数： 参数名 必选 描述 member(表单数组)
	 * 否 允许通过的MAC，无此字段则表示空数组
	 */
	public static String PASS_LIST_SET = "ifidc/passlist_set";
	/**
	 * 描述：返回当前的内网白名单列表和超时时间设置。 URL： http://192.168.1.1/api/0/ifidc/passlist_get
	 * 输入参数：无 返回：JSON 数组，包含所有member的mac。 JSON格式的返回例子
	 */
	public static String PASS_LIST_GET = "ifidc/passlist_get";
	/**
	 * 描述：设置可直接访问的外网主机白名单。 URL： http://192.168.1.1/api/0/ifidc/whitelist_set
	 * 输入参数： 参数名 必选 描述 MAC（表单数组） 否 白名单中的IP列表（无此字段则表示空数组） domain（表单数组） 否
	 * 白名单中的domain列表（无此字段则表示空数组）
	 * 
	 * 返回：状态码。 JSON格式的返回例子
	 */
	public static String WHITE_LIST_SET = "ifidc/whitelist_set";
	/**
	 * 描述：返回当前的外网白名单设置。 URL： http://192.168.1.1/api/0/ifidc/whitelist_get 输入参数：无
	 * 返回：JSON 对象，包含ip和domain两个数组字段。 JSON格式的返回例子
	 */
	public static String WHITE_LIST_GET = "ifidc/whitelist_get";
	/**
	 * 描述：设置用户可用上网时间。 URL： http://192.168.1.1/api/0/ifidc/timeout_set 输入参数： 参数名
	 * 必选 描述 timeout 是 允许用户上网的时间。单位秒
	 */
	public static String TIMEOUT_SET = "ifidc/timeout_set";
	/**
	 * 返回路由器的状态及相关信息 描述:返回路由器绑定信息(除了 token )。 授权:管理员,路由器被绑定到的一般账户。 请求方法:GET
	 * 请求参数: • router_id: 该参数可来自请求 URL 或者请求表单 正常响应: • 200 OK • json • • • • • •
	 * 格式响应内容 id ssid mac_address user: 绑定到的账户的基本信息,包括: • id • name • email
	 * bound_at: 路由器绑定时间 online_ip_address: 路由器在线 IP 地址信息, 内容为 IP 地址及其报告时间组成的数组
	 * [ip_address, reported_at]. 注意:如果路由器尚未主动访问过主网站查询获取中转任务的话, 该项不存在。 错误响应: •
	 * 400 Bad Request • 401 Unauthorized • 403 Forbidden • 404 Not Found
	 */
	public static String ROUTER_STATE = "service/router_state";
	/**
	 * 文件浏览
	 */
	public static String FILE_LIST = "cgi-bin/luci/syncboxlite/0/metadata/syncbox%s";

	/**
	 * 描述：设置路由器重启。 URL： http://192.168.1.1/api/0/module/sys_reboot_set 输入参数： 参数名
	 * 必选 描述 query 否 支持长操作，该参数单独使用。 query=1表示查询最近一次执行结果的状态码。 返回 -1表示还没进行任何操作；
	 * 返回1表示上一次操作仍然未完成； 返回其它数字，表示上一次操作已完成，数字便是其状态码。
	 */
	public static String SYS_REBOOT = "module/sys_reboot_set";
	/**
	 * 描述：设置路由器升级。 URL： http://192.168.1.1/api/0/module/sys_upgrade_set 输入参数：
	 * 参数名 必选 描述 query 否 支持长操作，该参数单独使用。 query=1表示查询最近一次执行结果的状态码。 返回
	 * -1表示还没进行任何操作； 返回1表示上一次操作仍然未完成； 返回其它数字，表示上一次操作已完成，数字便是其状态码。
	 */
	public static String SYS_UPGRADE = "module/sys_upgrade_set";

	/**
	 * 描述：获取连接此路由器的wifi客户端列表。
	 * URL：http://192.168.1.1/api/0/module/wifi_client_list 输入参数：无
	 * 返回：JSON数组，为每个客户端设备信息，包含“mac”，“ip”，“link” ，“host” ，“factory”等字段。
	 * JSON格式的返回例子（其中link为0表示，连接在主wifi上，为1表示连接在guest上）
	 */
	public static String WIFI_CLIENT_LIST = "module/wifi_client_list";

	/**
	 * 描述：返回指定设备的信息。 包含devname，ip/mask，mac，gateway，uptime（在线时间），累计TX/RX，实时 TX/RX
	 * 等信息。 URL： http://192.168.1.1/api/0/common/deviceinfo 输入参数： 参数名 必选 描述 dev
	 * 否 指定设备名，只返回该设备的信息；不指定返回所有设备的信息。 type 否
	 * Type为字母b，f组合，默认为”bf”，存在b表示返回基本设备信息，存在f表示返回流量信息
	 */
	public static String DEVICE_INFO = "common/deviceinfo";

	/**
	 * 描述：描述：下载功能API（此API不作逻辑处理，只将任务转交给后台的下载器。） URL：
	 * http://192.168.1.1/api/0/extension/ext_download 输入参数：以下参数请参考下载API的文档 参数名
	 * 必选 描述 Json 是 下载的JSON命令（具体命令另见downloader api文档）。
	 */
	public static String EXT_DOWNLOAD = "extension/ext_download";
	/**
	 * 描述：同时设置路由器统一名称（目前包括系统名称，主wifii的ssid）和密码（目前包括登录密码，但不包括wifi密码）。
	 * URL： http://192.168.1.1/api/0/common/wizard_namepw_set 输入参数： 参数名 必选 描述
	 * hostname 是/否 路由器名称，最少含有1个最多允许25个字符，否则返回参数错误（2）。 不使用该项表示保留原来的值。
	 * hostname和pw不能同时不使用。 pw 是/否 设置新密码；最少8位，最多64位，否则返回参数错误（2）。 不使用该项表示保留原来的值。
	 * hostname和pw不能同时不使用。 query 否 支持长操作，该参数单独使用。 query=1表示查询最近一次执行结果的状态码。 返回
	 * -1表示还没进行任何操作； 返回1表示上一次操作仍然未完成； 返回其它数字，表示上一次操作已完成，数字便是其状态码。
	 * 
	 * 返回：状态码。支持长操作，status=1表示本次操作没有完成，需要等待。 JSON格式的返回例子
	 */
	public static String WIZARD_NAME_PW_SET = "common/wizard_namepw_set";
	
	/**
	 * 描述：同时设置路由器统一名称（目前包括系统名称，主wifi，guest wifi）和密码（目前包括登录密码，主wifi密码）。
	 */
	public static String WIFI_NAME_SET = "common/wizard_namepw_set";
	
	/**
	 * 描述：设置 wifi 的 mac 列表。
	 */
	public static String WIFI_MACL_LIST_SET = "module/wifi_macl_list_set";
	
	/**
	 * 描述：获取当前 wifi mac 的设置。
	 */
	public static String WIFI_MACL_GET = "module/wifi_macl_get";
	
	/**
	 * 描述：设置 wifi 的 mac 过滤的启停和黑白名单模式，同时让 mac 列表生效。
	 */
	public static String WIFI_MACL_BASIC_SET = "module/wifi_macl_basic_set";
	
	/**
	 * 描述：设置路由器统一名称（目前包括系统名称，主wifi，guest wifi）。
	 */
	public static String SYS_HOSTNAME_SET = "module/sys_hostname_set";
}
