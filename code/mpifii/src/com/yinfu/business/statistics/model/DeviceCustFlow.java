package com.yinfu.business.statistics.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

public class DeviceCustFlow extends Model<DeviceCustFlow>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final DeviceCustFlow dao=new DeviceCustFlow();
	
	/**
	 * 获取客户端流量数据
	 * @param splitPage
	 * @return
	 */
	public SplitPage getCustFlowList(SplitPage splitPage){
		StringBuilder sql = new StringBuilder();
		sql.append(" select org.name orgname,s.name shopname,d.name devicename,ROUND(cf.input_octets/1024/1024,2) as upload");
		sql.append(",ROUND(cf.output_octets/1024/1024,2) as download,date_format(cf.start_time,'%Y-%m-%d') start_date,auth.tag,cf.client_mac ");
		splitPage = splitPageBase(splitPage, sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam,
			StringBuilder formSqlSb, List<Object> paramValue) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		String phone = queryParam.get("phone");
		String isHistoryQuery = queryParam.get("isHistoryQuery");
		String tableName = "bp_session";
		if(isHistoryQuery!=null && isHistoryQuery.equals("1")){
			tableName = "bp_session_history";
		}
		if(orgId==null || "".equals(orgId)){
			orgId=DataOrgUtil.getOrgIds();
		}
		formSqlSb.append(" from sys_org org");
		formSqlSb.append(" inner join bp_shop s on (");
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" s.id in("+shopId+") and");
		}
		formSqlSb.append(" org.id=s.org_id)");
		formSqlSb.append(" inner join bp_device d on (d.shop_id=s.id)");
		formSqlSb.append(" inner join "+tableName+" cf on (cf.sn=d.router_sn)");
		formSqlSb.append(" inner join bp_auth auth on (auth.auth_type='phone' and auth.client_mac=cf.client_mac)");
		formSqlSb.append(" where 1=1 ");
		if(orgId!=null && !orgId.equals("")){
			formSqlSb.append(" and org.id in("+orgId+")");
		}
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and cf.start_time>='"+startDate+"'");	
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and cf.start_time<='"+startDate+"'");
		}
		if(phone!=null && !phone.equals("")){
			formSqlSb.append(" and auth.tag='"+phone+"'");
		}
		formSqlSb.append(" group by cf.sn,cf.client_mac,date_format(cf.start_time,'%Y-%m-%d')");
		formSqlSb.append(" order by cf.start_time desc");
	}
	
	public SplitPage getCustFlowList2(SplitPage splitPage){
		StringBuilder sql = new StringBuilder();
		sql.append(" select org.name orgname,s.name shopname,d.name devicename,ROUND(cf.upload/1024/1024,2) as upload");
		sql.append(",ROUND(cf.download/1024/1024,2) as download,cf.create_date,auth.tag,cf.client_mac ");
		splitPage = splitPageBase(splitPage, sql.toString());
		return splitPage;
	}
	
	public void makeFilter2(Map<String, String> queryParam,
			StringBuilder formSqlSb, List<Object> paramValue) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		String phone = queryParam.get("phone");
		if(orgId==null || "".equals(orgId)){
			orgId=DataOrgUtil.getOrgIds();
		}
		formSqlSb.append(" from sys_org org");
		formSqlSb.append(" inner join bp_shop s on (");
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" s.id in("+shopId+") and");
		}
		formSqlSb.append(" org.id=s.org_id)");
		formSqlSb.append(" inner join bp_device d on (d.shop_id=s.id)");
		formSqlSb.append(" inner join bp_customer_flow cf on (cf.router_sn=d.router_sn)");
		formSqlSb.append(" inner join bp_auth auth on (auth.auth_type='phone' and auth.client_mac=cf.client_mac)");
		formSqlSb.append(" where 1=1 ");
		if(orgId!=null && !orgId.equals("")){
			formSqlSb.append(" and org.id in("+orgId+")");
		}
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and cf.create_date>='"+startDate+"'");	
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and cf.create_date<='"+startDate+"'");
		}
		if(phone!=null && !phone.equals("")){
			formSqlSb.append(" and auth.tag='"+phone+"'");
		}
		formSqlSb.append(" group by cf.create_date,cf.router_sn,cf.client_mac");
		formSqlSb.append(" order by cf.create_date desc");
	}
	

	//@formatter:off 
	/**
	 * Title: downFlowStaFile
	 * Description:流量分析下载
	 * Created On: 2015年6月12日 下午5:02:18
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public Render downFlowStaFile(Map<String, String> queryMap) {
		String startDate = queryMap.get("startDate");// 开始时间
		String endDate = queryMap.get("endDate");// 结束时间
		String shopId = queryMap.get("shopId");
		String orgId = queryMap.get("orgId");
		String phone = queryMap.get("phone");
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}	
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
		}
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		StringBuilder formSqlSb = new StringBuilder(" select org.name orgname,s.name shopname,d.name devicename,ROUND(cf.upload/1024/1024,2) as upload ");
		formSqlSb.append(" ,ROUND(cf.download/1024/1024,2) as download,cf.create_date,auth.tag,cf.client_mac  ");
		formSqlSb.append(" from sys_org org");
		formSqlSb.append(" inner join bp_shop s on org.id=s.org_id ");
		formSqlSb.append(" inner join bp_device d on (d.shop_id=s.id)");
		formSqlSb.append(" inner join bp_customer_flow cf on (cf.router_sn=d.router_sn)");
		formSqlSb.append(" inner join bp_auth auth on (auth.tag<>'noAuth' and auth.client_mac=cf.client_mac)");
		formSqlSb.append(" where 1=1 ");
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and cf.create_date>='"+startDate+"'");	
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and cf.create_date<='"+startDate+"'");
		}
		if(phone!=null && !phone.equals("")){
			formSqlSb.append(" and auth.tag='"+phone+"'");
		}
		if (shopId != null && !shopId.equals("")) {
			formSqlSb.append(" and s.id IN (" + shopId + ")  ");
		}
		if (orgId != null && !orgId.equals("")) {
			List<Record> orgList = new ArrayList<Record>();
			for (String oid : orgId.split(",")) {
				List<Record> resultList = DataOrgUtil.getChildrens(oid, true);
				orgList.addAll(resultList);
			}
			HashSet h = new HashSet(orgList);
			orgList.clear();
			orgList.addAll(h);
			String ordIds = DataOrgUtil.recordListToSqlIn(orgList, "id");
			formSqlSb.append(" and org.id IN (" + ordIds + ")  ");
		}
		formSqlSb.append(" group by cf.client_mac");
		List list=Db.find(formSqlSb.toString());
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"create_date","orgname","shopname","devicename","tag","client_mac","upload","download"}; 
		String[] heades = {"日期","组织名称","商铺名称","盒子名称","手机号码","终端地址","上传流量(M)","下载流量(M)"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("flowInfo.xls");
		return excel;
	}

}
