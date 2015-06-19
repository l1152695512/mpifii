package com.yinfu.business.statistics.model;

import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

public class AdvMgtReport extends Model<AdvMgtReport>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final AdvMgtReport dao=new AdvMgtReport();
	
	private static final String[] times=new String[]{"00:00","01:00","02:00","03:00","04:00",
		"05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00"
		,"16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
	
	public SplitPage getAdvList(SplitPage splitPage){
		String uv="uv";
		String advuv="advuv";
		if(!isQueryTime(splitPage.getQueryParam())){
			uv="advuv";
			advuv="uv";
		}
		StringBuffer sql=new StringBuffer("select adv.adv_id,adv.show_num,adv.click_num,adv.adv_date,adv.adv_time,");
		sql.append("adv.adv_uv as "+uv);
		sql.append(",adv.adv_today_uv as "+advuv);
		sql.append(",org.name orgname,s.name shopname,c.name advname,sp.name spacename,ap.start_date");
		splitPage = splitPageBase(splitPage, sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam,
			StringBuilder formSqlSb, List<Object> paramValue) {
		String time = "<> 'null'";
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		String asId = queryParam.get("as_id");
		String advName = queryParam.get("advName");
	    if(startDate!=null && !startDate.equals(endDate)){
	    	time=" is null";
		}
		formSqlSb.append(" from bp_adv_report adv ");
		formSqlSb.append(" join sys_org org on org.id=adv.org_id");
		formSqlSb.append(" join bp_shop s on s.id=adv.shop_id");
		formSqlSb.append(" join bp_adv_content c on c.id=adv.adv_id");
		formSqlSb.append(" join bp_adv_putin ap on ap.adv_content_id=c.id");
		formSqlSb.append(" join bp_adv_spaces sp on sp.id=ap.adv_space");
		formSqlSb.append(" where 1=1");
		if(orgId!=null && !orgId.equals("")){
			formSqlSb.append(" and adv.org_id in("+orgId+")");
		}
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" and s.id in("+shopId+")");
		}
		//sql.append(" and org.pid="+userOrgId);
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(adv.adv_date,'%Y-%m-%d')>='" +startDate +"'");
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(adv.adv_date,'%Y-%m-%d')<='" +endDate +"'");
		}
		if(time!=null && !time.equals("")){
			formSqlSb.append(" and adv.adv_time" +time);
		}
		if(asId!=null && !asId.equals("")){
			formSqlSb.append(" and sp.id="+asId);
		}
		if(advName!=null && !advName.equals("")){
			formSqlSb.append(" and c.name='"+advName+"'");
		}
	}
	
	/**
	 * 组装column统计图形xml
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String buildColumnXml(String startDate,String endDate,String[] des,List<Record> advList){
		//是否按小时查询
		String reportDate="adv_time";
		String reportUv="adv_uv";
		String caption =  "时间段 广告效果统计表";
		if(startDate!=null && !startDate.equals(endDate)){
			reportDate="adv_date";
			reportUv="adv_today_uv";
			caption =  startDate+"至"+endDate+" 广告效果统计表";
		}
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart caption='" + caption + "' xaxisname='日期' yaxisname='短信数' theme='fint'");
		xmlData.append(" baseFont ='微软雅黑'  baseFontSize='13' numberprefix='' rotatevalues='1' ");
		xmlData.append(" placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		xmlData.append("<categories>");
		for (int i = 0; i < des.length; i++) {
			xmlData.append("<category label='" + des[i] + "' />");
		}
		xmlData.append(" </categories>");
		xmlData.append(" <dataset seriesname='展示数' showvalues='1'>");
		for (int i = 0; i < des.length; i++) {
			int shownum=0;
			for (int k = 0; k < advList.size(); k++) {
				String str = advList.get(k).get(reportDate);
			    if(des[i]!=null && des[i].equals(str)){
			    	shownum=advList.get(k).getBigDecimal("show_num").intValue();
			    	break;
				}
			}
			xmlData.append("<set value='"+shownum+"' />");
		}
		xmlData.append(" </dataset> ");
		
		xmlData.append(" <dataset seriesname='点击数' showvalues='1'>");
		for (int i = 0; i < des.length; i++) {
			int clicknum=0;
			for (int k = 0; k < advList.size(); k++) {
				String str = advList.get(k).get(reportDate);
			    if(des[i]!=null && des[i].equals(str)){
			    	clicknum=advList.get(k).getBigDecimal("click_num").intValue();
			    	break;
				}
			}
			xmlData.append("<set value='"+clicknum+"' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" <dataset seriesname='人流量' showvalues='1'>");
		for (int i = 0; i < des.length; i++) {
			int uv=0;
			for (int k = 0; k < advList.size(); k++) {
				String str = advList.get(k).get(reportDate);
			    if(des[i]!=null && des[i].equals(str)){
			    	uv=advList.get(k).getBigDecimal(reportUv).intValue();
			    	break;
				}
			}
			xmlData.append("<set value='"+uv+"' />");
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" </chart>");
		return xmlData.toString();
	}
	
	public String[] getDesic(String startDate,String endDate){
		if(startDate!=null && !startDate.equals(endDate)){
			return DateUtil.scopeTimes(startDate, endDate);
		}
		return times;
	}
	
	public String getAdvInfoXml(Map<String, String> queryMap){
		String time = "<> 'null'";
		String startDate = queryMap.get("startDate");// 开始时间
		String endDate = queryMap.get("endDate");// 结束时间
		String shopId = queryMap.get("_query.shop_id");
		String orgId = queryMap.get("_query.org_id");
		String asId = queryMap.get("_query.as_id");
		String advName = queryMap.get("_query.advName");
	    if(startDate!=null && !startDate.equals(endDate)){
	    	time=" is null";
		}
		StringBuffer sql=new StringBuffer("select sum(adv.show_num) show_num,sum(adv.click_num) click_num,");
		sql.append("sum(adv.adv_uv) adv_uv,sum(adv.adv_today_uv) adv_today_uv,DATE_FORMAT(adv.adv_date,'%Y-%m-%d') adv_date,adv.adv_time");
		sql.append(" from bp_adv_report adv ");
		sql.append(" join sys_org org on org.id=adv.org_id");
		sql.append(" join bp_shop s on s.id=adv.shop_id");
		sql.append(" join bp_adv_content c on c.id=adv.adv_id");
		sql.append(" join bp_adv_putin ap on ap.adv_content_id=c.id");
		sql.append(" join bp_adv_spaces sp on sp.id=ap.adv_space");
		sql.append(" where 1=1");
		if(orgId!=null && !orgId.equals("")){
			sql.append(" and adv.org_id in("+orgId+")");
		}
		if(shopId!=null && !shopId.equals("")){
			sql.append(" and s.id in("+shopId+")");
		}
		//sql.append(" and org.pid="+userOrgId);
		if(startDate!=null && !startDate.equals("")){
			sql.append(" and DATE_FORMAT(adv.adv_date,'%Y-%m-%d')>='" +startDate +"'");
		}
		if(endDate!=null && !endDate.equals("")){
			sql.append(" and DATE_FORMAT(adv.adv_date,'%Y-%m-%d')<='" +endDate +"'");
		}
		if(time!=null && !time.equals("")){
			sql.append(" and adv.adv_time"+time);
		}
		if(asId!=null && !asId.equals("")){
			sql.append(" and sp.id="+asId);
		}
		if(advName!=null && !advName.equals("")){
			sql.append(" and c.name='"+advName+"'");
		}
		sql.append(" group by adv_date,adv_time");
		List<Record> advlist = Db.find(sql.toString());
		String[] des = getDesic(startDate,endDate);
		String dataxml=buildColumnXml(startDate,endDate,des,advlist);
		return dataxml;
	}
	
	public List<Record> getAdvSpacesAll(){
		return Db.find("select id,name from bp_adv_spaces");
	}
	
	/**
	 *判断是否查询时间段
	 * @return true 查询时间段
	 */
	public boolean isQueryTime(Map<String,String> dates){
		boolean bool=true;
		String startDate = dates.get("startDate");// 开始时间
		String endDate = dates.get("endDate");// 结束时间
		if(startDate!=null && !startDate.equals(endDate)){
			bool=false;
		}
		return bool;
	}

}
