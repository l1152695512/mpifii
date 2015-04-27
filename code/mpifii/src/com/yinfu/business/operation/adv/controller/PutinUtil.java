package com.yinfu.business.operation.adv.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;

public class PutinUtil {
	/**
	 * 获取未投放的策略，去重之后即可得到某种策略的可投放策略值，如要获取可投放的行业，则在可投放的所有策略里按照行业去重之后即可获取可投放的行业值
	 * 获取方式：符合条件的总投放策略减去已投放的策略
	 * 
	 * @param advSpace
	 * @param startDate
	 * @param endDate
	 * @param params
	 * @return
	 */
	private List<Record> getAvailablePlot(Object advSpace,String startDate,String endDate,Map<String,String> params){
		//sql解释：这个sql是获取全部组织、行业、星期、时间的总和记录数，如果新投放的广告时长少于一个星期需要减去对应星期的记录数
		StringBuffer sql = new StringBuffer();
		sql.append("select CONCAT(dd.id,'_',dt.id,'_',di.id,'_',so.id) id,dd.id week_id,dt.id time_id,di.id industry_id,if(bao.id is null,'1','0') permission,");
		sql.append("so.id org_id,so.`name` org_name,CONCAT(dd.id,'_',dt.id) weeks_times,di.id industry_id,di.value industry_name ");
		sql.append("from system_user su join sys_org so on (su.id=? and so.pid=su.org_id ");
		if(null != params.get("org")){
			sql.append("and so.id in ("+params.get("org")+")");
		}
		sql.append(") ");
		sql.append("join bp_dictionary di on (di.type='industry' ");
		if(null != params.get("industry")){
			sql.append("and di.id in ("+params.get("industry")+")");
		}
		sql.append(") ");
		sql.append("join bp_dictionary dd on (dd.type='adv_day' ");
		String weekInSql = checkPutinWeek(startDate,endDate);
		if(null == params.get("weeksAndTimes") && weekInSql.length() > 0){
			sql.append("and dd.id in ("+weekInSql+")");
		}
		sql.append(") ");
		sql.append("join bp_dictionary dt on (dt.type='adv_time') ");
		sql.append("left join bp_adv_org bao on (bao.edit_able and bao.adv_spaces=? and so.id=bao.org_id) ");
		sql.append("where 1=1 ");
		if(null != params.get("weeksAndTimes")){
			sql.append("and CONCAT(dd.id,'_',dt.id) in ("+params.get("weeksAndTimes")+") ");
		}
		List<Record> allPlots = Db.find(sql.toString(), new Object[]{ContextUtil.getCurrentUserId(),advSpace});
		List<String> putinPlots = putinPlots(advSpace,startDate,endDate,params);
		for(int i=allPlots.size()-1;i>=0;i--){
			Record rec = allPlots.get(i);
			if(putinPlots.contains(rec.get("id"))){
				allPlots.remove(i);
			}
		}
		return allPlots;
	}
	
