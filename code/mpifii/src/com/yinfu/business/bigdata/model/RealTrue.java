
package com.yinfu.business.bigdata.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.ColorUtil;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.business.util.StrUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.model.SplitPage.SplitPage;
/**
 * 设备实体
 * 
 * @author JiaYongChao 2014年7月23日
 */
/**
 * 设备实体
 * 
 * @author JiaYongChao 2014年7月23日
 */
@TableBind(tableName = "bg_sys_user_type")
public class RealTrue extends Model<RealTrue> {
	
	private static final long serialVersionUID = 1L;
	public static RealTrue dao = new RealTrue();
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select  gg.typename,sys_org.name,bb.phone,DATE_FORMAT(bb.startime,'%Y年%m月%d日') as startime   ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String tab=queryParam.get("tab");
//		String sysid= queryParam.get("sysid");
//		List listorg=DataOrgUtil.getShopsForUser(ContextUtil.getCurrentUserId());
//		String sysid =DataOrgUtil.recordListToSqlIn(listorg,"id");
		String sysid=	DataOrgUtil.getShopsForUserInTemporary(ContextUtil.getCurrentUserId());
		if (endDate == null || endDate.equals("")) {
			endDate = DateUtil.getNow();
		}	
		if (startDate == null || startDate.equals("")) {
			startDate = DateUtil.getOneMonth(endDate);
		}
		formSqlSb.append("  from bg_sys_user_type gg left join  "+tab+" bb on gg.typenum=bb.typenum ");
		formSqlSb.append(" left join bp_shop on bp_shop.id = bb.businessid ");
		formSqlSb.append(" join "+sysid+" ss on ss.id = bb.businessid  " );
		formSqlSb.append(" join sys_org on sys_org.id = bp_shop.org_id ");
		formSqlSb.append(" where 1=1 ");
//		if(StrUtil.isNotBlank(sysid))
//		formSqlSb.append(" and  bp_shop.id in ("+sysid+") ");
		if(StrUtil.isNotBlank(endDate))
		formSqlSb.append(" and bb.startime  <= '"+endDate+ " 23:59:59' ");
		if(StrUtil.isNotBlank(startDate))
		formSqlSb.append(" and  bb.startime >='"+ startDate+" 00:00:00' ");
		formSqlSb.append(" and  bb.phone is not null and bb.phone !=''");
	}
	public String  split(String tabname,String starttime,String endtime, String orgid){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='精准用户分析'  baseFont ='微软雅黑'  baseFontSize='13'  yAxisName='访问人数'  rotateYAxisName='0' yAxisMaxValue='8'  placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		if(StrUtil.isBlank("tabname"))return null;
		List<RealTrue> toUser=toUserTypesgetDay(tabname);
		sb.append("<categories>");
		if(toUser.size()<=0){
			sb.append("<category label='"+starttime+"' />");
		}
		for(int i=0;i<toUser.size();i++){
			String days= toUser.get(i).getStr("riqi");
			sb.append("<category label='"+days+"' />");
		}
		sb.append("</categories>");
			List<Record> bds =ContextUtil.getNextListByUser(orgid);
			for(int s=0;s<bds.size();s++){
				sb.append("<dataset seriesname='"+bds.get(s).getStr("name")+"'  showvalues='1'>");
				Integer sysid=bds.get(s).getInt("id");
				 List<Record> list =DataOrgUtil.getChildrens(sysid, true);
				 String orgIds= "";
				 for (Record record : list) {
					String orgId = String.valueOf(record.get("id"));
					orgIds+=orgId+",";
				}
				 orgIds =orgIds.substring(0, orgIds.length()-1);
				for(int i=0;i<toUser.size();i++){
					List<RealTrue> bd=toendMethod(tabname,toUser.get(i).getStr("yuefen"),orgIds);
					for (int j = 0; j < bd.size(); j++) {
						sb.append( "<set  value='"+bd.get(j).get("renshu")+"' />");
				}
			}
			sb.append( "</dataset>");
		}	
		sb.append("</chart>");
		return sb.toString();
	}
	public List<RealTrue> toUserTypesgetDay(String tab) {
			StringBuilder sb=new StringBuilder();
		sb.append("select b.id as bid,t.typename,DATE_FORMAT(startime,'%Y年%m月') riqi,t.typenum,b.counttime,b.startime,DATE_FORMAT(startime,'%Y-%m') yuefen,IFNULL(b.count,0) as count,DATE_FORMAT(startime,'%Y%m%d') days from bg_sys_user_type t right join "+tab+" b on t.typenum = b.typenum ");
//		sb.append(" left join bp_shop tt on tt.tel = b.phone ");
		sb.append(" where 1=1  ");
//		sb.append(" and b.startime<'"+endtime+"' and  b.startime>'"+starttime+"'    " );
		sb.append(" group by yuefen  LIMIT 12");
		return dao.find(sb.toString());
	}
	
	public List<RealTrue> toendMethod(String tab,String yuefen,String shopid) {
				StringBuilder sb=new StringBuilder();
				sb.append("select  b.startime,t.typename,count(b.phone) as renshu,DATE_FORMAT(startime,'%Y%m%d') as  dat from bg_sys_user_type t left join ");
				sb.append("(SELECT  businessid ,phone , typenum , startime  from "+tab+" t where 1=1 ");
				sb.append(" ) b on b.typenum = t.typenum ");
				sb.append("   where 1=1  ");
				sb.append("   and b.businessid in ( SELECT bp_shop.id from bp_shop  JOIN sys_org on bp_shop.org_id=sys_org.id where sys_org.id in ("+shopid+")) ");
				sb.append("   and DATE_FORMAT(startime,'%Y-%m')='"+yuefen+"' ");
//				sb.append(" and   b.typenum = '"+typenum+"'");
				return dao.find(sb.toString());
	}
}
