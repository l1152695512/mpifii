package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.FunnyTask;

public class FunnyMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(FunnyMarker.class);
	private Object shopId;

	private FunnyMarker(Object shopId) throws IOException {
		super("funny",".html");
		this.shopId = shopId;
		setContentData();
	}
	
	 public synchronized static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new FunnyMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成funny.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("funny",shopId));
    	root.put("funnylist", getFunny());
    }
    
    private List<Record> getFunny(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,title,txt,date_format(create_date,'%Y年%m月%d日') create_date,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(img),locate('/',reverse(img))-1)),'')) as img ");
    	sql.append("from bp_funny ");
    	sql.append("where shop_id=? and delete_date is null");
    	List<Record> list = Db.find(sql.toString(), new Object[]{FunnyTask.IMAGE_FOLDER.substring(FunnyTask.IMAGE_FOLDER.lastIndexOf("/")+1),shopId});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
