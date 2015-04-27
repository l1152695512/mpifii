package com.yinfu.routersyn.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.util.SynUtils;
import com.yinfu.routersyn.util.TarGzipUtil;
import com.yinfu.routersyn.util.VerifyUtil;

/**
 * 类中不存在数据库的增删改操作
 * @author l
 *
 */
public class BaseTask {
	private static Logger logger = Logger.getLogger(BaseTask.class);
	protected static final String BASE_DATA_FLODER=SynUtils.getResBaseFloder()+File.separator+"file"+File.separator+"Data";
	protected Record taskInfo;
//	protected String taskType;
//	protected String taskDesc;
	protected String baseFolder;
	private List<Record> synDevices;//需要同步的设备，如果该值为null则使用shopId查找需要同步的设备，用于管理平台绑定盒子时，只针对该盒子同步
	protected Object shopId;
	
	private List<Record> otherSynTask = new ArrayList<Record>();//本次压缩包附带的资源文件下载任务，应用于apk、audio、book、game、video这几类应用的同步，这几类资源生成的更新包中不包含大文件（音乐、视频、小说、游戏、apk）
	private boolean successDeleteNewTarGz = true;//判断整个过程执行成功后是否删除最新生成的压缩包，当所有该商铺的盒子的更新不依赖于当前新产生的压缩包时会删除该文件（即所有该商铺盒子都有同task_type的未完成任务），
	private List<File> failDeleteFile = new ArrayList<File>();//本次任务执行失败后需要删除的文件
	private List<File> successDeleteFile = new ArrayList<File>();//本次任务执行成功后需要删除的文件，用于同task_type时新文件覆盖旧文件
	
	public BaseTask(Record taskInfo,Object shopId,List<Record> devices) {
//		this.taskType = taskType;
//		this.taskDesc = taskDesc;
		//设置默认值
		if(null == taskInfo.get("is_show")){
			taskInfo.set("is_show", "1");
		}
		this.taskInfo = taskInfo;
		this.shopId = shopId;
		baseFolder = SynUtils.getResDownLoadFloder();
		checkDeviceType(devices);
	}
	
	public BaseTask(String baseFolder,Object shopId) {
		this.baseFolder = baseFolder;
		this.shopId = shopId;
	}


	private void checkDeviceType(List<Record> devices){//吸顶不需要同步内容
		if(null == devices){
			if(null != shopId){
				synDevices = Db.find("select distinct router_sn from bp_device where shop_id=?",new Object[]{shopId});
			}else{
				synDevices = new ArrayList<Record>();
			}
		}else{
			synDevices = devices;
		}
		synDevices = Db.find("select router_sn from bp_device where router_sn in ("+recordListToIdIn(synDevices,"router_sn")+") and type=1 ");
	}
	
