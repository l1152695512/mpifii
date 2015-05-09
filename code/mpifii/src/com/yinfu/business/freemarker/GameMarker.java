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

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class GameMarker {

    private Configuration cfg;
    private static GameMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PropertyUtils.getProperty("server.path", "E:/apache-tomcat-7.0.52/webapps/home")+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public GameMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static GameMarker getInstance() {
		if (m_instance == null) {
			m_instance = new GameMarker();
		}
		return m_instance;
	}
    
    /**
     * 根据shopId创建html
     * @param shopId(0:全部)
     * @return
     */
    public boolean createHtml(Object shopId){
		try {
			this.shopId = shopId;
			Map root = new HashMap();
			root.put("gamelist", getGame());

			Template t = cfg.getTemplate("game.ftl");
			String dir = savePath + shopId + File.separator + "mb"+ File.separator + "games";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "game.html"),
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
    
    
    private List<Record> getGame(){
    	String idType = PropertyUtils.getProperty("route.upload.type.game", "game");
    	String imgDir = PropertyUtils.getProperty("downdir.gameImg", "/storageroot/Data/mb/games/logo");
    	String[] img = imgDir.split("/");
    	imgDir = img[img.length-1];
    	
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat(?,'_',id) as id,name,times,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(icon),locate('/',reverse(icon))-1)),'')) as icon,");
    	sql.append("ifnull(reverse(left(reverse(link),locate('/',reverse(link))-1)),'') as link ");
    	sql.append("from bp_game ");
    	sql.append("where status=1 and delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{idType,imgDir});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
