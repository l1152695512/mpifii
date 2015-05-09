package com.yinfu.jbase.util.remote;

import java.net.URLEncoder;
import org.apache.commons.httpclient.NameValuePair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class AddWhiteList implements Runnable {
//	private static Logger logger = Logger.getLogger(AddWhiteList.class);
	private String clientMac;
	private int timeOut;//秒
	private String userName;
	private String password;

	public AddWhiteList(String clientMac, int timeOut, String userName, String password) {
		this.clientMac = clientMac;
		this.timeOut = timeOut;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void run() {
		YFHttpClient client = new YFHttpClient();
		client.setShowLog(true);
		String xsrf = client.serverInfo();
		String loginResult = client.login(userName, password, xsrf);
		JSONObject obj = JSONObject.parseObject(loginResult);
		JSONArray states = (JSONArray) obj.get("router_states");
		if (states.size() > 0) {
			JSONObject router = (JSONObject) states.get(0);
//			String routerId = router.getString("id");
			String token = router.getString("token");
			if (token.length() > 0) {
				NameValuePair[] paramsGet = { new NameValuePair("token", token) };
				String  getData = client.httpRouterGet(APIDefine.PASS_LIST_GET, paramsGet);
				JSONObject json = JSONObject.parseObject(getData);
				JSONArray array = null;
				if(json.containsKey("member")){
					getData = json.getString("member");
					array  = JSONArray.parseArray(getData);
				}else{
					array = new JSONArray();
				}
				
				array.add(URLEncoder.encode(clientMac+";"+timeOut));
				NameValuePair[] params=new NameValuePair[array.size()+1];
		        for (int i = 0; i < array.size(); i++) {
					 params[i]=new NameValuePair("member",array.get(i).toString());
				}
		        params[array.size()]=new NameValuePair("token",token);
		       
		        client.httpRouterGet(APIDefine.PASS_LIST_SET, params);
//				String setData = RouterHelper.passlistSet(token, params);
//				JSONObject backJson = JSONObject.fromObject(setData);
//				System.err.println(backJson.toString());
			}
		}
	}

}
