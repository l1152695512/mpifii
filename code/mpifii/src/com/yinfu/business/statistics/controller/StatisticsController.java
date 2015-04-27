
package com.yinfu.business.statistics.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.statistics.model.AdvSta;
import com.yinfu.business.statistics.model.AppSta;
import com.yinfu.business.statistics.model.Customer;
import com.yinfu.business.statistics.model.PassFlow;
import com.yinfu.business.statistics.model.ShopSta;
import com.yinfu.business.statistics.model.SmsFlow;
import com.yinfu.business.statistics.model.Statistics;
import com.yinfu.business.statistics.model.workOrderSta;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/statistics", viewPath = "")
public class StatisticsController extends Controller<Statistics> {
	//@formatter:off 
	/**
	 * Title: toDevice
	 * Description:设备统计
	 * Created On: 2014年12月2日 上午10:26:23
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toDevice(){
		Map<String,String> queryMap = new HashMap<String,String>();
		String orgId  = getPara("_query.org_id");
		String shopId = getPara("_query.shop_id");
		String type = getPara("_query.type");
		queryMap.put("_query.org_id", orgId);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.type", type);
		String dataXml = Statistics.dao.getDeviceStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		SplitPage splitPages = Statistics.dao.getShopList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/device.jsp");
	}
	//@formatter:off 
	/**
	 * Title: toShop
	 * Description:商铺统计
	 * Created On: 2014年12月4日 下午4:35:20
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toShop(){
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		SplitPage splitPages = ShopSta.dao.getShopList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/shop.jsp");
	}
	//@formatter:off 
	/**
	 * Title: toCustomer
	 * Description:客户信息统计
	 * Created On: 2014年12月8日 下午5:03:10
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toCustomer(){
		Map<String,String> queryMap = new HashMap<String,String>();
		String startDate  = getPara("_query.startDate");//开始时间
		String endDate = getPara("_query.endDate");//结束时间
		String orgId  = getPara("_query.org_id");
		String shopId = getPara("_query.shop_id");
		String type = getPara("_query.type");
		String phone = getPara("_query.phone");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.org_id", orgId);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.type", type);
		queryMap.put("_query.phone", phone);
		String dataXml = Customer.dao.getCustomerStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		SplitPage splitPages = Customer.dao.getCustomerList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/customer.jsp");
	}
	//@formatter:off 
	/**
	 * Title: toPassFlow
	 * Description:客流信息统计
	 * Created On: 2014年12月8日 下午9:17:40
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toPassFlow(){
		Map<String,String> queryMap = new HashMap<String,String>();
		String startDate  = getPara("_query.startDate");//开始时间
		String endDate = getPara("_query.endDate");//结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		String dataXml = PassFlow.dao.getPassFlowStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		SplitPage splitPages = PassFlow.dao.getPassFlowList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/passflow.jsp");
	}
	//@formatter:off 
	/**
	 * Title: toAppSta
	 * Description:
	 * Created On: 2014年12月9日 下午4:03:05
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toAppSta(){
		Map<String,String> queryMap = new HashMap<String,String>();
		String startDate  = getPara("_query.startDate");//开始时间
		String endDate = getPara("_query.endDate");//结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		String dataXml = AppSta.dao.getAppStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		SplitPage splitPages = AppSta.dao.getAppStaList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/appSta.jsp");
	}
	//@formatter:off 
	/**
	 * Title: toAppSta
	 * Description:
	 * Created On: 2014年12月9日 下午4:03:05
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toAdvSta(){
		Map<String,String> queryMap = new HashMap<String,String>();
		String startDate  = getPara("_query.startDate");//开始时间
		String endDate = getPara("_query.endDate");//结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);
		String dataXml = AdvSta.dao.getAdvStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		String dataXmlOrg = AdvSta.dao.getAdvOrgStaInfo(queryMap);
		setAttr("dataXmlOrg", dataXmlOrg);
		SplitPage splitPages = AdvSta.dao.getAdvStaList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/advSta.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: toAppSta
	 * Description:
	 * Created On: 
	 * @author 
	 * <p> 
	 */
	//@formatter:on
	public void toSmsFlowSta(){
		
		Map<String,String> queryMap = new HashMap<String,String>();
	    String startDate  = getPara("_query.startDate");//开始时间
		String endDate = getPara("_query.endDate");//结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		String monthnums = getPara("_query.monthnums");
		
		
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		queryMap.put("_query.monthnums", monthnums);
		//splitPage.setQueryParam(queryMap);
		String dataXml = SmsFlow.dao.getSmsFlowInfo(queryMap);
		setAttr("dataXml", dataXml);
		
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList",oList);

		SplitPage splitPages = SmsFlow.dao.getSmsFlowList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/smsflow.jsp");
	}
	
	public void toWorkOrderSta(){
		Map<String,String> queryMap = new HashMap<String,String>();
	    String startDate  = getPara("_query.startDate");//开始时间
		String endDate = getPara("_query.endDate");//结束时间
		String userName = getPara("_query.userName");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("userName", userName);
		
		SplitPage splitPages = workOrderSta.dao.wordOrderStatis(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/workorderSta.jsp");
	}
}
