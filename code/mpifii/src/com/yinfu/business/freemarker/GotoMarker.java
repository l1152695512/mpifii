package com.yinfu.business.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.InitDemoDbConfig;
import com.yinfu.jbase.util.PropertyUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class GotoMarker {

    private Configuration cfg;
    private static GotoMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PropertyUtils.getProperty("server.path")+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public GotoMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            System.err.println(savePath);
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static GotoMarker getInstance() {
		if (m_instance == null) {
			m_instance = new GotoMarker();
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
			root.put("gotoAdv",getGotoAdv());

			Template t = cfg.getTemplate("goto.ftl");
			String dir = savePath + shopId + File.separator + "mb";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "goto.html"),
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
   
    private Record getGotoAdv(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select sgr.image,sgr.link ");
    	sql.append("from bp_shop s join bp_shop_group_role sgr on (s.id=? and s.group_id=sgr.shop_group_id) ");
    	sql.append("join bp_adv_type bat on (bat.adv_type='adv_start' and sgr.adv_type_id=bat.id)");
    	Record rd = Db.findFirst(sql.toString(),new Object[]{shopId});
    	if(null != rd){
    		String image = rd.getStr("image");
    		if(StringUtils.isNotBlank(image)){
    			image = "logo/"+image.substring(image.lastIndexOf("/")+1);
    		}else{
    			image = "index/img/transition.png";
    		}
    		rd.set("image", image);
    	}
    	return rd;
    }
    
    public static void main(String[] args)throws Exception{
//    	InitDemoDbConfig.initPlugin();
//    	GotoMarker marker = getInstance();
//    	marker.createHtml(1);
    }
}
