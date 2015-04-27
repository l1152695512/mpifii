
package com.yinfu.business.application.video.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.App;
import com.yinfu.business.application.video.model.Video;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * @author JiaYongChao
 */
@ControllerBind(controllerKey = "/business/app/video")
public class VideoController extends Controller<Video> {
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "video" + File.separator;
	private static final String VIDEO_PATH = "upload" + File.separator + "video" + File.separator;
	
	//@formatter:off 
	/**
	 * Title: index
	 * Description:进入视频配置页面
	 * Created On: 2014年9月25日 下午5:17:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index() {
		SplitPage splitPages = Video.dao.getVideoList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/appcenter/video/videoIndex.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: add
	 * Description:进入视频增加页面
	 * Created On: 2014年9月25日 下午5:59:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add() {
		List<Record> typeList = Video.dao.getVideoType();
		setAttr("typeList", typeList);
		render("/page/business/appcenter/video/videoAdd.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:进入视频修改页面
	 * Created On: 2014年9月26日 下午5:47:18
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit() {
		List<Record> typeList = Video.dao.getVideoType();
		setAttr("typeList", typeList);
		Video video = Video.dao.findById(getPara("id"));
		setAttr("video", video);
		render("/page/business/appcenter/video/videoAdd.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: delete
	 * Description:视频删除
	 * Created On: 2014年9月28日 上午10:57:19
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void delete() {
		String id = getPara("id");// 主键ID
		Video video = Video.dao.findById(id);
		JSONObject returnData = new JSONObject();
		if (video.set("delete_date", new Date()).update()) {
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
	 * Description:保存视频信息
	 * Created On: 2014年9月25日 下午6:36:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save() {
		int max = 1024 * 1204 * 1024;
		UploadFile file1 = getFile("videoImage", PathKit.getWebRootPath() + "/" + LOGO_PATH, max);
		UploadFile file2 = getFile("videoInfo", PathKit.getWebRootPath() + "/" + VIDEO_PATH, max);
		String icon = "";
		String link = "";
		if (file1 != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File f = ImageKit.renameFile(file1, name);
			icon = "upload/image/video/" + f.getName();
			Fs.copyFileToHome(f, "image", "video");
		}
		if (file2 != null) {
			String name = String.valueOf(System.currentTimeMillis());
			File f = ImageKit.renameFile(file2, name);
			link = "upload/file/video/" + f.getName();
			Fs.copyFileToHome(f, "file", "video");
		}
		Video video = getModel();
		if (video.getId() == null) {// 新增
			renderJsonResult(video.set("icon", icon).set("link", link).set("create_date", new Date()).save());
		} else {// 修改
			if (!icon.equals("")) {
				renderJsonResult(video.set("icon", icon).set("link", link).update());
			} else {
				renderJsonResult(video.update());
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
		Video video = Video.dao.findById(id);
		if (id != null) {
			if (video.getInt("status") == 0) {//未发布(发布)
				if (video.set("status", 1).update()) {
					if(DataSynUtil.addTask("0", "video", id, "1")){
						PageUtil.changPageLog("0", "video", id, "1");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			} else {//已发布(取消发布)
				if (video.set("status", 0).update()) {
					if(DataSynUtil.addTask("0", "video", id, "2")){
						PageUtil.changPageLog("0", "video", id, "3");// 记录更新日志
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
