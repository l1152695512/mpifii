package com.yinfu.business.bigdata.controller;

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
	
}
