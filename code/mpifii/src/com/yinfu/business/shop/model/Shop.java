
package com.yinfu.business.shop.model;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.Consts;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.common.Result;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.jbase.util.CollectionUtils;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.ExcelRead;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Org;
import com.yinfu.system.model.User;

@TableBind(tableName = "bp_shop")
public class Shop extends Model<Shop> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Shop dao = new Shop();
	
	public List<Shop> list() {
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		return dao.find("select id,name from bp_shop where delete_date is null and owner = ? ", new Object[] { user.getId() });
	}
	
	public JSONObject delete(String id) {
		JSONObject json = new JSONObject();
		json.put("success", dao.deleteById(id));
		return json;
	}
	
	public JSONObject add(String name, String des) {
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		Shop shop = new Shop().set("owner", user.getId()).set("name", name).set("des", des);
		JSONObject json = new JSONObject();
		json.put("success", shop.save());
		return json;
	}
	
	public JSONObject edit(String id, String name, String des) {
		Shop shop = new Shop().set("id", id).set("name", name).set("des", des);
		JSONObject json = new JSONObject();
		json.put("success", shop.update());
		return json;
	}
	
	//@formatter:off 
	/**
	 * Title: getShopList
	 * Description:获得商铺列表
	 * Created On: 2014年9月23日 下午4:58:00
	 * @author JiaYongChao
	 * <p>
	 * @param splitPage 
	 * @return 
	 */
	//@formatter:on
	public SplitPage getShopList(SplitPage splitPage) {
		/*
		 * String userid = ContextUtil.getCurrentUserId(); String sql =
		 * " SELECT s.*,COUNT(*) AS counts  FROM bp_shop s   ";
		 * sql+=" LEFT JOIN bp_device d  ON s.`id` = d.`shop_id`  ";
		 * sql+=" LEFT JOIN SYSTEM_USER u ON u.`id` = s.`owner`  "; sql+=" where 1=1 and s.delete_date is null ";
		 * if(!ContextUtil.isAdmin()){ sql+=" and  u.id="+userid; } sql+=" GROUP BY d.`shop_id`  ";
		 */
		String sql = "SELECT s.*,u.id as userid,IF(u.name IS NULL ,'暂未绑定商户',u.name) AS username,SUM(IF(d.`id` IS NULL ,0,1)) AS sbs,so.name orgName ";
		splitPage = splitPageBase(splitPage, sql);
		return splitPage;
	}
	
	// modify by dengwuhua 改为组织关联
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		formSqlSb.append(" FROM bp_shop s ");
		formSqlSb.append(" LEFT JOIN SYSTEM_USER u ON s.`owner` = u.`id` ");
		formSqlSb.append(" LEFT JOIN bp_device d ON    d.`shop_id` = s.`id` ");
		// formSqlSb.append(" LEFT JOIN bp_shop_group sg on (s.group_id=sg.id) ");
		formSqlSb.append(" LEFT JOIN sys_org so on (s.org_id=so.id) ");
		formSqlSb.append("where 1=1 and s.delete_date is null ");
		if (null != queryParam) {
			Iterator<String> ite = queryParam.keySet().iterator();
			while (ite.hasNext()) {
				String key = ite.next();
				String[] keyInfo = key.split("_");
				if (keyInfo.length > 1 && "like".equalsIgnoreCase(keyInfo[1])) {
					formSqlSb.append("and s." + keyInfo[0] + " like '" + DbUtil.queryLike(queryParam.get(key)) + "' ");
				} else {
					formSqlSb.append("and s." + key + "='" + queryParam.get(key) + "' ");
				}
			}
		}
		formSqlSb.append(" GROUP BY s.`id`  ");
		formSqlSb.append(" order by s.create_date desc ");
	}
	
	//@formatter:off 
	/**
	 * Title: checkOwner
	 * Description:检查此商铺是否绑定商户
	 * Created On: 2014年9月29日 上午11:50:53
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public boolean checkOwner(String shopid) {
		if (shopid != null) {
			String sql = "SELECT s.*,u.`id` AS userid FROM bp_shop s LEFT JOIN SYSTEM_USER u ON s.`owner` = u.`id` WHERE s.`delete_date` IS NULL and s.id="
					+ shopid;
			Shop shop = dao.findFirst(sql);
			if (shop.get("userid") != null) {
				return true;
			}
			return false;
		}
		return false;
		
	}
	
	//@formatter:off 
	/**
	 * Title: getShopView
	 * Description:获得商铺详情
	 * Created On: 2014年9月29日 下午3:00:03
	 * @author JiaYongChao
	 * <p>
	 * @param shopid
	 * @param userid
	 * @return 
	 */
	//@formatter:on
	public Shop getShopView(String shopid, String userid) {
		if (shopid != null) {
			StringBuffer sql = new StringBuffer(" SELECT s.*,IF(u.name IS NULL ,'暂未绑定商户',u.name) AS username ");
			sql.append(" FROM bp_shop s  ");
			sql.append(" LEFT JOIN SYSTEM_USER u ON s.`owner` = u.`id` ");
			sql.append(" WHERE s.`delete_date` IS NULL  ");
			sql.append(" and s.id=" + shopid);
			Shop shop = dao.findFirst(sql.toString());
			return shop;
		}
		return new Shop();
	}
	
	//@formatter:off 
	/**
	 * Title: findListByUserId
	 * Description:通过用户id获得商铺列表
	 * Created On: 2014年9月29日 下午4:15:55
	 * @author JiaYongChao
	 * <p>
	 * @param userid
	 * @return 
	 */
	//@formatter:on
	public List<Shop> findListByUserId(String userid) {
		if (userid != null) {
			String shopIds = ContextUtil.getShopByUser();
			if (!"".equals(shopIds) && shopIds != null) {
				int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔，要考虑网络延迟
				StringBuffer sql = new StringBuffer("select a.id,a.lng,a.lat,a.name,a.addr,");
				sql.append("sum(case when date_add(b.report_date, interval ? second) > now() then 1 else 0 end) online ");
				sql.append("from bp_shop a left join bp_device b on a.id=b.shop_id ");
				sql.append("where a.delete_date is null and a.lng is not null and a.lat is not null ");
				
				if (!userid.equals("1")) {
					sql.append(" and a.id in( " + shopIds + ")");
				}
				sql.append(" group by a.id");
				return dao.find(sql.toString(), new Object[] { interval });
			} else {
				return new ArrayList<Shop>();
			}
			
		}
		return new ArrayList<Shop>();
	}
	
	public List<Shop> findInfoByName(String name) {
		String sql = " SELECT t.`id`,t.`name`,u.`name` AS username FROM bp_shop t LEFT JOIN SYSTEM_USER u ON t.`owner` = u.`id` WHERE t.`delete_date` IS NULL  ";
		if (name != null || !name.equals("")) {
			sql += " AND t.name LIKE '%" + name + "%' ";
		}
		return dao.find(sql);
	}
	
	//@formatter:off 
	/**
	 * Title: finAll
	 * Description:查询全部商铺
	 * Created On: 2014年12月2日 上午10:52:24
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public List<Shop> finAll() {
		String sql = " SELECT t.`id`,t.`name`,u.`name` AS username FROM bp_shop t LEFT JOIN SYSTEM_USER u ON t.`owner` = u.`id` WHERE t.`delete_date` IS NULL ";
		if (!ContextUtil.isAdmin()) {
			sql += " and t.owner = " + ContextUtil.getCurrentUserId();
		}
		return dao.find(sql);
	}
	
	//@formatter:off 
	/**
	 * Title: findByUserId
	 * Description:根据userid查询商铺列表
	 * Created On: 2014年12月30日 上午11:33:36
	 * @author JiaYongChao
	 * <p>
	 * @param userid
	 * @return 返回结果为1,2,3,4
	 */
	//@formatter:on
	public String findByUserId(String userid) {
		List<Record> list = new ArrayList<Record>();
		if (!userid.equals("1")) {
			list = DataOrgUtil.getShopsForUser(userid);
		} else {
			String sql = " SELECT s.* FROM bp_shop s ";
			list = Db.find(sql);
		}
		String ids = "";
		if (list.size() > 0) {
			for (Record s : list) {
				String id = String.valueOf(s.get("id"));
				ids += id + ",";
			}
			String shopids = ids.substring(0, ids.length() - 1);
			return shopids;
		} else {
			return "null";
		}
	}
	
	//@formatter:off 
	/**
	 * Title: findByUserId
	 * Description:根据userid查询商铺列表
	 * Created On: 2014年12月30日 下午8:04:03
	 * @author JiaYongChao
	 * <p>
	 * @param userid
	 * @return 
	 */
	//@formatter:on
	public List<Record> getListByUserId(String userid) {
		if (!userid.equals("1")) {
			List<Record> list = DataOrgUtil.getShopsForUser(userid);
			return list;
		} else {
			String sql = " SELECT s.* FROM bp_shop s ";
			List<Record> list = Db.find(sql);
			return list;
		}
	}
	
	//@formatter:off 
	/**
	 * Title: getShopListByOrg
	 * Description:通过组织ID获得商铺列表
	 * Created On: 2015年1月4日 下午9:17:51
	 * @author JiaYongChao
	 * <p>
	 * @param orgids组织id（数据格式1,2,3）
	 * @return List<Org>
	 */
	//@formatter:on
	public List<Shop> getShopListByOrg(String orgids) {
		List<Shop> dataList = new ArrayList<Shop>();
		if (orgids != null && !orgids.equals("")) {
			String[] orgid = orgids.split(",");
			for(String id:orgid){
				List<Record> orgList = DataOrgUtil.getChildrens(id, true);
				String ids = DataOrgUtil.recordListToSqlIn(orgList, "id");
				String sql = " select s.id as value,s.name as label from bp_shop s left join sys_org org on s.org_id = org.id where s.delete_date is null ";
				sql += " and org.id in (" + ids + ")";
				List<Shop> list = dao.find(sql);
				dataList.addAll(list);
 			}
		}
		HashSet<Shop> set = new HashSet<Shop>();
		//创建一个set用来去重复	
		for(Shop inte:dataList){ 
			set.add(inte);	
		}	
		List<Shop> list = new ArrayList<Shop>(); 	
		list.addAll(set);//把set放入list中
		return list;
	}
	
	//@formatter:off 
	/**
	 * Title: importAD
	 * Description:商铺导入
	 * Created On: 2015年1月14日 下午5:21:19
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
			try {
				StringBuffer sb = new StringBuffer("");
				workbook = new HSSFWorkbook(new FileInputStream(daoru.get(0)));
				int sheetNumber = workbook.getNumberOfSheets();
				List<Record> list = new ArrayList<Record>();// 商铺
				for (int i = 0; i < sheetNumber; i++) {
					int rowIndex = workbook.getSheetAt(i).getLastRowNum();
					int cellCount = workbook.getSheetAt(0).getRow(0).getLastCellNum();
					for (int j = 1; j <= rowIndex; j++) {
						request.getSession(false).setAttribute("total", rowIndex);
						request.getSession(false).setAttribute("progress", j);
						Record shop = new Record();
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
						if (result.get(0) != null) {// 商铺编号
							if (result.get(0).toString().length() < 3) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第1列商铺编号长度不符合<br>");
							}
							shop.set("sn", result.get(0).toString());
						} else {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第1列不允许为空<br>");
						}
						if (result.get(1) != null) {// 商铺名称
							if (result.get(1).toString().length() < 3) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第2列商铺名称长度不符合<br>");
							}
							shop.set("name", result.get(1).toString());
						} else {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第2列不允许为空<br>");
						}
						if (result.get(2) != null) {// 单位地址
							shop.set("addr", result.get(2).toString());
						}
						if (result.get(3) != null) {// 地市
							/***
							 * 开始判断所导入地市组织是否存在
							 */
							if (checkOrgExist(result.get(3).toString()) == true) {
								Org org = Org.dao.findByName(result.get(3).toString());
								shop.set("orgPId",org.get("id"));
							} else {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j +1) + "行，第4列地市组织不存在<br>");
							}
							
						}
						if (result.get(4) != null) {// 县区镇
							/***
							 * 开始判断所导入商铺是否存在并且判断这个商铺是否是上个地市组织的子
							 */
							if (checkOrgExist(result.get(3).toString()) == true) {//如果地市存在
								Map<String,String> resultMap = checkOrgRelation(result.get(3).toString(),result.get(4).toString());
								String res = resultMap.get("result").toString();
								if(res.equals("1")){
									sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第5列县区镇组织不存在<br>");
								}else if(res.equals("2")){
									sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第5列县区镇组织关系不正确<br>");
								}else{
									shop.set("orgId",res);
								}
								
							} else {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第4列地市组织不存在,县区镇组织导入失败<br>");
							}
							
						}
						if (result.get(5) != null) {// SN码
							if (result.get(5).toString().length() < 16) {
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第6列<br>");
							}
							shop.set("router_sn", result.get(5).toString());
						} else {
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第6列不允许为空<br>");
						}
						if (result.get(6) != null) {// 用户名
							User user = User.dao.findByUserName(result.get(6).toString());
							if(user==null){
								sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第" + (j + 1) + "行，第7列用户名不存在<br>");
							}else{
								shop.set("userId",user.get("id"));
							}
							
						}
						list.add(shop);
					}
				}
				String shopCards = "";
				for (int i = 0; i < list.size(); i++) {
					shopCards += list.get(i).get("sn") + ",";
					Map<String, Boolean> resultMap = dao.checkShopCard(list.get(i).getStr("sn"));
					Boolean repeat = resultMap.get("repeat");
					if (repeat == null) {
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请注意第" + (i + 2) + "行新增的商铺编号不能与已有记录重复！<br>");
					} else {
						continue;
					}
				}
				String shopCard = CollectionUtils.hasRepeat(shopCards);
				if (shopCard != null) {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXCEL中商铺编号：" + shopCard + "存在重复值<br>");
				}
				String shopNames = "";
				for (int i = 0; i < list.size(); i++) {
					shopNames += list.get(i).get("name") + ",";
					Map<String, Boolean> resultMap = dao.checkShopName(list.get(i).getStr("name"));
					Boolean repeat = resultMap.get("repeat");
					if (repeat == null) {
						sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请注意第" + (i + 2) + "行新增的商铺名称不能与已有记录重复！<br>");
					} else {
						continue;
					}
				}
				String shopName = CollectionUtils.hasRepeat(shopNames);
				if (shopName != null) {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXCEL中商铺名称：" + shopName + "存在重复值<br>");
				}
				/***
				 * SN是否存在是否重复判断
				 */
				String sns = "";
				for (int i = 0; i < list.size(); i++) {
					sns += list.get(i).get("router_sn") + ",";
					Map<String, String> resultMap = dao.checkRepetition(list.get(i).getStr("router_sn"));
					if (resultMap.size() > 0) {
						if(resultMap.get("noExist")!=null){
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请注意第" + (i + 2) + "行的SN[" + resultMap.get("noExist") + "]不存在！<br>");
						}else{
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请注意第" + (i + 2) + "行的SN[" + resultMap.get("ownShop") + "]已绑定商铺！<br>");
						}
						
					} else {
						continue;
					}
				}
				String sn = CollectionUtils.hasRepeat(sns);
				if (sn != null) {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXCEL中SN[" + sn + "]存在重复值<br>");
				}
				if (sb.length() > 0) {
					return returnTxt(sb.toString());
				}
				for (Record s : list) {
					final String card=s.get("sn");//编号
					final String name =s.get("name");//商铺名称
					final String addr = s.get("addr");//商铺地址
					final String orgPId = s.get("orgPId");//组织ID
					final String orgId = s.get("orgId");//组织ID
					final String router_sn = s.get("router_sn");//sn
					final String userId = s.get("userId");//用户名
					Db.tx(new IAtom() {
						@Override
						public boolean run() throws SQLException {
							Shop shop = new Shop();
							shop.set("sn", card);
							shop.set("name", name);
							shop.set("addr", addr);
							String org_id = orgId;
							if(org_id==null){
								org_id= orgPId;
							}
							shop.set("org_id", org_id);
							shop.set("owner", userId);
							shop.set("create_date", new Date());
							shop.save();
							String[] rsn= router_sn.split("/");
							for(int i=0;i<rsn.length;i++){
								Device d = Device.dao.findByRouterSN(rsn[i]);
								d.set("shop_id", shop.get("id"));
								d.update();
							}
							return true;
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				return returnResult();
			} finally {
				daoru.clear();
			}
			rs.setMsg("导入成功");
		}
		return rs;
	}
	
	//@formatter:off 
	/**
	 * Title: checkOrgRelation
	 * Description:判断组织关系
	 * Created On: 2015年1月19日 上午10:26:24
	 * @author JiaYongChao
	 * <p>
	 * @param pname父
	 * @param name子
	 * @return 
	 */
	//@formatter:on
	private Map<String, String> checkOrgRelation(String pname, String name) {
		Map<String, String> resultMap = new HashMap<String, String>();
		//判断子组织存在与否
		String sql ="select count(*) as counts,name as name,pid as pid,id as id from sys_org where name=?";
		Record r = Db.findFirst(sql, name);
		Long total = r.getLong("counts");
		if (total > 0) {
			Integer resPid = r.get("pid");
			if(resPid!=null){
				String sqlRelation = "select * from  sys_org where id=?";
				Record relation = Db.findFirst(sqlRelation, resPid);
				String resName = relation.getStr("name");
				//判断父子关系是否正确
				if(!pname.equals(resName)){
					resultMap.put("result", "2");
				}else{
					resultMap.put("result", r.get("id").toString());
				}
			}
		} else {
			if (total == 0) {
				resultMap.put("result", "1");
			}
		}
		return resultMap;
	}

	//@formatter:off 
	/**
	 * Title: checkOrgExist
	 * Description:检查组织是否存在
	 * Created On: 2015年1月19日 上午10:02:12
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	private boolean checkOrgExist(String name) {
		String sql = "select count(*) as counts from sys_org where name=?";
		Record r = Db.findFirst(sql, name);
		Long total = r.getLong("counts");
		if (total > 0) {
			return true;
		} else {
			if (total == 0) {
				return false;
			}
		}
		return false;
	}
	
	//@formatter:off 
	/**
	 * Title: checkShopCard
	 * Description:检查商铺编号是否重复
	 * Created On: 2015年1月14日 下午5:39:35
	 * @author JiaYongChao
	 * <p>
	 * @param str
	 * @return 
	 */
	//@formatter:on
	private Map<String, Boolean> checkShopCard(String sn) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		String checkSql = "select count(*) as counts from bp_shop t where t.sn = ?";
		Record r = Db.findFirst(checkSql, sn);
		Long total = r.getLong("counts");
		if (total > 0) {
			resultMap.put("repeat", true);
		} else {
			if (total == 0) {
				resultMap.put("repeat", false);
			}
		}
		return resultMap;
		
	}
	
	//@formatter:off 
	/**
	 * Title: checkShopName
	 * Description:检查商铺名称是否重复
	 * Created On: 2015年1月14日 下午5:39:58
	 * @author JiaYongChao
	 * <p>
	 * @param str
	 * @return 
	 */
	//@formatter:on
	private Map<String, Boolean> checkShopName(String name) {
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		String checkSql = "select count(*) as counts from bp_shop t where t.name = ?";
		Record r = Db.findFirst(checkSql, name);
		Long total = r.getLong("counts");
		if (total > 0) {
			resultMap.put("repeat", true);
		} else {
			if (total == 0)
				resultMap.put("repeat", false);
			
		}
		return resultMap;
		
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
	
	//@formatter:off 
    /**
     * Title: checkRepetition
     * Description:检查盒子是否有重复
     * Created On: 2015年1月14日 下午2:52:19
     * @author JiaYongChao
     * <p>
     * @param studentVo
     * @return 
     */
    //@formatter:on
	public Map<String, String> checkRepetition(String sn) {
		Map<String, String> resultMap = new HashMap<String, String>();
		// 0100860000005275,0100860000005102
		String[] sns = sn.split(",");
		String rSns = "";
		String sSns = "";
		/**
		 * 判断是否存在,不存在不能导入
		 */
		for (int i = 0; i < sns.length; i++) { // 0代表不存在,1代表存在
			String checkSql = "select count(*) as counts,t.router_sn as sn,shop_id as shopId from bp_device t where t.router_sn = ?";
			Record r = Db.findFirst(checkSql, sns[i]);
			Long total = r.getLong("counts");
			if (total == 0) {// 0代表不存在
				rSns += sns[i] + ",";
			}else{//存在了,判断是不是绑定商铺
				Integer shopId = r.getInt("shopId");
				if(shopId!=null){
					String router_sn = r.get("sn");
					sSns += router_sn + ",";
				}
			}
		}
		if(rSns.length()>0){
			String result = rSns.substring(0, rSns.length() - 1);
			resultMap.put("noExist", result);//把不存在的SN号记录返回
		}
		if(sSns.length()>0){
			String result = sSns.substring(0, sSns.length() - 1);
			resultMap.put("ownShop", result);///把已绑定商铺的SN号记录返回
		}
		return resultMap;
	}
}
