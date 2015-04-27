package com.yinfu.jbase.util.remote;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
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
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.util.PropertyUtils;


public class SynAllUtil {
	private static final String FILE_EXTENSION = ".tar.gz";
    private static SynAllUtil m_instance = null;
    private List<String> sqlList = null;
    private String shopId = "0";
    private String isPublish = "0";
    private String sn = "0";
    private String cmdSql = "";
    
    
	public SynAllUtil() {
		
	}
	
    public synchronized static SynAllUtil getInstance() {
		if (m_instance == null) {
			m_instance = new SynAllUtil();
		}
		return m_instance;
	}
    
    /**
     * 根据sn同步全部最新数据
     * @param sn
     * @return
     */
    public boolean synAllData(String sn){
    	boolean bool = false;
		try {
			//不再打包了
//			PackageAll packageAll = new PackageAll();
//			packageAll.start();//采用多线程打包
			
			String sql = "select a.shop_id,ifnull(b.is_publish,'0') is_publish from bp_device a left join bp_shop_page b on a.shop_id=b.shop_id where a.router_sn=?";
			Record rd = Db.findFirst(sql, new Object[]{sn});
			if(rd != null) {
				this.sn = sn;
				this.shopId = rd.getInt("shop_id")+"";
				this.isPublish = rd.getStr("is_publish");
				this.sqlList = new ArrayList<String>();
				String uid = UUID.randomUUID().toString();
				this.cmdSql = "insert into bp_cmd(uid,type,url,dir) values('"+uid;
				sqlList.add("insert into bp_task(router_sn,type,key_id,create_date,uid,is_publish) values('"+sn+"','synAll','"+sn+"',now(),'"+uid+"',"+isPublish+")");
			
				StringBuffer fSql = new StringBuffer();
				fSql.append("select a.app_id,c.name,ifnull(c.marker,'no') marker ");
				fSql.append("from bp_shop_page_app a ");
				fSql.append("left join bp_shop_page b ");
				fSql.append("on a.page_id=b.id ");
				fSql.append("left join bp_app c ");
				fSql.append("on a.app_id=c.id ");
				fSql.append("where b.shop_id = "+this.shopId);
				List<Record> list = Db.find(fSql.toString());
				
				synShop();
				if(list.size()>0){
					synAdv();
					synOtherAdv();
					synApp();
					String link = "file/freemarker/html/"+shopId+"/mb/index.html";
					File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
					if(!thisFile.exists()){
						IndexMarker im = IndexMarker.getInstance();
						im.createHtml(shopId);
					}
					sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
					String linkPc = "file/freemarker/html/"+shopId+"/pc/indexPc.html";
					sqlList.add(this.cmdSql+"',1,'"+linkPc+"','"+PropertyUtils.getProperty("downdir.indexPc","/storageroot/Data/pc")+"')");//html
				}
				
//				synVideo();
//				synAudio();
//				synGames();
//				synBook();
//				synApk();
//				synSystemAppData("video","bp_video","icon","logo","link","v","video/video.html");
//				synSystemAppData("audio","bp_audio","icon","logo","link","m","audio/audio.html");
//				synSystemAppData("app","bp_apk","icon","logo","link","f","app/app.html");
//				synSystemAppData("book","bp_book","img","logo","link","file","book/book.html");
//				synSystemAppData("games","bp_game","icon","logo","link","f","game/game.html");
				
				for(Record appRd : list){
//					if("video".equals(appRd.getStr("marker"))){
						
//					}else if("audio".equals(appRd.getStr("marker"))){
//					}else if("game".equals(appRd.getStr("marker"))){
//					}else if("book".equals(appRd.getStr("marker"))){
//					}else if("apk".equals(appRd.getStr("marker"))){
//						
//					}else 
						
					if("preferential".equals(appRd.getStr("marker"))){
						synPreferential();
					}else if("tide".equals(appRd.getStr("marker"))){
						synTide();
					}else if("flowpack".equals(appRd.getStr("marker"))){
						synFlowPack();
					}else if("funny".equals(appRd.getStr("marker"))){
						synFunny();
					}else if("introduce".equals(appRd.getStr("marker"))){
						synIntroduce();
					}else if("menu".equals(appRd.getStr("marker"))){
						synMenu();
					}
				}
				DbExt.batch(sqlList);
				bool = PageUtil.SynRouterAllLog(this.sn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("一键同步出错："+e);
			bool = false;
		}
		return bool;
    }
    
    private void synAdv(){
    	StringBuffer sql = new StringBuffer();
		sql.append("select distinct bac.img image ");
		sql.append("from bp_adv_shop bas ");
		sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
		sql.append("join bp_adv_type bat on (bat.template_id=? and bas.adv_type_id=bat.id) ");
		sql.append("join bp_adv_spaces basp on (basp.adv_type='adv' and basp.id=bat.adv_spaces) ");
		sql.append("where bas.shop_id =? and bac.img is not null ");
		List<Record> list = Db.find(sql.toString(), new Object[]{PageUtil.getTemplateId(shopId),shopId});
//    	String sql = "select image from bp_adv where shop_id=? and template_id=? and delete_date is null";
//    	List<Record> list = Db.find(sql, new Object[]{this.shopId,PageUtil.getTemplateId(shopId)});
		for(Record rd : list) {
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("image")+"','"+PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo")+"')");//图片
		}
    }
    
    private void synOtherAdv(){//同步其他广告，如底部广告、过渡页广告
    	//同步图片
    	StringBuffer sql = new StringBuffer();
    	sql.append("select distinct basp.adv_type,bac.img image ");
    	sql.append("from bp_adv_shop bas ");
    	sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
    	sql.append("join bp_adv_type bat on (bas.adv_type_id=bat.id) ");
    	sql.append("join bp_adv_spaces basp on (basp.adv_type='adv_start' and basp.id=bat.adv_spaces) ");
    	sql.append("where bas.shop_id =? ");
		List<Record> list = Db.find(sql.toString(),new Object[]{shopId});
    	
    	boolean synGotoHtml = false;
    	for(Record rd : list) {
    		String image = rd.getStr("image");
    		String downloadDir = PropertyUtils.getProperty("downdir.indexAdv","/storageroot/Data/mb/logo");
			if("adv_start".equals(rd.getStr("adv_type"))){//如果过渡页有设置则添加过渡页html的下载任务
				if(StringUtils.isBlank(image)){
					image = "images/business/adv/ad.png";
					downloadDir = "/storageroot/Data/mb/index/img";
				}
				synGotoHtml = true;
			}else if("adv_bottom".equals(rd.getStr("adv_type"))){
				if(StringUtils.isBlank(image)){
					image = "images/business/adv/transition.png";
					downloadDir = "/storageroot/Data/mb/index/img";
				}
			}
			sqlList.add(this.cmdSql+"',1,'"+image+"','"+downloadDir+"')");//图片
		}
    	//同步html
    	if(synGotoHtml){
    		String link = "file/freemarker/html/"+shopId+"/mb/goto.html";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				GotoMarker marker = GotoMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.index","/storageroot/Data/mb")+"')");//html
    	}
    }
    
    private void synShop(){
    	String sql = "select icon from bp_shop where id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			if(!"".equals(rd.getStr("icon")) && rd.getStr("icon") != null){
				sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexShop","/storageroot/Data/mb/logo")+"')");//图片
			}
		}
    }
    
