package com.yinfu.business.application.game.controller;

import java.io.File;
import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.game.model.Game;
import com.yinfu.business.application.radio.model.Radio;
import com.yinfu.business.application.video.model.Video;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Fs;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.model.SplitPage.SplitPage;

/**
 * @author JiaYongChao
 *
 */
@ControllerBind(controllerKey = "/business/app/game")
public class GameController extends Controller<Game> {
	private static final String LOGO_PATH = "upload" + File.separator + "image" + File.separator + "game" + File.separator;
	private static final String GAME_PATH = "upload" + File.separator + "game" + File.separator ;
	//@formatter:off 
	/**
	 * Title: index
//	 * Description:进入游戏配置页面
	 * Created On: 2014年9月25日 下午5:17:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void index(){
		SplitPage splitPages = Game.dao.getGameList(splitPage);
		setAttr("splitPage", splitPages);
		render("/page/business/appcenter/game/gameIndex.jsp");
	}
	//@formatter:off 
	/**
	 * Title: add
	 * Description:进入游戏增加页面
	 * Created On: 2014年9月25日 下午5:59:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void add(){
		render("/page/business/appcenter/game/gameAdd.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: edit
	 * Description:进入游戏修改页面
	 * Created On: 2014年9月28日 下午4:58:54
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void edit(){
		Game game = Game.dao.findById(getPara("id"));
		setAttr("game", game);
		render("/page/business/appcenter/game/gameAdd.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: delete
	 * Description:删除游戏
	 * Created On: 2014年9月28日 下午5:00:58
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void delete(){
		String id = getPara("id");//主键ID
		Game game = Game.dao.findById(id);
		JSONObject returnData = new JSONObject();
		if(game.set("delete_date", new Date()).update()){
			returnData.put("state","success");
			renderJson(returnData);
		}else{
			returnData.put("state","error");
			renderJson(returnData);
		}
	}
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存游戏信息
	 * Created On: 2014年9月25日 下午6:36:29
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		int max = 1024*1204*1024; 
		UploadFile file1 = getFile("gameImage",PathKit.getWebRootPath() + "/" +LOGO_PATH,max);
		UploadFile file2 = getFile("gameInfo",PathKit.getWebRootPath() + "/" +GAME_PATH,max);
		String icon = "";
		String link ="";
		if(file1!=null){
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file1, name);
			icon = "upload/image/game/" + src.getName();
			Fs.copyFileToHome(src, "image", "game");
		}
		if(file2!=null){
			String name = String.valueOf(System.currentTimeMillis());
			File src= ImageKit.renameFile(file2, name);
			link = "upload/file/game/" +src.getName();
			Fs.copyFileToHome(src, "file", "game");
		}
		Game Game = getModel();
		if (Game.getId() == null) {//新增
			renderJsonResult(Game.set("icon", icon).set("link", link).set("create_date",new Date()).save());
		}else{//修改
			if(!icon.equals("")){
				renderJsonResult(Game.set("icon", icon).set("link", link).update());
			}else{
				renderJsonResult(Game.update());
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
		Game game = Game.dao.findById(id);
		if (id != null) {
			if (game.getInt("status") == 0) {//未发布(发布)
				if (game.set("status", 1).update()) {
					if(DataSynUtil.addTask("0", "game", id, "1")){
						PageUtil.changPageLog("0", "game", id, "1");// 记录更新日志
					}
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			} else {//已发布(取消发布)
				if (game.set("status", 0).update()) {
					if(DataSynUtil.addTask("0", "game", id, "2")){
						PageUtil.changPageLog("0", "game", id, "3");// 记录更新日志
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
