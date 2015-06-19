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
    	sql.append("select distinct batr.res_url image ");
    	sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id =? and basp.adv_type='adv_start' and basp.id=bas.adv_spaces) ");
    	sql.append("join bp_adv_type bat on (basp.id=bat.adv_spaces) ");
    	sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and bas.content_id=batr.content_id) ");
//		sql.append("select distinct bac.img image ");
//		sql.append("from bp_adv_shop bas ");
//		sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
//		sql.append("join bp_adv_type bat on (bas.adv_type_id=bat.id) ");
//		sql.append("join bp_adv_spaces basp on (basp.adv_type='adv_start' and basp.id=bat.adv_spaces) ");
//		sql.append("where bas.shop_id =? ");
    	Record rd = Db.findFirst(sql.toString(),new Object[]{shopId});
    	String defaultImg = "index/img/transition.png";
    	if(null != rd){
    		String image = rd.getStr("image");
    		if(StringUtils.isNotBlank(image)){
    			image = "logo/"+image.substring(image.lastIndexOf("/")+1);
    		}else{
    			image = defaultImg;
    		}
    		rd.set("image", image);
    	}else{
    		rd = new Record();
    		rd.set("image", defaultImg);
    	}
    	rd.set("indexUrl", "index.html");
    	StringBuffer sqlT = new StringBuffer();
    	sqlT.append("select ifnull(t.marker,'') marker ");
		sqlT.append("from bp_shop s join bp_shop_page sp on (s.id=? and s.id = sp.shop_id) ");
		sqlT.append("join bp_temp t on (sp.template_id=t.id)");
    	Record rec = Db.findFirst(sqlT.toString(), new Object[]{shopId});
    	if(null != rec && !"template1".equals(rec.get("marker"))){
    		String authServerPath = PropertyUtils.getProperty("router.auth.url");
        	authServerPath = authServerPath.substring(0, authServerPath.lastIndexOf("/"));
    		rd.set("indexUrl", authServerPath+"/portal/mb/index");
    	}
    	return rd;
    }
    
    public static void main(String[] args)throws Exception{
//    	InitDemoDbConfig.initPlugin();
//    	GotoMarker marker = getInstance();
//    	marker.createHtml(1);
    }
}
