package com.yinfu.interceptor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.T;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ToolDateTime;
import com.yinfu.model.SplitPage.SplitPage;

public class ParamPkgInterceptor implements Interceptor {
	private static Logger log = Logger.getLogger(ParamPkgInterceptor.class);
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = (Controller) ai.getController();
		
		Class<?> controllerClass = controller.getClass();
		Class<?> superControllerClass = controllerClass.getSuperclass();
		
		Field[] fields = controllerClass.getDeclaredFields();
		Field[] parentFields = superControllerClass.getDeclaredFields();
		
		
		// 是否需要分页
		splitPage(controller, superControllerClass);
		// 封装controller变量值
		for (Field field : fields) {
			setControllerFieldValue(controller, field);
		}
		
		// 封装baseController变量值
		for (Field field : parentFields) {
			setControllerFieldValue(controller, field);
		}

		log.debug("*********************** 封装参数值到 controller 全局变量  end ***********************");
		
		ai.invoke();
		
		log.debug("*********************** 设置全局变量值到 request start ***********************");

		// 封装controller变量值
		for (Field field : fields) {
			setRequestValue(controller, field);
		}
		
		// 封装baseController变量值
		for (Field field : parentFields) {
			setRequestValue(controller, field);
		}
		
		log.debug("*********************** 设置全局变量值到 request end ***********************");
	}
	
	/**
	 * 分页参数处理
	 * @param controller
	 * @param superControllerClass
	 */
	public void splitPage(Controller controller, Class<?> superControllerClass){
		SplitPage splitPage = new SplitPage();
		// 分页查询参数分拣
		Map<String, String> queryParam = new HashMap<String, String>();
		Enumeration<String> paramNames = controller.getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			if (name.startsWith("_query.")) {
				String value = controller.getPara(name);
				if(null != value){
					value = value.trim();
					if(!value.isEmpty()){
						String key = name.substring(7);
						queryParam.put(key, value);
					}
				}
			}
		}
		splitPage.setQueryParam(queryParam);
		
		String orderColunm = controller.getPara("orderColunm");// 排序条件
		if(null != orderColunm && !orderColunm.isEmpty()){
			splitPage.setOrderColunm(orderColunm);
		}

		String orderMode = controller.getPara("orderMode");// 排序方式
		if(null != orderMode && !orderMode.isEmpty()){
			splitPage.setOrderMode(orderMode);
		}

		String pageNumber = controller.getPara("pageNumber");// 第几页
		if(null != pageNumber && !pageNumber.isEmpty()){
			splitPage.setPageNumber(Integer.parseInt(pageNumber));
		}
		
		String pageSize = controller.getPara("pageSize");// 每页显示几多
		if(null != pageSize && !pageSize.isEmpty()){
			splitPage.setPageSize(Integer.parseInt(pageSize));
		}
		
		controller.setSplitPage(splitPage);
	}
	
	/**
	 * 反射set值到全局变量
	 * @param controller
	 * @param field
	 */
	public void setControllerFieldValue(Controller controller, Field field){
		try {
			field.setAccessible(true);
			String name = field.getName();
			String value = controller.getPara(name);
			if(null == value || value.isEmpty()){// 参数值为空直接结束
				return;
			}
			String fieldType = field.getType().getSimpleName();
			if(fieldType.equals("String")){
				field.set(controller, value);
			
			}else if(fieldType.equals("int")){
				field.set(controller, Integer.parseInt(value));
				
			}else if(fieldType.equals("Date")){
				int dateLength = value.length();
				if(dateLength == ToolDateTime.pattern_ymd.length()){
					field.set(controller, ToolDateTime.parse(value, ToolDateTime.pattern_ymd));
				
				}else if(dateLength == ToolDateTime.pattern_ymd_hms.length()){
					field.set(controller, ToolDateTime.parse(value, ToolDateTime.pattern_ymd_hms));
				
				}else if(dateLength == ToolDateTime.pattern_ymd_hms_s.length()){
					field.set(controller, ToolDateTime.parse(value, ToolDateTime.pattern_ymd_hms_s));
				}
				
			}else if(fieldType.equals("BigDecimal")){
				BigDecimal bdValue = new BigDecimal(value);
				field.set(controller, bdValue);
				
			}else{
			}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} finally {
			field.setAccessible(false);
		}
	}

	/**
	 * 反射全局变量值到request
	 * @param controller
	 * @param field
	 */
	public void setRequestValue(Controller controller, Field field){
		try {
			field.setAccessible(true);
			Object value = field.get(controller);
			if(null == value){// 参数值为空直接结束
				return;
			}
			String name = field.getName();
			controller.setAttr(name, value);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} finally {
			field.setAccessible(false);
		}
	}
}
