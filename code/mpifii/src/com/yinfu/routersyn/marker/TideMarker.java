package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.TideTask;

public class TideMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(TideMarker.class);
	private Object shopId;
	
	private TideMarker(Object shopId) throws IOException {
		super("tide",".html");
		this.shopId = shopId;
		setContentData();
	}
	
	 public synchronized static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new TideMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成tide.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("tide",shopId));
    	root.put("tidelist", getTide());
    }
    
    private List<Record> getTide(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,name,des,price,preprice,picdes,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(img),locate('/',reverse(img))-1)),'')) as img ");
    	sql.append("from bp_tide ");
    	sql.append("where shop_id=? and delete_date is null");
    	List<Record> list = Db.find(sql.toString(), new Object[]{TideTask.IMAGE_FOLDER.substring(TideTask.IMAGE_FOLDER.lastIndexOf("/")+1),shopId});
    	return list;
    }
    
}
