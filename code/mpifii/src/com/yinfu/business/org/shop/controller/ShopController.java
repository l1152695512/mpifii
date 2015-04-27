package com.yinfu.business.org.shop.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.org.shop.model.Shop;
import com.yinfu.business.org.user.model.User;
import com.yinfu.business.util.DeleteUtils;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * @author JiaYongChao
 * 商铺管理
 */
@ControllerBind(controllerKey = "/business/org/shop")
public class ShopController extends Controller<Shop>{
	
	public void index(){
		if(getPara("orgId") != null){
			splitPage.queryParam.put("org_id", getPara("orgId"));
		}
		SplitPage splitPages = Shop.dao.getShopList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/system/org/shop/index.jsp");
	}
	
	public void addOrModify() {
		if(StringUtils.isNotBlank(getPara("id"))){
			Shop shop = Shop.dao.findById(getPara("id"),"id,org_id,sn,name,customer,location,lng,lat,tel,des");
			setAttr("shop", shop);
		}else{
			Map m = new HashMap();
			m.put("org_id", getPara("orgId"));
			setAttr("shop", m);
		}
		render("/page/system/org/shop/addOrModify.jsp");
	}
	
	
	public boolean checkShopSn(String shopSn) {
		Shop rec = Shop.dao.findFirst("select * from bp_shop where sn=?",new Object[]{shopSn});
		return rec != null;
	}

	public void save(){
		boolean success = false;
		Shop shop = getModel();
		if (shop.getId() == null) {//新增
			if (checkShopSn(shop.getStr("sn"))) {// 编号重复
				renderJson("error", "shopSnRepeat");
				return;
			}else{
				Object currentUserId = ContextUtil.getCurrentUserId();
				success = shop.set("create_date",new Date()).set("create_user",currentUserId).save();
			}
			
		}else{//修改
			success = shop.update();
		}
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
				new Object[]{shopId,1,defaultImg,"",""},
				new Object[]{shopId,2,defaultImg,"",""},
				new Object[]{shopId,3,defaultImg,"",""},
				new Object[]{shopId,4,defaultImg,"",""}};
		StringBuffer sql = new StringBuffer();
		sql.append("select basp.adv_index,ifnull(sgr.image,'') image,ifnull(sgr.link,'') link,ifnull(sgr.des,'') des ");
		sql.append("from bp_shop_group_role sgr join bp_shop s on (s.id=? and s.group_id=sgr.shop_group_id) ");
		sql.append("join bp_adv_type bat on (sgr.adv_type_id=bat.id) ");
		sql.append("join bp_adv_spaces basp on (basp.adv_type='adv' and basp.id=bat.adv_spaces) ");
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
		final Set<String> fileResources = new HashSet<String>();
		boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
			DeleteUtils utils = new DeleteUtils();
			List<Object> shopIds = new ArrayList<Object>();
			shopIds.add(getPara("id"));
			List<String> sqls = new ArrayList<String>();
			fileResources.addAll(utils.deleteShop(shopIds, sqls));
			DbExt.batch(sqls);
			return true;
		}});
		if(success){
			DeleteUtils.deleteShopResources(fileResources);
		}
		renderJsonResult(success);
		
	}


	public void view(){
		String shopid = getPara("id");
		String userid = getPara("userid");
		Shop shop = Shop.dao.findById(shopid, "id,name,sn,org_id");
		setAttr("shop",shop);
		render("/page/business/shop/shopMgtView.jsp");
	}
	
	public void mapLab(){
		String lng = getPara("lng");
		String lat = getPara("lat");
		setAttr("lng",lng);
		setAttr("lat",lat);
		render("/page/business/shop/mapLab.jsp");
	}
	
	@SuppressWarnings("unchecked")
	public void findByName(){
		String name = getPara("name");
		List<Shop> list = Shop.dao.findInfoByName(name);
		renderJson(list);
	}	
	
}
