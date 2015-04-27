package com.yinfu.routersyn.task;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.util.SynUtils;

/**
 * 应用场景：
 * 		1.商铺发布portal页面
 * 		2.绑定盒子到已发布portal页面的商铺
 * @author l
 *
 */
public class SynAllTask extends BaseTask {
	public static String marker = "publish";//marker字段放到task中，统一使用
//	private Object shopId;
	
	public SynAllTask(Object shopId,Record taskInfo,List<Record> publishDevices) {
		super(taskInfo,shopId,publishDevices);
//		this.shopId = shopId;
//		initPublishDevices(publishDevices);
	}
	
//	private void initPublishDevices(List<Record> publishDevices){
//		if(null == publishDevices){
//			synDevices = Db.find("select distinct router_sn from bp_device where shop_id=? ",new Object[]{shopId});
//		}else{
//			synDevices = publishDevices;
//		}
//	}
	
	@Override
	protected boolean copyRes(List<Record> otherSynTask){//这里会复制全部文件，不论是否安装
		//复制基础样式文件及图片文件
		copyBaseData("",new File(baseFolder).getParentFile());
		return new IndexTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new GotoTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new FlowPackTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new FunnyTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new IntroduceTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new PreferentialTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new RestaurantTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new TideTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new ApkTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new AudioTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new BookTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new GameTask(shopId, baseFolder).copyRes(otherSynTask)
				&& new VideoTask(shopId, baseFolder).copyRes(otherSynTask);
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
	 * 
	 * @return 					如果该方法执行失败则返回null，如果执行成功则返回需要删除的资源：包含两种资源，一种为调用方执行成功后需删除的文件，另一种是执行失败后需要删除的资源
	 */
	public static Map<String,List<File>> synRes(Object shopId,List<String> sqls,Record taskInfo,
			List<String> routerDeleteRes,List<Record> publishDevices){
    	if(SynUtils.checkShopPublished(shopId)){
    		taskInfo.set("task_type", marker);
    		SynAllTask index = new SynAllTask(shopId,taskInfo,publishDevices);
    		return index.execute(sqls, routerDeleteRes);
    	}else{
    		return new HashMap<String,List<File>>();
    	}
	}
	
}
