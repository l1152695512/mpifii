package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.VideoTask;

public class VideoMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(VideoMarker.class);
	private Object shopId;
	
	public VideoMarker(Object shopId) throws IOException {
		super("video",".html");
		this.shopId = shopId;
		setContentData();
	}
	public static boolean execute(Object shopId,String outputFolder){	
    	boolean success = false;
    	try {
    		success = new VideoMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成video.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("video",shopId));
    	root.put("videolist", getVideo());
    }
    
    private List<Record> getVideo(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat('video','_',a.id) as id,a.name,");
    	
//    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(a.icon),locate('/',reverse(a.icon))-1)),'')) as icon,");
//    	sql.append("ifnull(reverse(left(reverse(a.link),locate('/',reverse(a.link))-1)),'') as link,");
    	
    	sql.append("concat(?,'/',ifnull(substring_index(a.icon,'/',-1),'')) icon,");
    	sql.append("concat(?,'/',ifnull(substring_index(a.link,'/',-1),'')) link,");
    	sql.append("replace(group_concat(b.name),',','/') type ");
    	sql.append("from bp_video a, bp_video_type b ");
    	sql.append("where find_in_set(b.id, a.type) and a.status=1 and a.delete_date is null ");
    	sql.append("group by a.id");
    	List<Record> list = Db.find(sql.toString(), new Object[]{
    		VideoTask.IMAGE_FOLDER.substring(VideoTask.IMAGE_FOLDER.lastIndexOf("/")+1),
    		VideoTask.FILE_FOLDER.substring(VideoTask.FILE_FOLDER.lastIndexOf("/")+1)});
    	return list;
    }
    
}
