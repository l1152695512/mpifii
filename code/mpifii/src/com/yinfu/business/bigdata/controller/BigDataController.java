
package com.yinfu.business.bigdata.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.bigdata.model.BigData;
import com.yinfu.business.bigdata.model.UserBehavior;
import com.yinfu.business.bigdata.model.UserDetail;
import com.yinfu.business.statistics.model.ShopSta;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.business.util.DateUtils;
import com.yinfu.business.util.StrUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey = "/business/bigdata", viewPath = "")
public class BigDataController extends Controller<Model> {
	User user = ContextUtil.getCurrentUser();
	List<Record> list = DataOrgUtil.getShopsForUser(user.getInt("id"));
	
	// 用户行为 带图报表：
	public void big_function() {
		String starttime = getPara("starttime");
		String endtime = getPara("endtime");
		String shopId = getPara("qShopId");
		if (StrUtil.isBlank(endtime)) {
			endtime = DateUtil.getNow();
		}
		if (StrUtil.isBlank(starttime)) {
			starttime = DateUtil.getOneMonth(endtime);
		}
		if (StrUtil.isBlank(shopId)) {
			shopId = DataOrgUtil.recordListToSqlIn(list, "id");
		}
		renderText(BigData.dao.function("(select * from  bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))
				+ "  union all    SELECT * from bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth() + ")", starttime, endtime, shopId));
	}
	
	/**
	 * 用户行为分析展示（使用http://www.juntiansoft.com）
	 */
	
	public void function() {
		String startDate = getPara("starttime");
		String endDate = getPara("endtime");
		if (StrUtil.isBlank(endDate)) {
			endDate = DateUtil.getNow();
		}
		if (StrUtil.isBlank(startDate)) {
			startDate = DateUtil.getOneMonth(endDate);
		}
		String shopId = getPara("qShopId");
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList", oList);
		setAttr("startDate", startDate);
		setAttr("endDate", endDate);
		splitPage.queryParam.put("tab", "(select * from  bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))
				+ "  union all   SELECT * from bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth() + ")");
		splitPage.queryParam.put("startDate", startDate);
		splitPage.queryParam.put("endDate", endDate);
		if (StrUtil.isBlank(shopId)) {
			shopId = DataOrgUtil.recordListToSqlIn(list, "id");
		}
		splitPage.queryParam.put("shopId", shopId);
		splitPage.queryParam.put("endDate", endDate);
		splitPage.queryParam.put("startDate", startDate);
		SplitPage splitPages = BigData.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/bigdata/function.jsp");
	}
	
	/*
	 * public void flow() { render("/page/business/bigdata/flow.jsp"); }
	 */
	
	/*
	 * public void adv() { render("/page/business/bigdata/adv.jsp"); }
	 */
	
	//@formatter:off 
	/**
	 * Title: toUserTypes
	 * Description:用户偏好分析
	 * Created On: 2015年6月2日 上午10:25:27
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toUserTypes() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String shopId = getPara("shop_id");
		String orgId = getPara("org_id");
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getOrgListByUser();
		setAttr("orgList", oList);
		setAttr("_query.shop_id", shopId);
		setAttr("_query.org_id", orgId);
		render("/page/business/bigdata/userTypes.jsp");
	}
	
	// 用户偏好分析 to_user_type 其中页面有个饼图的方法是： big_xml_type()
	public void to_user_type() {
		String starttime = getPara("starttime");
		String endtime = getPara("endtime");
		String shopId = getPara("qShopId");
		String orgId = getPara("qOrgId");
		if (StrUtil.isBlank(endtime)) {
			endtime = DateUtil.getNow();
		}
		if (StrUtil.isBlank(starttime)) {
			starttime = DateUtil.getSevenDayBefore(endtime);
		}
		if (StrUtil.isBlank(shopId)) {
			shopId = DataOrgUtil.recordListToSqlIn(list, "id");
		}
		// String shopid=ContextUtil.getShopByUser();
		renderText(BigData.dao.split("(select * from  bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))
				+ "  union all   SELECT * from bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth() + ")", starttime, endtime, shopId));
	}
	
	/**
	 * big_xml_type(饼图的说明)
	 * 
	 * @param name
	 * @Exception 异常对象
	 * @since CodingExample　Ver(编码范例查看) 1.1 创建时间：2015 2015年1月11日 下午3:57:45 作者：mycode Copyright liting Corporation 2015
	 *        版权所有
	 */
	public void big_xml_type() {
		String starttime = getPara("starttime");
		String endtime = getPara("endtime");
		String shopId = getPara("qShopId");
		String orgId = getPara("qOrgId");
		if (StrUtil.isBlank(endtime)) {
			endtime = DateUtil.getNow();
		}
		if (StrUtil.isBlank(starttime)) {
			starttime = DateUtil.getSevenDayBefore(endtime);
		}
		if (StrUtil.isBlank(shopId)) {
			shopId = DataOrgUtil.recordListToSqlIn(list, "id");
		}
		String dataXml = BigData.dao.getDeviceStaInfo(
				"(select * from  bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth(DateUtils.getYearMonth(1))
						+ "  union all    SELECT * from bg_tat_uesrtype_mon_all" + DateUtils.getCurrentYearMonth() + ")", starttime, endtime, shopId);
		renderText(dataXml);
	}
	
	/******************** 新版大数据功能 ****************************/
	//@formatter:off 
	/**
	 * Title: user_preference
	 * Description:新版用户偏好分析
	 * Created On: 2015年6月2日 上午10:51:02
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void user_preference() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String orgId = getPara("org_id");
		String shopId = getPara("shop_id");
		String startDate = getPara("startDate");// 开始时间
		String endDate = getPara("endDate");// 结束时间
		queryMap.put("org_id", orgId);
		queryMap.put("shop_id", shopId);
		queryMap.put("startDate", startDate);
		queryMap.put("endDate", endDate);
		List<Record> oList = ContextUtil.getShopListByUser();
		setAttr("orgList", oList);
		Map<String, String> dataXml = new HashMap<String, String>();
		dataXml = BigData.dao.getPreferenceXml(queryMap);
		String pieXml = dataXml.get("pieXml").toString();
		setAttr("pieXml", pieXml);
		String lineXml = dataXml.get("lineXml").toString();
		setAttr("lineXml", lineXml);
		render("/page/business/bigdata/userTypes.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: user_behavior
	 * Description:用户行为分析
	 * Created On: 2015年6月4日 下午2:49:39
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void user_behavior() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String date = getPara("_query.date");// 时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.date", date);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getShopListByUser();
		setAttr("orgList", oList);
		List<Record> shopList = ContextUtil.getShopListByUser();
		setAttr("shopList", shopList);
		SplitPage splitPages = UserBehavior.dao.getBehaviorList(splitPage);
		setAttr("splitPage", splitPages);
		String dataXml = UserBehavior.dao.getBehaviorXml(queryMap);
		setAttr("dataXml", dataXml);
		render("/page/business/bigdata/function.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: user_detail
	 * Description:精准用户分析
	 * Created On: 2015年6月10日 下午3:31:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void user_detail() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String date = getPara("_query.date");// 时间
		String shopId = getPara("_query.shop_id");
		String orgId = getPara("_query.org_id");
		queryMap.put("_query.date", date);
		queryMap.put("_query.shop_id", shopId);
		queryMap.put("_query.org_id", orgId);
		List<Record> oList = ContextUtil.getShopListByUser();
		setAttr("orgList", oList);
		List<Record> shopList = ContextUtil.getShopListByUser();
		setAttr("shopList", shopList);
		SplitPage splitPages = UserDetail.dao.getDetailList(splitPage);
		setAttr("splitPage", splitPages);
		String dataXml = UserDetail.dao.getDetailXml(queryMap);
		setAttr("dataXml", dataXml);
		render("/page/business/bigdata/index.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: downPreferenceFile
	 * Description:用户偏好分析下载
	 * Created On: 2015年6月15日 下午5:52:19
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void downBehaviorFile() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String date = getPara("date");// 时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		queryMap.put("date", date);
		queryMap.put("org_id", orgId);
		queryMap.put("shop_id", shopId);
		render(UserBehavior.dao.downBehaviorFile(queryMap));
	}
	
	//@formatter:off 
		/**
		 * Title: downPreferenceFile
		 * Description:精准用户分析下载
		 * Created On: 2015年6月15日 下午5:52:19
		 * @author JiaYongChao
		 * <p> 
		 */
		//@formatter:on
	public void downDetailFile() {
		Map<String, String> queryMap = new HashMap<String, String>();
		String date = getPara("date");// 时间
		String orgId = getPara("orgId");
		String shopId = getPara("shopId");
		queryMap.put("date", date);
		queryMap.put("org_id", orgId);
		queryMap.put("shop_id", shopId);
		render(UserDetail.dao.downDetailFile(queryMap));
	}
}
