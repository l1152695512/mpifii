package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.RestaurantTask;

public class RestaurantMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(RestaurantMarker.class);
	private Object shopId;
	
	private RestaurantMarker(Object shopId) throws IOException {
		super("restaurant",".xml","index");
		this.shopId = shopId;
		setContentData();
	}
	
	public synchronized static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new RestaurantMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成restaurant.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("menu",shopId));
    	root.put("menuTypelist", getMenuType());
		root.put("menulist", getMenu());
    }
    
    private List<Record> getMenuType(){
    	String sql = "select id,name from bp_menu_type";
    	List<Record> list = Db.find(sql);
    	return list;
    }
    
    private List<Record> getMenu(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,name,type,old_price,new_price,times,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(icon),locate('/',reverse(icon))-1)),'')) as icon ");
    	sql.append("from bp_menu ");
    	sql.append("where shopId=? and delete_date is null");
    	List<Record> list = Db.find(sql.toString(), new Object[]{RestaurantTask.IMAGE_FOLDER.substring(RestaurantTask.IMAGE_FOLDER.lastIndexOf("/")+1),shopId});
    	return list;
    }
    
}
