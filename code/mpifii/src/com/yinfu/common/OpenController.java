package com.yinfu.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.system.model.User;

/***
 * 
 * 
 * 
 * @author 
 * 
 */
@ControllerBind(controllerKey = "/open")
public class OpenController extends Controller {
	//@formatter:off 
	/**
	 * Title: content
	 * Description:地图展示
	 * Created On: 2014年10月9日 上午10:15:26
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void mapShows(){
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔，要考虑网络延迟
		StringBuffer sql = new StringBuffer("select a.id,a.lng,a.lat,a.name,a.addr,");
		sql.append("sum(case when date_add(b.report_date, interval ? second) > now() then 1 else 0 end) online ");
		sql.append("from bp_shop a left join bp_device b on a.id=b.shop_id ");
		sql.append("where a.delete_date is null and a.lng is not null and a.lat is not null ");
		sql.append("group by a.id");
		
		List<Record> shopList = Db.find(sql.toString(), new Object[] { interval });
		setAttr("shopList", shopList);
		String city = PropertyUtils.getProperty("platfrom.city", "广州");
		setAttr("city", city);
		render("/page/open/mapShows.jsp");
	}
	
	
	public void getOnLineTotal(){
		String shopId = getPara("shopId");// 获得商铺Id
		int sum = 0;
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔，要考虑网络延迟
		String sql = "select ifnull(sum(online_num),0) online_num from bp_device where shop_id = ? and date_add(report_date, interval ? second) > now()";
		Record rd = Db.findFirst(sql, new Object[] { shopId, interval });
		sum = Integer.parseInt(rd.get("online_num").toString());
		
		JSONObject data = new JSONObject();
		data.put("onlineNum", sum);
		renderJson(data);
	}
	
	
	public void getTotal(){
		final JSONObject data = new JSONObject();
		Db.tx(new IAtom(){public boolean run() throws SQLException {//方法放到一个事物中，保证临时表可用
			int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);
			int currentOrg = PropertyUtils.getPropertyToInt("open.orgid", 2);
			String admin = getPara("admin");// 是否超级管理员
//			String shopSqlIn = "''";
			String temporaryName = "";
			if(admin.equals("no")){
//				shopSqlIn = DataOrgUtil.recordListToSqlIn(DataOrgUtil.getShopsForUser(ContextUtil.getCurrentUserId()), "id");
				temporaryName = DataOrgUtil.getShopsForUserInTemporary(ContextUtil.getCurrentUserId());
			}else{
				Record rr = Db.findFirst("select id from system_user where org_id="+currentOrg);
				if(rr != null){
//					shopSqlIn = DataOrgUtil.recordListToSqlIn(DataOrgUtil.getShopsForUser(rr.getInt("id")), "id");
					temporaryName = DataOrgUtil.getShopsForUserInTemporary(ContextUtil.getCurrentUserId());
				}
			}
			if(1==1){
//				if("todayPerson".equals(getPara("type"))){
					StringBuffer todayPersonSql = new StringBuffer();
					todayPersonSql.append("select count(distinct r.client_mac) person_num ");
					todayPersonSql.append("from bp_device d ");
					if(StringUtils.isNotBlank(temporaryName)){
						todayPersonSql.append("join "+temporaryName+" temp on (d.shop_id=temp.id)");
					}
					todayPersonSql.append("join bp_session r on (r.sn=d.router_sn and date(r.stop_time)=date(now())) ");
//				todayPersonSql.append("and d.shop_id in ("+shopSqlIn+")) ");
					Record today = Db.findFirst(todayPersonSql.toString());
					data.put("tNum", today.get("person_num"));
//				}
//			}else{
				StringBuffer sql1 = new StringBuffer();
				sql1.append("select count(distinct s.id) total_shop,");
				sql1.append("count(distinct(if(d.id is null,null,s.id))) online_shop,");
				sql1.append("IFNULL(SUM(d.online_num),0) online_person ");
				sql1.append("from bp_shop s ");
				if(StringUtils.isNotBlank(temporaryName)){
					sql1.append("join "+temporaryName+" temp on (s.id=temp.id)");
				}
				sql1.append("left join bp_device d on (date_add(d.report_date, interval "+interval+" second) > now() and d.shop_id=s.id) ");
				sql1.append("where s.delete_date is null ");
//			sql1.append("and s.id in ("+shopSqlIn+")");
				
				Record info = Db.findFirst(sql1.toString());
				int totalShop = 0;
				int onlineShop = 0;
				int onlinePerson = 0;
				try{
					totalShop = Integer.parseInt(info.get("total_shop").toString());
					onlineShop = Integer.parseInt(info.get("online_shop").toString());
					onlinePerson = Integer.parseInt(info.get("online_person").toString());
				}catch(Exception e){
					e.printStackTrace();
				}
				data.put("shopNum", totalShop);
				data.put("onlineShopNum", onlineShop);//只要某个商铺中有一个盒子在线，该商铺就是点亮的（在线的）
				data.put("offlineShopNum", (totalShop-onlineShop)>0?(totalShop-onlineShop):0);
				data.put("onlineNum", onlinePerson);
				
				
				//		String yestardayPersonSql = sqlCommon.toString()+"date_add(now(), interval -1 day)))";
				//		Record yestarday = Db.findFirst(yestardayPersonSql);
				//		data.put("yNum", yestarday.get("person_num"));
				
				StringBuffer ynumSql = new StringBuffer("select ifnull(sum(counts),0) ynum ");
				ynumSql.append("from bp_shop_pf a ");
				if(StringUtils.isNotBlank(temporaryName)){
					ynumSql.append("join "+temporaryName+" temp on (a.shop_id=temp.id) ");
				}
				ynumSql.append("where date(a.date)= date_sub(curdate(),interval 1 day)");
				Record ynumRd = Db.findFirst(ynumSql.toString());
				data.put("yNum", ynumRd.get("ynum"));
				
				//当前组织下一级组织的在线人数统计
				if(admin.equals("no")){
					String html = "";//"<chart bgAlpha='0' canvasBgAlpha='0' showplotborder='0' baseFont='微软雅黑' baseFontSize='12' baseFontColor='2079D4' showBorder='0' showYAxisValues='0'  bgcolor='FFFFFF' showalternatevgridcolor='0' showplotborder='1'  divlinecolor='CCCCCC' tooltipbordercolor='FFFFFF' palettecolors='008ee4' canvasborderalpha='1' showborder='0'>";
					User user = ContextUtil.getCurrentUser();
					currentOrg = user.getInt("org_id");
					
					Record rdOrg = Db.findFirst("select name from sys_org where id=?",new Object[]{currentOrg});
					if(rdOrg != null){
						data.put("orgName", rdOrg.getStr("name"));
					}
					//查询当前下一组织
					List<Record> list = Db.find("select id,name from sys_org where pid=? order by seq asc limit 5",new Object[]{currentOrg});
					int ii = 0;
					List<Record> onlineNumList = new ArrayList<Record>();
					for(Record rd : list){
						//所有组织
						//所有商铺
						String sqlIn = DataOrgUtil.recordListToSqlIn(DataOrgUtil.getChildrens(rd.getInt("id"),true),"id");
						List<Record> shopList = Db.find("select id from bp_shop where org_id in ("+sqlIn+") and delete_date is null ");
						String shopStr = DataOrgUtil.recordListToSqlIn(shopList,"id");
						Record numRd = Db.findFirst("select IFNULL(SUM(online_num),0) online_num from bp_device where shop_id in("+shopStr+") and date_add(report_date, interval "+interval+" second) > now()");
						if(numRd != null){
							onlineNumList.add(new Record().set("online_num", numRd.get("online_num")).set("html", "<set label='"+rd.getStr("name")+"' value='"+numRd.get("online_num")+"' />"));
//						html += "<set label='"+rd.getStr("name")+"' value='"+numRd.get("online_num")+"' />";
							ii = ii+25;
						}
					}
					Collections.sort(onlineNumList,new OrgOnlineNum());//对每个组织的在线人数排序
					Iterator<Record> ite = onlineNumList.iterator();
					while(ite.hasNext()){
						Record rec = ite.next();
						html += rec.getStr("html");
					}
					data.put("sonTot", html);
					data.put("hight", ii);
				}
			}
			//删除临时表，一定要记住
			if(StringUtils.isNotBlank(temporaryName)){
				Db.update("DROP TEMPORARY TABLE IF EXISTS "+temporaryName);
			}
			return true;
		}});
		renderJson(data);
	}
	
	
	/**
	 * 当前组织商铺信息
	 */
	public void getShop(){
		String userid = ContextUtil.getCurrentUserId();
		List<Shop> shopList = Shop.dao.findListByUserId(userid);
		renderJson(shopList);
	}
}
class OrgOnlineNum implements Comparator<Record>{
	@Override
    public int compare(Record o1, Record o2) {
    	int num1 = Integer.parseInt(o1.get("online_num").toString());
    	int num2 = Integer.parseInt(o2.get("online_num").toString());
    	if(num1 > num2){
    		return -1;
    	}else{
    		return 1;
    	}
    }
}
