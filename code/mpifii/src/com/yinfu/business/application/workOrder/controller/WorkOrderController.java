package com.yinfu.business.application.workOrder.controller;



import java.util.HashMap;
import java.util.List;




import java.util.Map;

import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.application.workOrder.model.WorkOrder;
import com.yinfu.business.org.shop.model.Shop;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;
import com.yinfu.system.model.Org;
import com.yinfu.system.model.User;

@ControllerBind(controllerKey="/business/app/workOrder",viewPath="")
public class WorkOrderController extends Controller<WorkOrder>{

	public void index(){
		Map<String,String> paraMap = new HashMap<String, String>();
		paraMap.put("queryWoid", getPara("queryWoid"));
		paraMap.put("queryWoType", getPara("queryWoType"));
		paraMap.put("queryName",  getPara("queryName"));
		paraMap.put("queryWoState", getPara("queryWoState"));
		splitPage.setQueryParam(paraMap);
		
		SplitPage splitPages = WorkOrder.dao.getWrokOrderList(splitPage);
		splitPages.getPage().getList();
		setAttr("splitPages", splitPages);
		render("/page/business/workOrder/workOrder.jsp");
	}
	
	public void add(){
		String orgIds = WorkOrder.dao.getOrgIds();
		List<Shop> shopList = Shop.dao.find("select id,name from bp_shop where org_id in("+orgIds+")");
		setAttr("shopList", shopList);
		List<Record> diclist = WorkOrder.dao.getBpDictionaryListByType(WorkOrder.DICTION_TYPE);
		setAttr("diclist", diclist);
		render("/page/business/workOrder/workOrderAddOrModify.jsp");
	}
    
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存工单信息
	 * Created On: 
	 * @author 
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		WorkOrder workOrder = getModel();
		Shop shop = getModel(Shop.class);
		String phone=getPara("_phone");
		boolean relBool = false;
		//1新增工单，需要系统默认创建商户帐号 使用客户的联系方式 作为商户帐号
		if(workOrder.getInt("wo_type")!=null && workOrder.getInt("wo_type")==1){

			long countUser=User.dao.getCount("select count(*) from system_user where name='"+phone+"'");
			if(countUser>0){
				//用户已存在 
				renderJson("error", "phoneRepeat");
				return;
			}
		}
		//追加工单 用户选择相应的商铺，对该商铺进行追加实施工单
		relBool=WorkOrder.dao.saveOrUpdateWorkOrder(workOrder,shop,phone);
		
		renderJsonResult(relBool);
	}
	
	
	public void edit(){
		String woId=getPara("woid");
		WorkOrder workOrder = WorkOrder.dao.findWorkOrderByWoId(woId);
		setAttr("workOrder", workOrder);
		String orgIds = WorkOrder.dao.getOrgIds();
		List<Shop> shopList = Shop.dao.find("select id,name from bp_shop where org_id in("+orgIds+")");
		setAttr("shopList", shopList);
		List<Record> diclist = WorkOrder.dao.getBpDictionaryListByType(WorkOrder.DICTION_TYPE);
		setAttr("diclist", diclist);
		render("/page/business/workOrder/workOrderAddOrModify.jsp");
	}
	
	public void delete(){
		final int shopId=Integer.parseInt(getPara("id"));
		final String woId=getPara("woId");
		boolean relBool=WorkOrder.dao.delWorkOrder(shopId,woId);
		renderJsonResult(relBool);
	}
	
	public void view(){
		String woId = getPara("woId");
		
		WorkOrder workOrder = WorkOrder.dao.findWorkOrderinfo(woId);
		setAttr("workOrder", workOrder);
		//List<Device>  deviceList = Device.dao.findListByShopId(shopId,"");
		//setAttr("deviceList", deviceList);
		render("/page/business/workOrder/workOrderView.jsp");
	}
	
	public void comp(){
		final String woId=getPara("woId");
		WorkOrder wo=WorkOrder.dao.findFirst("select * from bp_work_order where wo_id=?",new Object[]{woId});
		boolean bool=wo.set("wo_state", 2).update();
		renderJsonResult(bool);
	}
	
	
	public void loadShop(){
		String shopId = getPara("id");
		Shop s=Shop.dao.findById(shopId);
		User user = User.dao.findById(s.getInt("owner"));
		Org org = Org.dao.findById(s.getInt("org_id"));
		com.alibaba.fastjson.JSONObject jsonObj=new com.alibaba.fastjson.JSONObject();
		jsonObj.put("id", s.getId());
		jsonObj.put("name",  s.getStr("name"));
		jsonObj.put("workAddr", s.getStr("work_addr"));
		if(user!=null){
			jsonObj.put("phone", user.get("phone"));
		}else{
			jsonObj.put("phone", " ");
		}
		jsonObj.put("trde", s.getStr("trde"));
		jsonObj.put("orgId", org.get("id"));
		jsonObj.put("orgName", org.getStr("name"));
		jsonObj.put("broadbandType", s.getStr("broadband_type"));
		jsonObj.put("tel", s.getStr("tel"));
		renderJson(jsonObj);
	}
	
	public void downWorkOrderInfo(){
		String woId=getPara("woId");
		render(WorkOrder.dao.getDownExcel(woId));
	}
	
	public void downinfo(){
		Map<String,String> paraMap = new HashMap<String, String>();
		paraMap.put("queryWoid", getPara("woid"));
		paraMap.put("queryWoType", getPara("wotype"));
		paraMap.put("queryName",  getPara("name"));
		paraMap.put("queryWoState", getPara("wostate"));
		List<WorkOrder> list = WorkOrder.dao.findWorkOrderArrInfos(paraMap);
		PoiRender excel = new PoiRender(list); 
		String[] columns = {"woId","wotype","woState","name","orgName","workAddr","phone","apNum","routerNum","trde","broadbandType","tel"}; 
		String[] heades = {"工单编号","工单类型"," 状态","商铺名称","所属组织","商铺地址","手机号码","吸顶智能wifi数量","普通智能wifi数量","行业","商铺宽带运营商类型","固定电话号码"}; 
		excel.sheetName("工单信息").headers(heades).columns(columns).fileName("wordOrders.xls");
		render(excel);
	}
   
}
