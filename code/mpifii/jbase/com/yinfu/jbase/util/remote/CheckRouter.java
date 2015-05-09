package com.yinfu.jbase.util.remote;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.jfinal.plugin.activerecord.Db;

public class CheckRouter {
	public static ExecutorService threadPool = Executors.newCachedThreadPool();

	public void checkRouter() {
		try {
			String sql = "select remote_account,remote_pass from bp_device";
			List list = Db.find(sql);
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				String email = map.get("remote_account").toString();
				String password = map.get("remote_pass").toString();
				threadPool.execute(new UpdateStatus(email, password));
			}
		} catch (Exception e) {
		}

	}
	
	public static void main(String[] args) {
		CheckRouter cr=new CheckRouter();
		cr.checkRouter();
	}

}
