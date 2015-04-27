package com.yinfu.routersyn.marker;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.BookTask;

public class BookMarker extends BaseMarker{
	private static Logger logger = Logger.getLogger(BookMarker.class);
	private Object shopId;
	
	public BookMarker(Object shopId) throws IOException {
		super("book",".html");
		this.shopId = shopId;
		setContentData();
	}
	public static boolean execute(Object shopId,String outputFolder){	
    	boolean success = false;
    	try {
    		success = new BookMarker(shopId).createHtml(outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成book.html异常！", e);
		}
    	return success;
	}
	
    private void setContentData(){
    	root.put("name", getAppCustomName("book",shopId));
    	root.put("booktheme", getBookTheme());
		root.put("booklist", getBook());
    }
    
    private List<Record> getBookTheme(){
    	List<Record> list = Db.find("select id,name from bp_book_theme");
    	return list;
    }
    
    private List<Record> getBook(){
    	StringBuffer sql = new StringBuffer("select ");
    	sql.append("concat('book','_',a.id) as id,a.name,a.author,a.words,a.des,a.type,b.name typeName,a.theme,c.name themeName,");
//    	sql.append("concat(?,'/',ifnull(reverse(left(reverse(a.img),locate('/',reverse(a.img))-1)),'')) as img,");
//    	sql.append("ifnull(reverse(left(reverse(a.link),locate('/',reverse(a.link))-1)),'') as link ");
    	sql.append("concat(?,'/',ifnull(substring_index(a.img,'/',-1),'')) img,");
    	sql.append("concat(?,'/',ifnull(substring_index(a.link,'/',-1),'')) link ");
    	sql.append("from bp_book a ");
    	sql.append("left join bp_book_type b on a.type=b.id ");
    	sql.append("left join bp_book_theme c on a.theme=c.id ");
    	sql.append("where a.status=1 and a.delete_date is null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{
    		BookTask.IMAGE_FOLDER.substring(BookTask.IMAGE_FOLDER.lastIndexOf("/")+1),
    		BookTask.FILE_FOLDER.substring(BookTask.FILE_FOLDER.lastIndexOf("/")+1)});
    	return list;
    }
    
}
