
package com.yinfu.jbase.util;

import static java.io.File.separator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDiy文件系统操作工具类.
 *
 * @author 子秋(ziquee) http://www.jdiy.org
 */
@SuppressWarnings({ "unused", "ResultOfMethodCallIgnored" })
public final class Fs {
	
	private Fs() {
	}
	
	/**
	 * 复制文件(或文件夹). 此方法会自动建立要复制到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件 如：new File("c:/a.txt")
	 * @param toFile
	 *            复制后文件 如：new File("f:/aaa/bbb/ccc/ddd/b.txt") (上层目录不存在，将会自动创建它们)
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyAs(String, String, boolean)
	 * @see #copyTo(String, String, boolean)
	 * @see #copyTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyAs(File fromFile, File toFile, boolean overwrite) {
		if (!fromFile.exists()) {
			return false;
		}
		if (fromFile.equals(toFile)) {
			return false;
		} else if (fromFile.isFile()) {
			return cpFile(fromFile, toFile, overwrite);
		} else {
			return cpDir(fromFile, toFile, overwrite);
		}
	}
	
	/**
	 * 复制文件(或文件夹). 此方法会自动建立要复制到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件路径 如：c:/a.txt
	 * @param toFile
	 *            复制后路径 如：f:/aaa/bbb/ccc/ddd/b.txt
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyAs(java.io.File, java.io.File, boolean)
	 * @see #copyTo(String, String, boolean)
	 * @see #copyTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyAs(String fromFile, String toFile, boolean overwrite) {
		return copyAs(new File(fromFile), new File(toFile), overwrite);
	}
	
	/**
	 * 复制文件到某个路径下. 与{@link #copyAs(String, String, boolean)}, {@link #copyAs(java.io.File, java.io.File, boolean)}
	 * 方法不同的是， 此方法是将fromFile复制到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要复制的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将复制到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyTo(String, String, boolean)
	 * @see #copyAs(String, String, boolean)
	 * @see #copyAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyTo(File fromFile, File toDir, boolean overwrite) {
		if (toDir.isFile()) {
			return false;
		}
		return copyAs(fromFile, new File(toDir.getPath() + separator + fromFile.getName()), overwrite);
	}
	
	/**
	 * 复制文件到某个路径下. 与{@link #copyAs(String, String, boolean)}, {@link #copyAs(java.io.File, java.io.File, boolean)}
	 * 方法不同的是， 此方法是将fromFile复制到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要复制的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将复制到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #copyTo(java.io.File, java.io.File, boolean)
	 * @see #copyAs(String, String, boolean)
	 * @see #copyAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean copyTo(String fromFile, String toDir, boolean overwrite) {
		return copyTo(new File(fromFile), new File(toDir), overwrite);
	}
	
	/**
	 * 删除文件（或文件夹）. 如果file是一个文件夹，将删除此文件夹及其下的全部内容.
	 *
	 * @param file
	 *            要删除的文件（或文件夹）.
	 */
	public static void delAll(File file) {
		if (!file.exists())
			return;
		File[] fs;
		if (file.isDirectory())
			if ((fs = file.listFiles()) != null)
				for (File f : fs)
					delAll(f);
		file.delete();
	}
	
	/**
	 * 删除文件（或文件夹）. 如果file是一个文件夹，将删除此文件夹及其下的全部内容.
	 *
	 * @param file
	 *            要删除的文件（或文件夹）的绝对路径地址.
	 */
	public static void delAll(String file) {
		delAll(new File(file));
	}
	
	/**
	 * 删除空目录.
	 *
	 * @param dir
	 *            如果dir所表示的目录下面没有任何内容,则删除之.
	 * @see #delAll(java.io.File)
	 * @see #delEmpty(String)
	 */
	public synchronized static void delEmpty(File dir) {
		if (dir.isFile())
			return;
		String[] s = dir.list();
		if (s == null || s.length < 1)
			dir.delete();// 取消了删除空子级目录，因为当子目录或文件过多时占资源
	}
	
