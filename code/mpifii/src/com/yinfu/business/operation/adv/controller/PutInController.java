
package com.yinfu.business.operation.adv.controller;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.operation.adv.model.AdvPutinModel;
import com.yinfu.business.operation.adv.model.PutIn;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.routersyn.task.GotoTask;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/oper/adv/putin", viewPath = "/page/business/operation/adv/putin")
public class PutInController extends Controller<Device> {
	private static Logger logger = Logger.getLogger(PutInController.class);
	
	private static final String PREVIEW_PATH = "upload/image/adv/";
	
	public void index() {
		SplitPage splitPages = PutIn.dao.findList(splitPage);
		setAttr("splitPage", splitPages);
		render("index.jsp");
	}

	public void guide(){
		setAttr("weeks", getDic("adv_day"));
		setAttr("times", getDic("adv_time"));
		render("guide/guide.jsp");
	}
	
	private List<Record> getDic(String type){
		return Db.find("select id,`value` from bp_dictionary where type='"+type+"' order by create_date ");
	}
	
 	public void advSpacesList(){
		StringBuffer sql = new StringBuffer();
		sql.append("from system_user su join bp_adv_org bao on (su.id=? and su.org_id=bao.org_id) ");
		sql.append("join bp_adv_spaces bas on (bas.id=bao.adv_spaces) ");
		sql.append("join bp_adv_type bat on (bas.id=bat.adv_spaces) ");
		sql.append("group by bas.id ");
		Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				"select bas.id,bas.name,bas.adv_type,bas.preview_img,GROUP_CONCAT(distinct(CONCAT(bat.img_width,'*',bat.img_height,'*',bat.default_img)) order by bat.img_width/bat.img_height desc) imgs ", sql.toString(),new Object[]{ContextUtil.getCurrentUserId()});
		renderJson(returnData);
	}
	
 	public void getPlot(){
 		Map<String,String> params = new HashMap<String,String>();
 		String[] industrys = getParaValues("industrys");
 		if(null != industrys){
 			params.put("industry", arrayToSqlIn(industrys));
 		}
 		String[] orgs = getParaValues("orgs");
 		if(null != orgs){
 			params.put("org", arrayToSqlIn(orgs));
 		}
 		String[] weeksAndTimes = getParaValues("weeks_times");
 		if(null != weeksAndTimes){
 			params.put("weeksAndTimes", arrayToSqlIn(weeksAndTimes));
 		}
 		renderJson(new PutinUtil().getPlot(getPara("plotType"),getPara("spaceId"),getPara("startDate"),getPara("endDate"),params));
 	}
 	
 	private String arrayToSqlIn(String[] array){
 		StringBuffer sqlIn = new StringBuffer();
 		sqlIn.append("''");
 		for(int i=0;i<array.length;i++){
 			sqlIn.append(",'"+array[i]+"'");
 		}
 		return sqlIn.toString();
 	}
 	
	/**
	 * 投放广告向导，保存
	 * 
	 * 涉及到的表：bp_adv_content、bp_adv_type_res、bp_adv_putin、
	 * 			bp_adv_putin_area、bp_adv_putin_industry、bp_adv_putin_day、bp_adv_putin_time、
	 * 			
	 */
	public void save(){
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			Object advContentId = saveAdvContent();
			Record userOrg = Db.findFirst("select org_id from system_user where id=? ", new Object[]{ContextUtil.getCurrentUserId()});
			if(null != userOrg && null != userOrg.get("org_id")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					AdvPutinModel model = new AdvPutinModel().set("adv_space", getPara("adv_spaces_id")).set("org_id", userOrg.get("org_id"))
							.set("adv_content_id", advContentId).set("enable", "1").set("create_user", ContextUtil.getCurrentUserId())
							.set("start_date", new Timestamp(sdf.parse(getPara("start_time")).getTime())).set("end_date", new Timestamp(sdf.parse(getPara("end_time")).getTime()))
							.set("create_date", new Timestamp(System.currentTimeMillis()));
					model.save();
					saveIndustry(model.get("id"));
					saveWeekTime(model.get("id"));
					new PutinUtil().putinOrg(model.get("id"),getPara("adv_spaces_id"),userOrg.get("org_id"),getParaValues("orgs"));
//					savePlot(model.get("id"));
//					saveDayTime(model.get("id"));
//					saveOrgAdvPutinMessage(userOrg.get("org_id"),model.get("id"));
					return true;
				} catch (ParseException e) {
					e.printStackTrace();
					return false;
				}
				
			}else{
				return false;
			}
		}});
		renderJsonResult(isSuccess);
	}
	private Object saveAdvContent(){
		List<UploadFile> uploadFiles = getFiles(PathKit.getWebRootPath()+File.separator+PREVIEW_PATH.replaceAll("/", File.separator+File.separator));
		Iterator<UploadFile> ite = uploadFiles.iterator();
		Map<String,String> uploadImgSrc = new HashMap<String,String>();//保存上传的不同尺寸的图片资源
		while(ite.hasNext()){
			UploadFile file = ite.next();
			if(file!=null){
				String imgSize = file.getParameterName().split("_")[1];
				String name = String.valueOf(System.currentTimeMillis());
				File sourceFile = ImageKit.renameFile(file, name);
				Fs.copyFileToHome(sourceFile, "image", "adv");//将图片复制到商铺平台服务器上
				File src = file.getFile();
				String imgPath = PREVIEW_PATH + name + ImageKit.getFileExtension(src.getName());
				uploadImgSrc.put(imgSize, imgPath);
			}
		}
		String contentId = getPara("contentId");
		if(StringUtils.isNotBlank(contentId)){//编辑广告
			Record myAdv = Db.findFirst("select ifnull(name,'') name,ifnull(link,'') link,ifnull(des,'') des from bp_adv_content where id=? and create_user=?", 
					new Object[]{contentId,ContextUtil.getCurrentUserId()});
			if(null != myAdv && (!myAdv.getStr("name").equals(getPara("name")) || !myAdv.getStr("link").equals(getPara("link"))
					|| !myAdv.getStr("des").equals(getPara("des")) || !uploadImgSrc.isEmpty())){//如果选择了自己之前的广告，则保存
				Db.update("update bp_adv_content set name=?,link=?,des=?,update_date=now() where id=? ", 
						new Object[]{getPara("name"),getPara("link"),getPara("des"),contentId});
			}
		}else{//新增广告
			contentId = UUID.randomUUID().toString();
			Db.update("insert into bp_adv_content (id,name,link,des,create_user,create_date,update_date) values(?,?,?,?,?,now(),now())", 
					new Object[]{contentId,getPara("name"),getPara("link"),getPara("des"),ContextUtil.getCurrentUserId()});
		}
		if(uploadImgSrc.size() > 0){
			saveAdvRes(uploadImgSrc,contentId);
		}
		return contentId;
	}
	
	private void saveAdvRes(Map<String,String> uploadImgSrc,Object advContentId){
		StringBuffer imgSizeSqlIn = new StringBuffer();
		imgSizeSqlIn.append("''");
		Iterator<String> ite = uploadImgSrc.keySet().iterator();
		while(ite.hasNext()){
			imgSizeSqlIn.append(",'"+ite.next()+"'");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select id,CONCAT(img_width,'*',img_height) img_size ");
		sql.append("from bp_adv_type where adv_spaces=? and CONCAT(img_width,'*',img_height) in ("+imgSizeSqlIn.toString()+") ");
		List<Record> advTypes = Db.find(sql.toString(), new Object[]{getPara("adv_spaces_id")});
		List<Object[]> params = new ArrayList<Object[]>();
		Iterator<Record> iteTypes = advTypes.iterator();
		StringBuffer advTypeSqlIn = new StringBuffer();
		advTypeSqlIn.append("''");
		while(iteTypes.hasNext()){
			Record rec = iteTypes.next();
			advTypeSqlIn.append(",'"+rec.get("id").toString()+"'");
			params.add(new Object[]{rec.get("id"),advContentId,uploadImgSrc.get(rec.get("img_size"))});
		}
		Db.update("delete from bp_adv_type_res where content_id=? and adv_type_id in ("+advTypeSqlIn.toString()+") ", new Object[]{advContentId});
		DbExt.batch("insert into bp_adv_type_res(adv_type_id,content_id,res_url,create_date) values(?,?,?,now())", DataOrgUtil.listTo2Array(params));
	}
	
	private void saveIndustry(Object advPutinId){
		String[] industrys = getParaValues("industrys");
 		if(null != industrys && industrys.length>0){
 			Db.update("delete from bp_adv_putin_industry where adv_putin_id=? ", new Object[]{advPutinId});
 			Object[][] params = new Object[industrys.length][2];
 			for(int i=0;i<industrys.length;i++){
 				params[i] = new Object[]{advPutinId,industrys[i]};
 			}
 			DbExt.batch("insert into bp_adv_putin_industry(adv_putin_id,industry_id,create_date) values(?,?,now())", params);
 		}
	}
	
	private void saveWeekTime(Object advPutinId){
		String[] weeksAndTimes = getParaValues("weeks_times");
 		if(null != weeksAndTimes && weeksAndTimes.length>0){
 			Db.update("delete from bp_adv_putin_daytime where adv_putin_id=? ", new Object[]{advPutinId});
 			Map<String,Object> weeks = getWeeks();
 			Map<String,Record> times = getTimes();
 			Object[][] params = new Object[weeksAndTimes.length][5];
 			for(int i=0;i<weeksAndTimes.length;i++){
 				String[] weekAndTime = weeksAndTimes[i].split("_");
 				if(weekAndTime.length == 2){
 					params[i] = new Object[]{advPutinId,weekAndTime[0],weekAndTime[1],weeks.get(weekAndTime[0]),times.get(weekAndTime[1]).get("startDate"),times.get(weekAndTime[1]).get("endDate")};
 				}
 			}
 			DbExt.batch("insert into bp_adv_putin_daytime(adv_putin_id,week_id,time_id,week_value,time_start,time_end,create_date) values(?,?,?,?,?,?,now())", params);
 		}
	}
	
	private Map<String,Object> getWeeks(){
		Map<String,Object> weeks = new HashMap<String,Object>();
		List<Record> weekList = Db.find("select id,`key` from bp_dictionary where type='adv_day' ");
		Iterator<Record> ite = weekList.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			weeks.put(rec.get("id").toString(), rec.get("key"));
		}
		return weeks;
	}
	private Map<String,Record> getTimes(){
		Map<String,Record> times = new HashMap<String,Record>();
		List<Record> timeList = Db.find("select id,`key` from bp_dictionary where type='adv_time' ");
		Iterator<Record> ite = timeList.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			String key = rec.get("key").toString();
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String[] timeInfo = key.split("-");
//				Calendar start = Calendar.getInstance();
//				start.set(0, 0, 0, Integer.parseInt(timeInfo[0]), 0, 0);
//				Calendar end = Calendar.getInstance();
//				end.set(0, 0, 0, Integer.parseInt(timeInfo[1]), 0, 0);
				Date endDate = sdf.parse(timeInfo[1].trim()+":00");
				Date endDateBefpre = new Date(endDate.getTime()-1000);
				times.put(rec.get("id").toString(), 
						new Record().set("startDate", timeInfo[0].trim()+":00").set("endDate", sdf.format(endDateBefpre)));
				
			}catch(Exception e){
				e.printStackTrace();
				logger.error("转换bp_dictionary表中type='adv_time'的key值时出错（正确格式为：小时:分钟-小时:分钟）！", e);
			}
		}
		return times;
	}
	
	private void savePlot(Object advPutinId){
		String plot = getPara("plotRadio");
		if("area".equals(plot)){//投放策略为区域
			String province = getPara("province");
			String city = getPara("city");
			String area = getPara("area");
			List<Object[]> params = new ArrayList<Object[]>();
			if(StringUtils.isNotBlank(province)){
				params.add(new Object[]{advPutinId,province,1});
			}
			if(StringUtils.isNotBlank(city)){
				params.add(new Object[]{advPutinId,city,2});
			}
			if(StringUtils.isNotBlank(area)){
				params.add(new Object[]{advPutinId,area,3});
			}
			DbExt.batch("insert into bp_adv_putin_plot(adv_putin_id,value_id,sequence,create_date) values(?,?,?,now())", DataOrgUtil.listTo2Array(params));
		}else if("industry".equals(plot)){//投放策略为行业
			String[] industrys = getParaValues("industryCheckBox");
			Object[][] params = new Object[industrys.length][3];
			for(int i=0;i<industrys.length;i++){
				params[i] = new Object[]{advPutinId,industrys[i],i};
			}
			DbExt.batch("insert into bp_adv_putin_plot(adv_putin_id,value_id,sequence,create_date) values(?,?,?,now())", params);
		}
	}
	
	private void saveDayTime(Object advPutinId){
		String[] days = getParaValues("advDays");
		Object[][] dayParams = new Object[days.length][2];
		for(int i=0;i<days.length;i++){
			dayParams[i] = new Object[]{advPutinId,days[i]};
		}
		Db.update("delete from bp_adv_putin_day where adv_putin_id=? ", new Object[]{advPutinId});
		DbExt.batch("insert into bp_adv_putin_day(adv_putin_id,day_value,create_date) values(?,?,now())", dayParams);
		
		String[] times = getParaValues("advTimes");
		Object[][] timeParams = new Object[times.length][2];
		for(int i=0;i<times.length;i++){
			timeParams[i] = new Object[]{advPutinId,times[i]};
		}
		Db.update("delete from bp_adv_putin_time where adv_putin_id=? ", new Object[]{advPutinId});
		DbExt.batch("insert into bp_adv_putin_time(adv_putin_id,time_value,create_date) values(?,?,now())", timeParams);
	}
	
	/**
	 * 如果是统一投放则需要添加已分配该广告位的下级组织的提醒消息
	 * @param orgId
	 * @param advPutinId
	 */
	private void saveOrgAdvPutinMessage_old(Object orgId,Object advPutinId){
		if("1".equals(getPara("putinType"))){//统一投放
			List<Record> orgs = Db.find("select distinct so.id from sys_org so join bp_adv_org bao on (so.pid=? and bao.adv_spaces=? and bao.edit_able and so.id=bao.org_id) ", 
					new Object[]{orgId,getPara("adv_spaces_id")});
			if(orgs.size() > 0){
				Object[][] params = new Object[orgs.size()][2];
				for(int i=0;i<orgs.size();i++){
					Record rec = orgs.get(i);
					params[i] = new Object[]{rec.get("id"),advPutinId};
				}
				DbExt.batch("insert into bp_adv_putin_msg(org_id,adv_putin_id,status,create_date) values(?,?,0,now())", params);
			}
		}
	}
	
	public void changeAdvPutinStatus(){
		Record result = new Record().set("success", "0");
		Record userOrg = Db.findFirst("select org_id from system_user where id=? ", new Object[]{ContextUtil.getCurrentUserId()});
		if(null != userOrg){
			Record advPutin = Db.findFirst("select adv_space,date_format(start_date,'%Y-%m-%d') start_date,date_format(end_date,'%Y-%m-%d') end_date from bp_adv_putin where id=? ",new Object[]{getPara("advPutinId")});
			if(null != advPutin){
				if("1".equals(getPara("status"))){
					StringBuffer sql = new StringBuffer();
					sql.append("select distinct CONCAT(bapd.week_id,'_',bapd.time_id,'_',bapi.industry_id,'_',bapo.org_id) id ");
					sql.append("from bp_adv_putin_org bapo ");
					sql.append("join bp_adv_putin_industry bapi on (bapo.adv_putin_id = ? and bapi.adv_putin_id=?) ");
					sql.append("join bp_adv_putin_daytime bapd on (bapd.adv_putin_id = ?) ");
					List<Record> plots = Db.find(sql.toString(), new Object[]{getPara("advPutinId"),getPara("advPutinId"),getPara("advPutinId")});
					List<String> plotsId = new ArrayList<String>();
					Iterator<Record> ite = plots.iterator();
					while(ite.hasNext()){
						Record rec = ite.next();
						plotsId.add(rec.getStr("id"));
					}
					boolean notConflict = new PutinUtil().checkPutinConflict(plotsId, advPutin.get("adv_space"), advPutin.getStr("start_date"), advPutin.getStr("end_date"));
					if(notConflict){
						Db.update("update bp_adv_putin set enable=? where org_id=? and id=? ", 
								new Object[]{getPara("status"),userOrg.get("org_id"),getPara("advPutinId")});
						result.set("success", "1");
					}else{
						result.set("msg", "该广告的投放策略出现冲突，不能投放！");
					}
				}else{
					Db.update("update bp_adv_putin set enable=?,shelf_date=now() where org_id=? and id=? ", 
							new Object[]{getPara("status"),userOrg.get("org_id"),getPara("advPutinId")});
					result.set("success", "1");
				}
			}else{
				result.set("msg", "该广告不存在！");
			}
		}else{
			result.set("msg", "该用户不存在！");
		}
		renderJson(result);
	}
	
	public void editPutinAdv(){
		StringBuffer sql = new StringBuffer();
		sql.append("select bas.adv_type,bas.id adv_spaces_id,bac.id content_id,bac.name,bac.link,bac.des,");
		sql.append("GROUP_CONCAT(distinct CONCAT(bat.img_width,'*',bat.img_height,'*',bat.default_img,'*',ifnull(batr.res_url,'')) order by bat.img_width/bat.img_height desc) imgs ");
		sql.append("from bp_adv_putin bap join bp_adv_spaces bas on (bap.id=? and bas.id=bap.adv_space) ");
		sql.append("join bp_adv_type bat on (bat.adv_spaces=bap.adv_space) ");
		sql.append("join bp_adv_content bac on (bac.id=bap.adv_content_id) ");
		sql.append("left join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bac.id) ");
		sql.append("group by bap.id ");
		Record putinAdv = Db.findFirst(sql.toString(), new Object[]{getPara("advPutinId")});
		if(null != putinAdv){
			setAttr("advType", putinAdv.get("adv_type"));
			setAttr("advSpacesId", putinAdv.get("adv_spaces_id"));
			setAttr("contentId", putinAdv.get("content_id"));
			setAttr("name", putinAdv.get("name"));
			setAttr("link", putinAdv.get("link"));
			setAttr("des", putinAdv.get("des"));
			setAttr("imgs", putinAdv.get("imgs"));
			
		}
		render("editAdv.jsp");
	}
	public void savePutinAdv(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			Object contentId = saveAdvContent();
			//下发盒子的广告更新任务
			List<String> sqls = new ArrayList<String>();
			StringBuffer sql = new StringBuffer();
			sql.append("select bash.shop_id,GROUP_CONCAT(basp.adv_type) adv_type ");
			sql.append("from bp_adv_shop bash join bp_adv_spaces basp on (bash.adv_spaces=basp.id) ");
			sql.append("where content_id=? ");
			sql.append("group by shop_id ");
			List<Record> shops = Db.find(sql.toString(),new Object[]{contentId});
			Iterator<Record> ite = shops.iterator();
			while(ite.hasNext()){
				Record rec = ite.next();
				List<String> advTypes = Arrays.asList(rec.get("adv_type").toString().split(","));
				if(advTypes.contains("adv")){
					Record taskInfo = new Record().set("task_desc", "管理平台编辑了轮播图广告").set("is_show", "0");
					Map<String,List<File>> res = IndexTask.synRes(rec.get("shop_id"), sqls,taskInfo,null,null);
					if(null != res){
						SynUtils.putAllFiles(deleteRes, res);
					}else{
						return false;
					}
				}
				if(advTypes.contains("adv_start")){
					Record taskInfo = new Record().set("task_desc", "管理平台编辑了渐变页广告").set("is_show", "0");
					Map<String,List<File>> res = GotoTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,null);
					if(null != res){
						SynUtils.putAllFiles(deleteRes, res);
					}else{
						return false;
					}
				}
			}
			if(sqls.size() > 0){
				DbExt.batch(sqls);
			}
			return true;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	
//	public void editAdvPutinDayTime(){
//		setAttr("days", getDays(getPara("advPutinId")));
//		setAttr("times", getTimes(getPara("advPutinId")));
//		setAttr("advPutinId", getPara("advPutinId"));
//		render("editDayTime.jsp");
//	}
//	public void saveAdvPutinDayTime(){
//		saveDayTime(getPara("advPutinId"));
//		renderJsonResult(true);
//	}
	
}
