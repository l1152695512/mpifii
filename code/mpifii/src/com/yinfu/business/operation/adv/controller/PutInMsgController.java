
package com.yinfu.business.operation.adv.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.operation.adv.model.AdvPutinModel;
import com.yinfu.business.operation.adv.model.PutInMsg;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/oper/adv/putinMsg", viewPath = "/page/business/operation/adv/putinMsg")
public class PutInMsgController extends Controller<Device> {
	
	public void index() {
		SplitPage splitPages = PutInMsg.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("index.jsp");
	}
	
	public void checkMsg(){
		List<Record> userMsgs = new ArrayList<Record>();
		List<Record> msgs = Db.find("select bapm.id from system_user su join bp_adv_putin_msg bapm on (su.id=? and bapm.`status`=0 and su.org_id=bapm.org_id) ",
				new Object[]{ContextUtil.getCurrentUserId()});
		if(msgs.size() > 0){
			userMsgs.add(new Record().set("url", "/business/oper/adv/putinMsg").set("tip", "你有广告投放消息，请点击查看"));
		}
		renderJson(userMsgs);
	}
	
	public void checkPutinError(){
		renderJson(checkPutin(getPara("id"),null));
	}
	
	/**
	 * 检查本次投放是否能投放成功
	 * 1.判断父组织投放的广告是否已下架
	 * 2.判断父组织投放的广告是否已过期
	 * 3.判断父组织投放的广告在子组织下投放时有没有可投放的子组织
	 * 
	 * 参数orgs是检测可投放的组织中是否包含给定的全部组织，只有全部包含才是合法的
	 * @return
	 */
	private Record checkPutin(Object advPutinMsgId,String[] orgs){
		Record errorMsg = new Record().set("refresh", "0");
		StringBuffer sql = new StringBuffer();
		sql.append("select bap.id,bap.adv_space,bap.adv_content_id,bap.`enable`,date_format(bap.start_date,'%Y-%m-%d') start_date,date_format(bap.end_date,'%Y-%m-%d') end_date ");
		sql.append("from bp_adv_putin bap join bp_adv_putin_msg bapm on (bapm.id=? and bapm.adv_putin_id=bap.id) ");
		Record advPutin = Db.findFirst(sql.toString(), new Object[]{advPutinMsgId});
		if(null == advPutin){
			return errorMsg.set("msg", "投放策略不存在！");
		}
		if("0".equals(advPutin.get("enable").toString())){//该投放策略已禁用
			Db.update("update bp_adv_putin_msg set `status`=3 where id=? ", new Object[]{advPutinMsgId});
			return errorMsg.set("refresh", "1").set("msg", "该广告已下架！");
		}
		try {
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(advPutin.getStr("end_date"));
			if(endDate.before(new Date())){
				Db.update("update bp_adv_putin_msg set `status`=4 where id=? ", new Object[]{advPutinMsgId});
				return errorMsg.set("refresh", "1").set("msg", "该广告的投放策略已过期！");
			}
			Record user = Db.findFirst("select org_id from system_user where id=? ", new Object[]{ContextUtil.getCurrentUserId()});
			if(null == user){
				return errorMsg.set("msg", "非法操作（该用户不存在）！");
			}
			List<Record> availableOrgs = getAvailableOrgs(advPutin.get("id"),advPutin.get("adv_space"),
					advPutin.getStr("start_date"),advPutin.getStr("end_date"));
			if(availableOrgs.size() == 0){
				return errorMsg.set("msg", "组织下没有可投放的区域（策略冲突）！");
			}
			if(null != orgs){
				List<String> availableOrgsStr = new ArrayList<String>();
				Iterator<Record> ite = availableOrgs.iterator();
				while(ite.hasNext()){
					availableOrgsStr.add(ite.next().get("key").toString());
				}
				for(int i=0;i<orgs.length;i++){
					if(!availableOrgsStr.contains(orgs[i])){
						return errorMsg.set("msg", "组织投放冲突，请重新点击投放！");
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			errorMsg.set("msg", "操作异常！");
		}
		return errorMsg;
	}
	
	private List<Record> getAvailableOrgs(Object advPutinId,Object advSpace,String startDate,String endDate){
		Map<String,String> params = new HashMap<String,String>();
		StringBuffer sqlIndustry = new StringBuffer();
		sqlIndustry.append("select CONCAT('\\'',GROUP_CONCAT(distinct industry_id SEPARATOR '\\',\\''),'\\'') industrys ");
		sqlIndustry.append("from bp_adv_putin_industry where adv_putin_id=? ");
		sqlIndustry.append("GROUP BY adv_putin_id ");
		Record industrys = Db.findFirst(sqlIndustry.toString(), new Object[]{advPutinId});
		params.put("industry", industrys.getStr("industrys"));
		StringBuffer sqlWeekTime = new StringBuffer();
		sqlWeekTime.append("select CONCAT('\\'',GROUP_CONCAT(distinct CONCAT(week_id,'_',time_id) SEPARATOR '\\',\\''),'\\'') weeksAndTimes ");
		sqlWeekTime.append("from bp_adv_putin_daytime where adv_putin_id=? ");
		sqlWeekTime.append("GROUP BY adv_putin_id ");
		Record weekTimes = Db.findFirst(sqlWeekTime.toString(), new Object[]{advPutinId});
		params.put("weeksAndTimes", weekTimes.getStr("weeksAndTimes"));
		return new PutinUtil().getPlot("org",advSpace,startDate,endDate,params);
	}
	
	public void choiceOrgs(){
		setAttr("advPutinMsgId", getPara("id"));
		render("putin.jsp");
	}
	
	public void getAvailableOrgsAction(){
		StringBuffer sql = new StringBuffer();
		sql.append("select bap.id,bap.adv_space,date_format(bap.start_date,'%Y-%m-%d') start_date,date_format(bap.end_date,'%Y-%m-%d') end_date ");
		sql.append("from bp_adv_putin bap join bp_adv_putin_msg bapm on (bapm.id=? and bapm.adv_putin_id=bap.id) ");
		Record advPutin = Db.findFirst(sql.toString(), new Object[]{getPara("id")});
		List<Record> availableOrgs = getAvailableOrgs(advPutin.get("id"),advPutin.get("adv_space"),
				advPutin.getStr("start_date"),advPutin.getStr("end_date"));
		renderJson(availableOrgs);
	}
	
	public void putin(){
		Record errorMsg = checkPutin(getPara("id"),getParaValues("orgs"));
		if(null == errorMsg.get("msg") || StringUtils.isBlank(errorMsg.get("msg").toString())){
			boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select bap.id,bap.adv_space,bap.adv_content_id,date_format(bap.start_date,'%Y-%m-%d') start_date,date_format(bap.end_date,'%Y-%m-%d') end_date ");
				sql.append("from bp_adv_putin bap join bp_adv_putin_msg bapm on (bapm.id=? and bapm.adv_putin_id=bap.id) ");
				Record advPutin = Db.findFirst(sql.toString(), new Object[]{getPara("id")});
				Record user = Db.findFirst("select org_id from system_user where id=? ", new Object[]{ContextUtil.getCurrentUserId()});
				AdvPutinModel model = new AdvPutinModel().set("adv_space", advPutin.get("adv_space")).set("org_id", user.get("org_id"))
						.set("adv_content_id", advPutin.get("adv_content_id")).set("enable", "1").set("create_user", ContextUtil.getCurrentUserId())
						.set("start_date", advPutin.get("start_date")).set("end_date", advPutin.get("end_date"))
						.set("create_date", new Timestamp(System.currentTimeMillis()));
				model.save();
				copyIndustry(advPutin.get("id"),model.get("id"));
				copyWeekTime(advPutin.get("id"),model.get("id"));
				new PutinUtil().putinOrg(model.get("id"),advPutin.get("adv_space"),user.get("org_id"),getParaValues("orgs"));
				Db.update("update bp_adv_putin_msg set status=1 where id=? ", new Object[]{getPara("id")});
				return true;
			}});
			if(!isSuccess){
				errorMsg.set("msg", "操作失败稍后请重试！");
			}
		}
		renderJson(errorMsg);
	}
	
