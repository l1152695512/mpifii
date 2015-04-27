package com.yinfu.shiro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.handler.Handler;
import com.yinfu.jbase.util.Fs;

public class SessionHandler extends Handler
{
	
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
	{
		int index = target.indexOf(";jsessionid".toUpperCase());
		if (index != -1) target = target.substring(0, index);
		String cxt = Fs.getContextAllPath(request);
		request.setAttribute("cxt", cxt);
		nextHandler.handle(target, request, response, isHandled);
	}
}
