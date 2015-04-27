package com.yinfu.routersyn.task;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;
import com.yinfu.routersyn.marker.IndexMarker;
import com.yinfu.routersyn.util.SynUtils;

/**
 * 类中不存在数据库的增删改操作
 * @author l
 *
 */
public class IndexTask extends BaseTask {
	private static Logger logger = Logger.getLogger(IndexTask.class);
	public static String marker = "index";//marker字段放到task中，统一使用
	private static String THIS_APP_FOLDER = "mb";
	public static String IMAGE_FOLDER = THIS_APP_FOLDER + "/logo";
	private String htmlFolder;
	private String imageFolder;
	
	public IndexTask(Object shopId,Record taskInfo,List<Record> publishDevices) {
		super(taskInfo,shopId,publishDevices);
//		this.shopId = shopId;
		init();
	}
	public IndexTask(Object shopId,String baseFolder) {
		super(baseFolder,shopId);
//		this.shopId = shopId;
		init();
	}
	
	private void init(){
		htmlFolder = baseFolder + File.separator + "mb";
		imageFolder = baseFolder + File.separator + IMAGE_FOLDER.replaceAll("/", File.separator+File.separator);
		File file = new File(imageFolder);
		if(!file.exists()){
			file.mkdirs();
		}
	}

	@Override
	protected boolean copyRes(List<Record> otherSynTask){
		//复制基础样式文件及图片文件
		copyBaseData(THIS_APP_FOLDER.replaceAll("/", File.separator+File.separator)+File.separator+"index",new File(htmlFolder));
		//复制数据文件
//		bottomAdv();
		bannerAdv();
		app();
		shopInfo();
		return IndexMarker.execute(shopId, htmlFolder);//生成html;
	} 
	
	/**
	 * 
	 * @param shopId
	 * @param taskType
	 * 				index_adv_banner	修改了主页banner图广告
	 * 				index_app			增加或者删除了app
	 * 				index_shop			修改了商铺信息
	 * @param sqls				该任务涉及到的数据库更改的sql语句
	 * @param taskDesc			该同步任务的描述，如：添加了应用【搞笑段子】、删除了应用【一键认证】、修改了商铺信息、修改了广告图等等
	 * @param routerDeleteRes	该同步任务完成后需要删除的盒子里的文件
	 * @return 					如果该方法执行失败则返回null，如果执行成功则返回需要删除的资源：包含两种资源，一种为调用方执行成功后需删除的文件，另一种是执行失败后需要删除的资源
	 */
	public static Map<String,List<File>> synRes(Object shopId,List<String> sqls,Record taskInfo,
			List<String> routerDeleteRes,List<Record> publishDevices){
    	if(SynUtils.checkShopPublished(shopId)){
    		taskInfo.set("task_type",marker);
    		IndexTask index = new IndexTask(shopId,taskInfo,publishDevices);
    		return index.execute(sqls, routerDeleteRes);
    	}else{
    		return new HashMap<String,List<File>>();
    	}
	}
	
	private void bannerAdv(){
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct batr.res_url img ");
		sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id =? and basp.adv_type='adv' and basp.id=bas.adv_spaces) ");
		sql.append("join bp_adv_type bat on (bat.template_id=? and basp.id=bat.adv_spaces) ");
		sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
		sql.append("join bp_adv_type_res batr on (batr.res_url is not null and batr.adv_type_id=bat.id and batr.content_id=bac.id) ");
		List<Record> icons = Db.find(sql.toString(), new Object[]{shopId,PageUtil.getTemplateId(shopId)});
		Iterator<Record> ite = icons.iterator();
		while(ite.hasNext()){
			Record icon = ite.next();
			File iconFile = new File(SynUtils.getResBaseFloder()+File.separator+(icon.getStr("img").replaceAll("/", File.separator+File.separator)));
			try {
				if(iconFile.exists()){
					FileUtils.copyFileToDirectory(iconFile, new File(imageFolder));
//					logger.info("复制了广告图："+iconFile.getAbsolutePath());
				}else{
					logger.warn("广告图不存在！"+iconFile.getAbsolutePath());
				}
			} catch (IOException e) {//这里出现异常任务可继续
				e.printStackTrace();
				logger.warn("复制广告图异常！"+iconFile.getAbsolutePath(), e);
			}
		}
	}
	
