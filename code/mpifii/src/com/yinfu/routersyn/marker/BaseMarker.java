package com.yinfu.routersyn.marker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class BaseMarker {
	private static Logger logger = Logger.getLogger(BaseMarker.class);
	protected Map<String,Object> root = new HashMap<String,Object>();
    protected String ftlName;//不包含后缀名
    protected String htmlName;//不包含后缀名
    protected String fileExtension;//生成文件的后缀名，如：.html、.xml
    private Configuration cfg;
    
    public BaseMarker(String ftlName,String fileExtension) throws IOException{
		this(ftlName, fileExtension, ftlName);
	}
    
    public BaseMarker(String ftlName,String fileExtension,String htmlName) throws IOException{
		this.ftlName = ftlName;
		this.htmlName = htmlName;
		this.fileExtension = fileExtension;
    	cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
		cfg.setDirectoryForTemplateLoading(new File(PathKit.getWebRootPath()+File.separator+"file"+File.separator+"freemarker"+File.separator+"ftl"));
	}
	
    protected boolean createHtml(String outputFolder) throws IOException, TemplateException{
        Template t = cfg.getTemplate(ftlName+".ftl");
		File htmlFile = new File(outputFolder);
		htmlFile.mkdirs();
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFolder + File.separator + htmlName + fileExtension),"UTF-8"));
		t.process(root, out);
		out.flush();
		out.close();
//		logger.info("生成html("+outputFolder+File.separator + htmlName + fileExtension+")");
		return true;
    }
    
    /**
     * 获取app配置的名称，如果没配置就显示默认的
     * 
     * @param appMarker
     * @param shopId
     * @return
     */
    protected String getAppCustomName(Object appMarker,Object shopId){
    	if(null == shopId){
    		shopId = "";
    	}
		StringBuffer sql = new StringBuffer();
		sql.append("select ifnull(sac.name,a.name) name ");
		sql.append("from bp_app a ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and a.template_id=sac.template_id and sac.app_id=a.id) ");
		sql.append("where a.marker=? ");
		Record app = Db.findFirst(sql.toString(), new Object[]{shopId,appMarker});
		String appName = "";
		if(null != app && null != app.get("name")){
			appName = app.get("name").toString();
		}
		return appName;
	}
}
