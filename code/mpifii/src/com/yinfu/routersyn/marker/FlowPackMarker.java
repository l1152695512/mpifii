package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.FlowPackTask;

public class FlowPackMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(FlowPackMarker.class);
	private Object shopId;
	
	private FlowPackMarker(Object shopId) throws IOException {
		super("flowpack",".html");
		this.shopId = shopId;
		setContentData();
	}
	
	public static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new FlowPackMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成flowpack.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("flowpack",shopId));
    	root.put("flowpacklist", getFlowPack());
    }
    
    private List<Record> getFlowPack(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,title,des,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(pic),locate('/',reverse(pic))-1)),'')) as pic ");
    	sql.append("from bp_flow_pack ");
    	sql.append("where shop_id=? and delete_date is null");
    	List<Record> list = Db.find(sql.toString(), 
    			new Object[]{FlowPackTask.IMAGE_FOLDER.substring(FlowPackTask.IMAGE_FOLDER.lastIndexOf("/")+1),shopId});
    	return list;
    }
    
}
