 package com.yinfu.jbase.util.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.freemarker.AppMarker;
import com.yinfu.business.freemarker.AudioMarker;
import com.yinfu.business.freemarker.BookMarker;
import com.yinfu.business.freemarker.FlowPackMarker;
import com.yinfu.business.freemarker.FunnyMarker;
import com.yinfu.business.freemarker.GameMarker;
import com.yinfu.business.freemarker.GotoMarker;
import com.yinfu.business.freemarker.IndexMarker;
import com.yinfu.business.freemarker.IntroduceMarker;
import com.yinfu.business.freemarker.PreferentialMarker;
import com.yinfu.business.freemarker.RestaurantMarker;
import com.yinfu.business.freemarker.TideMarker;
import com.yinfu.business.freemarker.VideoMarker;
import com.yinfu.jbase.util.PropertyUtils;
 
  
 public class DataSynUtil
 {
	 /**
	  * 设备发布
	  * @param shopId
	  * @return
	  */
	 public static boolean publish(int shopId)
	 {
		 boolean bool = false;
		 String sql = "update bp_task set is_publish=1 where router_sn in(select router_sn from bp_device where shop_id=?)";
		 Db.update(sql, new Object[]{shopId});
		 return bool;
	 }
	 
	 /**
	  * 同步数据
	  * @param shopId（0：全部）
	  * @param taskType（index_app：首页应用；index_adv：广告,index_shop:商店，video:视频,book:阅读,apk:应用,preferential:最新优惠，tide：潮机推荐，flowpack：包子铺,introduce:商铺介绍）
	  * @param keyId
	  * @param cmdType（1：增加/修改；2：删除）
	  * @return
	  */
   public static boolean addTask(Object shopId,String taskType,Object keyId,String cmdType)
   {
	   boolean bool = false;
	   
	   String sql = "select a.router_sn,ifnull(b.is_publish,0) is_publish from bp_device a left join bp_shop_page b on a.shop_id=b.shop_id ";
	   if(shopId != null){
		   if(shopId.equals("0")){
			   sql += "where a.shop_id<>?";
		   }else{
			   sql += "where a.shop_id=?";
		   }
	   }
	   List<Record> list = Db.find(sql,new Object[]{shopId});
	   for(int i=0;i<list.size();i++){
		   Record rd = list.get(i);
		   String routersn = rd.getStr("router_sn");
		   String isPublish = rd.getStr("is_publish");
		   sql = "select id,uid,router_sn,type,key_id from bp_task where status=0 and router_sn= ? and type=? and key_id=?";
		   List<Record> taskList = Db.find(sql,new Object[]{routersn,taskType,keyId});
		   if(taskList.size()>0){
			   for(int j=0;j<taskList.size();j++){
				   Record task = taskList.get(j);
				   String uid = task.getStr("uid");
				   bool = edit(shopId,routersn,taskType,keyId,cmdType,uid);
			   }
		   }else{
			   bool = add(shopId,routersn,taskType,keyId,cmdType,isPublish);
		   }
	   }
	   
	   return bool;
   }
  
   
   private static boolean edit(Object shopId,String routersn,String taskType,Object keyId,String cmdType,String uid){
	   boolean bool = false;
	   List<String> sqlList = new ArrayList<String>();
	   String sql = "insert into bp_cmd(uid,type,url,dir) values('"+uid;
	   
	   List<String> delList = new ArrayList<String>();
	   delList.add("delete from bp_cmd where type<>2 and uid='"+uid+"'");
	   Db.batch(delList, delList.size());
	   if("index_app".equals(taskType)){
		   Record rd = Db.findById("bp_app", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexApp","/storageroot/Data/mb/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexApp","/storageroot/Data/mb/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   if(addHtml(routersn,linkPc)){
				   sqlList.add(sql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//htmlPc
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_adv".equals(taskType)){
		   Record rd = Db.findById("bp_adv", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   if(addHtml(routersn,linkPc)){
				   sqlList.add(sql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//htmlPc
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_shop".equals(taskType)){
		   Record rd = Db.findById("bp_shop", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexShop","/storageroot/Data/mb/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexShop","/storageroot/Data/mb/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   if(addHtml(routersn,linkPc)){
				   sqlList.add(sql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//htmlPc
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("video".equals(taskType)){
		   Record rd = Db.findById("bp_video", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.videoImg","/storageroot/Data/mb/video/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.videoFile","/storageroot/Data/mb/video/v")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.videoImg","/storageroot/Data/mb/video/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.videoFile","/storageroot/Data/mb/video/v")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/video/video.html','"+PropertyUtils.getProperty("downdir.video","/storageroot/Data/mb/video")+"')");//html
			   //生成html
			   VideoMarker vm = VideoMarker.getInstance();
			   if(vm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("audio".equals(taskType)){
		   Record rd = Db.findById("bp_audio", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.audioImg","/storageroot/Data/mb/audio/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.audioFile","/storageroot/Data/mb/audio/m")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.audioImg","/storageroot/Data/mb/audio/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.audioFile","/storageroot/Data/mb/audio/m")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/audio/audio.html','"+PropertyUtils.getProperty("downdir.audio","/storageroot/Data/mb/audio")+"')");//html
			   //生成html
			   AudioMarker am = AudioMarker.getInstance();
			   if(am.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("book".equals(taskType)){
		   Record rd = Db.findById("bp_book", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.bookImg","/storageroot/Data/mb/book/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.bookFile","/storageroot/Data/mb/book/file")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.bookImg","/storageroot/Data/mb/book/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.bookFile","/storageroot/Data/mb/book/file")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/book/book.html','"+PropertyUtils.getProperty("downdir.book","/storageroot/Data/mb/book")+"')");//html
			   //生成html
			   BookMarker bm = BookMarker.getInstance();
			   if(bm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("game".equals(taskType)){
		   Record rd = Db.findById("bp_game", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.gameImg","/storageroot/Data/mb/games/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.gameFile","/storageroot/Data/mb/games/f")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.gameImg","/storageroot/Data/mb/games/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.gameFile","/storageroot/Data/mb/games/f")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/games/game.html','"+PropertyUtils.getProperty("downdir.game","/storageroot/Data/mb/games")+"')");//html
			   //生成html
			   GameMarker gm = GameMarker.getInstance();
			   if(gm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("apk".equals(taskType)){
		   Record rd = Db.findById("bp_apk", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.apkImg","/storageroot/Data/mb/app/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.apkFile","/storageroot/Data/mb/app/f")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.apkImg","/storageroot/Data/mb/app/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.apkFile","/storageroot/Data/mb/app/f")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/app/app.html','"+PropertyUtils.getProperty("downdir.apk","/storageroot/Data/mb/app")+"')");//html
			   //生成html
			   AppMarker am = AppMarker.getInstance();
			   if(am.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("preferential".equals(taskType)){
		   Record rd = Db.findById("bp_preferential", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/preferential.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html
			   PreferentialMarker pm = PreferentialMarker.getInstance();
			   if(pm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("tide".equals(taskType)){
		   Record rd = Db.findById("bp_tide", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/tide.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			  
			   //生成html
			   TideMarker tm = TideMarker.getInstance();
			   if(tm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("flowpack".equals(taskType)){
		   Record rd = Db.findById("bp_flow_pack", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("pic")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("pic")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/flowpack.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html
			   FlowPackMarker fm = FlowPackMarker.getInstance();
			   if(fm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("funny".equals(taskType)){
		   Record rd = Db.findById("bp_funny", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/funny.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html
			   FunnyMarker fm = FunnyMarker.getInstance();
			   if(fm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("menu".equals(taskType)){
		   Record rd = Db.findById("bp_menu", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.RestaurantImg", "/storageroot/Data/mb/restaurant/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.RestaurantImg", "/storageroot/Data/mb/restaurant/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/restaurant/index.xml";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.Restaurant", "/storageroot/Data/mb/restaurant")+"')");//html
			   }
			   //生成html 
			   RestaurantMarker rm = RestaurantMarker.getInstance();
			   if(rm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("introduce".equals(taskType)){
		   Record rd = Db.findById("bp_introduce", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("file_path")+"','"+PropertyUtils.getProperty("downdir.IntroduceFile", "/storageroot/Data/mb/introduce/f")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("file_path")+"','"+PropertyUtils.getProperty("downdir.IntroduceFile", "/storageroot/Data/mb/introduce/f")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/introduce/introduce.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.introduce", "/storageroot/Data/mb/introduce")+"')");//html
			   }
			   //生成html 
			   IntroduceMarker im = IntroduceMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_adv_bottom".equals(taskType)){
		   Record rd = Db.findById("bp_shop_group_role", keyId);
		   if(rd != null){
			   sqlList.add(sql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_adv_start".equals(taskType)){
		   Record rd = Db.findById("bp_shop_group_role", keyId);
		   if(rd != null){
			   sqlList.add(sql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   String link = "file/freemarker/html/"+shopId+"/mb/goto.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   //生成html
			   GotoMarker im = GotoMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }
	   
	   return bool;
   }
   
   
   private static boolean add(Object shopId,String routersn,String taskType,Object keyId,String cmdType,String isPublish){
	   boolean bool = false;
	   List<String> sqlList = new ArrayList<String>();
	   String uid = UUID.randomUUID().toString();
	   String sql = "insert into bp_cmd(uid,type,url,dir) values('"+uid;
	   sqlList.add("insert into bp_task(router_sn,type,key_id,create_date,uid,is_publish) values('"+routersn+"','"+taskType+"','"+keyId+"',now(),'"+uid+"',"+isPublish+")");
	   if("index_app".equals(taskType)){
		   Record rd = Db.findById("bp_app", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexApp","/storageroot/Data/mb/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexApp","/storageroot/Data/mb/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   if(addHtml(routersn,linkPc)){
				   sqlList.add(sql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//htmlPc
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_adv".equals(taskType)){
		   Record rd = Db.findById("bp_adv", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   if(addHtml(routersn,linkPc)){
				   sqlList.add(sql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//htmlPc
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_shop".equals(taskType)){
		   Record rd = Db.findById("bp_shop", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexShop","/storageroot/Data/mb/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexShop","/storageroot/Data/mb/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   if(addHtml(routersn,linkPc)){
				   sqlList.add(sql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//htmlPc
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("video".equals(taskType)){
		   Record rd = Db.findById("bp_video", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.videoImg","/storageroot/Data/mb/video/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.videoFile","/storageroot/Data/mb/video/v")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.videoImg","/storageroot/Data/mb/video/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.videoFile","/storageroot/Data/mb/video/v")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/video/video.html','"+PropertyUtils.getProperty("downdir.video","/storageroot/Data/mb/video")+"')");//html
			   //生成html 
			   VideoMarker vm = VideoMarker.getInstance();
			   if(vm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("audio".equals(taskType)){
		   Record rd = Db.findById("bp_audio", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.audioImg","/storageroot/Data/mb/audio/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.audioFile","/storageroot/Data/mb/audio/m")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.audioImg","/storageroot/Data/mb/audio/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.audioFile","/storageroot/Data/mb/audio/m")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/audio/audio.html','"+PropertyUtils.getProperty("downdir.audio","/storageroot/Data/mb/audio")+"')");//html
			   //生成html 
			   AudioMarker am = AudioMarker.getInstance();
			   if(am.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("book".equals(taskType)){
		   Record rd = Db.findById("bp_book", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.bookImg","/storageroot/Data/mb/book/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.bookFile","/storageroot/Data/mb/book/file")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.bookImg","/storageroot/Data/mb/book/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.bookFile","/storageroot/Data/mb/book/file")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/book/book.html','"+PropertyUtils.getProperty("downdir.book","/storageroot/Data/mb/book")+"')");//html
			   //生成html 
			   BookMarker bm = BookMarker.getInstance();
			   if(bm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("game".equals(taskType)){
		   Record rd = Db.findById("bp_game", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.gameImg","/storageroot/Data/mb/games/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.gameFile","/storageroot/Data/mb/games/f")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.gameImg","/storageroot/Data/mb/games/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.gameFile","/storageroot/Data/mb/games/f")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/games/game.html','"+PropertyUtils.getProperty("downdir.game","/storageroot/Data/mb/games")+"')");//html
			   //生成html 
			   GameMarker gm = GameMarker.getInstance();
			   if(gm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("apk".equals(taskType)){
		   Record rd = Db.findById("bp_apk", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.apkImg","/storageroot/Data/mb/app/logo")+"')");//图片
				   sqlList.add(sql+"',1,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.apkFile","/storageroot/Data/mb/app/f")+"')");//文件
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.apkImg","/storageroot/Data/mb/app/logo")+"')");//图片
				   sqlList.add(sql+"',2,'"+rd.getStr("link")+"','"+PropertyUtils.getProperty("downdir.apkFile","/storageroot/Data/mb/app/f")+"')");//文件
			   }
			   sqlList.add(sql+"',1,'file/freemarker/html/"+shopId+"/mb/app/app.html','"+PropertyUtils.getProperty("downdir.apk","/storageroot/Data/mb/app")+"')");//html
			   //生成html 
			   AppMarker am = AppMarker.getInstance();
			   if(am.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("preferential".equals(taskType)){
		   Record rd = Db.findById("bp_preferential", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/preferential.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html 
			   PreferentialMarker pm = PreferentialMarker.getInstance();
			   if(pm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("tide".equals(taskType)){
		   Record rd = Db.findById("bp_tide", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/tide.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html 
			   TideMarker tm = TideMarker.getInstance();
			   if(tm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("flowpack".equals(taskType)){
		   Record rd = Db.findById("bp_flow_pack", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("pic")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("pic")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/flowpack.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html 
			   FlowPackMarker fm = FlowPackMarker.getInstance();
			   if(fm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("funny".equals(taskType)){
		   Record rd = Db.findById("bp_funny", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/mall/funny.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
			   }
			   //生成html 
			   FunnyMarker fm = FunnyMarker.getInstance();
			   if(fm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("menu".equals(taskType)){
		   Record rd = Db.findById("bp_menu", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.RestaurantImg", "/storageroot/Data/mb/restaurant/logo")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.RestaurantImg", "/storageroot/Data/mb/restaurant/logo")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/restaurant/index.xml";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.Restaurant", "/storageroot/Data/mb/restaurant")+"')");//html
			   }
			   //生成html 
			   RestaurantMarker rm = RestaurantMarker.getInstance();
			   if(rm.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("introduce".equals(taskType)){
		   Record rd = Db.findById("bp_introduce", keyId);
		   if(rd != null){
			   if("1".equals(cmdType)){//增加、修改
				   sqlList.add(sql+"',1,'"+rd.getStr("file_path")+"','"+PropertyUtils.getProperty("downdir.IntroduceFile", "/storageroot/Data/mb/introduce/f")+"')");//图片
			   }else if("2".equals(cmdType)){//删除
				   sqlList.add(sql+"',2,'"+rd.getStr("file_path")+"','"+PropertyUtils.getProperty("downdir.IntroduceFile", "/storageroot/Data/mb/introduce/f")+"')");//图片
			   }
			   String link = "file/freemarker/html/"+shopId+"/mb/introduce/introduce.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.introduce", "/storageroot/Data/mb/introduce")+"')");//html
			   }
			   //生成html 
			   IntroduceMarker im = IntroduceMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_adv_bottom".equals(taskType)){
		   Record rd = Db.findById("bp_shop_group_role", keyId);
		   if(rd != null){
			   sqlList.add(sql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   String link = "file/freemarker/html/"+shopId+"/mb/index.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   //生成html
			   IndexMarker im = IndexMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }else if("index_adv_start".equals(taskType)){
		   Record rd = Db.findById("bp_shop_group_role", keyId);
		   if(rd != null){
			   sqlList.add(sql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
			   String link = "file/freemarker/html/"+shopId+"/mb/goto.html";
			   if(addHtml(routersn,link)){
				   sqlList.add(sql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
			   }
			   //生成html
			   GotoMarker im = GotoMarker.getInstance();
			   if(im.createHtml(shopId)){
				   bool = Db.batch(sqlList, sqlList.size()).length>0;
			   }
		   }
	   }
	   return bool;
   }
   
   private static boolean addHtml(String routersn,String link){
	   boolean bool = false;
	   String getSql = "select 1 from bp_cmd a left join bp_task b on a.uid=b.uid where b.router_sn=? and a.status=0 and a.url=?";
	   Record rec = Db.findFirst(getSql,new Object[]{routersn,link});
	   if(rec == null){
		   bool = true;
	   }
	   return bool;
   }
   
   
   public static void main(String[] src){
	   
	   
	   //f19125d8-c92f-4507-b219-d4ecc9dc35ca
	   //d2907b02-75d4-4aa8-82d9-b6beaad38a1c
   }
 }

