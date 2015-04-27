package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.GameTask;

public class GameMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(GameMarker.class);
	private Object shopId;
	
	public GameMarker(Object shopId) throws IOException {
		super("game",".html");
		this.shopId = shopId;
		setContentData();
	}
	public static boolean execute(Object shopId,String outputFolder){	
    	boolean success = false;
    	try {
    		success = new GameMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成game.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("game2",shopId));
    	root.put("gamelist", getGame());
    }
    
    private List<Record> getGame(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select concat('game','_',id) id,name,times,");
    	sql.append("concat(?,'/',ifnull(substring_index(icon,'/',-1),'')) icon,");
    	sql.append("concat(?,'/',ifnull(substring_index(substring_index(link,'/',-1),'.tar.gz',1),''),'/index.htm') link ");
    	sql.append("from bp_game ");
    	sql.append("where status=1 and delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{
    		GameTask.IMAGE_FOLDER.substring(GameTask.IMAGE_FOLDER.lastIndexOf("/")+1),
    		GameTask.FILE_FOLDER.substring(GameTask.FILE_FOLDER.lastIndexOf("/")+1)});
    	return list;
    }
    
}
