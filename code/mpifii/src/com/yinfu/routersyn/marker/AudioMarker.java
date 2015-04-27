package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.AudioTask;

public class AudioMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(AudioMarker.class);
	private Object shopId;
	
	public AudioMarker(Object shopId) throws IOException {
		super("audio",".html");
		this.shopId = shopId;
		setContentData();
	}
	public static boolean execute(Object shopId,String outputFolder){	
    	boolean success = false;
    	try {
    		success = new AudioMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成audio.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("audio",shopId));
    	root.put("audiolist", getAudio());
    }
    
    private List<Record> getAudio(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat('audio','_',id) as id,name,");
    	sql.append("concat(?,'/',ifnull(substring_index(icon,'/',-1),'')) icon,");
    	sql.append("concat(?,'/',ifnull(substring_index(link,'/',-1),'')) link ");
    	sql.append("from bp_audio ");
    	sql.append("where status=1 and delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{
    		AudioTask.IMAGE_FOLDER.substring(AudioTask.IMAGE_FOLDER.lastIndexOf("/")+1),
    		AudioTask.FILE_FOLDER.substring(AudioTask.FILE_FOLDER.lastIndexOf("/")+1)});
    	return list;
    }
    
}
