
package com.yinfu.business.bigdata.model;

<<<<<<< HEAD
import java.sql.Struct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
=======
import java.util.ArrayList;
import java.util.Calendar;
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
<<<<<<< HEAD
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.ColorUtil;
import com.yinfu.business.util.StrUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.DbUtil;
=======
import com.yinfu.business.util.ColorUtil;
import com.yinfu.jbase.jfinal.ext.Model;
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
public class BigData extends Model<BigData> {
	private static final long serialVersionUID = 1L;
	public static BigData dao = new BigData();
	
<<<<<<< HEAD
	
	public List<BigData> tobin(String tab,String typename,String starttime,String  endtime,String shopid) {
		StringBuilder sb=new StringBuilder();
		sb.append("select IFNULL(t.typename,'"+typename+"') as typename,t.typenum,count(b.phone) as renshu,b.phone from bg_sys_user_type t  join ");
				sb.append("(SELECT t.*  from "+tab+" t where 1=1 ");
				sb.append(" and DATE_FORMAT(startime,'%Y-%m-%d')<='"+endtime+" ' and  DATE_FORMAT(startime,'%Y-%m-%d')>='"+starttime+"'    " );
				sb.append("   ) b on b.typenum = t.typenum  ");
				sb.append(" and b.businessid in ("+shopid+") ");
				sb.append(" and t.typename='"+typename+"'");
			return dao.find(sb.toString());
	}
	
	/* 用户类型 归属  饼图显示：*/
	public String getDeviceStaInfo(String tab,String startime,String endtime,String shopid) {
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<chart palette='4' decimals='0' baseFont ='微软雅黑'  baseFontSize='13'  showValues='1'   labelSepChar=':' showPercentValues='1'  enableSmartLabels='1' enableRotation='0'  bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='0' startingAngle='80' >");
		//八大类型展示
		 List<BigData> list=  toUserTypes(tab);
		 for(int j=0;j<list.size();j++){
			 List<BigData> bd=tobin(tab,list.get(j).getStr("typename"),startime,endtime,shopid);
			 if(bd.size()<=0){
				 xmlData.append("<set label='0' value='0' />");
			 }
			 for(int i=0;i<bd.size();i++){
				 xmlData.append("<set label='"+bd.get(i).getStr("typename")+"' value='" +bd.get(i).getLong("renshu") + "' />");
			 }
		 }
		 xmlData.append("</chart>");
		return xmlData.toString();
	}
	public SplitPage findList(SplitPage splitPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bb.phone,gg.typename,bb.count,DATE_FORMAT(bb.startime,'%Y-%m-%d') as startime   ");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValueshopid) {
		String startDate = queryParam.get("startDate");// 开始时间
		String endDate = queryParam.get("endDate");// 结束时间
		String tab=queryParam.get("tab");
		String shopid=queryParam.get("shopId");
		formSqlSb.append(" from bg_sys_user_type gg right join  "+tab+" bb on gg.typenum=bb.typenum ");
		formSqlSb.append(" where 1=1 ");
		if(StrUtil.isNotBlank(shopid))
		formSqlSb.append(" and  bb.businessid in ("+shopid+")" );
		formSqlSb.append("  and  bb.phone is not null  and  bb.phone !=''  ");
		formSqlSb.append(" and bb.startime  <= '"+endDate+ " 23:59:59' and  bb.startime >='"+ startDate+" 00:00:00' ");
	}
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	/**
	 * 用户信息查询
	 * 
	 * @param tab
	 * @return
	 */
<<<<<<< HEAD
	public List<BigData> toUserTypes(String tab,String days,String typename,String shopid) {
		StringBuilder sb=new StringBuilder();
		sb.append("select IFNULL(t.typename,'"+typename+"') as typename,b.startime,t.typenum,count(b.phone) as renshu,b.phone,DATE_FORMAT(startime,'%Y%m%d') as  dat from bg_sys_user_type t left join ");
				sb.append("(SELECT t.*   from "+tab+" t ");
				sb.append(" where 1=1  ");
				if(StrUtil.isNotBlank(days))
				sb.append("and DATE_FORMAT(t.startime,'%Y-%m-%d') = '"+days+"' ");//group by t.typenum,DATE_FORMAT(t.startime,'%Y-%m-%d')
				sb.append("    )b on b.typenum = t.typenum  ");
				sb.append(" and  b.businessid  in ("+shopid+")  ");
				sb.append("and t.typename='"+typename+"'");
				return dao.find(sb.toString());
	}
	public List<BigData> toUserTypes(String tab) {
			StringBuilder sb=new StringBuilder();
			sb.append("select t.* from bg_sys_user_type t  ");//left join "+tab+" tab on tab.typenum=t.typenum
//			sb.append(" group by t.id ");
			return dao.find(sb.toString());
	}
	
