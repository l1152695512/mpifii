
package com.yinfu.business.device.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.common.Result;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.CollectionUtils;
import com.yinfu.jbase.util.ExcelRead;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.RouterHelper;
import com.yinfu.jbase.util.remote.SynAllUtil;
import com.yinfu.jbase.util.remote.YFHttpClient;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * 设备实体
 * 
 * @author JiaYongChao 2014年7月23日
 */
@TableBind(tableName = "bp_device")
public class Device extends Model<Device> {
	private static final long serialVersionUID = 1L;
	public static Device dao = new Device();
	
	public SplitPage findList(SplitPage splitPage) {
		String sql = "select IF(u.id is null,'未分配',u.name) user_name,IF(s.id is null,IF(u.id is null,'','未指定商铺'),s.name) shop_name,d.id,d.name,d.time_out,d.router_sn,date_format(d.create_date,'%Y-%m-%d %H:%i:%s') create_date ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	protected void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append("from bp_device d ");
		formSqlSb.append("left join bp_shop s on (d.shop_id=s.id) ");
		formSqlSb.append("left join system_user u on (u.id=d.create_user) ");
		formSqlSb.append("where d.delete_date is null  ");
	}
	
	//@formatter:off 
		/**
		 * Title: findNoShopDeviceInfo
		 * Description:获得没有关联商铺的设备信息
		 * Created On: 2014年9月24日 下午2:38:07
		 * @author JiaYongChao
		 * <p>
		 * @param userid 
		 * @param 
		 * @return 
		 */
		//@formatter:on
	public List<Device> findNoShopDeviceInfo(String userid) {
		String sql = " select * from bp_device t where 1=1 and t.delete_date is null  and t.shop_id is null and t.create_user=" + userid;
		return dao.find(sql);
	}
	
	//@formatter:off 
		/**
		 * Title: findNoUserDeviceInfo
		 * Description:获得没有关联商户的设备信息
		 * Created On: 2014年9月24日 下午6:03:34
		 * @author JiaYongChao
		 * <p>
		 * @return 
		 */
		//@formatter:on
	public List<Device> findNoUserDeviceInfo() {
		String sql = "  select * from bp_device t where 1=1 and t.delete_date is null and t.shop_id is null and t.create_user is null  ";
		return dao.find(sql);
	}
	
	public List<Record> findDeviceWidthShopOrNoAssign(String shopId,String sn) {
		StringBuffer sql = new StringBuffer();
//		sql.append("select distinct if(d.shop_id=?,1,0) isMe,d.name,d.router_sn,d.id ");
//		sql.append("from bp_device d left join system_user u on (d.create_user=u.id) left join bp_shop s on (s.id=? and s.`owner`=u.id) ");
//		sql.append("where d.delete_date is null and ");// 未删除的盒子
//		if(StringUtils.isNotBlank(sn)){
//			sql.append("d.router_sn like '"+PageUtil.queryLike(sn)+"' and ");
//		}
//		sql.append("(");
//		sql.append("(d.shop_id is null and d.create_user is null) ");// 未分配的盒子
//		sql.append("or d.shop_id=? ");// 当前商铺已分配的盒子（必须放在下面的条件前面）
//		sql.append("or (d.shop_id is null and s.id is not null) ");// 当前商铺对应的用户的所有盒子
//		sql.append(")");
		
		sql.append("select distinct if(d.shop_id=?,1,0) isMe,d.name,d.router_sn,d.id ");
		sql.append("from bp_device d left join system_user u on (d.create_user=u.id) left join bp_shop s on (s.id=? and s.`owner`=u.id) ");
		sql.append("where d.delete_date is null and ");
		sql.append("(d.shop_id=? or (d.shop_id is null and (d.create_user is null or s.id is not null)");
		if(StringUtils.isNotBlank(sn)){
			sql.append("and d.router_sn like '"+PageUtil.queryLike(sn)+"' ");
		}
		sql.append(")) order by if(d.shop_id=?,1,0) desc");
		return Db.find(sql.toString(), new Object[] { shopId, shopId, shopId, shopId});
	}
	
