package com.yinfu.business.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class VideoMarker {

    private Configuration cfg;
    private static VideoMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PropertyUtils.getProperty("server.path", "E:/apache-tomcat-7.0.52/webapps/home")+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public VideoMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static VideoMarker getInstance() {
		if (m_instance == null) {
			m_instance = new VideoMarker();
		}
		return m_instance;
	}
    
    /**
     * 根据shopId创建视频html
     * @param shopId(0:全部)
     * @return
     */
    public boolean createHtml(Object shopId){
		try {
			this.shopId = shopId;
			Map root = new HashMap();
			root.put("videolist", getVideo());

			Template t = cfg.getTemplate("video.ftl");
			String dir = savePath + shopId + File.separator + "mb"+ File.separator + "video";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "video.html"),
					"UTF-8"));
			t.process(root, out);
			out.flush();
			out.close();
			bool = true;
			System.out.println("生成完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bool;
    }
   
    
    private List<Record> getVideo(){
    	String idType = PropertyUtils.getProperty("route.upload.type.video", "video");
    	String imgDir = PropertyUtils.getProperty("downdir.videoImg", "/storageroot/Data/mb/video/logo");
    	String[] img = imgDir.split("/");
    	imgDir = img[img.length-1];
    	
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat(?,'_',a.id) as id,a.name,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(a.icon),locate('/',reverse(a.icon))-1)),'')) as icon,");
    	sql.append("ifnull(reverse(left(reverse(a.link),locate('/',reverse(a.link))-1)),'') as link,");
    	sql.append("replace(group_concat(b.name),',','/') type ");
    	sql.append("from bp_video a, bp_video_type b ");
    	sql.append("where find_in_set(b.id, a.type) and a.status=1 and a.delete_date is null ");
    	sql.append("group by a.id");
    	List<Record> list = Db.find(sql.toString(), new Object[]{idType,imgDir});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
