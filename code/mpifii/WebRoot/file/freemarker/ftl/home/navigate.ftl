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
        <title>mo派生活</title>     
 <link rel="stylesheet" type="text/css" href="${cxt}/portal/mb/index4/css/common.css" />
        <link rel="stylesheet" type="text/css" href="${cxt}/portal/mb/index4/css/cer.css" />
        <!--<script src="${cxt}/portal/mb/index4/js/dynamicLoad.js" type="text/javascript"></script>-->
        <script src="${cxt}/portal/mb/index4/js/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script src="${cxt}/portal/mb/index4/js/TouchSlide.1.1.js" type="text/javascript"></script>
        
        <script type="text/javascript">var ydcp_id = '10028';</script>
		<script type="text/javascript" src="http://wifi.winasdaq.com/ydadh-min.js"></script>
    </head>
    <body>
    	<!--<header>
        	<h3 class="pic_logo"></h3>
        </header>-->
        
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
        <div class="application_area first">
        	<h1 class="title-1">应用专区</h1>
            <div class="slideWrapper">
                <ul>
                    <li>
                        <div class="application_list">
                            <ul>
				<#list nav1List as nav1>
                             		<#if nav1_index < 8>
                                		<li><a href="${cxt}/portal/mb/home?id=${nav1.id}&url=${(nav1.url)!}"><img src="${(nav1.logo)!}" ><p>${(nav1.title)!}</p></a></li>
                                	</#if>
                                </#list>
                            	
                            </ul>
                            <div class="cl"></div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="application_area">
        	<h1 class="title-2">热门推荐</h1>
            <div class="slideWrapper">
                <ul>
                    <li>
                        <div class="application_list">
                            <ul>
                             	<#list nav2List as nav2>
                            		<#if nav2_index < 4>
                            	 		<li><a href="${cxt}/portal/mb/home?id=${nav2.id}&url=${(nav2.url)!}"><img src="${(nav2.logo)!}" ><p>${(nav2.title)!}</p></a></li>
                            	 	</#if>
                            	</#list>
                            </ul>
                            <div class="cl"></div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="application_area">
        	<h1 class="title-3">生活服务</h1>
            <div class="slideWrapper">
                <ul>
                    <li>
                        <div class="application_list">
                            <ul>
                                <li><a href="http://m.zhuna.cn/"><img src="index4/images/jiudian.png" ><p>酒店</p></a></li>
                                <li><a href="http://touch.qunar.com/h5/flight/"><img src="index4/images/jipiao.png" ><p>机票</p></a></li>
                                <li><a href="http://wap.yikuaiqu.com/"><img src="index4/images/menpiao.png" ><p>门票</p></a></li>
                                <li><a href="http://m.ctrip.com/webapp/train/"><img src="index4/images/huoche.png" ><p>火车</p></a></li>
                                <li><a href="http://i.meituan.com/"><img src="index4/images/tuangou.png" ><p>团购</p></a></li>
                                <li><a href="http://m.ly.com/"><img src="index4/images/lvyou.png" ><p>旅游</p></a></li>
                                <li><a href="http://3g.ganji.com"><img src="index4/images/ganji.png" ><p>赶集网</p></a></li>
                                <li><a href="http://m.vip.com/"><img src="index4/images/wph.png" ><p>唯品会</p></a></li>
                            </ul>
                            <div class="cl"></div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="web_portals">
        	<div class="web_portals_list">
            	<div class="list_left"><span>车辆<br>之家</span></div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.che168.com/">二手车</a>
                        <a href="http://car.m.autohome.com.cn/">找车</a>
                    </div>
                </div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.mall.autohome.com.cn/">新车</a>
                        <a href="http://club.m.autohome.com.cn/">论坛</a>
                    </div>
                </div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://k.m.autohome.com.cn/">口碑</a>
                        <a href="http://kuaibao.m.autohome.com.cn/">快报</a>
                    </div>
                </div>
                <div class="cl"></div>
            </div>
            <div class="web_portals_list">
            	<div class="list_left"><span>兼职<br>招聘</span></div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.58.com/w/jiaoyupeixun/">家教</a>
                        <a href="http://m.58.com/w/liyiyanyi/">礼仪</a>
                    </div>
                </div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.58.com/w/jzmakeup/">化妆师</a>
                        <a href="http://m.58.com/w/jianzhifwy/">服务员</a>
                    </div>
                </div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.58.com/w/xiaoshoucuxiao/">促销</a>
                        <a href="http://m.58.com/w/xueshengjianzhi/">兼职</a>
                    </div>
                </div>
                <div class="cl"></div>
            </div>
            <div class="web_portals_list">
            	<div class="list_left"><span>新闻<br>资讯</span></div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.news.so.com/#news">头条</a>
                        <a href="http://m.news.so.com/#fun">娱乐</a>
                    </div>
                </div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.news.so.com/#domestic">国内</a>
                        <a href="http://m.news.so.com/#car">汽车</a>
                    </div>
                </div>
                <div class="list_right">
                	<div class="list_right_inner">
                    	<a href="http://m.news.so.com/#science">科技</a>
                        <a href="http://m.news.so.com/#it">互联网</a>
                    </div>
                </div>
                <div class="cl"></div>
            </div>
        </div>
        <script type="text/javascript">
			$(function(){
				$(".slideWrapper > ul > li:nth-of-type(2)").hide();
				$(".slide_circle > ul > li").click(function(){
					$(this).addClass("current_circle").siblings().removeClass("current_circle");
					$(".slideWrapper > ul >li:eq("+$(this).index()+")").show().siblings(".slideWrapper > ul >li").hide();
				});
				$().append(function(index, html) {

                });
			});
		</script>
		
		<div id="advDiv" style="border-top:1px solid #CCCCCC;bottom:0px;height:auto;width:100%;left:0;position:relative;text-align: center;overflow:hidden;background-color:#ccc;">
            <script type="text/javascript" src="http://wifi.winasdaq.com/ydadjs-min.js"></script>
        </div>
    </body>
</html>
