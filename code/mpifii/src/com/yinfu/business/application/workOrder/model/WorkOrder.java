package com.yinfu.business.application.workOrder.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.org.shop.model.Shop;
import com.yinfu.business.util.DeleteUtils;
import com.yinfu.business.util.PageUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.remote.HttpRequester;
import com.yinfu.jbase.util.remote.HttpRespons;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

@TableBind(tableName = "bp_work_order",pkName="wo_id")
public class WorkOrder extends Model<WorkOrder>{

	public final Log log = LogFactory.getLog(WorkOrder.class);
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    public static final WorkOrder dao=new WorkOrder();
    
    public static final String DICTION_TYPE="industry";

	public SplitPage getWrokOrderList(SplitPage splitPage){
		StringBuffer sql = new StringBuffer("SELECT s.id as id,");
		sql.append("s.sn as sn,");
		sql.append("s.name as name,");
		sql.append("s.work_addr as workAddr,");
		sql.append("s.broadband_type as broadbandType,");
		sql.append("s.tel as tel,");
		sql.append("s.org_id as orgId,");
		sql.append("u.phone as phone,");
		sql.append("d.value as trde,");
		sql.append("s.is_trde_cust as isTrdeCust,");
		sql.append("w.wo_id as woId,");
		sql.append("w.wo_state as woState,");
		sql.append("w.ap_num as apNum,");
		sql.append("w.router_num as routerNum,");
		sql.append("w.wo_type as wotype,");
		sql.append("w.ap_active_num as apActiveNum,");
		sql.append("w.router_active_num as routerActiveNum");
		splitPage = splitPageBase(splitPage,sql.toString());
		return splitPage;
	}
	
