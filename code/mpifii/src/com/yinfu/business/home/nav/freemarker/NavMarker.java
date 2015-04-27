package com.yinfu.business.home.nav.freemarker;

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
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.common.ContextUtil;
import com.yinfu.jbase.util.PropertyUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class NavMarker {

    private Configuration cfg;
    private static NavMarker m_instance = null;
    private boolean bool = false;
    private String rootPath = PathKit.getWebRootPath()+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = PropertyUtils.getProperty("server.path")+File.separator+"portal"+File.separator+"mb";
    
    
	public NavMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"+File.separator+"home"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static NavMarker getInstance() {
		if (m_instance == null) {
			m_instance = new NavMarker();
		}
		return m_instance;
	}
    
    /**
     * 创建nav静态jsp
     * @param shopId(0:全部)
     * @return
     */
    @SuppressWarnings("unchecked")
	public boolean createHtml(){
		try {
			Map root = new HashMap();
			root.put("cxt", "${pageContext.request.contextPath}");
			root.put("banner_advs", "${banner_advs}");
			root.put("rowLink", "${row.link}");
			root.put("rowImage", "${row.image}");
			
			root.put("nav1List", getNav(1));
			root.put("nav2List", getNav(2));
			

			Template t = cfg.getTemplate("navigate.ftl");
			String dir = savePath;
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "navigate.jsp"),
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
    
    
    private List<Record> getNav(int type){
    	String fileServerPath = PropertyUtils.getProperty("fileServer.path", "http://localhost:8080/mpifii/");
    	if(!fileServerPath.endsWith("/")){
    		fileServerPath += "/";
    	}
    	
    	Record rd = DataOrgUtil.getUserSetting(ContextUtil.getCurrentUserId(),"flag");
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("id,title,type,url,ifnull(concat(?,logo),'') logo,status,des ");
    	sql.append("from bp_nav ");
    	sql.append("where status=1 and type=? and org_root_id=? ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{fileServerPath,type,rd.getInt("id")});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
