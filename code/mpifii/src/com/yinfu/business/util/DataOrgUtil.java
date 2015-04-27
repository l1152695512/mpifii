package com.yinfu.business.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.InitDemoDbConfig;
import com.yinfu.common.ContextUtil;
import com.yinfu.system.model.User;

public class DataOrgUtil {
	public static final char[] RANDOM_CHARS = new char[]{
		'a','b','c','d','e','f','g','h','i','g','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
		'A','B','C','D','E','F','G','H','I','G','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
		'1','2','3','4','5','6','7','8','9','0'};
	/**
	 * 将getShopsForUser的返回值放到临时表中，查询时直接关联临时表，速度会快很多
	 * 
	 * 解决的问题：之前都是返回一个List<Record>，在实际查询时使用in去过滤，这个效率很差，所以把查询出来的数据放到临时表中，过滤数据使用join，效率高很多
	 * 
	 * 注意事项：调用该方法的代码必须放到Db.tx中，否则是不能使用此处生成的临时表
	 * 
	 * @param userId
	 * @return
	 */
	public static String getShopsForUserInTemporary (Object userId){
		String tableName = "tmp_table_"+RandomStringUtils.random(10, RANDOM_CHARS);
		Db.update("CREATE TEMPORARY TABLE "+tableName+"(id int NOT NULL,PRIMARY KEY (id))");
		List<Record> shops = getShopsForUser(userId);
		if(shops.size() > 0){
			StringBuffer sql = new StringBuffer("insert into "+tableName+" values("+shops.get(0).get("id")+")");
			for(int i=1;i<shops.size();i++){
				sql.append(",("+shops.get(i).get("id")+")");
			}
			Db.update(sql.toString());
//			Object[][] params = new Object[shops.size()][1];
//			for(int i=0;i<shops.size();i++){
//				Record shop = shops.get(i);
//				params[i][0] = shop.get("id");
//			}
//			DbExt.batch("insert into "+tableName+"values(?) ", params);
		}
		return tableName;
	}
	
	/**
	 * 查找某个用户的组织及以下组织中的商铺
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Record> getShopsForUser(Object userId){
		Record userInfo = Db.findFirst("select org_id from system_user where id=? ", new Object[]{userId});
		if(null != userInfo){
			String sqlIn = recordListToSqlIn(getChildrens(userInfo.get("org_id"),true),"id");
			return Db.find("select * from bp_shop where org_id in ("+sqlIn+") and delete_date is null ");
		}else{
			return new ArrayList<Record>();
		}
//		return recordListToSqlIn(shops, "id");
	}
	/**
	 * 根据多个组织id查出对应组织id下的组织，containMe为true时返回的组织列表中也包含当前组织
	 * @param orgsId
	 * @param containMe
	 * @return
	 */
	public static Map<Object,List<Record>> getChildrensForOrgs(List<Record> orgsId,boolean containMe){
		Map<Object,List<Record>> childrens = new HashMap<Object,List<Record>>();
		List<Record> orgs = Db.find("select * from sys_org");
		Iterator<Record> ite = orgsId.iterator();
		while(ite.hasNext()){
			Object orgId = ite.next().get("id");
			childrens.put(orgId, getChildrens(orgs,orgId,containMe));
		}
		return childrens;
	}
	
	/**
	 * 根据组织id查出该组织下的所有组织，containMe为true时返回的组织列表中也包含当前组织
	 * @param orgsId
	 * @param containMe
	 * @return
	 */
	public static List<Record> getChildrens(Object orgId,boolean containMe){
		List<Record> orgs = Db.find("select * from sys_org");
		return getChildrens(orgs,orgId,containMe);
	}
	
	private static List<Record> getChildrens(List<Record> orgs,Object orgId,boolean containMe){
		List<Record> childrens = new ArrayList<Record>();
		if(containMe){
			for(int i=0;i<orgs.size();i++){
				Record rec = orgs.get(i);
				if(rec.get("id").toString().equals(orgId.toString())){
					childrens.add(rec);
				}
			}
		}
		childrens.addAll(getChildren(orgs,orgId));
		return childrens;
	}
	private static List<Record> getChildren(List<Record> orgs,Object orgId){
		List<Record> childrenOrg = new ArrayList<Record>();
		for(int i=0;i<orgs.size();i++){
			Record rec = orgs.get(i);
			if(null != rec.get("pid") && rec.get("pid").toString().equals(orgId.toString())){
				childrenOrg.add(rec);
				childrenOrg.addAll(getChildren(orgs,rec.get("id")));//添加该节点的子节点
			}
		}
		return childrenOrg;
	}
	public static void main(String[] args) {
		InitDemoDbConfig.initPlugin("jdbc:mysql://127.0.0.1:3306/pifii?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
				"root", "ifidc1120");
		User user = ContextUtil.getCurrentUser();
		System.err.println(recordListToSqlIn(getShopsForUser(user.get("id")),"id"));
		
		
		
//		getParents("21",true);
	}
	
