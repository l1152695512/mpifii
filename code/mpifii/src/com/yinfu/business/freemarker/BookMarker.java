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

public class BookMarker {

    private Configuration cfg;
    private static BookMarker m_instance = null;
    private Object shopId = "0";
    private boolean bool = false;
    private String rootPath = PropertyUtils.getProperty("server.path", "E:/apache-tomcat-7.0.52/webapps/home")+File.separator+"file"+File.separator+"freemarker"+File.separator;
    private String savePath = rootPath+"html"+File.separator;
    
    
	public BookMarker() {
		try {
        	cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
			cfg.setDirectoryForTemplateLoading(new File(rootPath+File.separator+"ftl"));
		} catch (IOException e) {
		}
	}
	
    public synchronized static BookMarker getInstance() {
		if (m_instance == null) {
			m_instance = new BookMarker();
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
			root.put("booktheme", getBookTheme());
			root.put("booklist", getBook());

			Template t = cfg.getTemplate("book.ftl");
			String dir = savePath + shopId + File.separator + "mb"+ File.separator + "book";
			File htmlFile = new File(dir);
			htmlFile.mkdirs();
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dir + File.separator + "book.html"),
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
    
    private List<Record> getBookTheme(){
    	List<Record> list = Db.find("select id,name from bp_book_theme");
    	return list;
    }
   
    
    private List<Record> getBook(){
    	String idType = PropertyUtils.getProperty("route.upload.type.book", "book");
    	String imgDir = PropertyUtils.getProperty("downdir.bookImg", "/storageroot/Data/mb/book/logo");
    	String[] img = imgDir.split("/");
    	imgDir = img[img.length-1];
    	
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat(?,'_',a.id) as id,a.name,a.author,a.words,a.des,a.type,b.name typeName,a.theme,c.name themeName,");
    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(a.img),locate('/',reverse(a.img))-1)),'')) as img,");
    	sql.append("ifnull(reverse(left(reverse(a.link),locate('/',reverse(a.link))-1)),'') as link ");
    	sql.append("from bp_book a ");
    	sql.append("left join bp_book_type b on a.type=b.id ");
    	sql.append("left join bp_book_theme c on a.theme=c.id ");
    	sql.append("where a.status=1 and a.delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{idType,imgDir});
    	return list;
    }
    
    
    public static void main(String[] args)throws Exception{
    	
    }
}
