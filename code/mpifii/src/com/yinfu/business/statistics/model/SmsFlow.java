package com.yinfu.business.statistics.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.model.SplitPage.SplitPage;

@TableBind(tableName="bp_sms_flow")
public class SmsFlow extends Model<SmsFlow>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final SmsFlow dao = new SmsFlow();
	
	private static final Log log= LogFactory.getLog(SmsFlow.class);

	//@formatter:off 
	/**
	 * Title: getCustomerStaInfo
	 * Description:短信流水统计报表
	 * Created On: 2015年1月10日 下午4:27:20
	 * @author 
	 * <p>
	 * @param queryMap
	 * @return 
	 * @throws Exception 
	 */
	//@formatter:on
	public String getSmsFlowInfo(Map<String, String> queryMap)  {
		String xmlData="";
		try {
			String monthnums = queryMap.get("_query.monthnums"); 
			String startDate = queryMap.get("_query.startDate");// 开始时间
			String endDate = queryMap.get("_query.endDate");// 结束时间
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			int userOrgId = ContextUtil.getCurrentUser().getInt("org_id");
			//userOrgId=7;
			//查看短信统计周期为四个月
			if ((endDate!=null && !endDate.equals(""))&&(startDate!=null && !startDate.equals(""))) {
				endDate = sdf.format(sdf.parse(endDate));
				startDate = sdf.format(sdf.parse(startDate));
			}else{
				endDate = sdf.format(new Date());
				startDate =  DateUtil.getBeforOrAfterNMonth(endDate, -3);
			}
			//String orgIds = ContextUtil.getNextLevleByUser();// 获得当前用户的组织直属下一级组织
			
			StringBuilder sql = new StringBuilder();
			sql.append(" select org.id orgId,count(f.id) as zs, date_format(f.send_time,'%Y-%m') as dates ");
			sql.append(" from sys_org org ");
			sql.append(" left join bp_shop s on s.org_id=org.id");
			sql.append(" left join bp_sms_flow f on f.shop_id=s.id");
			sql.append(" where 1=1");
			sql.append(" and org.id in("+getOrgIds()+")");
			sql.append(" and DATE_FORMAT(f.send_time,'%Y-%m')>='" +startDate +"'");
			sql.append(" and DATE_FORMAT(f.send_time,'%Y-%m')<='" +endDate +"'");
			sql.append(" group by dates,orgId");
			sql.append(" order by dates");
			List<Record> zslist = Db.find(sql.toString());
			List<Record> orgGroupList = getOrgDimension(userOrgId); 
			List<String> timeList = getSmsSendTimeDime(startDate,endDate,monthnums);
			xmlData=buildColumnXml(startDate,endDate,timeList,orgGroupList,zslist);
		} catch (ParseException e) {
			log.error("短信统计日期转换格式错误", e);
		}
		return xmlData;
	}
	
	/**
	 * 得到该用户下组织的所有子组织维度
	 * @param orgPid
	 * @return
	 */
	public List<Record> getOrgDimension(int orgId){
		List<Record> list = Db.find("select id,name from sys_org where pid="+orgId+" group by name");
		if(list!=null && list.size()>0){
			return list;
		}else{//没有子节点
			return Db.find("select id,name from sys_org where id="+orgId+" group by name");
		}
		
	}
	
	/**
	 * 得到时间维度
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException 
	 */
	public List<String> getSmsSendTimeDime(String startDate,String endDate,String monthnums) throws ParseException{
		int month = 0;
		int count = -3;
		if(monthnums!=null && !monthnums.equals("")){
			month=Integer.parseInt(monthnums);
			switch (month) {
			case 0:
				count=0;
				break;
			case 1:
				count=-1;
				break;
			case 2:
				count=-2;
				break;
			case 3:
				count=-3;
				break;
			}
		}
		List<String> rellist =new ArrayList<String>();
	
		for(int i=count;i<0;i++){
			rellist.add(DateUtil.getBeforOrAfterNMonth(endDate, i));
		}
		rellist.add(endDate);
		return rellist;
	}
	
	/**
	 * 组装column统计图形xml
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String buildColumnXml(String startDate,String endDate,List<String> timelist,
			List<Record> orgList,List<Record> zsList){
		StringBuffer xmlData = new StringBuffer();
		String caption =  startDate+"至"+endDate+"短信统计表";
		xmlData.append("<chart caption='" + caption + "' xaxisname='日期' yaxisname='短信数' theme='fint'");
		xmlData.append(" baseFont ='微软雅黑'  baseFontSize='13' numberprefix='' rotatevalues='1' ");
		xmlData.append(" placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		xmlData.append("<categories>");
		for (int i = 0; i < timelist.size(); i++) {
			xmlData.append("<category label='" + timelist.get(i) + "' />");
		}
		xmlData.append(" </categories>");
		boolean addbool=true;
		long sumSms = 0;
		for (int i = 0; i < orgList.size(); i++) {
			Record orgObj = orgList.get(i);
			//得到该组织的本身和以下的子节点
			List<Record> orgChilds = DataOrgUtil.getChildrens(orgObj.getInt("id"), true);
			xmlData.append(" <dataset seriesname='"+orgList.get(i).get("name")+"' showvalues='1'>");
			for(int k=0;k<timelist.size();k++){
				addbool=true;
				sumSms=0;
			    for (int j = 0; j < zsList.size(); j++) {
				    Record zsObj = zsList.get(j);
					String zsdate = zsObj.getStr("dates").trim();
					String smsdate = timelist.get(k);
					if(zsdate.equals(smsdate)){
						if(isOrgChild(orgChilds,zsObj.getInt("orgId"))){
							sumSms+=Integer.parseInt(zsObj.get("zs").toString());
							addbool=false;
						}
					}
				    if(addbool && j==zsList.size()-1){
				    	xmlData.append("<set value='0' />");
				    }else if(j==zsList.size()-1){
						xmlData.append("<set value='" + sumSms + "' />");
					}	
				}
			}
			xmlData.append(" </dataset> ");
		}
		xmlData.append(" </chart>");
		
		return xmlData.toString();
	}
	
	public SplitPage getSmsFlowList(SplitPage splitPage){
		//int userOrgId = ContextUtil.getCurrentUser().getInt("org_id");
		StringBuilder sql = new StringBuilder();
		sql.append(" select org.name as orgName,s.name as shopName,count(f.id) as zs, date_format(ifnull(f.send_time,current_date()),'%Y-%m') as sendTime ");
		splitPage = splitPageBase(splitPage, sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam,
			StringBuilder formSqlSb, List<Object> paramValue) {
		
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String shopId = queryParam.get("shop_id");
		String orgId = queryParam.get("org_id");
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		formSqlSb.append(" from sys_org org ");
		formSqlSb.append(" inner join bp_shop s on s.org_id=org.id");
		formSqlSb.append(" left join bp_sms_flow f on f.shop_id=s.id");
		formSqlSb.append(" where 1=1");
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" and s.id in("+shopId+")");
		}
		//sql.append(" and org.pid="+userOrgId);
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(f.send_time,'%Y-%m-%d')>='" +startDate +"'");
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(f.send_time,'%Y-%m-%d')<='" +endDate +"'");
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
			formSqlSb.append(" and org.id in("+ordIds+")");
		}
		formSqlSb.append(" group by org.name,s.name");
	}
	
    /**
     * 判断该组织是否是子组织
     * @param orgChilds 父组织集合
     * @param orgChildId 子组织的编号
     * @return
     */
    public boolean isOrgChild(List<Record> orgChilds,int orgChildId){
    	boolean relBool = false;
    	for(Record orgChild:orgChilds){
    		if(orgChild.getInt("id")==orgChildId){
    			relBool = true;
    			break;
    		}
    	}
    	return relBool;
    }
    
	public String getOrgIds(){
		String relOrgIds = "";
		List<Record> list= ContextUtil.getOrgListByUser();
		for(int i=0;i<list.size();i++){
			if(i==list.size()-1){
				relOrgIds += list.get(i).getInt("id")+"";
			}else{
				relOrgIds += list.get(i).getInt("id")+",";
			}
		}
		return relOrgIds;
	}

	//@formatter:off 
	/**
	 * Title: downSmsFlowFile
	 * Description:短信统计下载
	 * Created On: 2015年6月12日 下午3:45:00
	 * @author JiaYongChao
	 * <p>
	 * @param queryMap
	 * @return 
	 */
	//@formatter:on
	public Render downSmsFlowFile(Map<String, String> queryMap) {
		String startDate = queryMap.get("startDate");// 开始时间
		String endDate = queryMap.get("endDate");// 结束时间
		String shopId = queryMap.get("shopId");
		String orgId = queryMap.get("orgId");
		if((shopId == null || shopId.equals("")) && !ContextUtil.isAdmin()  ){
			shopId = ContextUtil.getShopByUser();
		}
		StringBuilder formSqlSb = new StringBuilder(" select org.name as orgname,s.name as shopname,count(f.id) as zs, date_format(ifnull(f.send_time,current_date()),'%Y-%m') as sendTime ");
		formSqlSb.append(" from sys_org org ");
		formSqlSb.append(" inner join bp_shop s on s.org_id=org.id");
		formSqlSb.append(" left join bp_sms_flow f on f.shop_id=s.id");
		formSqlSb.append(" where 1=1");
		if(shopId!=null && !shopId.equals("")){
			formSqlSb.append(" and s.id in("+shopId+")");
		}
		if(startDate!=null && !startDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(f.send_time,'%Y-%m-%d')>='" +startDate +"'");
		}
		if(endDate!=null && !endDate.equals("")){
			formSqlSb.append(" and DATE_FORMAT(f.send_time,'%Y-%m-%d')<='" +endDate +"'");
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
			formSqlSb.append(" and org.id in("+ordIds+")");
		}
		formSqlSb.append(" group by org.name,s.name");
		List list=Db.find(formSqlSb.toString());
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"sendTime","orgname","shopname","zs"}; 
		String[] heades = {"短信发送日期","组织名称","商铺名称","总数量"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName("smsInfo.xls");
		return excel;
	}

}
