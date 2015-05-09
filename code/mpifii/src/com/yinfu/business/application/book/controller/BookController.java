
package com.yinfu.business.application.book.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.book.model.Book;
import com.yinfu.business.application.game.model.Game;
import com.yinfu.business.application.video.model.Video;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

@ControllerBind(controllerKey = "/business/app/book")
public class BookController extends Controller<Book> {
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "book" + File.separator;
	private static final String BOOK_PATH = "upload" + File.separator + "book" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: index
//	 * Description:进入小说配置页面
	 * Created On: 2014年9月25日 下午5:17:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index() {
		SplitPage splitPages = Book.dao.getBookList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/appcenter/book/bookIndex.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: add
	 * Description:进入小说增加页面
	 * Created On: 2014年9月25日 下午5:59:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add() {
		List<Record> typeList = Book.dao.getBookType();
		setAttr("typeList", typeList);
		List<Record> themeList = Book.dao.getBookThemes();
		setAttr("themeList", themeList);
		render("/page/business/appcenter/book/bookAdd.jsp");
	}
	
	//@formatter:off 
		/**
		 * Title: edit
		 * Description:进入小说修改页面
		 * Created On: 2014年9月28日 下午4:58:54
		 * @author JiaYongChao
		 * <p> 
		 */
		//@formatter:on
	public void edit() {
		List<Record> typeList = Book.dao.getBookType();
		setAttr("typeList", typeList);
		List<Record> themeList = Book.dao.getBookThemes();
		setAttr("themeList", themeList);
		Book book = Book.dao.findById(getPara("id"));
		setAttr("book", book);
		render("/page/business/appcenter/book/bookAdd.jsp");
	}
	
	//@formatter:off 
		/**
		 * Title: delete
		 * Description:删除小说
		 * Created On: 2014年9月28日 下午5:00:58
		 * @author JiaYongChao
		 * <p> 
		 */
		//@formatter:on
	public void delete() {
		String id = getPara("id");// 主键ID
		Book book = Book.dao.findById(id);
		JSONObject returnData = new JSONObject();
		if (book.set("delete_date", new Date()).update()) {
			returnData.put("state", "success");
			renderJson(returnData);
		} else {
			returnData.put("state", "error");
			renderJson(returnData);
		}
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存小说信息
	 * Created On: 2014年9月25日 下午6:36:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
		int max = 1024 * 1204 * 1024;
		UploadFile file1 = getFile("bookImage", PathKit.getWebRootPath() + "/" + LOGO_PATH, max);
		UploadFile file2 = getFile("bookInfo", PathKit.getWebRootPath() + "/" + BOOK_PATH, max);
		String icon = "";
		String link = "";
		if (file1 != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file1, name);
			icon = "upload/image/book/" +src.getName();
			Fs.copyFileToHome(src, "image", "book");
		}
		if (file2 != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file2, name);
			link = "upload/file/book/" +src.getName();
			Fs.copyFileToHome(src, "file", "book");
		}
		Book Book = getModel();
		if (Book.getId() == null) {// 新增
			renderJsonResult(Book.set("img", icon).set("link", link).set("create_date", new Date()).save());
		} else {// 修改
			if (!icon.equals("")) {
				renderJsonResult(Book.set("img", icon).set("link", link).update());
			} else {
				renderJsonResult(Book.update());
			}
		}
	}
	//@formatter:off 
	/**
	 * Title: changeStatus
	 * Description:改变状态
	 * Created On: 2014年10月8日 下午2:23:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void changeStatus() {
		String id = getPara("id");// id
		JSONObject returnData = new JSONObject();
		Book book = Book.dao.findById(id);
		if (id != null) {
			if (book.getInt("status") == 0) {//未发布(发布)
				if (book.set("status", 1).update()) {
					if(DataSynUtil.addTask("0", "book", id, "1")){
						PageUtil.changPageLog("0", "book", id, "1");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			} else {//已发布(取消发布)
				if (book.set("status", 0).update()) {
					if(DataSynUtil.addTask("0", "book", id, "2")){
						PageUtil.changPageLog("0", "book", id, "3");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			}
			
		} else {
			returnData.put("state", "error");
			renderJson(returnData);
		}
	}
}
