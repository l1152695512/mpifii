package com.yinfu.business.util;

import java.util.Random;

public class ColorUtil {
<<<<<<< HEAD
	
	/**
	 * 
	   
	 * getRandColorCode(这里用一句话描述这个方法的作用)    
	   
	 * TODO(这里描述这个方法适用条件 – 可选)    
	   
	 * TODO(这里描述这个方法的执行流程 – 可选)    
	   
	 * TODO(这里描述这个方法的使用方法 – 可选)    
	   
	 * TODO(这里描述这个方法的注意事项 – 可选)    
	   
	 * @param   name    
	   
	 * @param  @return    设定文件    
	   
	 * @return String    DOM对象    
	   
	 * @Exception 异常对象    
	   
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
=======
	/**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     * @return String
     */
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
public static String getRandColorCode(){
  String r,g,b;
  Random random = new Random();
  r = Integer.toHexString(random.nextInt(256)).toUpperCase();
  g = Integer.toHexString(random.nextInt(256)).toUpperCase();
  b = Integer.toHexString(random.nextInt(256)).toUpperCase();
  
  r = r.length()==1 ? "0" + r : r ;
  g = g.length()==1 ? "0" + g : g ;
  b = b.length()==1 ? "0" + b : b ;
  
  return r+g+b;
 }
<<<<<<< HEAD
	
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
}
