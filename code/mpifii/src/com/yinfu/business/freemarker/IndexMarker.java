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

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.InitDemoDbConfig;
import com.yinfu.jbase.util.PropertyUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class IndexMarker {

    private Configuration cfg;
    private static IndexMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PathKit.getWebRootPath()+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public IndexMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static IndexMarker getInstance() {
		if (m_instance == null) {
			m_instance = new IndexMarker();
		}
		return m_instance;
	}
    
    /**
     * 根据shopId创建首页html
     * @param shopId
     * @return
     */
    public boolean createHtml(Object shopId){
		try {
			this.shopId = shopId;
			Map root = new HashMap();
	        root.put("title", "PIFII");
	        root.put("content" , "广州因孚网络科技技术支持");
	        
	        root.put("adlist",getAdv());
	        root.put("shop", getShopInfo());
	        root.put("applist",getApp());
	        root.put("bottomAdv",getBottomAdv());
	        
	        Template t = cfg.getTemplate("index.ftl");
			String dir = savePath + shopId + File.separator + "mb";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "index.html"),
					"UTF-8"));
			t.process(root, out);
			out.flush();
			out.close();
			bool = true;
			System.out.println("生成完毕");
			
			t = cfg.getTemplate("indexPc.ftl");
			dir = savePath + shopId + File.separator + "pc";
			htmlFile = new File(dir);
			htmlFile.mkdirs();
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "indexPc.html"),
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
    
    private List<Record> getAdv(){
    	String sql = "select concat(?,'_',id) as id,REVERSE(LEFT(REVERSE(image),LOCATE('/',REVERSE(image))-1)) as src,ifnull(link,'#') as href,serial from bp_adv where shop_id=? and delete_date is null order by serial";
    	List<Record> list = Db.find(sql, new Object[]{PropertyUtils.getProperty("route.upload.type.adv"),shopId});
    	return list;
    }
    
    private Record getShopInfo(){
    	String sql = "select id,name,REVERSE(LEFT(REVERSE(icon),LOCATE('/',REVERSE(icon))-1)) as icon,addr,tel from bp_shop where id=?";
    	Record rd = Db.findFirst(sql,new Object[]{shopId});
    	return rd;
    }
    
    private List<Record> getApp(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat(?,'_',c.id) as id,c.name,REVERSE(LEFT(REVERSE(c.icon),LOCATE('/',REVERSE(c.icon))-1)) as icon,ifnull(c.link,'#') as link ");
    	sql.append("from bp_shop_page_app a ");
    	sql.append("left join bp_shop_page b ");
    	sql.append("on a.page_id = b.id ");
    	sql.append("left join bp_app c ");
    	sql.append("on a.app_id=c.id ");
    	sql.append("where b.shop_id=?");
    	List<Record> list = Db.find(sql.toString(), new Object[]{PropertyUtils.getProperty("route.upload.type.app"),shopId});
    	return list;
    }
   
    private Record getBottomAdv(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select sgr.image,sgr.link ");
    	sql.append("from bp_shop s join bp_shop_group_role sgr on (s.id=? and s.group_id=sgr.shop_group_id) ");
    	sql.append("join bp_adv_type bat on (bat.adv_type='adv_bottom' and sgr.adv_type_id=bat.id)");
    	Record rd = Db.findFirst(sql.toString(),new Object[]{shopId});
    	if(null != rd){
    		String image = rd.getStr("image");
    		if(StringUtils.isNotBlank(image)){
    			image = "logo/"+image.substring(image.lastIndexOf("/")+1);
    		}else{
    			image = "index/img/ad.png";
    		}
    		rd.set("image", image);
    	}
    	return rd;
    }
    
    public static void main(String[] args)throws Exception{
//    	InitDemoDbConfig.initPlugin();
//    	IndexMarker marker = getInstance();
//    	marker.createHtml(1);
    }
}