	public void makeFilter(Map<String, String> queryParam, StringBuilder formSqlSb, List<Object> paramValue) {
		String wo_id = queryParam.get("queryWoid");
		String wo_type = queryParam.get("queryWoType");
		String shop_name = queryParam.get("queryName");
		String wo_state = queryParam.get("queryWoState");
		formSqlSb.append(" FROM bp_shop s ");
		formSqlSb.append(" INNER JOIN bp_work_order w ON s.id=w.shop_id ");
		formSqlSb.append(" LEFT JOIN system_user u ON s.owner=u.id ");
		formSqlSb.append(" LEFT JOIN bp_dictionary d ON d.id=s.trde ");
		formSqlSb.append("where 1=1 ");
		if(!StringUtils.isBlank(wo_id)){
			formSqlSb.append(" and w.wo_id=?");
			paramValue.add(wo_id);
		}
		if(!StringUtils.isBlank(wo_type)){
			formSqlSb.append(" and w.wo_type=?");
			paramValue.add(wo_type);
		}
		if(!StringUtils.isBlank(wo_state)){
			formSqlSb.append(" and w.wo_state=?");
			paramValue.add(wo_state);
		}
		if(!StringUtils.isBlank(shop_name)){
			formSqlSb.append(" and s.name like'%"+shop_name+"%'");
		}
		String orgids=getOrgIds();
		if(orgids!=null && !orgids.equals("")){
			formSqlSb.append(" and s.org_id in("+orgids+")");	
		}
		
		//formSqlSb.append(" GROUP BY s.`id`  ");
		formSqlSb.append(" order by s.create_date desc ");
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
	
	/**
	 * 保存工单信息时同时生成商户帐号信息
	 * @param workOrder
	 * @param shop
	 * @return
	 */
	public boolean saveOrUpdateWorkOrder(final WorkOrder workOrder,final Shop shop,final String phone){
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = true;
			User curUser = ContextUtil.getCurrentUser();
			//判断是否行业客户 1 行业客户 2普通客户获取当前用户的所属组织
			if(shop.getInt("is_trde_cust")==2){
				shop.set("org_id", getCurrUserOrgId());
			}
			Map<String,String> loaclMap=getLocatByLocation(shop.getStr("work_addr"));
			shop.set("lng", loaclMap.get("lng"));
			shop.set("lat", loaclMap.get("lat"));
			String locat = getLocation(loaclMap);
			if(locat!=null && !locat.equals("")){
				shop.set("location",locat);
			}
			if (StringUtils.isBlank(workOrder.getStr("wo_id"))) {//新增
				//新增工单
				if(workOrder.getInt("wo_type")!=null && workOrder.getInt("wo_type").equals(1)){
					shop.set("create_date", new Date());
	                success=shop.save();

					User shopUser =new User();
					shopUser.set("phone", phone);
					shopUser.set("name", phone);
					String pwd=phone.substring(phone.length()-6, phone.length());
					shopUser.set("pwd", Sec.md5(pwd));
					shopUser.set("org_id", shop.get("org_id"));
					success=shopUser.save();
					//更新商铺关联用户
					success=shop.set("owner", shopUser.getId()).update();
					int roleId = Integer.parseInt(PropertyUtils.getProperty("role.id"));
					Object[] params = new Object[]{shopUser.getId(),roleId};
					success=Db.update("insert into system_user_role(user_id,role_id) values(?,?)", params)>0;
					//商铺认证
					success=PageUtil.initShopData(shop.getId(), shop.get("org_id"));
				}else{//追加工单
					shop.update();
				}
				workOrder.set("user_id", curUser.getId());
				workOrder.set("created_date", new Date());
		        workOrder.set("shop_id", shop.getId());
			    workOrder.set("wo_id", getWoIdByPhone(shop.getId(),phone));
				workOrder.set("wo_state", 1);//工单默认状态为1 派单中
				success = workOrder.save();	
			}else{//修改
				success = shop.update();
				success = workOrder.update();
				log.info("工单【"+workOrder.get("wo_id")+"】被用户【"+curUser.getStr("name")+"】修改");
			}
			return success;
		}
		});
		return success;
	}
	
	/**
	 * 如果该商铺存在追加订单，工单编号 联系方式+b+count
	 * @param phone
	 * @return
	 */
	public String getWoIdByPhone(int shopId,String phone){
		String relStr = phone;
		List<WorkOrder> relList = find("select wo_id from bp_work_order where shop_id="+shopId);
		if(relList!=null&&relList.size()>0){

			List<Integer> woIdLis = new ArrayList<Integer>();
			for(WorkOrder wo:relList){
				if(wo.getStr("wo_id").indexOf("b")!=-1){
					woIdLis.add(Integer.parseInt(wo.getStr("wo_id").split("b")[1]));
				}
			}
			if(woIdLis.size()!=0){
				Collections.sort(woIdLis);
				int maxNum = woIdLis.get(woIdLis.size()-1)+1;
				relStr = phone+"b"+maxNum;
			}else{
				if(relList.size()==1){
					return relStr+"b"+1;
				}
			}

		}
		return relStr;
	}
	
	/**
	 * 获取当前用户所属组织编号
	 * @return
	 */
	public int getCurrUserOrgId(){
		User curUser = ContextUtil.getCurrentUser();
		int orgid = Db.findFirst("select org_id from system_user where id="+curUser.getId()).get("org_id");
		return orgid;
	}
	
	/**
	 * 编辑信息
	 * @param woId
	 * @return
	 */
	public WorkOrder findWorkOrderByWoId(String woId){
		StringBuffer sql = new StringBuffer("SELECT s.name as shopName,");
		sql.append("s.id as id,");
		sql.append("s.sn as sn,");
		sql.append("s.name as name,");
		sql.append("s.work_addr as workAddr,");
		sql.append("s.broadband_type as broadbandType,");
		sql.append("s.tel as tel,");
		sql.append("u.phone as phone,");
		sql.append("s.org_id as orgId,");
		sql.append("s.trde as trde,");
		sql.append("s.is_trde_cust as isTrdeCust,");
		sql.append("w.wo_id as woId,");
		sql.append("w.wo_state as woState,");
		sql.append("w.ap_num as apNum,");
		sql.append("w.router_num as routerNum,");
		sql.append("w.wo_type as wotype,");
		sql.append("o.name as orgName");
		sql.append(" FROM bp_shop s");
		sql.append(" INNER JOIN bp_work_order w");
		sql.append(" ON s.id=w.shop_id");
		sql.append(" INNER JOIN sys_org o");
		sql.append(" ON s.org_id=o.id");
		sql.append(" INNER JOIN system_user u ON s.owner=u.id ");
		sql.append(" WHERE w.wo_id=?");
		WorkOrder relWO = findFirst(sql.toString(),new Object[]{woId});
		return relWO;
	}
	
	/**
	 * @param paraMap
	 * @return
	 */
	public List<WorkOrder> findWorkOrderArrInfos(Map<String,String> paraMap){
		String wo_id = paraMap.get("queryWoid");
		String wo_type = paraMap.get("queryWoType");
		String shop_name = paraMap.get("queryName");
		String wo_state = paraMap.get("queryWoState");
		
		StringBuffer sql = new StringBuffer("SELECT s.id as id,");
		sql.append("s.sn as sn,");
		sql.append("s.name as name,");
		sql.append("s.work_addr as workAddr,");
		sql.append("s.broadband_type as broadbandType,");
		sql.append("s.tel as tel,");
		sql.append("u.phone as phone,");
		sql.append("s.org_id as orgId,");
		sql.append("d.value as trde,");
		sql.append("o.name as orgName,");
		sql.append("w.wo_id as woId,");
		sql.append("if(w.wo_state<>1,'完成','派单中') as woState,");
		sql.append("w.ap_num as apNum,");
		sql.append("w.router_num as routerNum,");
		sql.append("if(w.wo_type<>2,'新增','追加') as wotype");
		sql.append(" FROM bp_shop s");
		sql.append(" INNER JOIN sys_org o ON s.org_id=o.id");
		sql.append(" INNER JOIN bp_work_order w ON s.id=w.shop_id");
		sql.append(" INNER JOIN system_user u ON s.owner=u.id ");
		sql.append(" LEFT JOIN bp_dictionary d ON d.id=s.trde ");
		sql.append(" WHERE 1=1");
		if(!StringUtils.isBlank(wo_id)){
			sql.append(" and w.wo_id="+wo_id);
		}
		if(!StringUtils.isBlank(wo_type)){
			sql.append(" and w.wo_type='"+wo_type+"'");
		}
		if(!StringUtils.isBlank(wo_state)){
			sql.append(" and w.wo_state='"+wo_state+"'");
		}
		if(!StringUtils.isBlank(shop_name)){
			sql.append(" and s.name like'%"+shop_name+"%'");
		}
		sql.append(" and o.id in("+getOrgIds()+")");
		return find(sql.toString());
	}
	
	/**
	 * 根据工单编号查看工单详细信息
	 * @param id
	 * @return
	 */
	public WorkOrder findWorkOrderinfo(String id){
		StringBuffer sql = new StringBuffer("SELECT s.id as id,");
		sql.append("s.sn as sn,");
		sql.append("s.name as name,");
		sql.append("s.work_addr as workAddr,");
		sql.append("s.broadband_type as broadbandType,");
		sql.append("s.tel as tel,");
		sql.append("u.phone as phone,");
		sql.append("s.org_id as orgId,");
		sql.append("d.value as trde,");
		sql.append("o.name as orgName,");
		sql.append("w.wo_id as woId,");
		sql.append("w.wo_state as woState,");
		sql.append("w.ap_num as apNum,");
		sql.append("w.router_num as routerNum,");
		sql.append("if(w.wo_type<>2,'新增','追加') as wotype");
		sql.append(" FROM bp_shop s");
		sql.append(" INNER JOIN sys_org o ON s.org_id=o.id");
		sql.append(" INNER JOIN bp_work_order w ON s.id=w.shop_id");
		sql.append(" INNER JOIN system_user u ON s.owner=u.id ");
		sql.append(" LEFT JOIN bp_dictionary d ON d.id=s.trde ");
		sql.append(" WHERE w.wo_id=?");
		
		WorkOrder relWO = findFirst(sql.toString(),new Object[]{id});
	
		return relWO;
	}
	
	/**
	 * 检查该商铺是否存在订单
	 * @param shopId
	 * @return
	 */
	public boolean checkShopExistWordOrder(int shopId){
		boolean relbool = false;
		long count = getCount("select count(1) from bp_work_order where shop_id="+shopId);
		if(count==0 || count==1){
			relbool=true;
		}
		
		return relbool;
	}
	
	/**
	 * 获取商铺用户
	 * @return
	 */
	public Record getShopUserByShopId(int shopId){
	   return Db.find("select * from bp_shop where id="+shopId).get(0);
	}
	
	public boolean delWorkOrder(final int shopId,final String woId){
		boolean rel=Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean bool=true;
			//删除订单时 判断该商铺是否存在订单，如果没有需要 同时删除 该商铺用户 以及权限
			long count = getCount("select count(1) from bp_work_order where shop_id="+shopId);
			if(count==1){
				int userId = Db.find("select * from bp_shop where id="+shopId).get(0).getInt("owner");
				Db.deleteById("system_user", userId);//删除商户用户
				Db.update("delete from system_user_role where user_id=?", new Object[]{userId});//删除关联角色
			}
			bool=WorkOrder.dao.deleteById(woId);
			//如果只有一个订单 同时将商铺进行删除
			if(bool && count==1){
				bool=Shop.dao.deleteById(shopId);
				Db.update("update bp_device set shop_id=null where shop_id=? ", shopId);
				delDeviceTask(shopId);
			}
			
		    return bool;
		}
		});
		return rel;
	}
	//删除解绑的盒子的同步任务
	public void delDeviceTask(int shopId){
		List<Object> ids = new ArrayList<Object>();
		List<Record> deviceList = Db.find("select router_sn from bp_device where shop_id=?",shopId);
		Iterator<Record> ite = deviceList.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			ids.add(rowData.get("router_sn"));
		}
		DeleteUtils utils = new DeleteUtils();
		List<String> sqls = new ArrayList<String>();
		utils.changeDeviceAssign(ids,sqls);
		DbExt.batch(sqls);
	}
	/**
	 * 商铺联系人号码后四位拼接 6位随机数
	 * @param phoneNum
	 * @return
	 */
	public String getWoId(String phoneNum){
		String woId="";
		if(phoneNum!=null && !"".equals(phoneNum)){
			String num = phoneNum.substring(phoneNum.length()-4, phoneNum.length());
			woId = num+getRandomStr();
		}else{
			woId = "8888"+getRandomStr();
		}
        return woId;
	}
	
	public  String getRandomStr(){
		String relStr = "";
		int[] a = new int[6];
        for (int i = 0; i < a.length; i++) {
            boolean flag = false;
            do {
                flag = false;
                int num = (int) (Math.random() * 10);
                for (int j = 0; j < i; j++) {
                    if (num == a[j]) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    a[i] = num;
                }
            } while (flag);
            relStr+=a[i];
        }
        return relStr;
	}
	
	
	public PoiRender getDownExcel(String woId){
		List<WorkOrder> conList = new ArrayList<WorkOrder>();
		WorkOrder wo = findWorkOrderinfo(woId);
		conList.add(wo);
		PoiRender excel = new PoiRender(conList); 
		String[] columns = {"woId","wotype","name","orgName","workAddr","phone","apNum","routerNum","trde","broadbandType","tel"}; 
		String[] heades = {"工单编号","工单类型","商铺名称","所属组织","商铺地址","手机号码","吸顶智能wifi数量","普通智能wifi数量","行业","商铺宽带运营商类型","固定电话号码"}; 
		excel.sheetName("所有").headers(heades).columns(columns).fileName(wo.getStr("woId")+".xls");
		return excel;
	}
	
	/**
	 * 根据商铺的详细地址得到详细坐标
	 * @param location
	 * @return
	 */
	public Map<String,String> getLocatByLocation(String workAddr){
		Map<String,String> locatMap = new HashMap<String,String>();
		HttpRequester http = new HttpRequester();
		String url = "http://api.map.baidu.com/geocoder/v2/?address="+workAddr+"&output=json&ak=XpGabbd4W3nxxzOi4WCu03yt&callback=showLocation";
		try {
			HttpRespons hr = http.sendGet(url);
			String rel = hr.getContent();
			if(rel.indexOf("location")!=-1){
				rel = rel.substring(rel.indexOf("location"), rel.indexOf("}"));
				rel=rel.substring(rel.indexOf("{")+1, rel.length());
				String[] arr=rel.split(",");
				String lng=arr[0].split(":")[1];
				String lat=arr[1].split(":")[1];
				locatMap.put("lng", lng);
				locatMap.put("lat", lat);
			}else{
				locatMap.put("lng", "");
				locatMap.put("lat", "");
			}
		} catch (IOException e) {
			log.error("工单的商铺地址转换经纬度错误", e);
		}
		return locatMap;
	}
	
	/**
	 * @param locatMap
	 * @return
	 */
	public String getLocation(Map<String,String> locatMap){
		String str="";
		try{
			if(locatMap.get("lng")!=null && !locatMap.get("lng").equals("")){
				String locaion=locatMap.get("lat")+","+locatMap.get("lng");
				String postUrl="http://api.map.baidu.com/geocoder/v2/?ak=XpGabbd4W3nxxzOi4WCu03yt";
				String postData ="callback=renderReverse&location="+locaion+"&output=json&pois=1";
	            URL url = new URL(postUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setUseCaches(false);
	            conn.setDoOutput(true);
	            
	            conn.setRequestProperty("Content-Length", "" + postData.length());
	            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	            out.write(postData);
	            out.flush();
	            out.close();
	            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK){
	            	log.info("经纬度转换地址发送请求失败");
	            	return "";
	            }
	            String line,result = "";
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	            in.close();
	            
				int i=result.indexOf("formatted_address");
				if(i!=-1){
					result=result.substring(i, result.indexOf(",", i));
					str=result.substring(result.indexOf(":")+2, result.length()-1);
				}
			}
		}catch(Exception e){
			log.error("工单的经纬度转换商铺地址错误", e);
		}
		return str;
	}
	
	/**
	 * 根据类型查询字典列表
	 * @param type
	 * @return
	 */
	public List<Record> getBpDictionaryListByType(String type){
		String sql="select id,value from bp_dictionary where type=? order by id";
		return Db.find(sql,new Object[]{type});
	}

}
