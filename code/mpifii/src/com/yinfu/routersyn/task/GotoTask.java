package com.yinfu.routersyn.task;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.marker.GotoMarker;
import com.yinfu.routersyn.util.SynUtils;

/**
 * 类中不存在数据库的增删改操作
 * @author l
 *
 */
public class GotoTask extends BaseTask {
	private static Logger logger = Logger.getLogger(GotoTask.class);
	public static String marker = "goto";//marker字段放到task中，统一使用
	private static String THIS_APP_FOLDER = "mb";
	public static String IMAGE_FOLDER = THIS_APP_FOLDER+"/logo";
	private String htmlFolder;
	private String imageFolder;
	
	public GotoTask(Object shopId,Record taskInfo,List<Record> publishDevices) {
		super(taskInfo,shopId,publishDevices);
//		this.shopId = shopId;
		init();
	}
	
	public GotoTask(Object shopId,String baseFolder) {
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
		return copyFile() && GotoMarker.execute(shopId, htmlFolder);//生成html;
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
    		BaseTask index = new GotoTask(shopId,taskInfo,publishDevices);
        	return index.execute(sqls, routerDeleteRes);
    	}else{
    		return new HashMap<String,List<File>>();
    	}
	}
	
	private boolean copyFile(){
		//复制基础样式文件及图片文件
		copyBaseData(THIS_APP_FOLDER.replaceAll("/", File.separator+File.separator)+File.separator+"index",new File(htmlFolder));
		//复制数据文件
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct batr.res_url img ");
		sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id =? and basp.adv_type='adv_start' and basp.id=bas.adv_spaces) ");
		sql.append("join bp_adv_type bat on (basp.id=bat.adv_spaces) ");
		sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
		sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bac.id) ");
    	Record rd = Db.findFirst(sql.toString(),new Object[]{shopId});
    	if(null != rd){
    		File iconFile = new File(SynUtils.getResBaseFloder()+File.separator+(rd.getStr("img").replaceAll("/", File.separator+File.separator)));
			try {
				if(iconFile.exists()){
					FileUtils.copyFileToDirectory(iconFile, new File(imageFolder));
//					logger.info("复制了goto文件："+iconFile.getAbsolutePath());
				}else{
					logger.warn("goto文件不存在！"+iconFile.getAbsolutePath());
				}
			} catch (IOException e) {//这里出现异常任务可继续
				e.printStackTrace();
				logger.warn("复制goto文件异常！"+iconFile.getAbsolutePath(), e);
			}
    	}
		return true;
	}
	
}
