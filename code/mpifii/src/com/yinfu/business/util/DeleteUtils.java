package com.yinfu.business.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class DeleteUtils {
	
	public static void main(String[] args) {
//		InitDemoDbConfig.initPlugin();
//		final List<String> sqls = new ArrayList<String>();
//		List<Object> shopIds = new ArrayList<Object>();
//		shopIds.add(1);shopIds.add(2);shopIds.add(3);shopIds.add(4);shopIds.add(5);
//		shopIds.add(6);shopIds.add(7);shopIds.add(8);shopIds.add(9);shopIds.add(10);
//		shopIds.add(11);shopIds.add(12);shopIds.add(13);shopIds.add(14);shopIds.add(15);
//		shopIds.add(16);shopIds.add(17);shopIds.add(18);shopIds.add(19);shopIds.add(20);
//		shopIds.add(21);shopIds.add(22);shopIds.add(23);shopIds.add(24);shopIds.add(25);
//		shopIds.add(26);shopIds.add(27);shopIds.add(28);shopIds.add(29);shopIds.add(30);
//		shopIds.add(31);
//		DeleteUtils utils = new DeleteUtils();
//		Set<String> deleteResources = utils.deleteShop(shopIds, sqls);
//		for(int i=0;i<sqls.size();i++){
//			System.err.println(sqls.get(i));
//		}
		System.err.println("========================================================================================================================");
//		Iterator<String> ite = deleteResources.iterator();
//		while(ite.hasNext()){
//			System.err.println(ite.next());
//		}
//		Db.tx(new IAtom(){public boolean run() throws SQLException {
//			int[] changeRows = DbExt.batch(sqls);
//			for(int i=0;i<changeRows.length;i++){
//				System.err.println(changeRows[i]);
//			}
//			return false;
//		}});
	}
	/**
	 * 暂时不做，
	 */
	public void deleteUser(){
		
	}
	
	/**
	 * 删除的表数据包含：bp_adv、bp_coupon（功能已删除）、bp_flow_pack、bp_funny、bp_introduce、bp_menu、
	 * bp_preferential、bp_router_push_message（功能暂时没有使用）、bp_sms、bp_statistics_app、
	 * bp_survey、bp_survey_option（关联bp_survey）、bp_tide、bp_shop、
	 * bp_shop_page、bp_shop_page_app（关联bp_shop_page）
	 * 另外也会删除盒子的一些任务
	 * 
	 * 解绑该商铺下的所有盒子（bp_device）
	 * 删除涉及到的所有资源文件
	 * 
	 * @return 要删除的资源文件地址
	 */
	public Set<String> deleteShop(List<Object> shopIds,List<String> sqls){
		Set<String> deleteResources = new HashSet<String>();
		String sqlInString = listToSqlInStr(shopIds);
		List<Record> devices = Db.find("select id from bp_device where shop_id in ("+sqlInString+")");
		List<Object> deviceIds = new ArrayList<Object>();
		Iterator<Record> ite = devices.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			deviceIds.add(rowData.get("id"));
		}
		deleteDevice(deviceIds,sqls,false);//初始化该商铺下所有的设备（将以前产生的数据清除）
		deleteShopResourcesSql(sqlInString,"bp_adv","image","shop_id",sqls,deleteResources);//删除商铺对应的广告数据
		deleteShopResourcesSql(sqlInString,"bp_flow_pack","pic","shop_id",sqls,deleteResources);//删除流量包应用数据
		deleteShopResourcesSql(sqlInString,"bp_funny","img","shop_id",sqls,deleteResources);//删除节操段子应用数据
		deleteShopResourcesSql(sqlInString,"bp_introduce","file_path","shop_id",sqls,deleteResources);//删除商铺介绍应用数据
		deleteShopResourcesSql(sqlInString,"bp_menu","icon","shopId",sqls,deleteResources);//删除菜品应用数据
		deleteShopResourcesSql(sqlInString,"bp_preferential","img","shop_id",sqls,deleteResources);//删除最新优惠包应用数据
		sqls.add("delete from bp_statistics_app where shop_id in ("+sqlInString+")");//删除商铺app点击统计数据
		deleteTableAssociated(sqlInString,sqls,"bp_survey","bp_survey_option","survey_id");//删除调查问卷数据
		deleteShopResourcesSql(sqlInString,"bp_tide","img","shop_id",sqls,deleteResources);//删除潮机推荐应用数据
		deleteTableAssociated(sqlInString,sqls,"bp_shop_page","bp_shop_page_app","page_id");//删除商铺potal页面信息
		
		deleteShopResourcesSql(sqlInString,"bp_shop","icon","id",sqls,deleteResources);//删除商铺信息
		sqls.add("update bp_device set shop_id = null where shop_id in ("+sqlInString+")");//解绑该商铺下的所有盒子
		return deleteResources;
	}
	
	/**
	 * 删除已删除记录中包含的资源文件
	 * @param resources
	 */
	public void deleteShopResources(Set<String> resources){
		Iterator<String> ite = resources.iterator();
		while(ite.hasNext()){
			String relativePath = ite.next();
			if(relativePath.startsWith("upload")){//仅删除上传的图片，默认的图片不会被删除
				File resource = new File(PathKit.getWebRootPath() + File.separator + relativePath);
				if(resource.exists()){
					resource.delete();
				}
			}
		}
	}
	
	private void deleteShopResourcesSql(String sqlInString,String tableName,String resourceName,
			String searchFieldName,List<String> sqls,Set<String> deleteResources){
		List<Record> list = Db.find("select distinct "+resourceName+" from "+tableName+" where "+searchFieldName+" in ("+sqlInString+")");
		Iterator<Record> ite = list.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			deleteResources.add(rowData.getStr(resourceName));
		}
		sqls.add("delete from "+tableName+" where "+searchFieldName+" in ("+sqlInString+")");
	}
	
	private void deleteTableAssociated(String sqlInString,List<String> sqls,String table1,String table2,String associatedField){
		List<Record> listP = Db.find("select b.id from "+table1+" a join "+table2+" b on (a.shop_id in ("+sqlInString+") and a.id=b."+associatedField+")");
		List<Object> listC = new ArrayList<Object>(); 
		Iterator<Record> ite = listP.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			listC.add(rowData.get("id"));
		}
		sqls.add("delete from "+table2+" where id in ("+listToSqlInStr(listC)+")");
		sqls.add("delete from "+table1+" where shop_id in ("+sqlInString+")");
	}
	
	/**
	 * 删除盒子，需要级联删除的数据表包含（以后可能会有增加）：
	 * bp_auth、bp_cmd、bp_task（关联bp_cmd）、bp_page_operate、bp_feedback、bp_pass_list、
	 * bp_report、bp_route_food_order_tbl、bp_route_food_tbl（关联bp_route_food_order_tbl）、
	 * bp_router_queue、bp_router_rom、bp_statistics、bp_statistics_all、bp_statistics_pf、
	 * bp_survey_result、
	 * 
	 * 盒子的删除请在调用处做处理
	 * @return
	 */
	public void deleteDevice(List<Object> deviceIds,List<String> sqls,boolean deleteDevice){
		String sqlInString = listToSqlInStr(deviceIds);
		sqls.add("delete from bp_auth where router_sn in ("+sqlInString+")");//删除认证信息
		addTaskAndCmdSql(sqlInString,sqls);//删除同步任务
		sqls.add("delete from bp_page_operate where router_sn in ("+sqlInString+")");//删除同步任务展示
		sqls.add("delete from bp_feedback where router_sn in ("+sqlInString+")");//删除意见反馈数据
		sqls.add("delete from bp_pass_list where sn in ("+sqlInString+")");//删除白名单
		sqls.add("delete from bp_report where sn in ("+sqlInString+")");//删除上报数据
		addFoodOrderSql(sqlInString,sqls);
		sqls.add("delete from bp_router_queue where router_sn in ("+sqlInString+")");//删除排队信息
		sqls.add("delete from bp_router_rom where sn in ("+sqlInString+")");//删除设备rom版本信息
		sqls.add("delete from bp_statistics where sn in ("+sqlInString+")");//删除统计信息
		sqls.add("delete from bp_statistics_all where router_sn in ("+sqlInString+")");//删除所有应用访问信息
		sqls.add("delete from bp_statistics_pf where router_sn in ("+sqlInString+")");//删除盒子客流信息
		sqls.add("delete from bp_survey_result where router_sn in ("+sqlInString+")");//删除盒子调查问卷信息
		if(deleteDevice){
			sqls.add("delete from bp_device where router_sn in ("+sqlInString+")");//删除盒子
		}
	}
	
	/**
	 * 删除bp_cmd、bp_task（关联bp_cmd）、bp_page_operate表中的数据
	 * 盒子重新分配需要级联删除的数据
	 * @return
	 */
	public void changeDeviceAssign(List<Object> ids,List<String> sqls){
		String sqlInString = listToSqlInStr(ids);
		addTaskAndCmdSql(sqlInString,sqls);//删除同步任务
		sqls.add("delete from bp_page_operate where router_sn in ("+sqlInString+")");//删除同步任务展示
	}
	
	private void addFoodOrderSql(String sqlInString,List<String> sqls){
		List<Record> listP = Db.find("select id from bp_route_food_order_tbl where router_sn in ("+sqlInString+")");
		List<Object> listC = new ArrayList<Object>(); 
		Iterator<Record> ite = listP.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			listC.add(rowData.get("id"));
		}
		sqls.add("delete from bp_route_food_tbl where order_id in ("+listToSqlInStr(listC)+")");
		sqls.add("delete from bp_route_food_order_tbl where router_sn in ("+sqlInString+")");
	}
	
	private void addTaskAndCmdSql(String sqlInString,List<String> sqls){
		List<Record> listCmd = Db.find("select uid from bp_task where router_sn in ("+sqlInString+")");
		List<Object> listUid = new ArrayList<Object>(); 
		Iterator<Record> ite = listCmd.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			listUid.add(rowData.get("uid"));
		}
		sqls.add("delete from bp_cmd where uid in ("+listToSqlInStr(listUid)+")");
		sqls.add("delete from bp_task where router_sn in ("+sqlInString+")");
	}
	
	private String listToSqlInStr(List<Object> ids){
		StringBuffer sqlIn = new StringBuffer("'");
		Iterator<Object> ite = ids.iterator();
		while(ite.hasNext()){
			Object id = ite.next();
			sqlIn.append(id.toString()+"','");
		}
		sqlIn.append("'");
		return sqlIn.toString();
	}
}
