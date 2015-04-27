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

public class RestaurantMarker {

    private Configuration cfg;
    private static RestaurantMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PathKit.getWebRootPath()+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public RestaurantMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static RestaurantMarker getInstance() {
		if (m_instance == null) {
			m_instance = new RestaurantMarker();
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
			root.put("menuTypelist", getMenuType());
			root.put("menulist", getMenu());

			Template t = cfg.getTemplate("restaurant.ftl");
			String dir = savePath + shopId + File.separator + "mb"+ File.separator + "restaurant";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "index.xml"),
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
   
    
    private List<Record> getMenuType(){
    	String sql = "select id,name from bp_menu_type";
    	List<Record> list = Db.find(sql);
    	return list;
    }
    
    private List<Record> getMenu(){
    	String imgDir = PropertyUtils.getProperty("downdir.RestaurantImg", "/storageroot/Data/mb/restaurant/logo");
    	String[] img = imgDir.split("/");
    	imgDir = img[img.length-1];
    	
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,name,type,old_price,new_price,times,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(icon),locate('/',reverse(icon))-1)),'')) as icon ");
    	sql.append("from bp_menu ");
    	sql.append("where shopId=? and delete_date is null");
    	List<Record> list = Db.find(sql.toString(), new Object[]{imgDir,shopId});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
