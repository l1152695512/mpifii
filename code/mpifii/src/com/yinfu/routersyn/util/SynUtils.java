package com.yinfu.routersyn.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.InitDemoDbConfig;
import com.yinfu.jbase.util.PropertyUtils;

public class SynUtils {
	public static final String ROUTER_SYN_FLODER = "upload/routerSyn";
	public static final String BASE_ROUTER_FLODER_NAME = "Data";
	
	public static String getResBaseFloder(){
		return PropertyUtils.getProperty("server.path").replaceAll("/", File.separator+File.separator);
	}
	
	public static String getResDownLoadFloder(){
		String folderName = UUID.randomUUID().toString();
		File absoluteFolder = new File(getResBaseFloder()+File.separator+(ROUTER_SYN_FLODER.replaceAll("/", File.separator+File.separator))+File.separator+folderName+File.separator+BASE_ROUTER_FLODER_NAME);
		if(!absoluteFolder.exists()){
			absoluteFolder.mkdirs();
		}
		return absoluteFolder.getAbsolutePath();
	}
	
	public static void deleteRes(List<File> resFiles){
		try{
			if(null != resFiles){
				Iterator<File> ite = resFiles.iterator();
				while(ite.hasNext()){
					File file = ite.next();
					String fileAbsolutePath = file.getAbsolutePath();
					if(fileAbsolutePath.startsWith(PathKit.getWebRootPath()+File.separator)){
						String filePath = fileAbsolutePath.substring((PathKit.getWebRootPath()+File.separator).length());
						if(!filePath.startsWith("upload")){
							continue;
						}
					}
					if(file.exists()){
//						file.delete();
						FileUtils.forceDelete(file);
					}
				}
			}
		}catch(Exception e){
		}
	}
	
	public static String getAppName(Object marker){
		String name = "";
		try{
			Record rec = Db.findFirst("select name from bp_app where marker=? ", new Object[]{marker});
			if(null != rec){
				name = rec.getStr("name");
			}
		}catch(Exception e){
		}
		return name;
	}
	
	public static void putAllFiles(Map<String,List<File>> sourceFiles,Map<String,List<File>> targetFiles){
		if(null == sourceFiles){
			return;
		}
		List<File> successDeleteFiles = sourceFiles.get("success");
		if(null == successDeleteFiles){
			successDeleteFiles = new ArrayList<File>();
			sourceFiles.put("success", successDeleteFiles);
		}
		if(null != targetFiles && null != targetFiles.get("success")){
			successDeleteFiles.addAll(targetFiles.get("success"));
		}
		
		List<File> failDeleteFiles = sourceFiles.get("fail");
		if(null == failDeleteFiles){
			failDeleteFiles = new ArrayList<File>();
			sourceFiles.put("fail", failDeleteFiles);
		}
		if(null != targetFiles && null != targetFiles.get("fail")){
			failDeleteFiles.addAll(targetFiles.get("fail"));
		}
	}
	
	public static void putFiles(Map<String,List<File>> sourceFiles,List<File> files,String type){
		if(null == sourceFiles || null == type || null == files){
			return;
		}
		List<File> oldFiles = sourceFiles.get(type);
		if(null == oldFiles){
			sourceFiles.put(type, files);
		}else{
			oldFiles.addAll(files);
		}
	}
	
	public static boolean checkShopPublished(Object shopId){
		Record rec = Db.findFirst("select ifnull(is_publish,'0') is_publish from bp_shop_page where shop_id=?", new Object[]{shopId});
		return rec != null && "1".equals(rec.get("is_publish").toString());
	}
	
	public static void main(String[] args) {
		InitDemoDbConfig.initPlugin("jdbc:mysql://127.0.0.1:3306/pifii?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
				"root", "ifidc1120");
		System.err.println(checkShopPublished(5));
	}
}
