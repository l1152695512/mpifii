package com.yinfu.business.bigdata.controller;

<<<<<<< HEAD
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.bigdata.model.BigData;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.business.util.DateUtils;
import com.yinfu.business.util.StrUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Org;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/business/bigdata", viewPath = "")
public class BigDataController extends Controller<Model> {
	User user = ContextUtil.getCurrentUser();
	List<Record> list = DataOrgUtil.getShopsForUser(user.getInt("id"));
	//用户行为 带图报表：
	public void big_function() {
		 String starttime=	getPara("starttime");
		 String endtime=getPara("endtime");
		 String shopId = getPara("qShopId");
		 if(StrUtil.isBlank(endtime)){
			 endtime=DateUtil.getNow();
		 }
		 if(StrUtil.isBlank(starttime)){
			 starttime = DateUtil.getOneMonth(endtime);
		 }
		 if(StrUtil.isBlank(shopId)){
			 shopId=DataOrgUtil.recordListToSqlIn(list,"id"); 
		 }
		renderText(BigData.dao.function("(select * from  bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))+"  union all    SELECT * from bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth()+")", starttime, endtime,shopId));
	}
	
	/**
	 * 用户行为分析展示（使用http://www.juntiansoft.com）
	 */
	
	public void function() {
			 String startDate=	getPara("starttime");
			 String endDate=getPara("endtime");
			 if(StrUtil.isBlank(endDate)){
				 endDate=DateUtil.getNow();
			 }
			 if(StrUtil.isBlank(startDate)){
				 startDate = DateUtil.getOneMonth(endDate);
			}
			 String shopId = getPara("qShopId");
			List<Record> oList = ContextUtil.getOrgListByUser();
			setAttr("orgList",oList);
			setAttr("startDate",startDate);
			setAttr("endDate",endDate);
			splitPage.queryParam.put("tab","(select * from  bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))+"  union all   SELECT * from bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth()+")" );
			splitPage.queryParam.put("startDate",startDate );splitPage.queryParam.put("endDate",endDate );
			if(StrUtil.isBlank(shopId)){
				shopId=DataOrgUtil.recordListToSqlIn(list,"id"); 
			}
			splitPage.queryParam.put("shopId",shopId );
			splitPage.queryParam.put("endDate",endDate );
			splitPage.queryParam.put("startDate",startDate );
			SplitPage splitPages = BigData.dao.findList(splitPage);
			setAttr("splitPage", splitPages);
		render("/page/business/bigdata/function.jsp");
	}
	
/*	public void flow() {

		render("/page/business/bigdata/flow.jsp");
	}*/
	
/*	public void adv() {

		render("/page/business/bigdata/adv.jsp");
	}*/
	
	public void toUserTypes() {
		Map<String,String> queryMap = new HashMap<String,String>();
		String shopId = getPara("shop_id");
		String orgId = getPara("org_id");
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		setAttr("_query.shop_id", shopId);
		setAttr("_query.org_id", orgId);
		 render("/page/business/bigdata/userTypes.jsp");
	}
//用户偏好分析 to_user_type 其中页面有个饼图的方法是： big_xml_type()
	public void to_user_type() {
 		 String starttime=	getPara("starttime");
		 String endtime=getPara("endtime");
		 String shopId = getPara("qShopId");
		 String orgId = getPara("qOrgId");
		 if(StrUtil.isBlank(endtime)){
			 endtime=DateUtil.getNow();
		 }
		 if(StrUtil.isBlank(starttime)){
			 starttime = DateUtil.getSevenDayBefore(endtime);
		 }
		 if(StrUtil.isBlank(shopId)){
				shopId=DataOrgUtil.recordListToSqlIn(list,"id"); 
		}
//		 String shopid=ContextUtil.getShopByUser();
		 renderText(BigData.dao.split("(select * from  bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))+"  union all   SELECT * from bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth()+")", starttime, endtime,shopId));
	}
	/**
	 * 
	 * big_xml_type(饼图的说明)    
	 * @param   name    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1    
	 * 创建时间：2015  2015年1月11日 下午3:57:45
	 * 作者：mycode	
	 * Copyright liting Corporation 2015     
	 * 版权所有
	 */
		public void big_xml_type() {
			 String starttime=	getPara("starttime");
			 String endtime=getPara("endtime");
			 String shopId = getPara("qShopId");
			 String orgId = getPara("qOrgId");
			 if(StrUtil.isBlank(endtime)){
				 endtime=DateUtil.getNow();
			 }
			 if(StrUtil.isBlank(starttime)){
				 starttime = DateUtil.getSevenDayBefore(endtime);
			 }
			 if(StrUtil.isBlank(shopId)){
				shopId=DataOrgUtil.recordListToSqlIn(list,"id"); 
			 }
			 String dataXml = BigData.dao.getDeviceStaInfo("(select * from  bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))+"  union all    SELECT * from bg_tat_uesrtype_mon_all"+DateUtils.getCurrentYearMonth()+")",starttime,endtime,shopId);
			 renderText(dataXml);
		}

/*	public void big_flow() {
		 String starttime=	getPara("starttime");
		 String endtime=getPara("endtime");
		 if(StrUtil.isBlank(endtime)){
			 endtime=DateUtil.getNow();
		 }
		 if(StrUtil.isBlank(starttime)){
			 starttime = DateUtil.getOneMonth(endtime);
		 }
		renderText(BigData.dao.flow("bg_targetstat_2014_12", starttime, endtime));
	}*/
		
=======
import java.util.Calendar;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Model;
import com.yinfu.business.bigdata.model.BigData;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/bigdata", viewPath = "")
public class BigDataController extends Controller<Model> {

	public void toUserTypes() {
		 String starttime=	getPara("starttime");
		 System.out.println(starttime+"11");
		 setAttr("starttime", starttime);
		render("/page/business/bigdata/userTypes.jsp");
	}

	/**
	 * 用户行为分析展示（使用http://www.juntiansoft.com）
	 */
	public void function() {

		render("/page/business/bigdata/function.jsp");
	}

	public void flow() {

		render("/page/business/bigdata/flow.jsp");
	}
	public void adv() {

		render("/page/business/bigdata/adv.jsp");
	}

	public void to_user_type() {
		 String starttime=	getPara("starttime");
		 String endtime=	getPara("endtime");
		renderText(BigData.dao.split("bg_targetstat_2014_12", starttime, endtime));
	}

/*	public void big_function() {
		renderText(BigData.dao.function("bg_targetstat_2014_12", null, null));
	}*/
	
/*	public void big_flow() {
		renderText(BigData.dao.function("bg_targetstat_2014_12", null, null));
	}*/
/*	
	public void big_adv() {
		renderText(BigData.dao.function("bg_targetstat_2014_12", null, null));
	}*/
	
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
}
