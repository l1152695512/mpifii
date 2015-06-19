
package com.yinfu.business.statistics.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.application.userres.model.UserRes;
import com.yinfu.business.statistics.model.AdvMgtReport;
import com.yinfu.business.statistics.model.AdvSta;
import com.yinfu.business.statistics.model.AppSta;
import com.yinfu.business.statistics.model.Customer;
import com.yinfu.business.statistics.model.DeviceCustFlow;
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
	public void toDevice() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String orgId = getPara("_query.org_id");
		String shopId = getPara("_query.shop_id");
		String type = getPara("_query.type");
		String router_sn = getPara("_query.router_sn");
		String dtype = getPara("_query.d_type");
		queryMap.put("_query.d_type", dtype);
		queryMap.put("_query.router_sn", router_sn);
		queryMap.put("_query.org_id", orgId);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.type", type);
		String dataXml = Statistics.dao.getDeviceStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		List<Record> oList = ContextUtil.getShopListByUser();
		setAttr("shopList", oList);
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
	public void toShop() {
		
		List<Record> oList = ContextUtil.getShopListByUser();
		setAttr("shopList", oList);
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
	public void toCustomer() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String orgId = getPara("_query.org_id");
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
		List<Record> shopList = ContextUtil.getShopListByUser();
		setAttr("shopList", shopList);
		SplitPage splitPages = Customer.dao.getCustomerList(splitPage);
		setAttr("splitPage", splitPages);
		// 菜单操作权限控制
		setAttr("displayVal", UserRes.dao.getResOperaByResName("客户分析", UserRes.OPERA_TYPE_E));
		
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
	public void toPassFlow() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		String dataXml = PassFlow.dao.getPassFlowStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		List<Record> shopList = ContextUtil.getShopListByUser();
		setAttr("shopList", shopList);
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
	public void toAppSta() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList", oList);
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
	public void toAdvSta() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		System.out.println(new Date());
		List<Record> shopList = ContextUtil.getShopListByUser();
		setAttr("shopList", shopList);
		System.out.println(new Date());
		String dataXml = AdvSta.dao.getAdvStaInfo(queryMap);
		setAttr("dataXml", dataXml);
		System.out.println(new Date());
		String dataXmlOrg = AdvSta.dao.getAdvOrgStaInfo(queryMap);
		setAttr("dataXmlOrg", dataXmlOrg);
		System.out.println(new Date());
		SplitPage splitPages = AdvSta.dao.getAdvStaList(splitPage);
		setAttr("splitPage", splitPages);
		System.out.println(new Date());
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
	public void toSmsFlowSta() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		String monthnums = getPara("_query.monthnums");
		queryMap.put("_query.startDate", startDate);
		queryMap.put("_query.endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		queryMap.put("_query.monthnums", monthnums);
		String dataXml = SmsFlow.dao.getSmsFlowInfo(queryMap);
		setAttr("dataXml", dataXml);
		List<Record> shopList = ContextUtil.getShopListByUser();
		setAttr("shopList", shopList);
		SplitPage splitPages = SmsFlow.dao.getSmsFlowList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/smsflow.jsp");
	}
	
	public void toWorkOrderSta() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String userName = getPara("_query.userName");
		String orgId = getPara("_query.org_id");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("userName", userName);
		queryMap.put("org_id", orgId);
		SplitPage splitPages = workOrderSta.dao.wordOrderStatis(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/workorderSta.jsp");
	}
	
	public void toAdvMgtSta() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("_query.startDate");// 开始时间
		String endDate = getPara("_query.endDate");// 结束时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		String asId = getPara("_query.as_id");
		String advName = getPara("_query.advName");
		if (startDate == null || "".equals(startDate)) {
			startDate = sdf.format(new Date());
		}
		if (endDate == null || "".equals(endDate)) {
			endDate = sdf.format(new Date());
		}
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		queryMap.put("_query.as_id", asId);
		queryMap.put("_query.advName", advName);
		List<Record> aslist = AdvMgtReport.dao.getAdvSpacesAll();
		setAttr("aslist", aslist);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList", oList);
		String dataXml = AdvMgtReport.dao.getAdvInfoXml(queryMap);
		setAttr("dataXml", dataXml);
		SplitPage splitPages = AdvMgtReport.dao.getAdvList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/advMgtReport.jsp");
	}
	
	public void downCustStaFile() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		String type = getPara("type");
		try {
			type = URLDecoder.decode(type, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String phone = getPara("phone");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("shopId", shopId);
		queryMap.put("type", type);
		queryMap.put("phone", phone);
		render(Customer.dao.downCustAllInfo(queryMap));
	}
	//@formatter:off 
	/**
	 * Title: downShopStaFile
	 * Description:商铺统计下载
	 * Created On: 2015年6月11日 下午3:08:46
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void downShopStaFile(){
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("shopId", shopId);
		render(ShopSta.dao.downShopStaFile(queryMap));
	}
	//@formatter:off 
	/**
	 * Title: downPassFlowFile
	 * Description:客流统计下载
	 * Created On: 2015年6月11日 下午3:58:23
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void downPassFlowFile(){
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("shopId", shopId);
		render(PassFlow.dao.downPassFlowFile(queryMap));
	}
	//@formatter:off 
	/**
	 * Title: downAdvStaFile
	 * Description:广告统计下载
	 * Created On: 2015年6月11日 下午5:06:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void downAdvStaFile(){
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("shopId", shopId);
		render(AdvSta.dao.downAdvStaFile(queryMap));
	}
	
	//@formatter:off 
	/**
	 * Title: downSmsFlowFile
	 * Description:短信统计下载
	 * Created On: 2015年6月12日 下午3:35:13
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void downSmsFlowFile(){
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("shopId", shopId);
		render(SmsFlow.dao.downSmsFlowFile(queryMap));
	}
	
	//@formatter:off 
	/**
	 * Title: downWorkOrderFile
	 * Description:工单统计下载
	 * Created On: 2015年6月12日 下午3:42:03
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void downWorkOrderFile(){
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String userName = getPara("userName");
		try {
			userName = URLDecoder.decode(userName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("userName", userName);
		render(workOrderSta.dao.downWorkOrderFile(queryMap));
	}
	
	/*//@formatter:off 
	*//**
	 * Title: downFlowStaFile
	 * Description:流量分析下载
	 * Created On: 2015年6月12日 下午3:43:50
	 * @author JiaYongChao
	 * <p> 
	 *//*
	//@formatter:on
	public void downFlowStaFile(){
		Map<String, String> queryMap = new HashMap<String, String>();
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		String phone = getPara("phone");
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		queryMap.put("orgId", orgId);
		queryMap.put("shopId", shopId);
		queryMap.put("phone", phone);
		render(DeviceCustFlow.dao.downFlowStaFile(queryMap));
	}*/
	public void toDeviceCustFlow() {
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList", oList);
		DeviceCustFlow.dao.getCustFlowList(splitPage);
		setAttr("splitPage", splitPage);
		render("/page/business/statistic/devicecustflowSta.jsp");
	}
}