	//@formatter:off 
		/**
		 * Title: svaeConfigDevice
		 * Description:报存配置信息
		 * Created On: 2014年9月24日 下午7:20:55
		 * @author JiaYongChao
		 * <p>
		 * @param ids设备id集合
		 * @param type shop或者user
		 * @param id shopid或者userid
		 * @return 
		 */
		//@formatter:on
	public boolean svaeConfigDevice(String ids, String type, String id) {
		if (ids != null && type != null) {
			List<String> sqlList = new ArrayList<String>();
			if (type.equals("shop")) {// 更新商铺设备关联
				String sql = " update bp_device t set t.shop_id=" + id + " where t.id in(" + ids + ")";
				sqlList.add(sql);
				Db.batch(sqlList, sqlList.size());
				
				List<Record> list = Db.find("select router_sn from bp_device where id in(" + ids + ")");
				for (Record rd : list) {
					SynAllUtil synAll = SynAllUtil.getInstance();
					synAll.synAllData(rd.getStr("router_sn"));
				}
			} else {
				String sql = " update bp_device t set t.create_user=" + id + "  where t.id in(" + ids + ")";
				sqlList.add(sql);
				Db.batch(sqlList, sqlList.size());
			}
			return true;
		}
		return false;
	}
	
	//@formatter:off 
		/**
		 * Title: findListByShopId
		 * Description:通过商铺ID获得设备列表
		 * Created On: 2014年9月29日 下午3:00:26
		 * @author JiaYongChao
		 * <p>
		 * @param shopid
		 * @param userid
		 * @return 
		 */
		//@formatter:on
	public List<Device> findListByShopId(String shopid, String userid) {
		if (shopid != null) {
			String sql = "SELECT * FROM bp_device d WHERE d.`delete_date` IS NULL and d.shop_id=" + shopid;
			return dao.find(sql);
		}
		return new ArrayList<Device>();
	}
	
	//@formatter:off 
		/**
		 * Title: findListByUserId
		 * Description:通过userid获得设备列表
		 * Created On: 2014年9月29日 下午3:56:22
		 * @author JiaYongChao
		 * <p>
		 * @param userid
		 * @return 
		 */
		//@formatter:on
	public List<Device> findListByUserId(String userid) {
		if (userid != null) {
			StringBuffer sql = new StringBuffer(" SELECT d.name,d.router_sn,d.mac,IF(s.`name` IS NULL,'暂未绑定商铺',s.`name`) AS shopname  ");
			sql.append(" FROM bp_device d ");
			sql.append(" LEFT JOIN bp_shop s ON d.`shop_id` =s.`id` ");
			sql.append(" WHERE d.`delete_date` IS NULL  ");
			sql.append(" and d.create_user=" + userid);
			return dao.find(sql.toString());
		}
		return new ArrayList<Device>();
	}
	
	public List<Device> findInfoByName(String name, String deviceType) {
		String sql = "  SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.type="
				+ deviceType + " and t.`delete_date` IS NULL ";
		if (name != null || !name.equals("")) {
			sql += " AND t.router_sn LIKE '%" + name + "%' ";
		}
		return dao.find(sql);
	}
	
