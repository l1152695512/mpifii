package com.yinfu.system.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.yinfu.UrlConfig;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.system.model.Bug;
import com.yinfu.system.validator.BugValidator;

@ControllerBind(controllerKey = "/system/bug", viewPath = UrlConfig.SYSTEM)
public class BugController extends Controller<Bug>
{


	public void status()
	{

		renderJsonResult(getModel().updateAndModifyDate());

	}

	@Before(value = { BugValidator.class })
	public void add()
	{
		renderJsonResult(getModel().saveAndCreateDate());

	}

	@Before(value = { BugValidator.class })
	public void edit()
	{
		renderJsonResult(getModel().updateAndModifyDate());

	}

	public void delete()
	{
		renderJsonResult(Bug.dao.deleteById(getPara("id")));
	}

}
