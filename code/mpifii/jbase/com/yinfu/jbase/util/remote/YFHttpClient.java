package com.yinfu.jbase.util.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
<<<<<<< HEAD

=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
<<<<<<< HEAD

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yinfu.jbase.util.PropertyUtils;
=======
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901

/**
 * 专门用于调用服务端接口的类 默认构造调用中转服务器地址
 * 
 * @author Robert Lo
 * 
 */
public class YFHttpClient extends HttpClient {
	/**
	 * 中转服务器地址
	 */
	private static String HOST = "https://witfii.com/";
	/**
	 * 路由操作的固定后缀
	 */
	private static String ROUTER_ROOT = "cgi-bin/luci/api/0/";

	private static YFHttpClient m_instance = null;
	/**
	 * 当前请求的根地址，默认为中转服务器地址，可以在操作情况下动态变更，使请求的根地址变化
	 */
	private String apiURL = HOST;
	/**
	 * 编码方式
	 */
	private String charset = "UTF-8";
	/**
	 * 是否输出请求返回的结果，默认为要输出
	 */
	private boolean showLog = true;
<<<<<<< HEAD
	
	static{
		HOST = PropertyUtils.getProperty("route.host", "https://a.witfii-backend.com/");
	}
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901

	public synchronized static YFHttpClient getInstance() {

		if (m_instance == null) {
			m_instance = new YFHttpClient();
		}

		return m_instance;
	}

	/**
	 * 默认的构造
	 * 
	 * @param apiURL
	 * @param charset
	 */
	public YFHttpClient() {
		getHttpConnectionManager().getParams().setConnectionTimeout(10000);
	}

	/**
	 * 不同的请求地址和编码构造
	 * 
	 * @param apiURL
	 * @param charset
	 */
	public YFHttpClient(String apiURL, String charset) {
		this.apiURL = apiURL;
		this.charset = charset;
		getHttpConnectionManager().getParams().setConnectionTimeout(10000);
	}

	/**
	 * 获得设备信息
	 * 
	 * @return xsrf 其它的信息丢弃，如需其它信息重写该方法
	 */
	public String serverInfo() {
		httpGet("server_info", null);

		Cookie[] cookies = getState().getCookies();

		String xsrf = "";

		for (Cookie c : cookies) {
			if (c.getName().equalsIgnoreCase("_xsrf")) {
				xsrf = c.getValue();
				break;
			}
		}

		return xsrf;
	}

	/**
	 * 登录
	 * 
	 * @param email
	 * @param password
	 * @param xsrf
	 * @return
	 */
	public String login(String email, String password, String xsrf) {
		String url = String.format("%s%s", apiURL, "service/login");

		NameValuePair[] nameValuePairs = { new NameValuePair("email", email),
				new NameValuePair("password", password),
				new NameValuePair("return_router_states", "1"),
				new NameValuePair("_xsrf", xsrf),
				new NameValuePair("return_user_info", "1") };

		return startPostRequest(url, nameValuePairs, charset, true);
	}
	
	public String token(String email, String password) {
		String xsrf = serverInfo();
		String loginResult = login(email, password, xsrf);

		JSONObject obj = JSONObject.parseObject(loginResult);
		JSONArray states = (JSONArray) obj.get("router_states");
		JSONObject router = (JSONObject) states.get(0);
		String token = router.getString("token");

		return token;
	}

	/**
	 * 针对路由器的Get请求
	 * 
	 * @param function
	 *            接口名
	 * @param params
	 *            入参
	 * @return
	 */
	public String httpRouterGet(String function, NameValuePair[] params) {
		String url = String.format("%s%s%s", apiURL, ROUTER_ROOT, function);
		return startGetRequest(url, params, charset);
	}

	/**
	 * 针对中间服务器的Get请求
	 * 
	 * @param function
	 *            接口名
	 * @param params
	 *            入参
	 * @return
	 */
	public String httpGet(String function, NameValuePair[] params) {
		String url = String.format("%s%s", apiURL, function);
		return startGetRequest(url, params, charset);
	}

