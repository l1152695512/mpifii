package com.yinfu.routersyn.marker;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class IntroduceMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(IntroduceMarker.class);
	private Object shopId;
	
	private IntroduceMarker(Object shopId) throws IOException {
		super("introduce",".html");
		this.shopId = shopId;
		setContentData();
	}
	
	 public synchronized static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new IntroduceMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成introduce.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("introduce",shopId));
    	root.put("introduce", getIntroduce());
    }
    
    private Record getIntroduce(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select a.des,b.name,");
    	sql.append("ifnull(reverse(left(reverse(a.file_path),locate('/',reverse(a.file_path))-1)),'') as link ");
    	sql.append("from bp_introduce a ");
    	sql.append("left join bp_shop b ");
    	sql.append("on a.shop_id=b.id ");
    	sql.append("where a.shop_id=? ");
    	Record rd = Db.findFirst(sql.toString(), new Object[]{this.shopId});
    	return rd;
    }
    
}
