package com.yinfu.routersyn.task;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.util.TarGzipUtil;

/**
 * 类中不存在数据库的增删改操作
 * @author l
 *
 */
public class DataTask extends BaseTask {
//	private static Logger logger = Logger.getLogger(DataTask.class);
	public static String marker = "data";//marker字段放到task中，统一使用
	private File latestData;
	
	public DataTask(Record taskInfo,List<Record> publishDevices,File latestData) {//用于发布、删除或者修改
		super(taskInfo,null,publishDevices);
		this.latestData = latestData;
//		initPublishDevices(publishDevices);
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
		return TarGzipUtil.unTarGzipFile(latestData.getAbsolutePath(), new File(baseFolder).getParent());
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
	public static Map<String,List<File>> synRes(List<String> sqls,Record taskInfo,
			List<String> routerDeleteRes,List<Record> publishDevices,File latestData){
		BaseTask index = new DataTask(taskInfo,publishDevices,latestData);
    	return index.execute(sqls, routerDeleteRes);
	}
	
}