	/**
	 * 去除起止日期之外的星期
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private String checkPutinWeek(String startDate,String endDate){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sqlIn = new StringBuffer();
		sqlIn.append("''");
		try {
			Date start = format.parse(startDate);
			Date end = format.parse(endDate);
			long days = (end.getTime()-start.getTime())/1000/60/60/24;
			if(days < 6){//投放的天数少于一个星期，则少了的星期是不能选择时间的(日期是包前又包后的)
				List<Record> weeks = Db.find("select id,`key` from bp_dictionary where type='adv_day' ");
				Map<String,Object> weeksMap = new HashMap<String, Object>();
				Iterator<Record> ite = weeks.iterator();
				while(ite.hasNext()){
					Record rec = ite.next();
					weeksMap.put(rec.getStr("key"), rec.get("id"));
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				while(!calendar.getTime().after(end)){
					sqlIn.append(",'"+weeksMap.get(calendar.get(Calendar.DAY_OF_WEEK)-1+"")+"'");
					calendar.add(Calendar.DATE,1);
				}
				return sqlIn.toString();
			}else{
				return "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) throws ParseException {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Date start = format.parse("2014-01-01");
//		Date end = format.parse("2014-01-07");
//		long days = (end.getTime()-start.getTime())/1000/60/60/24;
//		System.err.println(days);
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(format.parse("2015-03-18"));
//		for(int i=0;i<7;i++){
//			System.err.println(calendar.get(Calendar.DAY_OF_WEEK)-1);
//			calendar.add(Calendar.DATE,1);
//		}
		
//		InitDemoDbConfig.initPlugin("jdbc:mysql://127.0.0.1:3306/pifii?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
//				"root", "ifidc1120");
//		
//		System.err.println(insertWeekDay("2015-03-18","2015-03-20"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar start = Calendar.getInstance();
		start.set(0, 0, 0, 25, 20, 0);
		System.err.println(sdf.format(start.getTime()));
	}
	
	/**
	 * 获取满足条件的已投放的策略
	 * @param advSpace
	 * @param startDate
	 * @param endDate
	 * @param params 这个参数暂时没有使用，原因：对于获取未投放的策略值，只需要按照params过滤总的策略值减去已投放的策略值，已投放的策略值中即使包含需要params过滤的数据也不会影响按条件过滤后的未投放的策略值
	 * @return
	 */
	private List<String> putinPlots(final Object advSpace,final String startDate,final String endDate,Map<String,String> params){
		final List<String> plotList = new ArrayList<String>();
		Db.tx(new IAtom(){public boolean run() throws SQLException {//方法放到一个事物中，保证临时表可用
			String tableName = "bp_adv_putin_temp_"+RandomStringUtils.random(10, DataOrgUtil.RANDOM_CHARS);
			Db.update("CREATE TEMPORARY TABLE "+tableName+"(`week_id` int(11) NOT NULL,`in_date` date NOT NULL,KEY `bp_adv_putin_temp_in_date_index` (`in_date`))");
			String insertSql = insertWeekDay(startDate,endDate);
			if(insertSql.length() > 0){
				Db.update("insert into "+tableName+" "+insertSql);
			}
			//sql解释：临时表用于过滤掉不会引起时间冲突的周，如旧的广告是7号（星期二）结束的，新的广告是6号开始的，
			//这个时候针对旧的广告投放只需要过滤出星期一、星期二的投放时间，因为只有这两天的数据才会影响新的广告投放策略，
			//这种情况出现的原因是因为新的投放策略和旧的投放策略的时间重叠小于一周，所以要去掉无关的周，这种情况目前包含四种：
			//1.新发布的广告和旧的广告起止时间重叠部分在新广告的开始，且重叠天数小于一周（这种情况的广告需要把重叠部分的周过滤出来，其他的周不要）
			//2.新发布的广告和旧的广告起止时间重叠部分在新广告的结束，且重叠天数小于一周（这种情况的广告需要把重叠部分的周过滤出来，其他的周不要）
			//3.新发布的广告的起止时间包含旧的广告的发布起止时间（这种情况的广告不需要做周的过滤）
			//4.旧的广告的起止时间包含新的广告的发布起止时间（这种情况的广告不需要做周的过滤，但是加临时表过滤时会出现问题，所以需要加上条件(bap.start_date>? and bap.end_date <?)）
			//没有交集的广告已过滤，不用考虑
			
			//针对上面的情况，处理方式：对1和2的处理方式是通过临时表过滤数据，临时表中的数据包含新广告的起始日期后的7天（处理第一种情况的旧广告），截至日期前的7天（处理第二种情况的旧广告）
			StringBuffer sql = new StringBuffer();
			sql.append("select CONCAT(bapd.week_id,'_',bapd.time_id,'_',bapi.industry_id,'_',bapo.org_id) id ");
			sql.append("from system_user su join sys_org so on (su.id=? and so.pid=su.org_id) ");
			sql.append("join bp_adv_putin bap on (bap.enable and bap.adv_space=? and ");
			sql.append("(bap.start_date BETWEEN ? and ? or bap.end_date BETWEEN ? and ?) and  bap.org_id=so.pid) ");
			sql.append("join bp_adv_putin_industry bapi on (bapi.adv_putin_id=bap.id) ");
			sql.append("join bp_adv_putin_org bapo on (bapo.enable and bapo.adv_putin_id=bap.id) ");
			sql.append("join bp_adv_putin_daytime bapd on (bapd.adv_putin_id=bap.id) ");
			sql.append("join "+tableName+" bapt on ((bap.start_date>? and bap.end_date <?) or (bap.start_date<? and bap.end_date >?) ");
			sql.append("or (bapd.week_id=bapt.week_id and bapt.in_date BETWEEN bap.start_date and bap.end_date)) ");
			List<Record> plots = Db.find(sql.toString(), 
					new Object[]{ContextUtil.getCurrentUserId(),advSpace,startDate,endDate,startDate,endDate,startDate,endDate,startDate,endDate});
			Iterator<Record> ite = plots.iterator();
			while(ite.hasNext()){
				Record plot = ite.next();
				plotList.add(plot.getStr("id"));
			}
			return true;
		}});
		return plotList;
	}
	
