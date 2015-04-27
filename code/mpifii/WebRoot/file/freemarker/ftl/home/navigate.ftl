<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection" content="telephone=no" />
        <title>生活</title>     
        <link rel="stylesheet" type="text/css" href="${cxt}/portal/mb/index2/css/common.css" />
        <link rel="stylesheet" type="text/css" href="${cxt}/portal/mb/index2/css/cer.css" />
        <link rel="stylesheet" type="text/css" href="${cxt}/portal/mb/index2/css/cer_inner.css" />
        <script src="${cxt}/portal/mb/index2/js/TouchSlide.1.1.js" type="text/javascript"></script>
        <script src="${cxt}/portal/mb/index2/js/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script src="${cxt}/portal/mb/index2/js/baidu.search.js" type="text/javascript"></script>
    </head>
    <body>
        <div id="slideBox" class="slideBox">
            <div class="bd">
                <ul>
                <c:forEach items="${banner_advs}" var="row">
					<li>
                        <a class="pic" href="${rowLink}"><img src="${rowImage}" onerror="this.src='${cxt}/portal/mb/index1/img/ad-1.jpg'"/></a>
                    </li>
				</c:forEach>
                </ul>
            </div>
            <div class="hd">
                <ul></ul>
            </div>
            <script type="text/javascript">
	            var bannerImgWidth = $(document.body).width();
	        	var bannerImgHeight = 150/290*bannerImgWidth;
	        	$("#slideBox .bd img").css({"width":bannerImgWidth,"height":bannerImgHeight});
                try{
                    TouchSlide({ 
                        slideCell:"#slideBox",
                        titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
                        mainCell:".bd ul", 
                        effect:"leftLoop", 
                        autoPage:true,//自动分页
                        autoPlay:true //自动播放
                    });
                }catch(e){
                }
            </script>
        </div>
        <div class="m_search kline">
            <form id="form" action="http://www.baidu.com/s" target="_self" method="get" accept-charset="utf-8">
                <div class="search">
                	<input type="text" placeholder="请输入关键字" name="wd" id="searchText" autocomplete="off">
                	<input type="hidden" name="tn" value="47096130_pg">
					<input type="hidden" name="ch" value="1">
                    <div id="auto">
                    </div>
                </div>
                <button type="submit" id="btnSearch">百度一下</button>
            </form>
        </div>
        <div class="web_portals">
            <ul>
            	<#list nav1List as nav1>
					<li><a href="${cxt}/portal/mb/home?id=${nav1.id}&url=${(nav1.url)!}"><img src="${(nav1.logo)!}" ><p>${(nav1.title)!}</p></a></li>
				</#list>
            </ul>
        	<div class="cl"></div>
        </div>
        <div class="m_title">
        	<#list nav2List as nav2>
	            <div class="m_siteclass  kline m_shenghuo">
	            	<a href="${cxt}/portal/mb/home?id=${nav2.id}&url=${(nav2.url)!}" target="_self">
	                    <div class="title">
	                        <h2>${(nav2.title)!}</h2>
	                        <div class="addinfo">
	                            <span>${(nav2.des)!}</span>
	                        </div>
	                    </div>
	                    <div class="path_ajax"></div>
	                </a>
	            </div>
            </#list>
            
            <div class="m_copyright">
                <p>CopyRight &copy; MO派生活</p>
            </div>
        </div>
    </body>
</html>
