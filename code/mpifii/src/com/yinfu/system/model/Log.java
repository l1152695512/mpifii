package com.yinfu.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.Consts;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.IpUtil;
import com.yinfu.model.SplitPage.SplitPage;
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
	public static final int EVENT_DOWN = 7;
	public static final int EVENT_UPLOAD = 8;
	public static final int EVENT_LOGIN_OUT=9;
	
	

	public void insert(Controller con, int operation)
	{

		String ip = IpUtil.getIp(con.getRequest());
		String from = con.getRequest().getHeader("Referer");
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		Log event = new Log().set("ip", ip).set("from", from);
		if (user != null) event.set("uid", user.getId());
        String url=con.getRequest().getRequestURI();
        String menuUrl = "";
        if(url!=null && !url.equals("")){
        	url=url.substring(1);
        	url=url.substring(url.indexOf("/"));
        	menuUrl=url.substring(0, url.lastIndexOf("/"));
        }
        event.set("log_url", url);
        int resId = getResIdByUrl(menuUrl);
        event.set("res_id", resId);
        
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
	
	/**
	 * 根据url得到菜单编号
	 * @param url
	 * @return
	 */
	public int getResIdByUrl(String url){
		if(url.indexOf("device")!=-1){
			url="/system/warehouse";
		}
		Record re = Db.findFirst("select id from system_res where url='"+url+"' or url='"+url+"/index'");
		if(re!=null){
			return re.get("id");
		}else {
			return 0;
		}
	}
	
	public SplitPage getSysLogList(SplitPage splitPage) {
		String sql = "SELECT sl.*,date_format(sl.date,'%Y-%m-%d %H:%i:%s') createdate,u.name username,sr.name as resname";
		splitPage = splitPageBase(splitPage,sql);
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" from system_log sl");
		formSqlSb.append(" inner join system_user u on (u.id=sl.uid)");
		formSqlSb.append(" left join  system_res sr on (sl.res_id=sr.id)");
		formSqlSb.append(" where sl.log_url<>'null'");
		String orgs = DataOrgUtil.getOrgIds();
		String userName = queryParam.get("username");
		if(null!=orgs && !orgs.equals("")){
			formSqlSb.append(" and u.org_id in("+orgs+")");
		}
		if(null != userName && !userName.equals("")){
			formSqlSb.append(" and u.name like '%"+userName+"%'");
		}
		formSqlSb.append(" order by sl.date desc");
	}

	

}
