package com.yinfu.routersyn.task;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.marker.BookMarker;
import com.yinfu.routersyn.util.SynUtils;

/**
 * 类中不存在数据库的增删改操作
 * @author l
 *
 */
public class BookTask extends BaseTask {
	private static Logger logger = Logger.getLogger(BookTask.class);
	public static String marker = "book";//marker字段放到task中，统一使用
	private static String THIS_APP_FOLDER = "mb/book";
	public static String IMAGE_FOLDER = THIS_APP_FOLDER+"/logo";
	public static String FILE_FOLDER = THIS_APP_FOLDER+"/file";
	private String htmlFolder;
	private String imageFolder;
	private Object resId;
	
	public BookTask(Object shopId,Object resId,Record taskInfo,List<Record> publishDevices) {//用于发布、删除或者修改
		super(taskInfo,shopId,publishDevices);
//		this.shopId = shopId;
		this.resId = resId;
		init();
//		initPublishDevices(publishDevices);
	}
	
	public BookTask(Object shopId,String baseFolder) {//仅用于复制资源文件，如图片和html，用于发布应用或者新绑定盒子
		super(baseFolder,shopId);
//		this.shopId = shopId;
		init();
	}
	
	private void init(){
		htmlFolder = baseFolder + File.separator + "mb" + File.separator + "book";
		imageFolder = baseFolder + File.separator + IMAGE_FOLDER.replaceAll("/", File.separator+File.separator);
		File file = new File(imageFolder);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
//	private void initPublishDevices(List<Record> publishDevices){
//		if(null == publishDevices){
//			synDevices = Db.find("select distinct router_sn from bp_device");
//		}else{
//			synDevices = publishDevices;
//		}
//	}

	@Override
	protected boolean copyRes(List<Record> otherSynTask){
		return copyFile(otherSynTask) && BookMarker.execute(shopId,htmlFolder);//生成html;
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
	public static Map<String,List<File>> synRes(Object shopId,Object resId,List<String> sqls,Record taskInfo,
			List<String> routerDeleteRes,List<Record> publishDevices){
		taskInfo.set("task_type", marker);
		BaseTask index = new BookTask(shopId,resId,taskInfo,publishDevices);
    	return index.execute(sqls, routerDeleteRes);
	}
	
	private boolean copyFile(List<Record> otherSynTask){
		copyBaseData(THIS_APP_FOLDER.replaceAll("/", File.separator+File.separator),new File(htmlFolder).getParentFile());
		StringBuffer sqls = new StringBuffer();
		if(null == resId){//整体图片资源同步
			sqls.append("select img img_path,link file_path,delete_date,md5,name from bp_book where delete_date is null and status=1 ");
		}else if(null != resId && !"-1".equals(resId.toString())){//单个图片文件同步，如果是添加资源则复制，删除资源则更新之前添加该任务的进度，这里如果是修改资源的话，没修改文件则resId应该为-1，修改了则resId应该为实际资源id
			sqls.append("select img img_path,link file_path,delete_date,md5,name from bp_book where id = '"+resId+"'");//这里要确保删除资源文件为软删除，不然这个记录也不会被查找出来
		}
		if(sqls.length() > 0){
			List<Record> fileRecs = Db.find(sqls.toString());
			Iterator<Record> ite = fileRecs.iterator();
			while(ite.hasNext()){
				Record fileRec = ite.next();
				if(null == fileRec.get("delete_date")){//添加的资源
					File file = new File(SynUtils.getResBaseFloder()+File.separator+(fileRec.getStr("img_path").replaceAll("/", File.separator+File.separator)));
					try {
						if(file.exists()){
							FileUtils.copyFileToDirectory(file, new File(imageFolder));
//							logger.info("复制了book图片文件："+file.getAbsolutePath());
						}else{
							logger.warn("book图片文件不存在！"+file.getAbsolutePath());
						}
					} catch (IOException e) {//这里出现异常任务可继续
						e.printStackTrace();
						logger.warn("复制book图片文件异常！"+file.getAbsolutePath(), e);
					}
					otherSynTask.add(new Record().set("operate_type", "add").set("res_url", fileRec.get("file_path"))
							.set("task_type", marker+"_").set("local_path", FILE_FOLDER).set("md5", fileRec.get("md5"))
							.set("task_desc", "同步电子书【"+fileRec.get("name")+"】"));
				}else{//删除的资源
					otherSynTask.add(new Record().set("operate_type", "delete").set("res_url", fileRec.get("file_path")));
				}
			}
		}
		return true;
	}
	
}
