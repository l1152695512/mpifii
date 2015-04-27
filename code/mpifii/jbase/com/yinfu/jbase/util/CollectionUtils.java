
package com.yinfu.jbase.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 * @author JiaYongChao
   集合类型的工具类
 */
public class CollectionUtils {
	
	//@formatter:off 
	/**
	 * Title: isNotEmpty
	 * Description:检查所传的集合元素是否为空
	 * Created On: 2015年1月14日 下午2:29:11
	 * @author JiaYongChao
	 * <p>
	 * @param collection
	 * @return 
	 */
	//@formatter:on
	public static boolean isNotEmpty(Collection<Object> collection) {
		if (collection == null) {
			return false;
		}
		if (collection.size() == 0) {
			return false;
		}
		return true;
	}
	//@formatter:off 
	/**
	 * Title: arrayToString
	 * Description:将数组元素去掉重复值，并转换为(a,b,c)格式的字符串
	 * Created On: 2015年1月14日 下午2:29:19
	 * @author JiaYongChao
	 * <p>
	 * @param source
	 * @return 
	 */
	//@formatter:on
	public static String arrayToString(String[] source) {
		if (source != null) {
			Set<String> sourceSet = new HashSet<String>(Arrays.asList(source));
			String condictionVal = "";
			for (String id : sourceSet) {
				if (StringUtils.isNotEmpty(id)) {
					condictionVal += id + ",";
				}
			}
			if (condictionVal.lastIndexOf(",") != -1)
				return condictionVal.substring(0, condictionVal.length() - 1);
			else
				return null;
		}
		return null;
	}
	
	//@formatter:off 
	/**
	 * Title: listToString
	 * Description:将list中的元素转化为字符串，以逗号隔开
	 * Created On: 2015年1月14日 下午2:29:30
	 * @author JiaYongChao
	 * <p>
	 * @param srcList
	 * @return 
	 */
	//@formatter:on
	public static String listToString(List<?> srcList) {
		try {
			if (srcList != null && srcList.size() > 0) {
				String resultStr = "";
				int len = srcList.size();
				for (int i = 0; i < len; i++) {
					Object object = srcList.get(i);
					if (object != null) {
						resultStr += object.toString();
					}
					if (i < len - 1) {
						resultStr += ",";
					}
				}
				if (StringUtils.isNotEmpty(resultStr)) {
					return resultStr;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	//@formatter:off 
	/**
	 * Title: convertElementType
	 * Description:转换List数组中的元素类型
	 * Created On: 2015年1月14日 下午2:29:41
	 * @author JiaYongChao
	 * <p>
	 * @param sourceList
	 * @return 
	 */
	//@formatter:on
	public static List<Long> convertElementType(List<String> sourceList) {
		if (sourceList != null && sourceList.size() > 0) {
			List<Long> destList = new ArrayList<Long>();
			for (String obj : sourceList) {
				if (StringUtils.isNotEmpty(obj)) {
					destList.add(new Long(obj));
				}
			}
			return destList;
		}
		return new ArrayList<Long>();
	}
	
	//@formatter:off 
	/**
	 * Title: hasRepeat
	 * Description:获取重复值
	 * Created On: 2015年1月14日 下午2:29:49
	 * @author JiaYongChao
	 * <p>
	 * @param s
	 * @return 
	 */
	//@formatter:on
	public static String hasRepeat(String s) {
		String[] com = s.split(",");
		String info = "";
		for (int i = 0; i < com.length; i++) {
			int k = 0;
			for (int j = 0; j < com.length; j++) {
				if (com[i].equals(com[j])) {
					k++;
					if (k == 2) {
						if ("".equals(info)) {
							info += com[j] + ",";
						} else {
							info = removeDuplicate(info, com[j]);
						}
					}
				}
			}
		}
		if (info != "") {
			return info.substring(0, info.length() - 1);
		}
		return null;
	}
	
	public static String removeDuplicate(String newInfo, String oldInfo) {
		if (!newInfo.contains(oldInfo)) {
			newInfo += oldInfo + ",";
		}
		return newInfo;
	}
	//@formatter:off 
	/**
	 * Title: removeRepeat
	 * Description:去除重复值
	 * Created On: 2015年1月14日 下午2:30:00
	 * @author JiaYongChao
	 * <p>
	 * @param s
	 * @return 
	 */
	//@formatter:on
	public static String removeRepeat(String s) {
		Set<Object> tempSet = new HashSet<Object>();
		String[] t = s.split(",");
		for (int i = 0; i < t.length; i++) {
			tempSet.add(t[i]);
		}
		Object[] tempObject = tempSet.toArray();
		String temp = "";
		for (int i = 0; i < tempObject.length; i++) {
			temp += tempObject[i] + ",";
		}
		return temp;
	}
	
}
