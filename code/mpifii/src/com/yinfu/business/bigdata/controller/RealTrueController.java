
package com.yinfu.business.bigdata.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.bigdata.model.RealTrue;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.business.util.DateUtils;
import com.yinfu.business.util.StrUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/realtrue", viewPath = "")
public class RealTrueController extends Controller<Model> {
	/* 精准用户分析 */
	public void index() {
		Db.tx(new IAtom(){public boolean run() throws SQLException {
			
			Map<String,String> queryMap = new HashMap<String,String>();
			String startDate  = getPara("starttime");//开始时间
			String endDate = getPara("endtime");//结束时间
			queryMap.put("startDate", startDate);
			queryMap.put("endDate", endDate);
			 if(StrUtil.isBlank(endDate)){
				 endDate=DateUtil.getNow();
			 }
			 if(StrUtil.isBlank(startDate)){
				 startDate = DateUtil.getSevenDayBefore(endDate);
			}
			 String orgid  = getPara("_query.org_id");//开始时间
//			List<Record> list =ContextUtil.getNextListByUser();
//			String sysid =DataOrgUtil.recordListToSqlIn(list,"id");
			String sysid=	DataOrgUtil.getShopsForUserInTemporary(ContextUtil.getCurrentUserId());
			splitPage.queryParam.put("sysid",sysid );
			com.yinfu.business.util.DateUtils.getCurrentYearMonth();
			splitPage.queryParam.put("tab","(select * from  bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))+"  union all   SELECT * from bg_tat_uesrtype_mon_all"+com.yinfu.business.util.DateUtils.getCurrentYearMonth()+")" );
			List<Record> oList = ContextUtil.getOrgListByUser();
			setAttr("orgList",oList);
			SplitPage splitPages = RealTrue.dao.findList(splitPage);
			setAttr("splitPage", splitPages);
			String dataXml = RealTrue.dao.split("(select * from  bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))+"  union all   SELECT * from bg_tat_uesrtype_mon_all"+com.yinfu.business.util.DateUtils.getCurrentYearMonth()+")", startDate, startDate , orgid);
			setAttr("dataXml", dataXml);
			//删除临时表，一定要记住
			if(StringUtils.isNotBlank(sysid)){
				Db.update("DROP TEMPORARY TABLE IF EXISTS "+sysid);
			}
			return true;//方法放到一个事物中，保证临时表可用
		}});
			render("/page/business/bigdata/index.jsp");
	}
}