	/**
	 * 获取商铺的组织ID
	 * 
	 * @param shopId
	 * @return
	 */
	public static Object getShopOrg(Object shopId){
		Record shopOrg = Db.findFirst("select org_id from bp_shop where id=?", new Object[]{shopId});
		if(null != shopOrg){
			return shopOrg.get("org_id");
		}else{
			return null;
		}
	}
	/**
	 * 查找某个组织的父组织
	 * 
	 * @param orgId
	 * @return 包含当前查找的记录,顺序是外层到内层
	 */
	public static List<Record> getParents(Object orgId,boolean containMe){
		List<Record> orgs = Db.find("select * from sys_org");
		List<Record> parents = new ArrayList<Record>();
		getParent(orgs,orgId,parents);
		if(!containMe){
			if(parents.size() > 0){//去掉当前的
				parents.remove(0);
			}
		}
		return parents;
	}
	private static void getParent(List<Record> orgs,Object orgPId,List<Record> parents){
		for(int i=0;i<orgs.size();i++){
			Record rec = orgs.get(i);
			if(rec.get("id").toString().equals(orgPId.toString())){
				if(null != rec.get("pid")){
					getParent(orgs,rec.get("pid"),parents);
				}
				parents.add(rec);
				break;
			}
		}
	}
	
	/**
	 * 获取用户自定义的内容
	 * 查找方式，从子到父，如果有配置了的就跳出循环
	 * @param userId
	 * @return
	 */
	public static String getUserSettingData(String defaultValue,Object userId,String fieldName){
		String thisValue = defaultValue;
		Record thisOrg = getUserSetting(userId,fieldName);
		if(null != thisOrg){
			thisValue = thisOrg.get(fieldName).toString();
		}
		return thisValue;
	}
	
	/**
	 * 将list转换为sql的in查询字符串
	 * 
	 * @param resData
	 * @param columnName
	 * @return '1','2',''		没有数据时返回''
	 */
	public static String recordListToSqlIn(List<Record> resData,String columnName){
		StringBuffer sqlIn = new StringBuffer("'");
		Iterator<Record> ite = resData.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			sqlIn.append(rowData.get(columnName)+"','");
		}
		sqlIn.append("'");
		return sqlIn.toString();
	}
	
	/**
	 * 将list的数组转换为二维数组，主要用于Db.batch()，批量处理
	 * 
	 * @param list
	 * @return
	 */
	public static Object[][] listTo2Array(List<Object[]> list){
		if(list.size() == 0){
			return null;
		}
		int size = list.get(0).length;
		Object[][] paramsArr = new Object[list.size()][size];
		for(int i=0;i<list.size();i++){
			paramsArr[i] = list.get(i);
		}
		return paramsArr;
	}
	
	
	/**
	 * 获取用户自定义根节点的内容
	 * 查找方式，从子到父，如果有配置了的就跳出循环
	 * @param userId
	 * @return
	 */
	public static Record getUserSettingByorg(Object orgId,String fieldName){
		Record rd = null;
		if(null != orgId && StringUtils.isNotBlank(orgId.toString())){
			List<Record> parentsOrg = getParents(orgId,true);
			for(int i=parentsOrg.size()-1;i>=0;i--){
				Record thisOrg = parentsOrg.get(i);
				if(null != thisOrg.get(fieldName) && StringUtils.isNotBlank(thisOrg.get(fieldName).toString())){
					rd = thisOrg;
					break;
				}
			}
		}
		return rd;
	}
	
	
	/**
	 * 获取用户自定义根节点的内容
	 * 查找方式，从子到父，如果有配置了的就跳出循环
	 * @param userId
	 * @return
	 */
	public static Record getUserSetting(Object userId,String fieldName){
		Record rd = null;
		Record userInfo = Db.findFirst("select org_id from system_user where id=? ", new Object[]{userId});
		if(null != userInfo && null != userInfo.get("org_id") && StringUtils.isNotBlank(userInfo.get("org_id").toString())){
			List<Record> parentsOrg = getParents(userInfo.get("org_id"),true);
			for(int i=parentsOrg.size()-1;i>=0;i--){
				Record thisOrg = parentsOrg.get(i);
				if(null != thisOrg.get(fieldName) && StringUtils.isNotBlank(thisOrg.get(fieldName).toString())){
					rd = thisOrg;
					break;
				}
			}
		}
		return rd;
	}
}
