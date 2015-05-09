package com.yinfu.business.shop.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.util.DeleteUtils;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.SynAllUtil;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.User;

/**
 * @author JiaYongChao
 * 商铺管理
 */
@ControllerBind(controllerKey = "/business/shop")
public class ShopController extends Controller<Shop>{
//	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "shop" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: index
	 * Description:进入商铺管理首页
	 * Created On: 2014年9月23日 下午4:39:54
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index(){
		SplitPage splitPages = Shop.dao.getShopList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/shop/shopMgtIndex.jsp");
	}
	//@formatter:off 
	/**
	 * Title: add
	 * Description:增加商铺信息
	 * Created On: 2014年9月23日 下午9:02:08
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add(){
		List<User> userList = User.dao.getUserList();
		setAttr("userList", userList);
		render("/page/business/shop/shopMgtAdd.jsp");
	}
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:修改商铺信息
	 * Created On: 2014年9月23日 下午9:02:11
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit(){
//		List<User> userList = User.dao.getUserList();
//		setAttr("userList", userList);
		if(StringUtils.isNotBlank(getPara("id"))){
			StringBuffer sql = new StringBuffer();
			sql.append("select s.id,s.name,s.location,s.lng,s.lat,s.owner,s.group_id,s.tel,s.des,u.name as owner_name,sg.name groupName ");
			sql.append("from bp_shop s join system_user u on (s.owner=u.id) ");
			sql.append("left join bp_shop_group sg on (s.group_id=sg.id) ");
			sql.append("where s.id=? ");
			Shop shop = Shop.dao.findFirst(sql.toString(), new Object[]{getPara("id")});
			setAttr("shop", shop);
		}
		render("/page/business/shop/shopMgtAdd.jsp");
	}
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存商铺信息
	 * Created On: 2014年9月23日 下午9:02:28
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = true;
			Shop shop = getModel();
			if (shop.getId() == null) {//新增
				if(null == shop.get("tel")){
					shop.set("tel", "");
				}
				if(null == shop.get("des")){
					shop.set("des", "");
				}
				Object currentUserId = ContextUtil.getCurrentUserId();
				success = shop.set("create_date",new Date()).set("create_user",currentUserId).save();
				if(success){
					success = insertAdv(shop.get("owner"),shop.get("name"));
				}
			}else{//修改
				shop.remove("owner");//不能修改商铺的拥有者
				success = shop.update();
			}
			return success;
		}});
		renderJsonResult(success);
	}
	private boolean insertAdv(Object owner,Object name){//添加商铺时要添加默认广告（4张），
		Record shop = Db.findFirst("select id from bp_shop where owner=? and name=? order by create_date desc ", new Object[]{owner,name});
		if(null != shop){
			Db.update("delete from bp_adv where shop_id=? ", new Object[]{shop.get("id")});
			Object[][] params = getDefaultAdvInfo(shop.get("id"));
			int[] changeRow = DbExt.batch("insert into bp_adv(shop_id,serial,image,link,des,create_date) values(?,?,?,?,?,now())",params);
			
			for(int i=0;i<changeRow.length;i++){
				if(changeRow[i] != 1){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	private Object[][] getDefaultAdvInfo(Object shopId){
		String defaultImg = PropertyUtils.getProperty("adv.default.img", "images/business/ad-1.jpg");
		Object[][] params = new Object[][]{
				new Object[]{shopId,1,defaultImg,"#",""},
				new Object[]{shopId,2,defaultImg,"#",""},
				new Object[]{shopId,3,defaultImg,"#",""},
				new Object[]{shopId,4,defaultImg,"#",""}};
		StringBuffer sql = new StringBuffer();
		sql.append("select bat.adv_index,ifnull(sgr.image,'') image,ifnull(sgr.link,'') link,ifnull(sgr.des,'') des ");
		sql.append("from bp_shop_group_role sgr join bp_shop s on (s.id=? and s.group_id=sgr.shop_group_id) ");
		sql.append("join bp_adv_type bat on (bat.adv_type='adv' and sgr.adv_type_id=bat.id) ");
		List<Record> advInfos = Db.find(sql.toString(),new Object[]{shopId});
		Iterator<Record> ite = advInfos.iterator();
		while(ite.hasNext()){
			Record row = ite.next();
			int serial = row.getInt("adv_index");
			if((serial-1) < params.length && (serial-1)>-1){
				if(StringUtils.isNotBlank(row.getStr("image"))){
					params[serial-1][2] = row.get("image");
				}
				params[serial-1][3] = row.get("link");
				params[serial-1][4] = row.get("des");
			}
		}
		return params;
	}
	/**
	 * 已做级联删除，会删除商铺的其他信息（真删除），具体可参考
	 */
	public void delete(){
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			DeleteUtils utils = new DeleteUtils();
			List<Object> shopIds = new ArrayList<Object>();
			shopIds.add(getPara("id"));
			List<String> sqls = new ArrayList<String>();
			Set<String> fileResources = utils.deleteShop(shopIds, sqls);
			DbExt.batch(sqls);
			utils.deleteShopResources(fileResources);
			return true;
		}});
		renderJsonResult(success);
		
