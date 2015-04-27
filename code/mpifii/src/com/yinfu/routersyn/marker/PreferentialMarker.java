package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.PreferentialTask;

public class PreferentialMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(PreferentialMarker.class);
	private Object shopId;
	
	private PreferentialMarker(Object shopId) throws IOException {
		super("preferential",".html");
		this.shopId = shopId;
		setContentData();
	}
	
	 public synchronized static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new PreferentialMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成preferential.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("preferenti",shopId));
    	root.put("preferentiallist", getPreferential());
    }
    
    private List<Record> getPreferential(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,title,txt,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(img),locate('/',reverse(img))-1)),'')) as img ");
    	sql.append("from bp_preferential ");
    	sql.append("where shop_id=? and delete_date is null");
    	List<Record> list = Db.find(sql.toString(), new Object[]{PreferentialTask.IMAGE_FOLDER.substring(PreferentialTask.IMAGE_FOLDER.lastIndexOf("/")+1),shopId});
    	return list;
    }
    
}
