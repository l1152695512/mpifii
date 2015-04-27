package com.yinfu.routersyn.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.log4j.Logger;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;

public class TarGzipUtil {
	private static Logger logger = Logger.getLogger(TarGzipUtil.class);
	
	// 不能对每层都包含文件和目录的多层次目录结构打包
	private void CompressedFiles_Gzip(String folderPath,String targzipFilePath) {
		File srcPath = new File(folderPath);
		int length = srcPath.listFiles().length;
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		File[] files = srcPath.listFiles();
		try {
			// 建立压缩文件输出流
			FileOutputStream fout = new FileOutputStream(targzipFilePath);
			// 建立tar压缩输出流
			TarOutputStream tout = new TarOutputStream(fout);
			for (int i = 0; i < length; i++) {
				String filename = srcPath.getPath() + File.separator
						+ files[i].getName();
				// 打开需压缩文件作为文件输入流
				FileInputStream fin = new FileInputStream(filename); // filename是文件全路径
				TarEntry tarEn = new TarEntry(files[i]); // 此处必须使用new
															// TarEntry(File
															// file);
				// tarEn.setName(files[i].getName());
				// //此处需重置名称，默认是带全路径的，否则打包后会带全路径
				tout.putNextEntry(tarEn);
				int num;
				while ((num = fin.read(buf)) != -1) {
					tout.write(buf, 0, num);
				}
				tout.closeEntry();
				fin.close();
			}

			tout.close();
			fout.close();

			// 建立压缩文件输出流
			FileOutputStream gzFile = new FileOutputStream(targzipFilePath
					+ ".gz");
			// 建立gzip压缩输出流
			GZIPOutputStream gzout = new GZIPOutputStream(gzFile);
			// 打开需压缩文件作为文件输入流
			FileInputStream tarin = new FileInputStream(targzipFilePath); // targzipFilePath是文件全路径
			int len;
			while ((len = tarin.read(buf)) != -1) {
				gzout.write(buf, 0, len);
			}
			gzout.close();
			gzFile.close();
			tarin.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// 循环遍历目录结构中的文件并添加至tar的输出流
	private void addFiles(TarOutputStream tout, String folderPath,String relativePath) {
		File srcPath = new File(folderPath);
		int length = srcPath.listFiles().length;
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		File[] files = srcPath.listFiles();
		try {
			for (int i = 0; i < length; i++) {
				
				if (files[i].isFile()) {
//					System.out.println("file:" + files[i].getName());
					String filename = srcPath.getPath() + File.separator + files[i].getName();
					// 打开需压缩文件作为文件输入流
					FileInputStream fin = new FileInputStream(filename); // filename是文件全路径
					TarEntry tarEn = new TarEntry(files[i]); // 此处必须使用new TarEntry(File file);
					tarEn.setName(relativePath+"/"+files[i].getName());
					//此处需重置名称，默认是带全路径的，否则打包后会带全路径
					tout.putNextEntry(tarEn);
					int num;
					while ((num = fin.read(buf)) != -1) {
						tout.write(buf, 0, num);
					}
					tout.closeEntry();
					fin.close();
				} else {
//					System.out.println(files[i].getPath());
					addFiles(tout, files[i].getPath(),relativePath+"/"+files[i].getName());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}
	
	/**
	 * 生成tar并压缩成tar.gz
	 * @param folderPath
	 * @return
	 */
	public static boolean tarGzipFile(String folderPath) {
		return tarGzipFile(folderPath,folderPath+".tar.gz");
	}
	
	public static boolean tarGzipFile(String folderPath,String ouputFileName) {
		boolean success = false;
		try {
			new TarGzipUtil().WriteToTarGzip(folderPath,ouputFileName);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			logger.warn("生成压缩包失败！", e);
		}
		return success;
	}
	
	// 生成tar并压缩成tar.gz
	private void WriteToTarGzip(String folderPath, String ouputFileName) throws IOException {
		if(!new File(ouputFileName).exists()){
			new File(ouputFileName).getParentFile().mkdirs();
			new File(ouputFileName).getParentFile().createNewFile();
		}
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		// 建立压缩文件输出流
		FileOutputStream fout = new FileOutputStream(folderPath+".tar");
		// 建立tar压缩输出流
		TarOutputStream tout = new TarOutputStream(fout);
		addFiles(tout, folderPath,new File(folderPath).getName());
		tout.close();
		fout.close();
		// 建立压缩文件输出流
		FileOutputStream gzFile = new FileOutputStream(ouputFileName);
		// 建立gzip压缩输出流
		GZIPOutputStream gzout = new GZIPOutputStream(gzFile);
		// 打开需压缩文件作为文件输入流
		FileInputStream tarin = new FileInputStream(folderPath+".tar"); // targzipFilePath是文件全路径
		int len;
		while ((len = tarin.read(buf)) != -1) {
			gzout.write(buf, 0, len);
		}
		gzout.close();
		gzFile.close();
		tarin.close();
		File tarfile = new File(folderPath+".tar");
		tarfile.delete();
	}

	public static void main(String[] args) {
		// 方法一：对于目录中只含文件的文件夹打包并压缩
		// CompressedFiles_Gzip("E:\\list","E:\\list.tar");
		// 方法二：对目录中既含有文件又含有递归目录的文件夹打包
		
//		doUncompressFile("E://1//Xiangqi.tar.gz");
//		WriteToTarGzip("E:/1/DangerRanger");
//		tarGzipFile("E:/test/abc/Data","E:/test/abc.tar.gz");
//		tarGzipFile("E:/aa/t");
		
		unTarGzipFile("E:/aa/t.tar.gz", "E:/aa");
	}
	
	/*
     * 执行入口,rarFileName为需要解压的文件路径(具体到文件),destDir为解压目标路径
     */
    public static boolean unTarGzipFile(String rarFileName, String destDir) {
    	File file = new File(destDir);
    	if (!file.exists()) {
             file.mkdirs();
    	}
    	return new TarGzipUtil().unTarGz(rarFileName,destDir.replaceAll("/", File.separator+File.separator));
    }
 
    public boolean unTarGz(String rarFileName, String destDir) {
    	boolean success = false;
    	FileInputStream fis = null;
    	ArchiveInputStream in = null;
    	BufferedInputStream bufferedInputStream = null;
    	BufferedOutputStream bufferedOutputStream = null;
    	try {
    		fis = new FileInputStream(rarFileName);
    		GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
    		ArchiveStreamFactory factory = new ArchiveStreamFactory();
    		in = factory.createArchiveInputStream("tar", is);
    		bufferedInputStream = new BufferedInputStream(in);
    		TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
    		while (entry != null) {
    			String name = new String(entry.getName().getBytes(),"utf-8");
    			File file = new File(destDir+File.separator+name.replaceAll("/", File.separator+File.separator));
    			if(!file.exists()){
    				file.getParentFile().mkdirs();
    				file.createNewFile();
    			}
    			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
    			int b;
    			while ((b = bufferedInputStream.read()) != -1) {
    				bufferedOutputStream.write(b);
    			}
    			bufferedOutputStream.flush();
    			bufferedOutputStream.close();
    			entry = (TarArchiveEntry) in.getNextEntry();
    		}
    		success = true;
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (ArchiveException e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if (bufferedInputStream != null) {
    				bufferedInputStream.close();
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return success;
	}
	
    private static boolean doUncompressFile(String inFileName) {
        try {
            if (!getExtension(inFileName).equalsIgnoreCase("gz")) {
                System.err.println("File name must have extension of \".gz\"");
                return false;
            }
            System.out.println("Opening the compressed file.");
            GZIPInputStream in = null;
            in = new GZIPInputStream(new FileInputStream(inFileName));
            System.out.println("Open the output file.");
            String outFileName = getFileName(inFileName);
            FileOutputStream out = null;
            out = new FileOutputStream(outFileName);
            System.out.println("Transfering bytes from compressed file to the output file.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            System.out.println("Closing the file and stream");
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
   
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1);
        }       
        return ext;
    }
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');
 
        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i);
        }       
        return fname;
    }
}