//		String id = getPara("id");//主键ID
//		Shop shop = Shop.dao.findById(id);
//		JSONObject returnData = new JSONObject();
//		if(shop.set("delete_date", new Date()).update()){
//			returnData.put("state","success");
//			renderJson(returnData);
//		}else{
//			returnData.put("state","error");
//			renderJson(returnData);
//		}
	}
	//@formatter:off 
	/**
	 * Title: configDevice
	 * Description:配置设备
	 * Created On: 2014年9月23日 下午9:03:35
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void configDevice(){
		String shopId = getPara("id");
//		String userid = getPara("userid");//用户id
//		List<Device> deviceList = Device.dao.findNoShopDeviceInfo(userid);
		List<Record> deviceList = Device.dao.findDeviceWidthShopOrNoAssign(shopId);
		setAttr("infoid", shopId);
//		setAttr("type", "shop");
		setAttr("deviceList", deviceList);
		render("/page/business/device/deviceList.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: checkOwner
	 * Description:检查此商铺有没有绑定商户
	 * Created On: 2014年9月29日 上午11:49:32
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void checkOwner(){
		String shopId = getPara("id");//商铺ID
		boolean result = Shop.dao.checkOwner(shopId);
		JSONObject returnData = new JSONObject();
		if(result== true){
			returnData.put("state","success");
			renderJson(returnData);
		}else{
			returnData.put("state","error");
			renderJson(returnData);
		}
	}
	//@formatter:off 
	/**
	 * Title: view
	 * Description:商铺信息查看
	 * Created On: 2014年9月29日 下午2:16:50
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void view(){
		String shopid = getPara("id");
		String userid = getPara("userid");
		Shop shop = Shop.dao.getShopView(shopid,userid);
		setAttr("shop",shop);
		List<Device>  deviceList = Device.dao.findListByShopId(shopid,userid);
		setAttr("deviceList", deviceList);
		render("/page/business/shop/shopMgtView.jsp");
	}
	
	public void mapLab(){
		String lng = getPara("lng");
		String lat = getPara("lat");
		setAttr("lng",lng);
		setAttr("lat",lat);
		render("/page/business/shop/mapLab.jsp");
	}
	
	/**
	 * Description:更改、删除或者添加商铺的盒子
	 * Created On: 2014年10月12日 下午15:36:50
	 * @author liuzhao
	 */
	public void saveShopAssignedDevice(){
		List<String> changDevice = null == getPara("ids")?new ArrayList<String>():Arrays.asList(getPara("ids").split(","));
		
		List<Record> list = Db.find("select distinct id from bp_device where delete_date is null and shop_id=? ", new Object[]{getPara("shopId")});
		List<String> assignedDevice = new ArrayList<String>();
		Iterator<Record> ite = list.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			assignedDevice.add(rowData.get("id").toString()+"");
		}
		//添加新绑定的盒子
		final List<String> addDevice = new ArrayList<String>();
		Iterator<String> iteChang = changDevice.iterator();
		while(iteChang.hasNext()){
			String thisDeviceId = iteChang.next();
			if(StringUtils.isNotBlank(thisDeviceId) && !assignedDevice.contains(thisDeviceId)){
				addDevice.add(thisDeviceId);
			}
		}
		final Object[][] paramsAdd = new Object[addDevice.size()][2];
		for(int i=0;i<addDevice.size();i++){
			paramsAdd[i] = new Object[]{getPara("shopId"),addDevice.get(i)};
		}
		//删除解除绑定的盒子
		final List<String> deleteDevice = new ArrayList<String>();
		Iterator<String> iteAssigned = assignedDevice.iterator();
		while(iteAssigned.hasNext()){
			String thisDeviceId = iteAssigned.next();
			if(StringUtils.isNotBlank(thisDeviceId) && !changDevice.contains(thisDeviceId)){
				deleteDevice.add(thisDeviceId);
			}
		}
		final Object[][] paramsDelete = new Object[deleteDevice.size()][1];
		for(int i=0;i<deleteDevice.size();i++){
			paramsDelete[i] = new Object[]{deleteDevice.get(i)};
		}
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = true;
			if(paramsAdd.length > 0){
				int[] changAdd = DbExt.batch("update bp_device set shop_id=? where id =? and shop_id is null ", paramsAdd);
				for(int i=0;i<changAdd.length;i++){
					if(changAdd[i] < 1){
						return false;
					}
				}
				//添加盒子的同步任务
				String sqlIn = Arrays.toString(addDevice.toArray());
				sqlIn = sqlIn.substring(1, sqlIn.length()-1);
				sqlIn = sqlIn.replaceAll(",", "','");
				List<Record> synList = Db.find("select router_sn from bp_device where id in('" + sqlIn + "')");
				for(Record rd : synList){
					try{
						SynAllUtil synAll = SynAllUtil.getInstance();
						success = synAll.synAllData(rd.getStr("router_sn"));
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
				}
			}
			
			if(success && paramsDelete.length > 0){
				int[] changDelete = DbExt.batch("update bp_device set shop_id = null where id =? ", paramsDelete);
				for(int i=0;i<changDelete.length;i++){
					if(changDelete[i] < 1){
						return false;
					}
				}
				//删除解绑的盒子的同步任务
				String sqlIn = Arrays.toString(deleteDevice.toArray());
				sqlIn = sqlIn.substring(1, sqlIn.length()-1);
				sqlIn = sqlIn.replaceAll(",", "','");
				List<Object> ids = new ArrayList<Object>();
				List<Record> deviceList = Db.find("select router_sn from bp_device where id in('" + sqlIn + "')");
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
			return success;
		}});
		
		renderJsonResult(success);
	}
	
	//@formatter:off 
	/**
	 * Title: getAllShop
	 * Description:获得所有所有商铺信息
	 * Created On: 2014年10月14日 上午10:58:10
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getAllShopInfo(){
		String userid = ContextUtil.getCurrentUserId();
		List<Shop> shopList = Shop.dao.findListByUserId(userid);
		renderJson(shopList);
	}

	public void listUser(){
		render("/page/business/shop/listUser.jsp");
	}
	public void getUserName(){
		StringBuffer sqlFrom = new StringBuffer();
		sqlFrom.append("from system_user su join system_user_role sur on (su.id=sur.user_id) ");
		sqlFrom.append("join system_role sr on (sr.unique_mark='business' and sur.role_id=sr.id) ");
		sqlFrom.append("where su.delete_date is null ");
		Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				"select su.id,su.name ",sqlFrom.toString());
		renderJson(returnData);
	}
	public void listShopGroup(){
		setAttr("userId",getPara("userId"));
		render("/page/business/shop/listShopGroup.jsp");
	}
	
	public void getShopGroup(){
		Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				"select distinct id,name ","from bp_shop_group where user_id=? or user_id=? ",
				new Object[]{getPara("userId"),ContextUtil.getCurrentUserId()});
		renderJson(returnData);
	}
	//@formatter:off 
	/**
	 * Title: findByName
	 * Description:
	 * Created On: 2014年11月28日 上午11:55:48
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	@SuppressWarnings("unchecked")
	public void findByName(){
		String name = getPara("name");
		List<Shop> list = Shop.dao.findInfoByName(name);
		renderJson(list);
	}	
	
}
