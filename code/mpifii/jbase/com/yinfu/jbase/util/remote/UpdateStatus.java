package com.yinfu.jbase.util.remote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.httpclient.NameValuePair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;

public class UpdateStatus implements Runnable {
	private String email;
	private String password;

	public UpdateStatus(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public void run() {
		YFHttpClient client = new YFHttpClient();
		client.setShowLog(true);
		
		String xsrf = client.serverInfo();
		String loginResult = client.login(email, password, xsrf);
		JSONObject obj = JSONObject.parseObject(loginResult);
		JSONArray states = (JSONArray) obj.get("router_states");

		if (states.size() > 0) {

			JSONObject router = (JSONObject) states.get(0);
			String routerId = router.getString("id");
			String token = router.getString("token");

			String status = "0";

			if (token.length() > 0) {
				NameValuePair[] params = { new NameValuePair("router_id",
						routerId) };
				String stateJsonStr = client.httpGet(APIDefine.ROUTER_STATE,
						params);

				JSONObject statusObj = JSONObject.parseObject(stateJsonStr);
				JSONArray array = (JSONArray) statusObj
						.get("online_ip_address");

				if (array.size() >= 2) {
					String updateDate = (String) array.get(1);

					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss");
					dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));

					try {
						Date d = dateformat.parse(updateDate);
						Date current = new Date();

						String cs = dateformat.format(current);
						current = dateformat.parse(cs);

						if ((current.getTime() - d.getTime()) / 1000 / 60 < 5) {
							status = "1";
						}

					} catch (ParseException e) {
					}

				}
			}

			String sql = String
					.format("UPDATE  bp_device_tbl SET status='%s' where user_name='%s'",
							status, email);
			Db.update(sql);
		}
	}

}