	/**
	 * 删除空目录.
	 *
	 * @param path
	 *            目录的绝对路径，如果此目录下面没有任何内容，则删除之.
	 * @see #delEmpty(java.io.File)
	 * @see #delAll(String)
	 */
	public static void delEmpty(String path) {
		delEmpty(new File(path));
	}
	
	/**
	 * 清空目录. <br />
	 * 此方法将删除目录下面的所有子文件夹及文件（当前的目录本身并不会删除）
	 *
	 * @param dir
	 *            要清空的目录.
	 * @see #empty(String)
	 */
	public static void empty(File dir) {
		File[] fs;
		if (dir.isDirectory())
			if ((fs = dir.listFiles()) != null)
				for (File f : fs)
					delAll(f);
	}
	
	/**
	 * 清空目录. <br />
	 * 此方法将删除目录下面的所有子文件夹及文件（当前的目录本身并不会删除）
	 *
	 * @param dir
	 *            要清空的目录的绝对路径地址.
	 * @see #empty(java.io.File)
	 * @since 这是JDiy-1.9 及后续版本新增的方法.
	 */
	public static void empty(String dir) {
		delEmpty(new File(dir));
	}
	
	/**
	 * 读取属性文件. 此方法提供属性文件的便捷访问.
	 *
	 * @param resource
	 *            属性文件的路径地址.（路径地址可以是本地物理路径地址，也可以是一个URL地址）
	 * @return Properties对象, 通过该对象获取属性变量值.
	 */
	public static Properties getProperties(String resource) {
		Properties properties = new Properties();
		try {
			properties.load(getResource(resource).openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * 以classpath为相对根路径，获取资源.
	 *
	 * @param relativePath
	 *            相对路径地址，例如：<br />
	 *            a.txt - %classpath%/a.txt<br />
	 *            ./a.txt - 同上 <br />
	 *            /a.txt - 同上 <br />
	 *            ../a.txt - %classpath%的上级路径下的a.txt <br />
	 *            abc/a.txt- %classpath%/abc/a.txt
	 * @return 该资源的URL对象，可以通过 <b>returnurl.getPath()</b>来获取该资源的真实物理路径(returnurl即此方法返回的URL对象)。
	 * @throws java.net.MalformedURLException
	 *             MalformedURLException
	 */
	public static URL getResource(String relativePath) throws MalformedURLException {
		if (relativePath == null) {
			relativePath = "./";
		} else if (relativePath.startsWith("/")) {
			relativePath = "." + relativePath;
		} else if (!relativePath.startsWith("./") && !relativePath.startsWith("../")) {
			relativePath = "./" + relativePath;
		}
		@SuppressWarnings("ALL")
		String classAbsPath = Fs.class.getClassLoader().getResource("").toString();
		String parentStr = relativePath.substring(0, relativePath.lastIndexOf("../") + 3);
		relativePath = relativePath.substring(relativePath.lastIndexOf("../") + 3);
		int containSum = Txt.containSum(parentStr, "../");
		for (int i = 0; i < containSum; i++)
			classAbsPath = classAbsPath.substring(0, classAbsPath.lastIndexOf("/", classAbsPath.length() - 2) + 1);
		
		return new URL(classAbsPath + relativePath);
	}
	
	/**
	 * 移动文件(或文件夹). 此方法会自动建立要移动到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件 如：new File("c:/a.txt")
	 * @param toFile
	 *            移动后文件 如：new File("f:/aaa/bbb/ccc/ddd/b.txt") (上层目录不存在，将会自动创建它们)
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveAs(String, String, boolean)
	 * @see #moveTo(String, String, boolean)
	 * @see #moveTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveAs(File fromFile, File toFile, boolean overwrite) {
		if (copyAs(fromFile, toFile, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}
	
	/**
	 * 移动文件(或文件夹). 此方法会自动建立要移动到的文件(或文件夹)所在路径所需的上层目录.
	 *
	 * @param fromFile
	 *            原文件路径 如：c:/a.txt
	 * @param toFile
	 *            移动后路径 如：f:/aaa/bbb/ccc/ddd/b.txt
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveAs(java.io.File, java.io.File, boolean)
	 * @see #moveTo(String, String, boolean)
	 * @see #moveTo(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveAs(String fromFile, String toFile, boolean overwrite) {
		if (copyAs(fromFile, toFile, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}
	
	/**
	 * 移动文件到某个路径下. 与{@link #moveAs(String, String, boolean)}, {@link #moveAs(java.io.File, java.io.File, boolean)}
	 * 方法不同的是， 此方法是将fromFile移动到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要移动的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将移动到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveTo(String, String, boolean)
	 * @see #moveAs(String, String, boolean)
	 * @see #moveAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveTo(File fromFile, File toDir, boolean overwrite) {
		if (copyTo(fromFile, toDir, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}
	
	/**
	 * 移动文件到某个路径下. 与{@link #moveAs(String, String, boolean)}, {@link #moveAs(java.io.File, java.io.File, boolean)}
	 * 方法不同的是， 此方法是将fromFile移动到toDir路径的下面，即toDir一定是一个目录。
	 *
	 * @param fromFile
	 *            要移动的文件（或文件夹）
	 * @param toDir
	 *            文件（或文件夹）将移动到此目录下。
	 * @param overwrite
	 *            指定当目标位置已经存在文件时，是否覆盖该文件.
	 * @return 操作成功将返回true.应尽可能的检查返回状态，以判断操作是否成功.
	 * @see #moveTo(java.io.File, java.io.File, boolean)
	 * @see #moveAs(String, String, boolean)
	 * @see #moveAs(java.io.File, java.io.File, boolean)
	 */
	public static boolean moveTo(String fromFile, String toDir, boolean overwrite) {
		if (copyTo(fromFile, toDir, overwrite)) {
			delAll(fromFile);
			return true;
		}
		return false;
	}
	
	/**
	 * 读取文本文件的内容.
	 *
	 * @param file
	 *            要读取的文件. 可以为任意文本类型的文件，例如：txt, log, html, bat, sh, js, css等.
	 * @param encoding
	 *            文本文件的编码类型. 例如： utf-8(默认值), gb2312等等.
	 * @return 文本文件的内容.
	 * @throws IOException
	 * @see #readFile(java.io.File)
	 * @see #writeFile(java.io.File, String)
	 * @see #writeFile(java.io.File, String, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static String readFile(File file, String encoding) throws IOException {
		StringBuilder contents = new StringBuilder();
		BufferedReader reader = null;
		try {
			if (encoding == null || "".equals(encoding))
				encoding = "utf-8";
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
			reader = new BufferedReader(read);
			String text;
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(System.getProperty("line.separator"));
			}
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ignore) {
			}
		}
		return contents.toString();
	}
	
	/**
	 * 读取文本文件的内容. <br/>
	 * 程序默认以utf-8的编码形式来读取文本文件的内容. 如果文本文件不是utf-8的编码，请使用{@link #readFile(java.io.File, String)}方法指定编码.
	 *
	 * @param file
	 *            要读取的文件. 可以为任意文本类型的文件，例如：txt, log, html, bat, sh, js, css等.
	 * @return 文本文件的内容.
	 * @throws IOException
	 * @see #readFile(java.io.File, String)
	 * @see #writeFile(java.io.File, String)
	 * @see #writeFile(java.io.File, String, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static String readFile(File file) throws IOException {
		return readFile(file, "utf-8");
	}
	
	/**
	 * 将文本内容保存为文件. <br />
	 * 在保存文件时，(如果上层路径不存在).系统将自动创建其上层目录路径.
	 *
	 * @param file
	 *            要保存的文件.
	 * @param str
	 *            要保存的文本内容.
	 * @param encoding
	 *            指定文本文件编码方式. 例如：utf-8(默认值), gb2312 等等...
	 * @see #writeFile(java.io.File, String)
	 * @see #readFile(java.io.File)
	 * @see #readFile(java.io.File, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static void writeFile(File file, String str, String encoding) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, encoding);
			osw.write(str);
			osw.flush();
		} finally {
			try {
				if (null != osw)
					osw.close();
				if (null != fos)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将文本内容保存为文件. <br />
	 * 在保存文件时，(如果上层路径不存在).系统将自动创建其上层目录路径.
	 *
	 * @param file
	 *            要保存的文件.
	 * @param b
	 *            要保存的文本内容.
	 * @see #writeFile(java.io.File, String)
	 * @see #readFile(java.io.File)
	 * @see #readFile(java.io.File, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static void writeFile(File file, byte[] b) throws IOException {
		FileOutputStream fos = null;
		try {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			fos.write(b);
		} finally {
			try {
				if (null != fos)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将文本内容保存为文件. <br />
	 * 程序默认以utf-8的编码形式保存文本文件，如要使用其它编码方式保存， 请使用{@link #writeFile(java.io.File, String, String)}方法. <br />
	 * 在保存文件时，(如果上层路径不存在).系统将自动创建其上层目录路径.
	 *
	 * @param file
	 *            要保存的文件.
	 * @param str
	 *            要保存的文本内容.
	 * @see #writeFile(java.io.File, String, String)
	 * @see #readFile(java.io.File)
	 * @see #readFile(java.io.File, String)
	 * @since 这是JDiy-1.9及后续版本新增的方法.
	 */
	public static void writeFile(File file, String str) throws IOException {
		writeFile(file, str, "utf-8");
	}
	
	/**
	 * 返回指定路径下的文件（夹）所占用的空间字节大小.
	 *
	 * @param file
	 *            文件（夹）
	 * @return 该路径下的文件(夹)所占用的空间字节大小.
	 * @see #size(String)
	 * @see #sizeStr(String)
	 */
	public static long size(File file) {
		if (!file.exists())
			return 0L;
		if (file.isFile())
			return file.length();
		
		File[] fs = file.listFiles();
		long sizes = 0L;
		if (fs != null)
			for (File f1 : fs)
				sizes += size(f1);
		
		return sizes;
	}
	
	/**
	 * 返回指定路径下的文件（夹）所占用的空间字节大小.
	 *
	 * @param path
	 *            文件（夹）的绝对地址
	 * @return 该路径下的文件(夹)所占用的空间字节大小.
	 * @see #size(java.io.File)
	 * @see #sizeStr(String)
	 */
	public static long size(String path) {
		return size(new File(path));
	}
	
	/**
	 * 根据文件大小自动格式化为以Byte,Kb, Mb, Gb等单位的字符串显示. 系统采用1024进位.
	 *
	 * @param size
	 *            文件（夹）的字节大小
	 * @return 返回该字节大小格式化后的Byte, Kb, Mb, Gb等单位的字符串
	 * @see #size(String)
	 */
	public static String sizeStr(long size) {
		double size0;
		String s = "Byte";
		if (size < 1024 * 1024 && size > 1024) {
			size0 = size / 1024.0;
			s = "KB";
		} else if (size < 1024 * 1000 * 1024 && size > 1024 * 1024) {
			size0 = size / (1024 * 1024.0);
			s = "MB";
		} else if (size < 1024L * 1024 * 1024 * 1024 && size > 1024 * 1024 * 1024) {
			size0 = size / (1024 * 1024 * 1024.0);
			s = "GB";
		} else {
			size0 = size;
		}
		size0 = Math.round(size0 * 100.0) / 100.0;
		String ss = String.valueOf(size0);
		if (ss.lastIndexOf(".") == ss.length() - 2) {
			ss += "0";
		}
		return ss + " " + s;
	}
	
	/**
	 * 获取文件（夹）的占用空间大小，并格式化为Byte,Kb, Mb, Gb等单位的字符串显示. 系统采用1024进位.
	 *
	 * @param file
	 *            要获取大小的文件(或文件夹).
	 * @return 返回该文字的字节大小字符串
	 * @see #size(String)
	 */
	public static String sizeStr(File file) {
		return sizeStr(size(file));
	}
	
	/**
	 * 获取文件（夹）的占用空间大小，并格式化为Byte,Kb, Mb, Gb等单位的字符串显示. 系统采用1024进位.
	 *
	 * @param path
	 *            要获取大小的文件(或文件夹)的绝对路径.
	 * @return 返回该文字的字节大小字符串
	 * @see #size(String)
	 */
	public static String sizeStr(String path) {
		return sizeStr(size(path));
	}
	
	private static boolean cpFile(File fromFile, File toFile, boolean overWrite) {
		if (!overWrite && toFile.exists()) {
			return false;
		}
		int byteread;
		toFile.getParentFile().mkdirs();
		try {
			InputStream inStream = new FileInputStream(fromFile.getPath()); // 读入原文件
			FileOutputStream fs = new FileOutputStream(toFile.getPath());
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1)
				fs.write(buffer, 0, byteread);
			
			inStream.close();
			fs.close();
			return true;
		} catch (Exception ioe) {
			ioe.printStackTrace();
			return true;
		}
		
	}
	
	private static boolean cpDir(File fromFile, File toFile, boolean overWrite) {
		if (!overWrite && toFile.exists()) {
			return false;
		}
		toFile.mkdirs();
		File[] files = fromFile.listFiles();
		boolean isOk = true;
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					if (!cpFile(file, new File(toFile.getPath() + separator + file.getName()), true))
						isOk = false;
				} else {
					if (!cpDir(file, new File(toFile.getPath() + separator + file.getName()), true))
						isOk = false;
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 获取上下文URL全路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getContextAllPath(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort())
				.append(request.getContextPath());
		String path = sb.toString();
		sb = null;
		return path;
	}
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath) {
		
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件 @param t 复制到的新文件
	 */
	
	public static void fileChannelCopy(File s, File t) {
		
		FileInputStream fi = null;
		
		FileOutputStream fo = null;
		
		FileChannel in = null;
		
		FileChannel out = null;
		
		try {
			
			fi = new FileInputStream(s);
			
			fo = new FileOutputStream(t);
			
			in = fi.getChannel();// 得到对应的文件通道
			
			out = fo.getChannel();// 得到对应的文件通道
			
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				fi.close();
				
				in.close();
				
				fo.close();
				
				out.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
	}
	
	/**
	 * 复制单个文件
	 *
	 * @param srcFileName
	 *            待复制的文件名
	 * @param destFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(File srcFile, String destFileName) {
		// 判断原文件是否存在
		if (!srcFile.exists()) {
			/* System.out.println("复制文件失败：原文件" + srcFileName + "不存在！"); */
			return false;
		} else if (!srcFile.isFile()) {
			/* System.out.println("复制文件失败：" + srcFileName + "不是一个文件！"); */
			return false;
		}
		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在，而且复制时允许覆盖。
		} else {
			if (!destFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				if (!destFile.getParentFile().mkdirs()) {
					return false;
				}
			}
			/* destFile.mkdirs(); */
		}
		// 准备复制文件
		int byteread = 0;// 读取的位数
		InputStream in = null;
		OutputStream out = null;
		try {
			// 打开原文件
			in = new FileInputStream(srcFile);
			// 打开连接到目标文件的输出流
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			// 一次读取1024个字节，当byteread为-1时表示文件已经读完
			while ((byteread = in.read(buffer)) != -1) {
				// 将读取的字节写入输出流
				out.write(buffer, 0, byteread);
			}
			/* System.out.println("复制单个文件" + srcFileName + "至" + destFileName + "成功！"); */
			return true;
		} catch (Exception e) {
			System.out.println("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，注意先关闭输出流，再关闭输入流
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//@formatter:off 
		/**
		 * Title: copyFileToHome
		 * Description:拷贝文件到服务器上
		 * Created On: 2014年9月30日 上午10:49:11
		 * @author JiaYongChao
		 * <p>
		 * @param file要拷贝的文件
		 * @param type文件类型(文件还是图片)
		 * @param property 文件属性（视频，小说...等）
		 */
		//@formatter:on
	public static void copyFileToHome(File file, String type, String property) {
		/* String newFile = new File("D:\\home\\upload\\"+type+"\\"+property+"\\"); */
		String toDir = PropertyUtils.getProperty("server.path") + File.separator+"upload"+ File.separator + type + File.separator + property + File.separator + file.getName();
		Fs.copyFile(file, toDir);
	}
	
	private static final Logger log = LoggerFactory.getLogger(Fs.class);
}
