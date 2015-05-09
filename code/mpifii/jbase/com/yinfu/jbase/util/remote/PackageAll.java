package com.yinfu.jbase.util.remote;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.freemarker.AppMarker;
import com.yinfu.business.freemarker.AudioMarker;
import com.yinfu.business.freemarker.BookMarker;
import com.yinfu.business.freemarker.GameMarker;
import com.yinfu.business.freemarker.VideoMarker;
import com.yinfu.jbase.util.PropertyUtils;


public class PackageAll extends Thread{
	private static boolean PACKAGEING = false;
	private static final String FILE_EXTENSION = ".tar.gz";
    
	@Override
	public void run() {
		super.run();
		if(!PACKAGEING){
			packageAll();
		}else{
    		System.err.println("打包程序正在运行！");
    	}
	}
	
    private void packageAll(){
		PACKAGEING = true;
		try{
			String baseDir = PropertyUtils.getProperty("server.path")+File.separator+"file" + File.separator+"init"+File.separator+"Data";//商户平台的根目录
			new File(baseDir).mkdirs();
			boolean videoChange = packageApp("video","bp_video","icon","logo","link","v","video/video.html");
			boolean audioChange = packageApp("audio","bp_audio","icon","logo","link","m","audio/audio.html");
			boolean appChange = packageApp("app","bp_apk","icon","logo","link","f","app/app.html");
			boolean bookChange = packageApp("book","bp_book","img","logo","link","file","book/book.html");
//        	boolean gamesChange = packageApp("games","bp_game","icon","logo","link","f","game/game.html");
			File existGzip = new File(baseDir+FILE_EXTENSION);
			if(!existGzip.exists() || videoChange || audioChange || appChange || bookChange){
				if(existGzip.exists()){
					existGzip.delete();
				}
				changeInitPackageStatus(0);
				TarGzipCompress.WriteToTarGzip(baseDir);//生成新的打包文件
				changeInitPackageStatus(1);
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			PACKAGEING = false;
		}
    }
    
    private void changeInitPackageStatus(int status){
    	Db.update("update bp_init set status=? ", new Object[]{status});
    }
    
    private boolean packageApp(String serverAppDirName,String tableName,String imgFieldName,String ImgRouterForderName,
    		String fileFieldName,String fileRouterForderName,String appIndexHtmlPath) throws IOException{
    	String baseDir = PropertyUtils.getProperty("server.path")+File.separator;//商户平台的根目录
    	String appInitDir = "file" + File.separator+"init"+File.separator+"Data"+File.separator +"mb"+File.separator + serverAppDirName;//app初始化的文件夹
    	new File(baseDir+appInitDir).mkdirs();
    	String appIndexHtmlBasePath = "file"+File.separator+"freemarker"+File.separator+"html"+File.separator+"0"+File.separator+"mb"+File.separator;//应用生成html的位置
    	List<String> existResourceNameList = new ArrayList<String>();
    	List<File> forders = new ArrayList<File>();
    	forders.add(new File(baseDir+appInitDir+File.separator+fileRouterForderName));//列出所有的文件
    	forders.add(new File(baseDir+appInitDir+File.separator+ImgRouterForderName));//列出所有的图片
    	getExistResourceName(forders,existResourceNameList);
    	List<Record> list = Db.find("select "+imgFieldName+" img,"+fileFieldName+" file from "+tableName+" where status=1 and delete_date is null ");
		boolean changed = false;
    	for(Record rd : list) {
			String fileName = rd.getStr("file").substring(rd.getStr("file").lastIndexOf("/")+1);
			if(!existResourceNameList.contains(fileName)){//如果文件中不存在该文件则拷贝过去
				File fileDestDir = new File(baseDir+appInitDir+File.separator+fileRouterForderName);
				File fileSrcFile = new File(baseDir+(rd.getStr("file").replaceAll("/",File.separator+File.separator)));
				if(fileSrcFile.exists()){
					FileUtils.copyFileToDirectory(fileSrcFile, fileDestDir);
					System.err.println(">>>>>"+baseDir+(rd.getStr("file").replaceAll("/",File.separator+File.separator))+"---"+baseDir+appInitDir+File.separator+fileRouterForderName);
					changed = true;
				}
			}else{//如果存在则删除
				existResourceNameList.remove(fileName);
			}
			String imgName = rd.getStr("img").substring(rd.getStr("img").lastIndexOf("/")+1);
			if(!existResourceNameList.contains(imgName)){//如果文件中不存在该图片,则拷贝过去
				File imgDestDir = new File(baseDir+appInitDir+File.separator+ImgRouterForderName);
				File imgSrcFile = new File(baseDir+(rd.getStr("img").replaceAll("/",File.separator+File.separator)));
				if(imgSrcFile.exists()){
					FileUtils.copyFileToDirectory(imgSrcFile, imgDestDir);
					System.err.println(">>>>>"+baseDir+(rd.getStr("img").replaceAll("/",File.separator+File.separator))+"---"+baseDir+appInitDir+File.separator+ImgRouterForderName);
					changed = true;
				}
			}else{//如果存在则删除
				existResourceNameList.remove(imgName);
			}
		}
    	String appIndexHtmlPathAll = baseDir+appIndexHtmlBasePath+(appIndexHtmlPath.replaceAll("/", File.separator+File.separator));
    	File appIndexHtmlFile = new File(appIndexHtmlPathAll);
    	if(changed || existResourceNameList.size()>0 || !appIndexHtmlFile.exists()){
    		changed = true;
			if(generHtml(tableName)){
				appIndexHtmlFile = new File(appIndexHtmlPathAll);
			}
    	}
		if(appIndexHtmlFile.exists()){
			FileUtils.copyFileToDirectory(appIndexHtmlFile, new File(baseDir+appInitDir));
		}
		deleteResourceByName(forders,existResourceNameList);//删除不需要的资源文件
//		TarGzipCompress.WriteToTarGzip(baseDir+appInitDir);//生成新的打包文件
		return changed;
    }
    
    /**
     * 按照文件名在指定目录中查找文件并删除
     * @param searchForders
     * @param fileNames
     */
    private void deleteResourceByName(List<File> searchForders,List<String> fileNames){
    	Iterator<File> ite = searchForders.iterator();
    	while(ite.hasNext()){
    		File forder = ite.next();
    		if(forder.exists()){
    			if(forder.isFile()){
    				if(fileNames.contains(forder.getName())){
    					forder.delete();
					}
    			}else{
    				File[] file = forder.listFiles();
    				List<File> fordersInThisFolder = new ArrayList<File>();
    				for (int i = 0; i < file.length;i++) {
    					if(file[i].isFile()){
    						if(fileNames.contains(file[i].getName())){
    							file[i].delete();
    						}
    					}else{
    						fordersInThisFolder.add(file[i]);
    					}
    				}
    				deleteResourceByName(fordersInThisFolder,fileNames);
    			}
    		}
    	}
    }
    
    private void getExistResourceName(List<File> forders,List<String> resourceName){
    	Iterator<File> ite = forders.iterator();
    	while(ite.hasNext()){
    		File forder = ite.next();
    		if(forder.exists()){
    			if(forder.isFile()){
    				resourceName.add(forder.getName());
    			}else{
    				File[] file = forder.listFiles();
    				List<File> fordersInThisFolder = new ArrayList<File>();
    				for (int i = 0; i < file.length;i++) {
    					if(file[i].isFile()){
    						resourceName.add(file[i].getName());
    					}else{
    						fordersInThisFolder.add(file[i]);
    					}
    				}
    				getExistResourceName(fordersInThisFolder,resourceName);
    			}
    		}
    	}
    }
    
    public static void main(String[] args) {
    	PackageAll aa = new PackageAll();
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
    	aa.deleteResourceByName(forders,names);
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
