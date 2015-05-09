package com.yinfu.jbase.util.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;

public class TarGzipCompress {
	// 不能对每层都包含文件和目录的多层次目录结构打包
	public static void CompressedFiles_Gzip(String folderPath,String targzipFilePath) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 循环遍历目录结构中的文件并添加至tar的输出流
	public static void addFiles(TarOutputStream tout, String folderPath,String relativePath) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	// 生成tar并压缩成tar.gz
	public static void WriteToTarGzip(String folderPath) {
		WriteToTarGzip(folderPath,folderPath+".tar");
	}
	
	// 生成tar并压缩成tar.gz
	public static void WriteToTarGzip(String folderPath, String targzipFilePath) {
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		try {
			// 建立压缩文件输出流
			FileOutputStream fout = new FileOutputStream(targzipFilePath);
			// 建立tar压缩输出流
			TarOutputStream tout = new TarOutputStream(fout);
			addFiles(tout, folderPath,new File(folderPath).getName());
			tout.close();
			fout.close();
			// 建立压缩文件输出流
			FileOutputStream gzFile = new FileOutputStream(targzipFilePath+".gz");
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
			File tarfile = new File(targzipFilePath);
			tarfile.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main1(String[] args) {
		// 方法一：对于目录中只含文件的文件夹打包并压缩
		// CompressedFiles_Gzip("E:\\list","E:\\list.tar");
		// 方法二：对目录中既含有文件又含有递归目录的文件夹打包
		
		WriteToTarGzip("E:/1/DangerRanger");
		WriteToTarGzip("E:/1/Dragons");
		WriteToTarGzip("E:/1/Galactians2");
		WriteToTarGzip("E:/1/HyperGunner");
		WriteToTarGzip("E:/1/RebelRescue");
		WriteToTarGzip("E:/1/Xiangqi");
		
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
    public static void main(String[] args) {
        doUncompressFile("E://1//Xiangqi.tar.gz");
    }
}