	private void copyIndustry(Object sourceAdvPutinId,Object targetAdvPutinId){
		List<Record> industrys = Db.find("select distinct industry_id from bp_adv_putin_industry where adv_putin_id=? ", new Object[]{sourceAdvPutinId});
		Object[][] params = new Object[industrys.size()][2];
		for(int i=0;i<industrys.size();i++){
			params[i] = new Object[]{targetAdvPutinId,industrys.get(i).get("industry_id")};
		}
		DbExt.batch("insert into bp_adv_putin_industry(adv_putin_id,industry_id,create_date) values(?,?,now())", params);
	}
	
	private void copyWeekTime(Object sourceAdvPutinId,Object targetAdvPutinId){
		List<Record> weekTimes = Db.find("select distinct week_id,week_value,time_id,time_start,time_end from bp_adv_putin_daytime where adv_putin_id=? ", new Object[]{sourceAdvPutinId});
		Object[][] params = new Object[weekTimes.size()][3];
		for(int i=0;i<weekTimes.size();i++){
			params[i] = new Object[]{targetAdvPutinId,weekTimes.get(i).get("week_id"),weekTimes.get(i).get("week_value"),
					weekTimes.get(i).get("time_id"),weekTimes.get(i).get("time_start"),weekTimes.get(i).get("time_end")};
		}
		DbExt.batch("insert into bp_adv_putin_daytime(adv_putin_id,week_id,week_value,time_id,time_start,time_end,create_date) values(?,?,?,?,?,?,now())", params);
	}
	
	public void ignore(){
		StringBuffer sql = new StringBuffer();
		sql.append("select bapm.id ");
		sql.append("from system_user su join bp_adv_putin_msg bapm on (su.id=? and bapm.id=? and su.org_id=bapm.org_id) ");
		Record rec = Db.findFirst(sql.toString(), new Object[]{ContextUtil.getCurrentUserId(),getPara("id")});
		if(null != rec){
			Db.update("update bp_adv_putin_msg set status=2 where id=? ", new Object[]{getPara("id")});
			renderJsonResult(true);
		}else{
			renderJsonResult(false);
		}
	}
}
