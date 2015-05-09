package com.yinfu.jbase.util.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author hadeslee
 */
public final class VerifyUtil {

    /**
     * 得到文件的MD5码,用于校验
     * @param file
     * @param jpb 
     * @return
     */
    public static String getMD5(File file, JProgressBar jpb) {
        FileInputStream fis = null;
        jpb.setMaximum((int) file.length());
        jpb.setValue(0);
        jpb.setString("正在计算:" + file.getName() + "的MD5值");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = -1;
            System.out.println("开始算");
            int value = 0;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
                value += length;
                jpb.setValue(value);
            }
            System.out.println("算完了");
            return bytesToString(md.digest());
        } catch (IOException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param file
     * @return
     */
    public static String getMD5(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = -1;
            System.out.println("开始算");
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            System.out.println("算完了");
            return bytesToString(md.digest());
        } catch (IOException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 得到文件的SHA码,用于校验
     * @param file
     * @return
     */
    public static String getSHA(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = -1;
            System.out.println("开始算");
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            System.out.println("算完了");
            return bytesToString(md.digest());
        } catch (IOException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * 
     * @param file
     * @param jpb
     * @return
     */
    public static String getSHA(File file,JProgressBar jpb) {
        FileInputStream fis = null;
        jpb.setMaximum((int) file.length());
        jpb.setValue(0);
        jpb.setString("正在计算:" + file.getName() + "的MD5值");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = -1;
            System.out.println("开始算");
            int value=0;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
                value+=length;
                jpb.setValue(value);
            }
            System.out.println("算完了");
            return bytesToString(md.digest());
        } catch (IOException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(VerifyUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String bytesToString(byte[] data) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);

    }

    public static void main(String[] args) {
    	long startTime = System.currentTimeMillis();
//    	System.err.println(getMD5(new File("E:\\MyEclipse-8.6.rar")));
    	System.err.println(getMD5(new File("E:\\zqsh.zip")));
    	System.err.println((System.currentTimeMillis()-startTime));
    }
}