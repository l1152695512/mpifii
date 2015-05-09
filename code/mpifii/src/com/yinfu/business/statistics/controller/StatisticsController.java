
package com.yinfu.business.statistics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.statistics.model.Statistics;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/statistics", viewPath = "")
public class StatisticsController extends Controller<Statistics> {

	
	public void getOnLineTotal(){
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getOnLineTotal(shopId)));
	}
	
	
	public void getClientTotal(){
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getClientTotal(shopId)));
	}
	
	public void getAdvClick(){
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getAdvClick(shopId)));
	}
	
	
	public void getAdvShow(){
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getAdvShow(shopId)));
	}
	
	
	public void getSmsTotal(){
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getSmsTotal(shopId)));
	}
	
	
	public void toDevice(){
		String shopIds = getPara("_query.id_in");// 获得商铺Id
		setAttr("shopIds", shopIds);
		
		Statistics statistics = new Statistics();
		List<Record> shopList = statistics.getShopList("","not in",shopIds);
		setAttr("shopList", shopList);
		
		List<Record> ckShopList = statistics.getShopList("","in",shopIds);
		setAttr("ckShopList", ckShopList);
		
		
		SplitPage splitPages = statistics.getShopList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/statistic/device.jsp");
	}
	
}
