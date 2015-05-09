package com.yinfu.jbase.util.remote;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.httpclient.NameValuePair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class test2 {
	public static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	 public static void main(String[] args) { 
//		 getToken("12345678");
		 
//		 deviceinfo("12345678");
		 
//		 systat_get("12345678");
		 
//		 authurl_get("12345678");
//		 authurl_set("12345678");
//		 
//		 logserver_get("12345678");
//		 logserver_set("12345678");
//		 
//		 def_location_get("12345678");
		// def_location_set("12345678");
		 
//		 timeout_get("12345678");
//		 timeout_set("12345678");
		 
		 routerHelper("test1@pifii.com","123456");
		 
//		 passlist_set("neofeng@pifii.com","88888888");
		 
		 //38:bc:1a:00:6b:f5
		 //e8:bb:a8:70:ae:ef
//		 threadPool.execute(new AddWhiteList("88:e3:ab:b8:8b:d9",180,"cmcc4@pifii.com","123456"));
	 }
	 
	 
	 
	 public static void passlist_set(String email,String pass){
		String token = RouterHelper.routerToken(email, pass);
		String getData = RouterHelper.passlistGet(token);
		JSONObject json = JSONObject.parseObject(getData);
		JSONArray array = null;
//		if (json.containsKey("member")) {
//			getData = json.getString("member");
//			array = JSONArray.fromObject(getData);
//		} else {
//			array = new JSONArray();
//		}
		array = new JSONArray();
		array.add("e8:bb:a8:70:aa");
		NameValuePair[] params = new NameValuePair[array.size() + 1];
		for (int i = 0; i < array.size(); i++) {
			params[i] = new NameValuePair("member", array.get(i).toString());
		}
		params[array.size()] = new NameValuePair("token", token);
		RouterHelper.passlistSet(token, params);
	 }
	 
	 public static void systat_get(String pass){
		 String token = getToken(pass);
			if(!"".equals(token)){
				HttpRequester http = new HttpRequester();
				String url = "http://192.168.1.1/cgi-bin/luci/api/0/module/systat_get?token="+token;
				try {
					HttpRespons hr = http.sendPost(url);
					System.out.println(hr.getContent());
				} catch (IOException e) {
				}
			}else{
				System.out.println("查询失败");
			}
	 }
	 
	 public static void deviceinfo(String pass){
		 String token = getToken(pass);
			if(!"".equals(token)){
				HttpRequester http = new HttpRequester();
				String url = "http://192.168.10.1/cgi-bin/luci/api/0/common/deviceinfo?token="+token;
				try {
					HttpRespons hr = http.sendPost(url);
					System.out.println(hr.getContent());
				} catch (IOException e) {
				}
			}else{
				System.out.println("查询失败");
			}
	 }
	
	public boolean sysData(String userName,String password,String sourcePath,String savePath){
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
	
	
	public static String getToken(String pass){
		String token = "";
		HttpRequester http = new HttpRequester();
		Map map = new HashMap();
		map.put("pass", pass);
		try {
			HttpRespons hr = http.sendPost("http://192.168.1.1/cgi-bin/luci/api/0/account/login", map);
			System.out.println(hr.getContent());
			JSONObject json = JSONObject.parseObject(hr.getContent());
			if(json.containsKey("status") && "0".equals(json.getString("status")) && json.containsKey("token")){
				token = json.getString("token");
			}
		} catch (IOException e) {
		}
		return token;
	}
	
	public static void authurl_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/authurl_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
	}
	
	public static void authurl_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("auth", "http://www.pifii.com:8080/ttopyd/authorizeAccess");
			param.put("ad", "http://hz.com/ifidc/pifii.html");
			//param.put("ad", "http://123.pifii.com/ifidc/pifii/pifii.html");
			
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/authurl_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void logserver_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/logserver_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void logserver_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("port", "10000");
			param.put("ip", "113.106.98.60");

			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/logserver_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	
	public static void def_location_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/def_location_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void def_location_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("url", "http://hz.com/ifidc/pifii.html");

			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/def_location_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void timeout_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/timeout_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void timeout_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("timeout", "300");

			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/timeout_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	
	public static void senMsm(String phone,String code){
		HttpRequester http = new HttpRequester();
		Map<String,String> param = new HashMap<String,String>();
		param.put("account", "cf_gzyf");
		param.put("password", "2ZGH2v");
		param.put("mobile", phone);
		String verifyCode = code;
		param.put("content", "您的验证码是："+verifyCode+"。请不要把验证码泄露给其他人。");
		try {
			HttpRespons hr = http.sendPost("http://106.ihuyi.cn/webservice/sms.php?method=Submit",param);
			System.out.println(hr.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void routerHelper(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.passlistGet(token);
		 System.out.println(backData);
	}
	
	 public static void main2(String[] args) { 
//		 getToken("12345678");
//		 
//		 authurl_get("12345678");
//		 authurl_set("12345678");
//		 
//		 logserver_get("12345678");
//		 logserver_set("12345678");
//		 
//		 def_location_get("12345678");
//		 def_location_set("12345678");
		 
		 
		 
		 
		 
		 
		 //http://192.168.2.1/cgi-bin/luci/syncboxlite/0/files/syncbox/data/download/030002130052302C42836E055EEB3EF64210D5-1A32-2326-51E7-D643D5856A7C.flv?dl=1&token=ab0031udfflgpttu
		 HttpRequester http = new HttpRequester();
		 try {
//			 Map<String,String> param = new HashMap<String,String>();
//			 param.put("mac", "EC:85:2F:6D:47:2E");
//			// param.put("option", "-");
//			 String urlString = "http://192.168.10.1/cgi-bin/luci/api/0/module/lan_kick_get?token=fe3459vzue0gaa6z";
//			 HttpRespons hr = http.sendGet(urlString);
//			 System.out.println(hr.getContent());
			 
			 //一个YFHttpClient操作实例
//			 YFHttpClient client = new YFHttpClient();
//			 
//			 NameValuePair[] params={new NameValuePair("token","fe39a6djbvqerngo")};
//			 //调用一个接口
//			 client.httpRouterGet(APIDefine.AUTHURL_GET, params);
//			 //调用另一个接口
//			 client.httpRouterGet(APIDefine.LAN_KICK_GET, params);
			 
			 
			 
			 
//			 Map<String,String> param = new HashMap<String,String>();
////			 param.put("mac", "08:57:00:0F:66:73");
////			 param.put("option", "-");
//			 String urlString = "http://192.168.10.1/cgi-bin/luci/syncboxlite/0/metadata/syncbox/data/music?token=ab0031h8mv3ssvlx&list=true";
//			 HttpRespons hr = http.sendGet(urlString);
//			 System.out.println(hr.getContent());
//			 
//				String token = RouterHelper.routerToken("cmcc1@pifii.com", "123456");
//				RouterHelper.passlistGet(token);
			 
			 //threadPool.execute(new AddWhiteList("08:57:00:0F:66:73","cmcc3@pifii.com", "123456"));
				
//				String markInfo = "60:d9:c7:82:d3:87";
//				
//				String getData = RouterHelper.passlistGet(token);
//				JSONObject json = JSONObject.fromObject(getData);
//				JSONArray array = null;
//				if(json.containsKey("member")){
//					getData = json.getString("member");
//					array  = JSONArray.fromObject(getData);
//				}else{
//					array = new JSONArray();
//				}
//				
//				array.add(markInfo);
//				NameValuePair[] params=new NameValuePair[array.size()+1];
//		        for (int i = 0; i < array.size(); i++) {
//					 params[i]=new NameValuePair("member",array.get(i).toString());
//				}
//		        params[array.size()]=new NameValuePair("token",token);
//		       
//				String setData = RouterHelper.passlistSet(token, params);
				
				
				//RouterHelper.passlistGet(token);
				
				//RouterHelper.log(token,"113.106.98.60","10000");
//				YFHttpClient client = new YFHttpClient();
//				RouterInfo info = client.routerInfo("cmcc1@pifii.com", "123456");
//				String token = info.getToken();

//				 JSONObject json=new JSONObject();
//				 json.put("cmd", "status");
//				 JSONObject obj=new JSONObject();
//				 json.put("param", obj);
//				 String backData = RouterHelper.download(json.toString(),token);
//				 JSONObject backJson = JSONObject.fromObject(backData);
//				 if(backJson.containsKey("errcode") && "0".equals(backJson.getString("errcode"))){
//					 if(backJson.containsKey("taskinfo")){
//						 JSONObject taskJson = backJson.getJSONObject("taskinfo");
//						 if(taskJson.containsKey("73ae3c5c1b16f2f4")){
//							 JSONObject bkJson = taskJson.getJSONObject("73ae3c5c1b16f2f4");
//							 System.out.println(bkJson.toString());
//						 }
//					 }
//				 }
				
				JSONObject setJson = new JSONObject();
				setJson.put("cmd", "setdownopt");

				JSONObject setObj = new JSONObject();
				setObj.put("down-speed", "0");
				setObj.put("errcode", "0");
				setObj.put("up-speed", "51200");
				setObj.put("seed-time", "60");
				setObj.put("up-each", "20480");
				setObj.put("max-down", "3");
				setObj.put("down-dir", "/storageroot/data/doc");
				setObj.put("down-each", "0");
				
				JSONObject option = new JSONObject();
				option.put("option", setObj);

				setJson.put("param", option);
				//String backSetData = RouterHelper.download(setJson.toString(),token);
				//JSONObject bkSetJson = JSONObject.fromObject(backSetData);
				
				
				//RouterHelper.wifi_macl_list_del("cfg077767",token);
				//RouterHelper.wifi_macl_basic_set("1", "0", token);
				//String getData = RouterHelper.wifi_macl_list_set("74:E5:0B:F4:C8:D0",token);
//				getData = JSONObject.fromObject(getData).getString("member");
//				JSONArray array = JSONArray.fromObject(getData);
//				array.add("D8:B0:4C:F3:47:49");
//				NameValuePair[] params=new NameValuePair[array.size()+1];
//		        for (int i = 0; i < array.size(); i++) {
//					 params[i]=new NameValuePair("member",array.get(i).toString());
//				}
//		        params[array.size()]=new NameValuePair("token",token);
//		       
//				String setData = RouterHelper.passlistSet(token, params);
			 
			 
			 
			 
			 
//			 String url = "http://192.168.10.1/cgi-bin/luci/api/0/extension/ext_download?token=ab0031h8mv3ssvlx";
//			 
//			 JSONObject json=new JSONObject();
//			 json.put("cmd", "add");
//			 String [] array=new String[]{"http://v.youku.com/v_show/id_XNjgyMzU1NzY4.html"};
//			 JSONObject obj=new JSONObject();
//			obj.put("link", array);
//			obj.put("islocal", false);
//			json.put("param", obj);
//			// String jsons = "{\\\"cmd\\\":\\\"add\\\",\\\"param\\\":{\\\"link\\\":[\\\"http://v.youku.com/v_show/id_XNjgzNTAyMzA4.html\\\"],\\\"islocal\\\":false}}";
//			 Map map = new HashMap();
//			 System.out.println(json.toString());
//			 map.put("json", json.toString());
//			 HttpRespons hrr = http.sendPost(url,map);
//			 System.out.println(hrr.getContent());
//			 
			
			 
			 
//			 String url = "http://192.168.2.1/cgi-bin/luci/syncboxlite/0/metadata/syncbox/data/download?token=ab0031udfflgpttu&list=true";
//			 HttpRespons hrr = http.sendGet(url);
//			 System.out.println(hrr.getContent());
			 //https://openapi.youku.com/v2/searches/show/top_unite.json?client_id=47cf22b310cb6c23&category=电影&genre=2004
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