    private void synApp(){
//    	String sql = "select c.icon from bp_shop_page_app a left join bp_shop_page b on a.page_id=b.id left join bp_app c on a.app_id=c.id where b.shop_id=?";
    	StringBuffer sql = new StringBuffer();
    	sql.append("select distinct ifnull(sac.icon,ifnull(tai.icon,a.icon)) icon ");
    	sql.append("from bp_app a ");
    	sql.append("left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
    	sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
    	sql.append("left join bp_temp_app_icon tai on (sp.template_id=tai.template_id and a.id=tai.app_id) ");
    	sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sp.template_id=sac.template_id and sac.app_id=a.id) ");
    	sql.append("where spa.id is not null ");
    	List<Record> list = Db.find(sql.toString(), new Object[]{PageUtil.getPageIdByShopId(shopId),shopId});
		for(Record rd : list) {
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.indexApp","/storageroot/Data/mb/logo")+"')");//图片
		}
    }
    
    private void synPreferential(){
    	boolean flag = false;
    	String sql = "select img from bp_preferential where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+shopId+"/mb/mall/preferential.html";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				PreferentialMarker marker = PreferentialMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
    }
    
    private void synFlowPack(){
    	boolean flag = false;
    	String sql = "select pic from bp_flow_pack where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			 sqlList.add(this.cmdSql+"',1,'"+rd.getStr("pic")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+shopId+"/mb/mall/flowpack.html";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				FlowPackMarker marker = FlowPackMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
    }
    