	public List<BigData> toUserTypesgetDay(String tab,String starttime,String endtime) {
 				StringBuilder sb=new StringBuilder();
				sb.append("select t.typename,b.counttime,b.startime,DATE_FORMAT(startime,'%Y-%m-%d') startday,IFNULL(b.count,0) as count,DATE_FORMAT(startime,'%Y%m%d') days,b.phone from bg_sys_user_type t right join "+tab+" b on t.typenum = b.typenum where 1=1 ");
				sb.append(" and DATE_FORMAT(startime,'%Y-%m-%d')<='"+endtime+"' and  DATE_FORMAT(startime,'%Y-%m-%d')>='"+starttime+"'    " );
				sb.append(" group by days  LIMIT 7");
				return dao.find(sb.toString());
	}
	/**
	 * 类别访问量
	 * @param tabname
	 * @param starttime
	 * @param endtime
	 * @param shopid
	 * @return
	 */
	public String  split(String tabname,String starttime,String endtime,String shopid){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='类别访问量'  baseFont ='微软雅黑'  baseFontSize='13'  yAxisName='访问人数' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		if(StrUtil.isBlank("tabname"))return null;
		List<BigData> toUser=toUserTypesgetDay(tabname,starttime,endtime);
		sb.append("<categories>");
		if(toUser.size()<=0){
			sb.append("<category label='"+starttime+"' />");
		}
		for(int i=0;i<toUser.size();i++){
			String days= toUser.get(i).getStr("startday"); 
			sb.append("<category label='"+days+"' />");
		}
		sb.append("</categories>");
		List<BigData> bds=	toUserTypes(tabname);
		for(int i=0;i<bds.size();i++){
			sb.append("<dataset seriesname='"+bds.get(i).getStr("typename")+"'  showvalues='1'>");
			for (int j = 0; j < toUser.size(); j++) {
				String days= toUser.get(j).getStr("startday");
				List<BigData> bd=toUserTypes(tabname,days,bds.get(i).getStr("typename"),shopid);
				if(bd.size()<=0){
					sb.append( "<set value='0' />");
				}
				for(int s=0;s<bd.size();s++){
					sb.append( "<set value='"+bd.get(s).get("renshu")+"' />");
				}
			}
			sb.append( "</dataset>");
=======
	public List<BigData> toUserTypes(String tab,String starttime,String endtime) {
		StringBuilder sb=new StringBuilder();
		sb.append("select t.typename,b.counttime,IFNULL(b.count,0) as count,IFNULL(b.visit_time,now()) as visit_time,b.phone,b.sn,b.keyword, DATE_FORMAT(visit_time,'%Y%m%d') days from bg_sys_user_type t left join "+tab+" b on t.typenum = b.typenum where 1=1 ");
//			if(starttime.trim().length()>0&&starttime!=null)
//				sb.append("and b.visit_time <'' and b.visit_time>'' ");
		sb.append(" group by t.typename,days ");
		System.out.println(sb.toString()+"");
		return dao.find(sb.toString());
	}
	
	public String  split(String tabname,String starttime,String endtime){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='类别访问量' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		if(tabname.trim().length()<0&&tabname==null)return null;
		List<BigData> bd=toUserTypes(tabname,starttime,endtime);
//		sb.append("<categories><category label='2014-12-08' /><category label='2014-12-09' /><category label='2014-12-10' />");
		sb.append("<categories>");
		for(int i=0;i<bd.size();i++){
			sb.append("<category label='2014-12-08' />");
		}
		sb.append("</categories>");
		for(int i=0;i<bd.size();i++){
			sb.append("<dataset seriesname='"+bd.get(i).getStr("typename")+"' color='"+ColorUtil.getRandColorCode()+"' showvalues='1'><set value='"+bd.get(i).getLong("count")+"' /></dataset>");
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		}
		sb.append("</chart>");
		return sb.toString();
	}
<<<<<<< HEAD
	public List<BigData> toadvdays(String tab) {
			StringBuilder sb=new StringBuilder();
			sb.append("SELECT bt.counttime,bt.count   from  "+tab+" bt  left join  bg_sys_user_type bsut on bsut.typenum = bt.typenum  where 1=1 GROUP BY counttime");
			return dao.find(sb.toString());
	}
	
	public List<BigData> toadv(String tab) {
		StringBuilder sb=new StringBuilder();
		sb.append("SELECT bt.counttime,bs.name,bt.count  from  "+tab+"  bt LEFT JOIN bp_shop  bs on  bt.businessid = bs.id left join   bg_sys_user_type bsut on bsut.typenum = bt.typenum  where 1=1 GROUP BY name");
		return dao.find(sb.toString());
	}
	
	public String  adv(String tabname,String starttime,String endtime){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='类别访问量'  baseFont ='微软雅黑'  baseFontSize='13'  numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		List<BigData> bd=toadvdays(tabname);
		sb.append("<categories><category label='2014-12-08' /><category label='2014-12-09' /><category label='2014-12-10' /></categories>");
		for(int i=0;i<bd.size();i++){
			sb.append("<dataset seriesname='"+bd.get(i).getStr("typename")+"' color='"+ColorUtil.getRandColorCode()+"' showvalues='1'><set value='"+bd.get(i).getInt("count")+"' /></dataset>");
		}
		sb.append("</chart>");
		return sb.toString();
	}
	/*   用户行为分析              */
	public String  function(String tabname,String starttime,String endtime,String shopid){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='用户行为分析' yAxisName='访问人数' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		sb.append("<categories>");
		   List<BigData> month=    getmontyInte(tabname, starttime, endtime);
		for(int i=0;i<month.size();i++){
			sb.append("<category label='"+month.get(i).getStr("riqi")+"' />");
		}
		sb.append("</categories>");
		List<BigData> bds=	tofuntype(tabname);
		for(int i=0;i<bds.size();i++){
			sb.append("<dataset seriesname='"+bds.get(i).getStr("typename")+"'  showvalues='1'>");
			for (int t = 0; t < month.size(); t++) {
				List<BigData> bd=tofunction(tabname,month.get(t).getStr("yuefen"),bds.get(i).getStr("typename"),shopid);
				for(int s=0;s<bd.size();s++){
					sb.append( "<set value='"+bd.get(s).get("renshu")+"' />");
			}
		}
			sb.append( "</dataset>");
		}
		sb.append("</chart>");
		return sb.toString();
	}
	public List<BigData> getmontyInte(String tab,String starttime,String endtime) {
		StringBuilder sb=new StringBuilder();
	sb.append("select t.typename,DATE_FORMAT(startime,'%Y年%m月') riqi,b.counttime,DATE_FORMAT(startime,'%Y-%m') yuefen,IFNULL(b.count,0) as count,b.phone from bg_sys_user_type t right join "+tab+" b on t.typenum = b.typenum where 1=1 ");
	if(StrUtil.isEmpty(endtime)){
		sb.append(" and startime is not null ");
	}else{
		sb.append(" and startime<  '"+endtime+"'");
	}
	if(StrUtil.isEmpty(starttime)){
		sb.append(" and startime  is not null ");
	}else{
		sb.append(" and  startime>  '"+starttime+"'    ");
	}
	sb.append(" group by yuefen  LIMIT 12");
	return dao.find(sb.toString());
	}
	//所属类型
	public List<BigData> tofuntype(String tab) {
		StringBuilder sb=new StringBuilder();
		sb.append("select t.typename,b.counttime,IFNULL(b.count,0) as count,b.phone from bg_sys_user_type t left join "+tab+" b on t.typenum = b.typenum where 1=1 ");
		sb.append(" group by t.id ");
		return dao.find(sb.toString());
	}
	//查询天数
	public List<BigData> tofunctionDay(String tab,String starttime,String endtime) {
			StringBuilder sb=new StringBuilder();
			sb.append("select t.typename,b.counttime,IFNULL(b.count,0) as count,b.phone  from bg_sys_user_type t right join "+tab+" b on t.typenum = b.typenum where 1=1 ");
			if(starttime!=null && endtime!=null){
				sb.append(" and b.counttime<'"+endtime+"' and  b.counttime>'"+starttime+"'    " );
			}
			sb.append(" group by counttime  LIMIT 10 ");
		return dao.find(sb.toString());
	}
	//用户行为分析
	public List<BigData> tofunction(String tab,String days,String typename,String  shopid) {
		StringBuilder sb=new StringBuilder();
				sb.append("select IFNULL(t.typename,'"+typename+"') as typename,b.startime,t.typenum,count(b.phone) as renshu from bg_sys_user_type t right join ");
				sb.append("(SELECT businessid ,phone , typenum , startime  from "+tab+" t where 1=1 ");
				if(StrUtil.isNotBlank(days))
				sb.append("and DATE_FORMAT(t.startime,'%Y-%m') = '"+days+"' ");
				sb.append("    )b on b.typenum = t.typenum  ");
				sb.append("   where 1=1 ");
				sb.append("and t.typename='"+typename+"'");
				if(StrUtil.isNotBlank(shopid))
				sb.append(" and  b.businessid in ("+ shopid+")      ");
		return  dao.find(sb.toString());
	}
=======
	
/*	public String  function(String tabname,String starttime,String endtime){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='类别访问量' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		if(tabname.trim().length()<0&&tabname==null) return null;
		List<BigData> bd=toUserTypes(tabname);
		sb.append("<categories><category label='2014-12-08' /><category label='2014-12-09' /><category label='2014-12-10' /></categories>");
		for(int i=0;i<bd.size();i++){
			sb.append("<dataset seriesname='"+bd.get(i).getStr("typename")+"' color='"+ColorUtil.getRandColorCode()+"' showvalues='1'><set value='"+bd.get(i).getLong("count")+"' /></dataset>");
		}
		sb.append("</chart>");
		return sb.toString();
	}*/
	/*public String  flow(String tabname,String starttime,String endtime){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='类别访问量' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		if(tabname.trim().length()<0&&tabname==null)return null;
		List<BigData> bd=toUserTypes(tabname);
		sb.append("<categories><category label='2014-12-08' /><category label='2014-12-09' /><category label='2014-12-10' /></categories>");
		for(int i=0;i<bd.size();i++){
			sb.append("<dataset seriesname='"+bd.get(i).getStr("typename")+"' color='"+ColorUtil.getRandColorCode()+"' showvalues='1'><set value='"+bd.get(i).getLong("count")+"' /></dataset>");
		}
		sb.append("</chart>");
		return sb.toString();
	}*/
	/*public String  adv(String tabname,String starttime,String endtime){
		StringBuilder sb=new StringBuilder();
		sb.append("<chart palette='5' caption='类别访问量' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		if(tabname.trim().length()<0&&tabname==null)return null;
		List<BigData> bd=toUserTypes(tabname);
		sb.append("<categories><category label='2014-12-08' /><category label='2014-12-09' /><category label='2014-12-10' /></categories>");
		for(int i=0;i<bd.size();i++){
			sb.append("<dataset seriesname='"+bd.get(i).getStr("typename")+"' color='"+ColorUtil.getRandColorCode()+"' showvalues='1'><set value='"+bd.get(i).getLong("count")+"' /></dataset>");
		}
		sb.append("</chart>");
		return sb.toString();
	}*/
	
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
}
