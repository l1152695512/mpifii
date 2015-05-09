
package com.yinfu.business.bigdata.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.business.util.ColorUtil;
import com.yinfu.jbase.jfinal.ext.Model;
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
	
	/**
	 * 用户信息查询
	 * 
	 * @param tab
	 * @return
	 */
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
		}
		sb.append("</chart>");
		return sb.toString();
	}
	
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
	
}
