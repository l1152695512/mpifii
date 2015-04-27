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

public class IntroduceMarker {

    private Configuration cfg;
    private static IntroduceMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PathKit.getWebRootPath()+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public IntroduceMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static IntroduceMarker getInstance() {
		if (m_instance == null) {
			m_instance = new IntroduceMarker();
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
			root.put("introduce", getIntroduce());

			Template t = cfg.getTemplate("introduce.ftl");
			String dir = savePath + shopId + File.separator + "mb"+ File.separator + "introduce";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "introduce.html"),
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
   
    
    private Record getIntroduce(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select a.des,b.name,");
    	sql.append("ifnull(reverse(left(reverse(a.file_path),locate('/',reverse(a.file_path))-1)),'') as link ");
    	sql.append("from bp_introduce a ");
    	sql.append("left join bp_shop b ");
    	sql.append("on a.shop_id=b.id ");
    	sql.append("where a.shop_id=? ");
    	Record rd = Db.findFirst(sql.toString(), new Object[]{this.shopId});
    	return rd;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