//	private void bottomAdv(){
//		StringBuffer sql = new StringBuffer();
//    	sql.append("select sgr.image ");
//    	sql.append("from bp_shop s join bp_shop_group_role sgr on (s.id=? and s.group_id=sgr.shop_group_id) ");
//    	sql.append("join bp_adv_type bat on (bat.adv_type='adv_bottom' and sgr.adv_type_id=bat.id)");
//    	Record rd = Db.findFirst(sql.toString(),new Object[]{shopId});
//    	if(null != rd){
//    		File iconFile = new File(SynUtils.getResBaseFloder()+File.separator+(rd.getStr("image").replaceAll("/", File.separator+File.separator)));
//    		try {
//				if(iconFile.exists()){
//					FileUtils.copyFileToDirectory(iconFile, new File(imageFolder));
//					logger.info("复制了adv_bottom图标："+iconFile.getAbsolutePath());
//				}else{
//					logger.warn("adv_bottom图标不存在！"+iconFile.getAbsolutePath());
//				}
//			} catch (IOException e) {//这里出现异常任务可继续
//				e.printStackTrace();
//				logger.warn("复制adv_bottom图标异常！"+iconFile.getAbsolutePath(), e);
//			}
//    	}
//	}
	
	/**
	 * 不管是安装还是删除应用，这里都会同步的资源文件包含：
	 * 		已安装的app的图标；
	 * 		重新生成的index.html
	 * @return
	 */
	private void app(){
		StringBuffer sql = new StringBuffer();
//		sql.append("select distinct a.icon ");
//		sql.append("from bp_shop_page_app spa join bp_shop_page sp on (sp.shop_id=? and sp.id = spa.page_id) ");
//		sql.append("join bp_app a on (a.delete_date is null and spa.app_id=a.id) ");
		
		sql.append("select distinct ifnull(sac.icon,ifnull(tai.icon,a.icon)) icon ");
		sql.append("from bp_app a ");
		sql.append("left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
		sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
		sql.append("left join bp_temp_app_icon tai on (sp.template_id=tai.template_id and a.id=tai.app_id) ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sp.template_id=sac.template_id and sac.app_id=a.id) ");
		sql.append("where spa.id is not null and a.template_id=? ");
		List<Record> icons = Db.find(sql.toString(), new Object[]{PageUtil.getPageIdByShopId(shopId),shopId,PageUtil.getTemplateId(shopId)});
		Iterator<Record> ite = icons.iterator();
		while(ite.hasNext()){
			Record icon = ite.next();
			File iconFile = new File(SynUtils.getResBaseFloder()+File.separator+(icon.getStr("icon").replaceAll("/", File.separator+File.separator)));
			try {
				if(iconFile.exists()){
					FileUtils.copyFileToDirectory(iconFile, new File(imageFolder));
//					logger.info("复制了app图标："+iconFile.getAbsolutePath());
				}else{
					logger.warn("app图标不存在！"+iconFile.getAbsolutePath());
				}
			} catch (IOException e) {//这里出现异常任务可继续
				e.printStackTrace();
				logger.warn("复制app图标异常！"+iconFile.getAbsolutePath(), e);
			}
		}
	}
	
	private void shopInfo(){
		Record shopIcon = Db.findFirst("select ifnull(icon,'') icon from bp_shop where id=?", new Object[]{shopId});
		if(null != shopIcon){
			String icon = shopIcon.getStr("icon");
			if(StringUtils.isBlank(icon)){
				icon = "images/business/userPic/morentouxiang.png";
			}
			File iconFile = new File(SynUtils.getResBaseFloder()+File.separator+(icon.replaceAll("/", File.separator+File.separator)));
			try {
				if(iconFile.exists()){
					FileUtils.copyFileToDirectory(iconFile, new File(imageFolder));
//					logger.info("复制了商铺图标："+iconFile.getAbsolutePath());
				}else{
					logger.warn("商铺图标不存在！"+iconFile.getAbsolutePath());
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("复制商铺图标异常！"+iconFile.getAbsolutePath(), e);
			}
		}
	}
	
}