	private String insertWeekDay(String startDate,String endDate){
		List<Record> weeks = Db.find("select id,`key` from bp_dictionary where type='adv_day' ");
		Map<String,Object> weeksMap = new HashMap<String, Object>();
		Iterator<Record> ite = weeks.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			weeksMap.put(rec.getStr("key"), rec.get("id"));
		}
		Map<String,String> insertWeekd = new HashMap<String,String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startC = Calendar.getInstance();
		try {
			startC.setTime(format.parse(startDate));
			for(int i=0;i<7;i++){
				if(startC.getTime().after(format.parse(endDate))){
					break;
				}
				insertWeekd.put(format.format(startC.getTime()),startC.get(Calendar.DAY_OF_WEEK)-1+"");
				startC.add(Calendar.DATE,1);
			}
			
			Calendar startE = Calendar.getInstance();
			startE.setTime(format.parse(endDate));
			for(int i=0;i<7;i++){
				if(startE.getTime().before(format.parse(startDate))){
					break;
				}
				insertWeekd.put(format.format(startE.getTime()),startE.get(Calendar.DAY_OF_WEEK)-1+"");
				startE.add(Calendar.DATE,-1);
			}
			StringBuffer sqlInsert = new StringBuffer();
			Iterator<String> weekIte = insertWeekd.keySet().iterator();
			while(weekIte.hasNext()){
				String inDate = weekIte.next();
				String weekId = weeksMap.get(insertWeekd.get(inDate)).toString();
				sqlInsert.append(",('"+weekId+"','"+inDate+"')");
			}
			if(sqlInsert.length() > 0){
				return "values"+sqlInsert.substring(1);
			}else{
				return "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public List<Record> getPlot(String plotType,Object advSpace,String startDate,String endDate,Map<String,String> params){
		List<Record> plots = getAvailablePlot(advSpace,startDate,endDate,params);
		if("org".equals(plotType)){
			return getPlots(plots,"org_id","org_name","permission");
		}else if("industry".equals(plotType)){
			return getPlots(plots,"industry_id","industry_name","");
		}else if("weeksAndTimes".equals(plotType)){
			return getPlots(plots,"weeks_times","","");
		}else{
			return new ArrayList<Record>();
		}
	}
	
	private List<Record> getPlots(List<Record> plots,String keyField,String valueField,String otherField){
		List<String> keys = new ArrayList<String>();
		List<Record> thisPlots = new ArrayList<Record>();
		Iterator<Record> ite = plots.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			String thisKey = rec.get(keyField).toString();
			if(!keys.contains(thisKey)){
				keys.add(thisKey);
				Record thisRec = new Record().set("key", thisKey);
				if(StringUtils.isNotBlank(valueField)){
					thisRec.set("value", rec.get(valueField));
				}
				if(StringUtils.isNotBlank(otherField)){
					thisRec.set(otherField, rec.get(otherField));
				}
				thisPlots.add(thisRec);
			}
		}
		return thisPlots;
	}

	public boolean checkPutinConflict(List<String> plots,Object advSpace,String startDate,String endDate){
		List<Record> availablePlot = getAvailablePlot(advSpace,startDate,endDate,new HashMap<String,String>());
		Iterator<Record> ite = availablePlot.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			int index = plots.indexOf(rec.get("id"));
			if(-1 != index){
				plots.remove(index);
			}
			if(plots.size() == 0){
				return true;
			}
		}
		return plots.size() == 0;
	}
	
	/**
	 * 保存按组织投放的信息，如果该广告位分配给了下级组织，则发送投放广告的消息，如果没有分配则直接投放广告到该组织下
	 * @param advPutinId
	 * @param advSpace
	 * @param orgId
	 * @param orgs
	 */
	public void putinOrg(Object advPutinId,Object advSpace,Object orgId,String[] orgs){
//		String[] orgs = getParaValues("orgs");
 		if(null != orgs && orgs.length > 0){
// 			List<String> myOrgs = new ArrayList<String>();
 			List<String> otherOrgs = new ArrayList<String>();
 			StringBuffer sql = new StringBuffer();
 			sql.append("select distinct so.id,ifnull(bao.edit_able,0) edit_able ");
 			sql.append("from sys_org so left join bp_adv_org bao on (bao.adv_spaces=? and bao.edit_able and so.id=bao.org_id) ");
 			sql.append("where so.pid=? ");
 			List<Record> childrenOrgs = Db.find(sql.toString(), new Object[]{advSpace,orgId});
 			Iterator<Record> ite = childrenOrgs.iterator();
 			while(ite.hasNext()){
 				Record rec = ite.next();
 				if("1".equals(rec.get("edit_able").toString())){//该广告已分配出去
 					otherOrgs.add(rec.get("id").toString());
 				}
 			}
 			if(otherOrgs.size() > 0){
 				Object[][] params = new Object[otherOrgs.size()][2];
				for(int i=0;i<otherOrgs.size();i++){
					params[i] = new Object[]{otherOrgs.get(i),advPutinId};
				}
				DbExt.batch("insert into bp_adv_putin_msg(org_id,adv_putin_id,status,create_date) values(?,?,0,now())", params);
 			}
			Db.update("delete from bp_adv_putin_org where adv_putin_id=? ", new Object[]{advPutinId});
			Object[][] orgParams = new Object[orgs.length][2];
			for(int i=0;i<orgs.length;i++){
				orgParams[i] = new Object[]{advPutinId,orgs[i]};
			}
			DbExt.batch("insert into bp_adv_putin_org(adv_putin_id,org_id,enable,create_date) values(?,?,1,now())", orgParams);
 		}
	}
	
}