	protected Map<String,List<File>> execute(List<String> sqls,List<String> routerDeleteRes){
		if(synDevices.size() == 0){
			deleteBaseFolder();
			return new HashMap<String,List<File>>();
		}
		boolean success = false;
    	String tarGzAbsolutePath = baseFolder.substring(0, baseFolder.lastIndexOf(File.separator))+".tar.gz";//生成的压缩包名称
    	try {
    		success = copyRes(otherSynTask) //将html文件和图片文件拷贝到要压缩的文件夹中
    				&& TarGzipUtil.tarGzipFile(baseFolder,tarGzAbsolutePath)//生成压缩包;
    				&& insertOrUpdateTask(sqls,tarGzAbsolutePath,routerDeleteRes);//将需要增删改的sql放入sqls中
    	} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成html异常！", e);
			File tarGz = new File(tarGzAbsolutePath);
			if(tarGz.exists()){//如果压缩包已生成则删除
				tarGz.delete();
			}
		}finally{
			deleteBaseFolder();
		}
    	Map<String,List<File>> deleteRes = null;
    	if(success){
    		deleteRes = new HashMap<String,List<File>>();
    		failDeleteFile.add(new File(tarGzAbsolutePath));
    		deleteRes.put("fail", failDeleteFile);
    		deleteRes.put("success", successDeleteFile);
    	}
    	return deleteRes;
    }
	
	private void deleteBaseFolder(){
		try {
			if(baseFolder.endsWith(SynUtils.BASE_ROUTER_FLODER_NAME)){
				File file = new File(baseFolder);
				if(file.exists()){
					File parentFile = file.getParentFile();
					if(parentFile.exists()){
						FileUtils.deleteDirectory(parentFile);//删除临时生成的文件夹
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void copyBaseData(String resRelativePath,File destDir){//拷贝某个app的基础文件，如css、js、图片，如果需要拷贝则必须保证项目file/Data下对应的文件是最新的
		//复制基础样式文件及图片文件
		try {
			File sourceFile = new File(BASE_DATA_FLODER+File.separator+resRelativePath);
			if(sourceFile.exists()){
				if(!destDir.exists()){
					destDir.mkdirs();
				}
				FileUtils.copyDirectoryToDirectory(sourceFile, destDir);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {//复制公共文件包含Data目录下的："commoncss","commonimg","commonjs","pifii.html"
			File destDataDir = destDir.getParentFile();
			String[] commDirs = {"commoncss","commonimg","commonjs","pifii.html"};
			for(int i=0;i<commDirs.length;i++){
				File sourceFile = new File(BASE_DATA_FLODER+File.separator+commDirs[i]);
				if(sourceFile.exists()){
					if(!destDataDir.exists()){
						destDataDir.mkdirs();
					}
					if(commDirs[i].indexOf(".") != -1){//要拷贝的是文件类型
						FileUtils.copyFileToDirectory(sourceFile, destDataDir);
					}else{//要拷贝的是文件夹类型
						FileUtils.copyDirectoryToDirectory(sourceFile, destDataDir);
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//需要重载的方法
	protected boolean copyRes(List<Record> otherSynTask){
		return true;
	} 
	
	/**
	 * 添加需要增删改的sql
	 * 		bp_res_task:如果有同类型的任务，如对某个应用数据增删改，他们要同步的文件是一样的，这种就属于同类型的文件，即task_type一样，
	 * 					假如新增的任务和之前未完成的任务的task_type一样，则会修改之前未完成的任务的cmd为d（盒子中删除该任务），
	 * 		bp_res_task_delete:
	 * 		这里会生成当前商铺的所有盒子的下载任务，如果任务已存在则会更新任务，
	 * @param sqls
	 * @param taskType
	 * @param tarGzAbsolutePath
	 * @param taskDesc
	 * @param routerDeleteRes
	 * @return
	 */
	private boolean insertOrUpdateTask(List<String> sqls,String tarGzAbsolutePath,List<String> routerDeleteRes){
		String resTarGzName = tarGzAbsolutePath.substring(tarGzAbsolutePath.lastIndexOf(File.separator)+1);
		File tarGzFile = new File(tarGzAbsolutePath);
		long fileLength = tarGzFile.length();
		String md5 = VerifyUtil.getMD5(tarGzFile);
		if(null != md5){
//			if(null == synDevices){
//				synDevices = Db.find("select distinct router_sn from bp_device where shop_id=?", new Object[]{shopId});//查找该商铺中所有的盒子
//			}
			//对于Data升级包的特殊处理，原因：data升级任务的task_type并不是data，而是data_data升级包的id，所以在匹配data升级包任务时要去掉后面的data升级包id
			String newTaskType = taskInfo.getStr("task_type");
			String sqlTaskType = "task_type";
			if(newTaskType.startsWith(DataTask.marker+"_")){
				newTaskType = DataTask.marker+"_";
				sqlTaskType = "left(task_type,5)";
			}
			Iterator<Record> ite = synDevices.iterator();
			while(ite.hasNext()){
				Record device = ite.next();
				StringBuffer unfinishedTaskSql = new StringBuffer();
				unfinishedTaskSql.append("select id,res_url from bp_res_task where router_sn=? ");
				if(null != taskInfo.get("id") && StringUtils.isNotBlank(taskInfo.get("id").toString())){//这种情况用于处理任务下发失败后，重新更新该任务的资源文件及任务状态，添加时间：2015-2-5
					unfinishedTaskSql.append("and id != '"+taskInfo.getStr("id")+"' ");
				}
				unfinishedTaskSql.append("and "+sqlTaskType+"=? and step!=2 and related_task_id is null order by operate_date ");
				List<Record> unfinishedTask = Db.find(unfinishedTaskSql.toString(), new Object[]{device.getStr("router_sn"),newTaskType});
				long thisFileLength = fileLength;
				String thisMd5 = md5;
				resTarGzName = tarGzUnfinishedTask(tarGzFile,unfinishedTask,routerDeleteRes);
				if(null == resTarGzName){//生成新压缩包失败
					return false;
				}
				if(!resTarGzName.equals(tarGzFile.getName())){//如果有生成新的压缩包，则更新文件大小，并更新md5
					File newFile = new File(tarGzFile.getParent()+File.separator+resTarGzName);
					thisFileLength = newFile.length();
					thisMd5 = VerifyUtil.getMD5(newFile);
				}
				String taskId = "";
				if(null == taskInfo.get("id") || StringUtils.isBlank(taskInfo.get("id").toString())){
					taskId = UUID.randomUUID().toString();
					//添加当前的下载任务
					sqls.add("insert into bp_res_task(id,router_sn,res_type,task_type,res_url,local_path,md5,file_size,step,progress,cmd,is_show,delete_when_done,task_desc,gen_res_date,operate_date) "
							+ "values('"+taskId+"','"+device.getStr("router_sn")+"','archive','"+taskInfo.getStr("task_type")+"','"+SynUtils.ROUTER_SYN_FLODER+"/"+resTarGzName+"','/storageroot','"+thisMd5+"',"+thisFileLength+",0,0,'',"+taskInfo.getStr("is_show")+",0,'"+taskInfo.getStr("task_desc")+"',now(),now())");
				}else{//这种情况用于处理任务下发失败后，重新更新该任务的资源文件及任务状态，添加时间：2015-2-5
					taskId = taskInfo.getStr("id");
					sqls.add("update bp_res_task set res_url='"+SynUtils.ROUTER_SYN_FLODER+"/"+resTarGzName+"',md5='"+thisMd5+"',file_size="+thisFileLength+",step=0,progress=0 where id='"+taskId+"'");
				}
				//修改同类型的未完成的下载任务的cmd为d，即删除该下载任务
				sqls.add("update bp_res_task set cmd='d',related_task_id='"+taskId+"' where id in ("+recordListToIdIn(unfinishedTask,"id")+")");
				addOtherSynTask(device.getStr("router_sn"),sqls,taskId);
				if(null != routerDeleteRes){//插入需要删除的盒子文件
					for(int i=0;i<routerDeleteRes.size();i++){
						String id = UUID.randomUUID().toString();
						sqls.add("insert into bp_res_task_delete(id,task_id,router_file_path,create_date) values('"+id+"','"+taskId+"','"+routerDeleteRes.get(i)+"',now())");
					}
				}
			}
			if(successDeleteNewTarGz){
				successDeleteFile.add(tarGzFile);
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将同task_type未完成的任务文件解压复制到最新的打包文件中，保证同task_type的任务只有一个，就不会出现旧的文件覆盖新的文件
	 * 解决的问题：
	 * 		1.如index页面，更改页面的地方可能会很多（商铺更改、轮播图广告更改、app安装卸载、底部广告更改），每次更改时只会同步本次更改影响到的资源，
	 * 			不会包含整个资源文件（如：更改了广告则不会去同步商铺图片），这样就会出现一些问题：某次更改了商铺信息和和广告信息，
	 * 			则这两次同步的资源中包含各自对应的图片以及共有的index.html，这时盒子来请求更新资源，会去下载这两个资源，如果盒子是按照平台操作的时间顺序去下载完成的，
	 * 			则不会有问题，如果不是，就会出现旧的index.html覆盖了新的index.html；
	 * 		2.Data基础文件更新，每次的Data文件中不是包含全部文件，所以必须按照先后顺序下载完成
	 * @return
	 * 
	 * 不再需要调用该方法了：问题1已经改了，会复制index.html涉及到的所有资源，如广告图、应用图标、商户信息，问题2已经之前已经考虑了，不存在该问题了
	 */
	private String tarGzUnfinishedTask(File latestTarGzFile,List<Record> unfinishedTask,List<String> routerDeleteRes){
		String newFileName = latestTarGzFile.getName();
		if(unfinishedTask.size() > 0){
//			String newFilePath = new File(SynUtils.getResDownLoadFloder()).getParent();
			try{
				Iterator<Record> ite = unfinishedTask.iterator();
				while(ite.hasNext()){
					Record rec = ite.next();
					File tarGz = new File(SynUtils.getResBaseFloder()+File.separator+rec.getStr("res_url"));
					if(tarGz.exists()){
						successDeleteFile.add(tarGz);
//						TarGzipUtil.unTarGzipFile(tarGz.getAbsolutePath(), newFilePath);
					}
				}
				//删除旧压缩包中已删除的文件资源
//				if(null != routerDeleteRes){
//					Iterator<String> iteDelete = routerDeleteRes.iterator();
//					while(iteDelete.hasNext()){
//						String filePath = iteDelete.next();
//						String startMarker = "/storageroot";
//						if(filePath.startsWith(startMarker)){
//							filePath = newFilePath+filePath.substring(startMarker.length()).replaceAll("/", File.separator+File.separator);
//							File deleteFile = new File(filePath);
//							if(deleteFile.exists()){
//								deleteFile.delete();
//							}
//						}
//					}
//				}
				//将最新生成的压缩包也解压到文件中，然后一起打包
//				boolean success = TarGzipUtil.unTarGzipFile(latestTarGzFile.getAbsolutePath(), newFilePath) 
//						&& TarGzipUtil.tarGzipFile(newFilePath+File.separator+SynUtils.BASE_ROUTER_FLODER_NAME,newFilePath+".tar.gz");//生成的压缩包名称);
//				if(success){
//					File newTargzFile = new File(newFilePath+".tar.gz");
//					newFileName = newTargzFile.getName();
//					failDeleteFile.add(newTargzFile);
//				}else{
//					return null;
//				}
			}catch(Exception e){
//				e.printStackTrace();
				logger.warn("复制旧的压缩包到新的压缩包过程中出现异常！", e);
//				File errorTarGzFile = new File(newFilePath+".tar.gz");
//				if(errorTarGzFile.exists()){
//					errorTarGzFile.delete();
//				}
//			}finally{
//				File tempFile = new File(newFilePath);
//				if(tempFile.exists()){
//					try {
//						FileUtils.deleteDirectory(tempFile);//删除临时生成的文件夹
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			}
			successDeleteNewTarGz = false;
		}else{
			successDeleteNewTarGz = false;
		}
		return newFileName;
	}
	
	/**
	 * 同步其他基于该压缩包附带的资源同步文件（主要是大文件），如：视频、小说、安装包、音乐、游戏
	 * @return
	 */
	private boolean addOtherSynTask(String routerSn,List<String> sqls,Object tarGzTaskId){
		if(otherSynTask.size() > 0){
			Iterator<Record> ite = otherSynTask.iterator();
			while(ite.hasNext()){
				Record task = ite.next();
				if("delete".equals(task.getStr("operate_type"))){//如果该文件为删除类型的
					sqls.add("update bp_res_task set cmd='d',related_task_id='"+tarGzTaskId+"' where router_sn='"+routerSn+"' and res_url = '"+task.getStr("res_url")+"' ");//将之前该盒子下载的该资源标记为删除，服务器上的资源文件会在当前操作执行成功后删除
				}else if("add".equals(task.getStr("operate_type"))){//该文件为添加类型的
					String taskId = UUID.randomUUID().toString();
					File file = new File(SynUtils.getResBaseFloder()+File.separator+task.getStr("res_url"));
					if(file.exists()){
						long fileSize = file.length();
						String resType = "file";
						if(null != task.get("res_type")){
							resType = task.get("res_type").toString();
						}
						sqls.add("insert into bp_res_task(id,router_sn,res_type,task_type,res_url,local_path,md5,file_size,step,progress,cmd,is_show,delete_when_done,task_desc,gen_res_date,operate_date) "
								+ "values('"+taskId+"','"+routerSn+"','"+resType+"','"+task.getStr("task_type")+taskId+"','"+task.getStr("res_url")+"','"+"/storageroot/Data/"+task.getStr("local_path")+"','"+task.getStr("md5")+"',"+fileSize+",0,0,'',1,0,'"+task.getStr("task_desc")+"',now(),now())");
					}else{
						logger.warn("需要同步的资源文件不存在！"+file.getAbsolutePath());
					}
				}
			}
		}
		return true;
	}
	
	private String recordListToIdIn(List<Record> resData,String columnName){
		StringBuffer sqlIn = new StringBuffer("'");
		Iterator<Record> ite = resData.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			sqlIn.append(rowData.get(columnName)+"','");
		}
		sqlIn.append("'");
		return sqlIn.toString();
	}
	
}
