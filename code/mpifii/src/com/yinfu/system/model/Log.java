package com.yinfu.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.Consts;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.IpUtil;
import com.yinfu.model.highchart.Chart;

@TableBind(tableName = "system_log")
public class Log extends Model<Log>
{
	private static final long serialVersionUID = -128801010211787215L;

	public static Log dao = new Log();
	public static final int EVENT_VISIT = 1;
	public static final int EVENT_LOGIN = 2;
	public static final int EVENT_ADD = 3;
	public static final int EVENT_UPDATE = 4;
	public static final int EVENT_DELETE = 5;
	public static final int EVENT_GRANT = 6;
	
	

	public void insert(Controller con, int operation)
	{

		String ip = IpUtil.getIp(con.getRequest());
		String from = con.getRequest().getHeader("Referer");
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		Log event = new Log().set("ip", ip).set("from", from);
		if (user != null) event.set("uid", user.getId());

		event.set("operation", operation).saveAndDate();

	}

	public Chart getVisitCount()
	{
		Chart chart = new Chart();
		List<Long> series = new ArrayList<Long>();

		List<Log> date = dao.find(sql("system.log.getVisitCount"));
		for (Log event : date)
		{
			chart.categories.add(event.getDate());
			series.add(event.getCount());
		}
		Collections.reverse(chart.categories);
		Collections.reverse(series);
		chart.setSeriesDate("登录用户", series);

		return chart;

	}

	

}