	//@formatter:off 
	/**
	 * Title: findAll
	 * Description:查询全部盒子
	 * Created On: 2014年12月2日 上午10:45:24
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Device> findAll() {
		String sql = "  SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.`delete_date` IS NULL ";
		if (!ContextUtil.isAdmin()) {
			sql += " and t.create_user=" + ContextUtil.getCurrentUserId();
		}
		return dao.find(sql);
	}
	
	//@formatter:off 
	/**
	 * Title: findByShop
	 * Description:通过shopId查询设备
	 * Created On: 2014年12月2日 上午11:31:00
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public List<Device> findByShop(String shopId) {
		String sql = "  SELECT t.`id`,t.`router_sn`,t.`name`,s.`name` AS sname FROM bp_device t  LEFT JOIN bp_shop s ON t.`shop_id` = s.`id` WHERE t.`delete_date` IS NULL ";
		if (!ContextUtil.isAdmin()) {
			sql += " and t.create_user=" + ContextUtil.getCurrentUserId();
		}
		if (shopId != null && !shopId.equals("")) {
			sql += " and t.shop_id=" + shopId;
		}
		return dao.find(sql);
	}
	
	//@formatter:off 
	/**
	 * Title: checkUpdate
	 * Description:检查是否需要更新
	 * Created On: 2015年1月6日 下午3:09:42
	 * @author JiaYongChao
	 * <p>
	 * @param deviceId
	 * @return 
	 */
	//@formatter:on
	public JSONObject checkUpdate(String deviceId) {
		JSONObject state = new JSONObject();
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select router_sn,ifnull(remote_account,concat(router_sn,'@pifii.com')) remote_account,ifnull(remote_pass,'88888888') remote_pass ");
			sql.append("from bp_device ");
			sql.append("where date_add(report_date, interval ? second) > now() and id=?");
			
			Record rd = Db.findFirst(sql.toString(), new Object[]{interval,deviceId});
			if(rd != null){
				String name = rd.getStr("remote_account");
				String pass = rd.getStr("remote_pass");
				
				YFHttpClient client = new YFHttpClient();
				client.setShowLog(false);
				String xsrf = client.serverInfo();
				String loginResult = client.login(name, pass, xsrf);
				if("".equals(loginResult)){
					if(pass.equals("88888888")){
						pass = "2014@pifii.com-yinfu";
					}else{
						pass = "88888888";
					}
					loginResult = client.login(name, pass, xsrf);
				}
				if("".equals(loginResult)){
					state.put("state", 2);
				}else{
					JSONObject obj = JSONObject.parseObject(loginResult);
					JSONArray states = (JSONArray) obj.get("router_states");
					if (states.size() > 0) {
						JSONObject router = (JSONObject) states.get(0);
						String token = router.getString("token");
						if(!"".equals(token)){
							NameValuePair[] paramsGet = { new NameValuePair("token", token),new NameValuePair("_xsrf", xsrf)};
							String getData = client.httpRouterGet("module/sys_upgrade_get", paramsGet);
							JSONObject json = JSONObject.parseObject(getData);
							if(json.containsKey("available") && !"0".equals(json.get("available").toString())){
								state.put("state", 0);
							}else{
								state.put("state", 2);
							}
						}
					}
				}
			}else{
				state.put("state", 1);
			}
		} catch (Exception e) {
			state.put("state", 2);
		}
		return state;
	}
	
	//@formatter:off 
	/**
	 * Title: deviceUpdate
	 * Description:
	 * Created On: 2015年1月6日 下午3:29:55
	 * @author JiaYongChao
	 * <p>
	 * @param deviceId
	 * @return 
	 */
	//@formatter:on
	public String deviceUpdate(String deviceId) {
		JSONObject state = new JSONObject();
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);
		state.put("status", -1);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select router_sn,ifnull(remote_account,concat(router_sn,'@pifii.com')) remote_account,ifnull(remote_pass,'88888888') remote_pass ");
			sql.append("from bp_device ");
			sql.append("where date_add(report_date, interval ? second) > now() and id=?");
			
			Record rd = Db.findFirst(sql.toString(), new Object[]{interval,deviceId});
			if(rd != null){
				String name = rd.getStr("remote_account");
				String pass = rd.getStr("remote_pass");
				
				YFHttpClient client = new YFHttpClient();
				client.setShowLog(false);
				String xsrf = client.serverInfo();
				String loginResult = client.login(name, pass, xsrf);
				if("".equals(loginResult)){
					if(pass.equals("88888888")){
						pass = "2014@pifii.com-yinfu";
					}else{
						pass = "88888888";
					}
					loginResult = client.login(name, pass, xsrf);
				}
				if("".equals(loginResult)){
				
				}else{
					JSONObject obj = JSONObject.parseObject(loginResult);
					JSONArray states = (JSONArray) obj.get("router_states");
					if (states.size() > 0) {
						JSONObject router = (JSONObject) states.get(0);
						String token = router.getString("token");
						if(!"".equals(token)){
							NameValuePair[] paramsGet = { new NameValuePair("token", token),new NameValuePair("_xsrf", xsrf)};
							String getData = client.httpRouterGet("module/sys_upgrade_set", paramsGet);
							JSONObject json = JSONObject.parseObject(getData);
							if(json.containsKey("status") && (("0".equals(json.get("status").toString())) || ("1".equals(json.get("status").toString())))){
								return getData;
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return state.toString();
	}
	
	//@formatter:off 
	/**
	 * Title: getAccount
	 * Description:获得盒子的名称和密码
	 * Created On: 2015年1月6日 下午3:30:02
	 * @author JiaYongChao
	 * <p>
	 * @param sn
	 * @return 
	 */
	//@formatter:on
	private Record getAccount(String sn) {
		Record rec = Db.findFirst(
				"select name,ifnull(remote_account,'') remote_account,ifnull(remote_pass,'') remote_pass from bp_device where router_sn=? ",
				new Object[] { sn });
		if (null == rec) {
			rec = new Record();
		}
		if (null == rec.get("remote_account") || StringUtils.isBlank(rec.getStr("remote_account"))) {
			rec.set("remote_account", sn + "@pifii.com");
		}
		if (null == rec.get("remote_pass") || StringUtils.isBlank(rec.getStr("remote_pass"))) {
			rec.set("remote_pass", "88888888");
		}
		return rec;
	}
	
	//@formatter:off 
	/**
	 * Title: checkIsOnline
	 * Description:检查盒子是否在线
	 * Created On: 2015年1月6日 下午3:38:40
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	private boolean checkIsOnline(String sn) {
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔，要考虑网络延迟
		String sql = "select 1 from bp_device where router_sn = ? and date_add(report_date, interval ? second) > now()";
		Record rd = Db.findFirst(sql, new Object[] { sn, interval });
		if (rd != null) {
			return true;
		} else {
			return false;
		}
	}
	
	//@formatter:off 
	/**
	 * Title: importAD
	 * Description:保存导入的数据
	 * Created On: 2015年1月14日 下午2:22:40
	 * @author JiaYongChao
	 * <p>
	 * @param daoru
	 * @param daoruFileName
	 * @param request
	 * @return 
	 */
	//@formatter:on
	public Result importAD(List<File> daoru, List<String> daoruFileName, HttpServletRequest request) {
		Result rs = new Result();
		if (daoru.size() <= 0 || daoruFileName.size() <= 0) {
			rs.setMsg("请选择要导入的EXCEL文件");
			rs.setState("fail");
		} else if (!(daoruFileName.get(0)).substring(daoruFileName.get(0).lastIndexOf(".")).equals(".xls")) {
			rs.setMsg("只能导入EXCEL文件");
			rs.setState("Onlyfail");
		} else {
			ExcelRead excel = new ExcelRead();
			excel.importExcel(daoru.get(0));
			HSSFWorkbook workbook;
			  StringBuffer celueSb=new StringBuffer("");
			try {
				StringBuffer sb = new StringBuffer("");
				workbook = new HSSFWorkbook(new FileInputStream(daoru.get(0)));
				int sheetNumber = workbook.getNumberOfSheets();
				List<Device> list = new ArrayList<Device>();
				for (int i = 0; i < sheetNumber; i++) {
					int rowIndex = workbook.getSheetAt(i).getLastRowNum();
					int cellCount = workbook.getSheetAt(0).getRow(0).getLastCellNum();
					for (int j = 1; j <= rowIndex; j++) {
						request.getSession(false).setAttribute("total", rowIndex);
						request.getSession(false).setAttribute("progress", j);
						Device device = new Device();
						HSSFSheet sheet = workbook.getSheetAt(i);
						HSSFRow row = sheet.getRow(j);
						// int cellCount = row.getLastCellNum();
						List<Object> result = new ArrayList<Object>();
						for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
							HSSFCell cell = row.getCell(cellIndex);
							// 获得指定单元格中的数据
							Object cellStr = excel.getCellString(cell);
							result.add(cellStr);
						}
						if (result.get(0) != null) {//sn
							if (result.get(0).toString().length() <16) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第1列字符需大于16位<br>");
							}
							device.set("router_sn", result.get(0).toString().trim());
						} else {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第1列不允许为空<br>");
						}
						if (result.get(1) != null) {//类型
							if (result.get(1).toString().length()!=1) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第2列<br>");
							}
							device.set("type", Long.valueOf(result.get(1).toString().trim()));
						} else {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第2列不允许为空<br>");
						}
						if (result.get(2) != null) {//超时时间
							/*if (result.get(2).toString().length() >0) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 3) + "行，第3列<br>");
							}*/
							device.set("time_out", Long.valueOf(result.get(2).toString().trim()));
						} else {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第3列不允许为空<br>");
						}
						if (result.get(3) != null) {//描述
							/*if (result.get(3).toString().length() >0) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 4) + "行，第4列<br>");
							}*/
							device.set("des", result.get(3).toString().trim());
						} 
						device.set("create_date", new Date());
						//device.set("create_user", ContextUtil.getCurrentUser().get("id"));
						list.add(device);
					}
				}
				String sns = "";
				for (int i = list.size()-1; i >=0; i--) {
					sns += list.get(i).get("router_sn") + ",";
					Map<String, Boolean> resultMap = dao.checkRepetition(list.get(i).getStr("router_sn"));
					Boolean repeat = resultMap.get("repeat");
					if (repeat == true) {
						celueSb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请注意第" + (i + 2) + "行新增的SN："+list.get(i).get("router_sn")+"与已有记录重复，系统未导入此记录！<br>");
						list.remove(i);
					} else {
						continue;
					}
				}
				String sn = CollectionUtils.hasRepeat(sns);
				if (sn != null) {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXCEL中SN" + sn + "存在重复值<br>");
				}
				if (sb.length() > 0) {
					return returnTxt(sb.toString());
				}
				for (Device d : list) {
					d.set("remote_account", String.valueOf(d.get("router_sn"))+"@pifii.com");
					d.set("remote_pass","2014@pifii.com-yinfu");
					d.save();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return returnResult();
			} finally {
				daoru.clear();
			}
			if (celueSb.length() > 0) {
				rs.setMsg("导入成功,但有警告：<br>" + celueSb.toString());
			}else{
				rs.setMsg("导入成功");
			}
			
		}
		return rs;
	}
	
	public Result returnResult() {
		Result rs = new Result();
		rs.setMsg("导入出错,请参照模板检查Excel的字段数");
		rs.setState("errorLen");
		return rs;
	}
	
	public Result returnTxt(String txt) {
		Result rs = new Result();
		rs.setMsg("导入出错的地方：<br>" + txt);
		rs.setState("errorTxt");
		return rs;
	}
    public Result returnRecover(String txt){
        Result rs=new Result();
        rs.setMsg("未导入的地方：<br>"+txt);
        rs.setState("successTxt");
        return rs;
    }
	//@formatter:off 
    /**
     * Title: checkRepetition
     * Description:检查是否有重复
     * Created On: 2015年1月14日 下午2:52:19
     * @author JiaYongChao
     * <p>
     * @param studentVo
     * @return 
     */
    //@formatter:on
	public Map<String, Boolean> checkRepetition(String sn) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		String checkSql = "select count(*) as counts from bp_device t where t.router_sn = ?";
		Record r = Db.findFirst(checkSql, sn);
		Long total = r.getLong("counts");
		if (total > 0) {
			resultMap.put("repeat", true);
		} else {
			if (total == 0)
				resultMap.put("repeat", false);
			
		}
		return resultMap;
		
	}
	//@formatter:off 
	/**
	 * Title: findByRouterSN
	 * Description:根据SN获得设备信息
	 * Created On: 2015年1月19日 上午11:01:30
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public Device findByRouterSN(String sn){
		String sql="select * from bp_device where router_sn=?";
		Device d = dao.findFirst(sql,sn);
		return d;
		
	}
}