    private void synTide(){
    	boolean flag = false;
    	String sql = "select img from bp_tide where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+shopId+"/mb/mall/tide.html";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				TideMarker marker = TideMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
	}
    
    private void synFunny(){
    	boolean flag = false;
    	String sql = "select img from bp_funny where shop_id=? and delete_date is null";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("img")+"','"+PropertyUtils.getProperty("downdir.mallImg","/storageroot/Data/mb/mall/images")+"')");//图片
		}
		
		if(flag){
			String link = "file/freemarker/html/"+this.shopId+"/mb/mall/funny.html";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				FunnyMarker marker = FunnyMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.mall","/storageroot/Data/mb/mall")+"')");//html
		}
	}
    
    private void synIntroduce(){
    	boolean flag = false;
    	String sql = "select file_path from bp_introduce where shop_id=?";
    	List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("file_path")+"','"+PropertyUtils.getProperty("downdir.introduceFile","/storageroot/Data/mb/introduce/f")+"')");//图片
		}
		if(flag){
			String link = "file/freemarker/html/"+this.shopId+"/mb/introduce/introduce.html";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				IntroduceMarker marker = IntroduceMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.introduce","/storageroot/Data/mb/introduce")+"')");//html
		}
    }
    
	private void synMenu(){
		boolean flag = false;
		String sql = "select icon from bp_menu where shopId=? and delete_date is null";
		List<Record> list = Db.find(sql, new Object[]{this.shopId});
		for(Record rd : list) {
			flag = true;
			sqlList.add(this.cmdSql+"',1,'"+rd.getStr("icon")+"','"+PropertyUtils.getProperty("downdir.menuFile","/storageroot/Data/mb/restaurant/logo")+"')");//图片
		}
		if(flag){
			String link = "file/freemarker/html/"+this.shopId+"/mb/restaurant/index.xml";
			File thisFile = new File(PropertyUtils.getProperty("server.path")+File.separator+link.replaceAll("/",File.separator+File.separator));
			if(!thisFile.exists()){
				RestaurantMarker marker = RestaurantMarker.getInstance();
				marker.createHtml(shopId);
			}
			sqlList.add(this.cmdSql+"',1,'"+link+"','"+PropertyUtils.getProperty("downdir.restaurant","/storageroot/Data/mb/restaurant")+"')");//html
		}
	}
    
    public static void main(String[] args) {
    	SynAllUtil aa = new SynAllUtil();
    	List<File> forders = new ArrayList<File>();
    	forders.add(new File("E:\\aa"));
    	forders.add(new File("E:\\test"));
    	List<String> names = new ArrayList<String>();
//    	aa.getExistResourceName(forders,names);
//    	System.err.println("------------1");
//    	System.err.println(Arrays.toString(names.toArray()));
    	
    	names.add("video.tar.gz");
    	names.add("games.tar.gz");
    	names.add("Data_ynsqt.tar.gz");
    	names.add("Data_pifii.tar.gz");
    	names.add("book.tar.gz");
    	names.add("audio.tar.gz");
    	names.add("app.tar.gz");
    	
    	names.add("app.html");
    	names.add("360安全卫士.png");
    	
    	names.add("book_1.tar.gz");
	}
    
    /**
     * 打包每一种应用
     * 同步盒子基础应用数据
     * @param serverAppDirName		路由器中应用的文件夹名称
     * @param tableName				应用对应的表名称
     * @param imgFieldName			表中图片的字段名
     * @param ImgRouterForderName	路由器中该app图像放的文件夹名称
     * @param fileFieldName			表中文件的字段名
     * @param fileRouterForderName	路由器中该app文件放的文件夹名称
     * @param appIndexHtmlPath		商户平台生产的app页面的相对路径
     * @throws IOException
     * 
     * 
     * 修优化：	1.当应用删除后是不会重新生成压缩包的
     * 			2.每个基础应用的create_date字段必须在该应用数据更新后更新该字段值
     */
    private void synSystemAppData(String serverAppDirName,String tableName,String imgFieldName,String ImgRouterForderName,
    		String fileFieldName,String fileRouterForderName,String appIndexHtmlPath) throws IOException{
    	String baseDir = PropertyUtils.getProperty("server.path")+File.separator;//商户平台的根目录
    	String appInitDir = "file" + File.separator+"init"+File.separator + serverAppDirName;//app初始化压缩包之前的文件夹
    	String appIndexHtmlBasePath = "file"+File.separator+"freemarker"+File.separator+"html"+File.separator+"0"+File.separator+"mb"+File.separator;//应用生成html的位置
    	File existGzip = new File(baseDir+appInitDir+FILE_EXTENSION);
    	if(existGzip.exists()){
    		String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(existGzip.lastModified()));
    		List<Record> data = Db.find("select id from "+tableName+" where create_date > ?", new Object[]{createDate});
    		if(data.size() == 0){//如果文件没有更新，则使用以前打过包的文件
    			sqlList.add(this.cmdSql+"',1,'"+appInitDir.replaceAll(File.separator+File.separator, "/")+FILE_EXTENSION+"','"+PropertyUtils.getProperty("downdir.index")+"')");//book.tar.gz
    			return;
    		}
    	}
    	FileUtils.deleteDirectory(new File(baseDir+appInitDir));//删除以前的文件夹
    	List<Record> list = Db.find("select "+imgFieldName+" img,"+fileFieldName+" file from "+tableName+" where status=1 ");
		for(Record rd : list) {
			File fileDestDir = new File(baseDir+appInitDir+File.separator+fileRouterForderName);
			File fileSrcFile = new File(baseDir+(rd.getStr("file").replaceAll("/",File.separator+File.separator)));
			FileUtils.copyFileToDirectory(fileSrcFile, fileDestDir);
			
			File imgDestDir = new File(baseDir+appInitDir+File.separator+ImgRouterForderName);
			File imgSrcFile = new File(baseDir+(rd.getStr("img").replaceAll("/",File.separator+File.separator)));
			FileUtils.copyFileToDirectory(imgSrcFile, imgDestDir);
		}
		String appIndexHtmlPathAll = baseDir+appIndexHtmlBasePath+(appIndexHtmlPath.replaceAll("/", File.separator+File.separator));
		File appIndexHtmlFile = new File(appIndexHtmlPathAll);
		if(!appIndexHtmlFile.exists()){
			if(generHtml(tableName)){
				appIndexHtmlFile = new File(appIndexHtmlPathAll);
			}
		}
		if(appIndexHtmlFile.exists()){
			FileUtils.copyFileToDirectory(appIndexHtmlFile, new File(baseDir+appInitDir));
		}
		if(existGzip.exists()){//删除以前的打包文件
			existGzip.delete();
		}
		TarGzipCompress.WriteToTarGzip(baseDir+appInitDir);//生成新的打包文件
		sqlList.add(this.cmdSql+"',1,'"+(baseDir+appInitDir).replaceAll(File.separator+File.separator, "/")+FILE_EXTENSION+"','"+PropertyUtils.getProperty("downdir.index")+"')");
    }
    private boolean generHtml(String appMark){
    	if("bp_video".equals(appMark)){
    		VideoMarker vm = VideoMarker.getInstance();
    		return vm.createHtml("0");
    	}else if("bp_audio".equals(appMark)){
    		AudioMarker am = AudioMarker.getInstance();
    		return am.createHtml("0");
    	}else if("bp_apk".equals(appMark)){
    		AppMarker am = AppMarker.getInstance();
    		return am.createHtml("0");
    	}else if("bp_book".equals(appMark)){
    		BookMarker bm = BookMarker.getInstance();
    		return bm.createHtml("0");
    	}else if("bp_game".equals(appMark)){
    		GameMarker gm = GameMarker.getInstance();
    		return gm.createHtml("0");
    	}
    	return false;
    }
    
}
