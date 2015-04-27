package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.ApkTask;

public class ApkMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(ApkMarker.class);
	private Object shopId;

	public ApkMarker(Object shopId) throws IOException {
		super("app",".html");
		this.shopId = shopId;
		setContentData();
	}
	public static boolean execute(Object shopId,String outputFolder){
    	boolean success = false;
    	try {
    		success = new ApkMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成app.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("apk2",shopId));
    	root.put("apptheme", getAppTheme());
		root.put("applist", getApp());
    }
    
    private List<Record> getAppTheme(){
    	List<Record> list = Db.find("select id,name from bp_apk_theme");
    	return list;
    }
   
    
    private List<Record> getApp(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat('apk','_',a.id) as id,a.name,a.score,a.down_num,a.theme,b.name themeName,");
    	sql.append("concat(?,'/',ifnull(substring_index(icon,'/',-1),'')) icon,");
    	sql.append("concat(?,'/',ifnull(substring_index(link,'/',-1),'')) link ");
    	sql.append("from bp_apk a ");
    	sql.append("left join bp_apk_theme b on a.theme=b.id ");
    	sql.append("where a.status=1 and a.delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{
    		ApkTask.IMAGE_FOLDER.substring(ApkTask.IMAGE_FOLDER.lastIndexOf("/")+1),
    		ApkTask.FILE_FOLDER.substring(ApkTask.FILE_FOLDER.lastIndexOf("/")+1)});
    	return list;
    }
    
}
