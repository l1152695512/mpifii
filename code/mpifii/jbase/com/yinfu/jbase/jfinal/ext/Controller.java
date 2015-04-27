package com.yinfu.jbase.jfinal.ext;

import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.yinfu.model.SplitPage.SplitPage;
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class Controller<T> extends com.jfinal.core.Controller
{

	/**
	 * 默认 jsp 视图
	 */
	public static final String VIEW_TYPE = ".jsp";

	ControllerBind controll;

	public Controller()
	{
		controll = this.getClass().getAnnotation(ControllerBind.class);
	}
	//@formatter:off 
	/**
	 * Title: getModel
	 * Description:
	 * Created On: 2014年8月5日 下午3:28:49
	 * @author JiaYongChao
	 * <p>
	 * @return 
	 */
	//@formatter:on
	public T getModel()
	{
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class modelClass = (Class) pt.getActualTypeArguments()[0];

		return (T) super.getModel(modelClass);
	}

	//@formatter:off 
	/**
	 * Title: renderExcel
	 * Description:
	 * Created On: 2014年8月5日 下午3:28:52
	 * @author JiaYongChao
	 * <p>
	 * @param data
	 * @param fileName
	 * @param headers 
	 */
	//@formatter:on
	public void renderExcel(List<?> data, String fileName, String[] headers)
	{

		PoiRender excel = PoiRender.me(data);
		excel.fileName(fileName);
		excel.headers(headers);
		excel.cellWidth(5000);
		render(excel);
	}

	/**
	 * 前台页面统一处理，如果为false，前台不需要自己处理，如果为true，才会走ajax的success方法
	 */
	public void renderJsonResult(boolean result)
	{
		if (result){
			renderNull();
		}else{
			renderError(550);
		}
	}

	//@formatter:off 
	/**
	 * Title: renderJson500
	 * Description:
	 * Created On: 2014年8月5日 下午3:29:47
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void renderJson500()
	{
		renderText("{\"msg\":\"没有任何修改或 服务器错误\"}");
	}

	//@formatter:off 
	/**
	 * Title: renderJsonError
	 * Description:
	 * Created On: 2014年8月5日 下午3:29:49
	 * @author JiaYongChao
	 * <p>
	 * @param msg 
	 */
	//@formatter:on
	public void renderJsonError(String msg)
	{
		renderText("{\"msg\":\" " + msg + " \"}");
	}

	//@formatter:off 
	/**
	 * Title: renderJson200
	 * Description:
	 * Created On: 2014年8月5日 下午3:29:53
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void renderJson200()
	{
		renderText("{\"code\":200}");
	}

	//@formatter:off 
	/**
	 * Title: forwardAction
	 * Description:
	 * Created On: 2014年8月5日 下午3:29:56
	 * @author JiaYongChao
	 * <p>
	 * @param msg
	 * @param url 
	 */
	//@formatter:on
	public void forwardAction(String msg, String url)
	{

		setAttr("msg", msg);
		forwardAction(url);
	}

	//@formatter:off 
	/**
	 * Title: render
	 * Description:
	 * Created On: 2014年8月5日 下午3:29:58
	 * @author JiaYongChao
	 * <p>
	 * @param msg
	 * @param url 
	 */
	//@formatter:on
	public void render(String msg, String url)
	{
		setAttr("msg", msg);
		render(url);
	}


	/***
	 * 
	 * 什么时候用 gson 呐
	 * 
	 * 如果是 原生的 List<Model> 直接返回即可 用 renderJson
	 * 
	 * 
	 * @param obj
	 */
	public void renderGson(Object obj)
	{

		renderJson(new Gson().toJson(obj));
	}

	/***
	 * 
	 * 好像有个问题
	 * 
	 * @param obj
	 */
	public void renderFastJson(Object obj)
	{
		renderJson(JSON.toJSONString(obj));
	}
	/****************************分页基本信息开始********************************************/
	protected SplitPage splitPage;	// 分页封装
	
	public void setSplitPage(SplitPage splitPage) {
		this.splitPage = splitPage;
	}
	/**
	 * 获取查询参数
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	public Map<String, String> getQueryParam(){
		Map<String, String> queryParam = new HashMap<String, String>();
		Enumeration<String> paramNames = getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			String value = getPara(name);
			if (name.startsWith("_query") && !value.isEmpty()) {// 查询参数分拣
				String key = name.substring(7);
				if(null != value && !value.trim().equals("")){
					queryParam.put(key, value.trim());
				}
			}
		}
		
		return queryParam;
	}

	/**
	 * 设置默认排序
	 * @param colunm
	 * @param mode
	 */
	public void defaultOrder(String colunm, String mode){
		if(null == splitPage.getOrderColunm() || splitPage.getOrderColunm().isEmpty()){
			splitPage.setOrderColunm(colunm);
			splitPage.setOrderMode(mode);
		}
	}
	
	/**
	 * 排序条件
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	public String getOrderColunm(){
		String orderColunm = getPara("orderColunm");
		return orderColunm;
	}

	/**
	 * 排序方式
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	public String getOrderMode(){
		String orderMode = getPara("orderMode");
		return orderMode;
	}
	
	
}