	/**
	 * 针对路由器的Post请求
	 * 
	 * @param function
	 *            接口名
	 * @param params
	 *            入参
	 * @return
	 */
	public String httpRouterPost(String function, NameValuePair[] params) {
		String url = String.format("%s%s%s", apiURL, ROUTER_ROOT, function);
		return startPostRequest(url, params, charset, true);
	}

	/**
	 * 针对中间服务器的Post请求
	 * 
	 * @param function
	 *            接口名
	 * @param params
	 *            入参
	 * @return
	 */
	public String httpPost(String function, NameValuePair[] params) {
		String url = String.format("%s%s", apiURL, function);
		return startPostRequest(url, params, charset, true);
	}

	/**
	 * 通用Get 方法
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public String startGetRequest(String url, NameValuePair[] params,
			String charset) {

		url = urlWithParams(url, params);

		GetMethod method = new GetMethod(url);
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);   
		Protocol.registerProtocol("https", myhttps); 
		
		String response = "";

		method.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=" + charset);

		
		try {
			int statusCode = executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("请求失败: " + method.getStatusLine());
			}

			byte[] responseBody = method.getResponseBody();
			response = new String(responseBody, charset);

			System.out.println("URL: " + url);
			System.out.println("StatusCode:" + method.getStatusCode());

			resultOutput(response);

		} catch (HttpException e) {
			System.err.println("执行GET请求" + url + "时异常:" + e.toString());
		} catch (IOException e) {
			System.err.println("执行GET请求" + url + "时异常:" + e.toString());
		} finally {
			method.releaseConnection();
		}

		return response;
	}

	/**
	 * 通用POST 方法
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @param pretty
	 * @return
	 */
	public String startPostRequest(String url, NameValuePair[] params,
			String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
//		url=URLEncoder.encode(url);
		PostMethod method = new PostMethod(url);
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);   
		Protocol.registerProtocol("https", myhttps); 
		
		if (params != null) {
			method.setRequestBody(params);
		}
		method.setDoAuthentication(false);
		method.getParams().setContentCharset(charset);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 8000);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		method.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=" + charset);

		try {
			executeMethod(method);

			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				InputStreamReader isr = new InputStreamReader(is, charset);

				BufferedReader reader = new BufferedReader(isr);

				String line = "";

				while ((line = reader.readLine()) != null) {
					if (pretty) {
						String ll = System.getProperty("line.separator");
						response.append(line).append(ll);
					} else {
						response.append(line);
					}
				}

				reader.close();
			}

			url = urlWithParams(url, params);

			System.out.println("URL: " + url);
			System.out.println("StatusCode:" + method.getStatusCode());

		} catch (IOException e) {
			System.err.println("执行Post请求" + url + "时异常:" + e.toString());
		} finally {
			method.releaseConnection();
		}

		resultOutput(response.toString());

		return response.toString();
	}

	/**
	 * 得到请求的URL以及串连起来的完整参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public String urlWithParams(String url, NameValuePair[] params) {

		if (params != null && params.length > 0) {
			url = url + "?";

			for (int i = 0; i < params.length; i++) {
				NameValuePair param = params[i];

				url = url + param.getName() + "=" + param.getValue();
				if (i < params.length - 1) {
					url = url + "&";
				}
			}

		}

		return url;
	}

	/**
	 * 将请求返回的内容以格式化后的JSON字符串输出
	 * 
	 * @param response
	 *            返回的结果
	 */
	public void resultOutput(String response) {
		if (response.length() > 0 && showLog) {
			try {
				JSONObject obj = JSONObject.parseObject(response);
			} catch (Exception e) {
				JSONArray obj = JSONArray.parseArray(response);
			}
		}
	}

	public String getApiURL() {
		return apiURL;
	}

	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isShowLog() {
		return showLog;
	}

	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
	}
}
